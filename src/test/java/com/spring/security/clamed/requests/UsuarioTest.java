package com.spring.security.clamed.requests;



import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.annotation.ExceptionProxy;

import java.net.URI;
import java.rmi.server.ExportException;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
// Caso queira observar o compartamento do teste no banco de dados desative @Transactional
// Com @Transaction ativado os testes não fazem alteração do estado dos registros no banco de dados
// Sempre após finalizado o teste transação rodará um RollBack voltando o estado do registro(s) no banco
@Transactional

public class UsuarioTest {

    private URI path;
    private MockHttpServletRequest request;
    private ResultMatcher expectedResult;

    @Autowired
    private MockMvc mock;

    private String jwtToken;

    // Este método é executado sempre antes de todos os métodos de test anotados com @Test
    // Neste caso estou realizando a autenticação que será comum para todos os métodos de testes
    // Populando o atributo jwtToken para passar nas requisções montadas nos testes
    @Before
    public void setUp() throws Exception{

        // montagem do objeto JSON que será considerado na requisição, neste caso para login
        String json = "{\"login\": \"brunomoura\", \"senha\": \"102030\"}";

        // define em qual o controlador que irá utilizar na requisição
        path = new URI("/login");

        // monta uma requisição informando o verbo, neste caso POST,
        // informando qual path, tipo de conteúdo, e o valor do conteúdo que será informado na requisição
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(path)
                .contentType(MediaType.APPLICATION_JSON).content(json);

        // define o resultado esperado da requisição
        expectedResult = MockMvcResultMatchers.status().isOk();

        // executa a requisção verificando se o retorno da mesma é igual ao retorno esperado,
        // e armazena a resposta na variável response
        String response = mock.perform(request).andExpect(expectedResult).andReturn().getResponse()
               .getContentAsString();

        // cria um objeto JSON com a resposta da requisição
        // verificar a dependencia org.json no pom.xml
        JSONObject data = new JSONObject(response);

        // armazeno o valor do token obtido na requisição para ser informado nos métodos de teste
        jwtToken = data.getString("Authorization");


    }

    @Test
    public void testCadastrar() throws Exception{

        String jsonCadastro = "{\"nome\": \"Usuario Teste99\", \"login\": \"usuarioteste99\", \"senha\": \"usuarioteste99\"}";

        path = new URI("/usuarios");

       MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(path)
               .content(jsonCadastro)
               .header("Content-Type", "application/json")
               .header("Authorization", jwtToken);

        expectedResult = MockMvcResultMatchers.status().isCreated();

        mock.perform(request).andExpect(expectedResult);

    }

    @Test
    public void testAtualizar() throws Exception {

        String jsonAtualiza = "{\"id\": \"6\", \"nome\": \"Usuario Atualiza6\", \"login\": \"usuarioatualizar6\", \"senha\": \"usuarioatualizar6\"}";

        path = new URI("/usuarios");

        // monta a requisição para realizar PUT
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(path)
                .content(jsonAtualiza)
                .header("Content-Type", "application/json")
                .header("Authorization", jwtToken);

        expectedResult = MockMvcResultMatchers.status().isOk();

        mock.perform(request).andExpect(expectedResult);
    }

    @Test
    public void testRemover() throws Exception {

        path = new URI("/usuarios");

        // monta a requisição para realizar DELETE informando o parâmetro idUsuario
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(path)
                .param("idUsuario","7")
                .header("Content-Type", "application/json")
                .header("Authorization", jwtToken);

        expectedResult = MockMvcResultMatchers.status().isOk();

        mock.perform(request).andExpect(expectedResult);
    }

    @Test
    public void testListar() throws Exception{

        path = new URI("/usuarios");

        // monta a requisição para realizar GET para obter a listagem de usuários
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(path)
                .header("Content-Type", "application/json")
                .header("Authorization", jwtToken);

        expectedResult = MockMvcResultMatchers.status().isOk();

        mock.perform(request).andExpect(expectedResult);

    }

    @Test
    public void testListarPorNome() throws Exception{

        path = new URI("/usuarios/");

        // monta a requisição para realizar GET e obter uma lista de usuários por nome
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(path)
                .param("nome","Bruno")
                .header("Content-Type", "application/json")
                .header("Authorization", jwtToken);

        expectedResult = MockMvcResultMatchers.status().isOk();

        mock.perform(request).andExpect(expectedResult);



    }

}
