package com.quipux.quipuxbackendtest.controller;

import com.quipux.quipuxbackendtest.domain.playlist.*;
import com.quipux.quipuxbackendtest.domain.song.Song;
import com.quipux.quipuxbackendtest.domain.song.SongDTO;
import com.quipux.quipuxbackendtest.domain.user.AuthenticationDTO;
import com.quipux.quipuxbackendtest.infra.exception.ValidationExceptionDataDTO;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlaylistControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<AuthenticationDTO> dtoJackson;
    @Autowired
    private JacksonTester<List<PlaylistsListDTO>> playlistListDTOJackson;
    @Autowired
    private JacksonTester<PlaylistDTO> playlistDTOJackson;
    @Autowired
    private JacksonTester<ValidationExceptionDataDTO> validationDTOJackson;
    @Autowired
    private PlaylistRepository repository;
    private Playlist playlist1;
    private Playlist playlist2;

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
        playlist1 = createPlaylist1();
        playlist2 = createPlaylist2();
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 200 (OK) e o json da playlist quando é acessar /lists")
    void testList() throws Exception {
        // Given
        List<PlaylistsListDTO> listDTO = List.of(
                new PlaylistsListDTO(playlist1, "http://localhost/lists/" + playlist1.getNome()),
                new PlaylistsListDTO(playlist2, "http://localhost/lists/" + playlist2.getNome())
        );

        // When
        MockHttpServletResponse response = mvc.perform(get("/lists")).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(playlistListDTOJackson.write(listDTO).getJson());
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 200 (OK) e a descrição da playlist quando acessar /lists/{listName} passando um nome válido.")
    void testDescribeList() throws Exception {
        // Given
        PlaylistDTO dto = new PlaylistDTO(playlist1);

        // When
        MockHttpServletResponse response = mvc.perform(get("/lists/" + playlist1.getNome())).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(playlistDTOJackson.write(dto).getJson());
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 404 (NOT FOUND) quando acessar /lists/{listName} passando um nome inválido.")
    void testInvalidDescribeList() throws Exception {
        // Given
        PlaylistDTO dto = new PlaylistDTO(playlist1);

        // When
        MockHttpServletResponse response = mvc.perform(get("/lists/NOME_INVALIDO")).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 201 (CREATED) e os dados da playlist quando chamar /lists via POST passando uma playlist válida")
    void testCreate() throws Exception {
        // Given
        SongDTO songDTO = new SongDTO("Musica", "Artista", "Album", "Ano", "Genero");
        PlaylistDTO dto = new PlaylistDTO("Playlist", "Descricao", List.of(songDTO));
        String json = playlistDTOJackson.write(dto).getJson();

        // When
        MockHttpServletResponse response =
                mvc.perform(
                        post("/lists")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                ).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeader("location")).isEqualTo("http://localhost/lists/" + dto.nome());
        assertThat(response.getContentAsString()).isEqualTo(json);
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 400 (BAD REQUEST) quando chamar /lists via POST passando uma playlist inválida")
    void testInvalidCreate() throws Exception {
        // Given
        SongDTO songDTO = new SongDTO("Musica", "Artista", "", "Ano", "Genero");
        PlaylistDTO dto = new PlaylistDTO("Playlist", null, List.of(songDTO));
        String jsonBody = playlistDTOJackson.write(dto).getJson();
        String validation1Json = validationDTOJackson.write(new ValidationExceptionDataDTO("descricao", "must not be blank")).getJson();
        String validation2Json = validationDTOJackson.write(new ValidationExceptionDataDTO("musicas[0].album", "must not be blank")).getJson();

        // When
        MockHttpServletResponse response =
                mvc.perform(
                        post("/lists")
                                .contentType(APPLICATION_JSON)
                                .content(jsonBody)
                ).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains(validation1Json);
        assertThat(response.getContentAsString()).contains(validation2Json);
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 400 (BAD REQUEST) quando chamar /lists via POST passando uma playlist inválida")
    void testInvalidCreate2() throws Exception {
        // Given
        SongDTO song1 = new SongDTO("Musica1", "Artista1", "Album1", "Ano1", "Genero1");
        PlaylistDTO dto = new PlaylistDTO("Playlist1", "Descricao Playlist1", List.of(song1));
        String jsonBody = playlistDTOJackson.write(dto).getJson();
        String validationJson = validationDTOJackson.write(new ValidationExceptionDataDTO("nome", "Essa playlist já existe")).getJson();

        // When
        MockHttpServletResponse response =
                mvc.perform(
                        post("/lists")
                                .contentType(APPLICATION_JSON)
                                .content(jsonBody)
                ).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(validationJson);
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 204 (NO CONTENT) quando chamar /lists/{listName} passando um nome valido")
    void testDelete() throws Exception {
        // Given
        String playlistName = playlist1.getNome();

        // When
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.delete("/lists/" + playlistName)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 404 (NOT FOUND) quando chamar /lists/{listName} passando um nome invalido")
    void testInvalidDelete() throws Exception {
        // Given
        String playlistName = "NOME_INVALIDO";

        // When
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.delete("/lists/" + playlistName)).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }


    @Test
    @DisplayName("Deve retornar 403 (FORBIDDEN) quando tentar acessar qualquer endpoint da Playlist sem enviar o token")
    void testAuthorization() throws Exception {
        MockHttpServletResponse listAllResponse = mvc.perform(get("/lists")).andReturn().getResponse();
        MockHttpServletResponse playlistDescriptionResponse = mvc.perform(get("/lists/Lista 1")).andReturn().getResponse();
        MockHttpServletResponse createPlaylistResponse = mvc.perform(MockMvcRequestBuilders.post("/lists")).andReturn().getResponse();
        MockHttpServletResponse deletePlaylistResponse = mvc.perform(MockMvcRequestBuilders.delete("/lists/Lista 1")).andReturn().getResponse();

        assertThat(listAllResponse.getStatus()).isEqualTo(FORBIDDEN.value());
        assertThat(playlistDescriptionResponse.getStatus()).isEqualTo(FORBIDDEN.value());
        assertThat(createPlaylistResponse.getStatus()).isEqualTo(FORBIDDEN.value());
        assertThat(deletePlaylistResponse.getStatus()).isEqualTo(FORBIDDEN.value());
    }

    @Test
    @DisplayName("Deve retornar HTTP status diferente de 403 (FORBIDDEN) quando acessar qualquer endpoint da Playlist enviando o token")
    void testAuthorization2() throws Exception {
        String token = getToken();

        MockHttpServletResponse listAllResponse = mvc.perform(get("/lists").header("Authorization", "Bearer " + token)).andReturn().getResponse();
        MockHttpServletResponse playlistDescriptionResponse = mvc.perform(get("/lists/Lista 1").header("Authorization", "Bearer " + token)).andReturn().getResponse();
        MockHttpServletResponse createPlaylistResponse = mvc.perform(MockMvcRequestBuilders.post("/lists").header("Authorization", "Bearer " + token)).andReturn().getResponse();
        MockHttpServletResponse deletePlaylistResponse = mvc.perform(MockMvcRequestBuilders.delete("/lists/Lista 1").header("Authorization", "Bearer " + token)).andReturn().getResponse();

        assertThat(listAllResponse.getStatus()).isNotEqualTo(FORBIDDEN.value());
        assertThat(playlistDescriptionResponse.getStatus()).isNotEqualTo(FORBIDDEN.value());
        assertThat(createPlaylistResponse.getStatus()).isNotEqualTo(FORBIDDEN.value());
        assertThat(deletePlaylistResponse.getStatus()).isNotEqualTo(FORBIDDEN.value());
    }


    private Playlist createPlaylist1() {
        SongDTO song1 = new SongDTO("Musica1", "Artista1", "Album1", "Ano1", "Genero1");
        PlaylistDTO dto = new PlaylistDTO("Playlist1", "Descricao Playlist1", List.of(song1));
        Playlist playlist = new Playlist(dto);
        repository.save(playlist);
        return playlist;
    }

    private Playlist createPlaylist2() {
        SongDTO song1 = new SongDTO("Musica2", "Artista2", "Album2", "Ano2", "Genero2");
        SongDTO song2 = new SongDTO("Musica3", "Artista3", "Album3", "Ano3", "Genero3");
        PlaylistDTO dto = new PlaylistDTO("Playlist2", "Descricao Playlist2", List.of(song1, song2));
        Playlist playlist = new Playlist(dto);
        repository.save(playlist);
        return playlist;
    }

    private String getToken() throws Exception {
        MockHttpServletResponse response =
                mvc.perform(
                        post("/login")
                                .contentType(APPLICATION_JSON)
                                .content(dtoJackson.write(new AuthenticationDTO("ADMIN", "ADMIN")).getJson())
                ).andReturn().getResponse();

        return response.getContentAsString().replace("{\"token\":\"", "").replace("\"}", "");
    }

}