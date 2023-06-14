package com.quipux.quipuxbackendtest.controller;

import com.quipux.quipuxbackendtest.domain.user.AuthenticationDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<AuthenticationDTO> dtoJackson;

    @Test
    @DisplayName("Deve retornar 400 (Bad Request) quando chamar /login sem enviar um body")
    void testInvalidLogin1() throws Exception {
        // Given

        // When
        MockHttpServletResponse response = mvc.perform(post("/login")).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve retornar 400 (Bad Request) quando chamar /login enviando credenciais inválidas")
    void testInvalidLogin2() throws Exception {
        // Given
        String login = "INVALID_LOGIN";
        String password = "INVALID_PASSWORD";
        AuthenticationDTO dto = new AuthenticationDTO(login, password);
        String credentials = dtoJackson.write(dto).getJson();

        // When
        MockHttpServletResponse response = mvc.perform(post("/login").contentType(APPLICATION_JSON).content(credentials)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo("Falha na autenticação");
    }

    @Test
    @DisplayName("Deve retornar 200 (Ok) quando chamar /login enviando credenciais válidas")
    void testValidLogin() throws Exception {
        // Given
        String login = "ADMIN";
        String password = "ADMIN";
        AuthenticationDTO dto = new AuthenticationDTO(login, password);
        String credentials = dtoJackson.write(dto).getJson();

        // When
        MockHttpServletResponse response = mvc.perform(post("/login").contentType(APPLICATION_JSON).content(credentials)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotBlank();
    }

}