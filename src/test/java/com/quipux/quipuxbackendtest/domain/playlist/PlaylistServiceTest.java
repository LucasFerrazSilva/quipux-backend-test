package com.quipux.quipuxbackendtest.domain.playlist;

import com.quipux.quipuxbackendtest.domain.song.Song;
import com.quipux.quipuxbackendtest.domain.song.SongDTO;
import com.quipux.quipuxbackendtest.domain.song.SongRepository;
import com.quipux.quipuxbackendtest.infra.exception.ValidationException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PlaylistServiceTest {

    @Autowired
    private PlaylistService service;
    @Autowired
    private PlaylistRepository repository;
    @Autowired
    private SongRepository songRepository;
    private Playlist playlist;

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
        playlist = createPlaylist1();
    }

    @Test
    @DisplayName("Deve listar playlists corretamente")
    void testListPlaylists() {
        // Given
        Playlist playlist2 = createPlaylist2();
        List<PlaylistsListDTO> expectedListDTOS = List.of(
                new PlaylistsListDTO(playlist, buildHref(playlist.getNome())),
                new PlaylistsListDTO(playlist2, buildHref(playlist2.getNome()))
        );
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance().scheme("http").host("localhost:8080");

        // When
        List<PlaylistsListDTO> listDTOS = service.listPlaylists(uriComponentsBuilder);

        // Then
        assertThat(listDTOS).isNotEmpty();
        assertThat(listDTOS).hasSameElementsAs(expectedListDTOS);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia")
    void testEmptyListPlaylists() {
        // Given
        repository.deleteAll();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance().scheme("http").host("localhost:8080");

        // When
        List<PlaylistsListDTO> listDTOS = service.listPlaylists(uriComponentsBuilder);

        // Then
        assertThat(listDTOS).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar descricao da playlist corretamente")
    @Transactional
    void testGetPlaylistDescription() {
        // Given
        PlaylistDTO expectedDTO = new PlaylistDTO(playlist);

        // When
        PlaylistDTO dto = service.getPlaylistDescription(playlist.getNome());

        // Then
        assertThat(dto).isEqualTo(expectedDTO);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando nome for invalido")
    void testInvalidGetPlaylistDescription() {
        // Given
        String nomeInvalido = "NOME_INVALIDO";

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> service.getPlaylistDescription(nomeInvalido));
    }

    @Test
    @DisplayName("Deve criar Playlist valida")
    @Transactional
    void testCreate() {
        // Given
        SongDTO song1 = new SongDTO("Musica2", "Artista2", "Album2", "Ano2", "Genero2");
        PlaylistDTO dto = new PlaylistDTO("Playlist2", "Descricao Playlist2", List.of(song1));

        // When
        PlaylistDTO playlistDTO = service.create(dto);
        Playlist newPlaylist = repository.findByNome(dto.nome()).get();
        List<Song> musicas = songRepository.findByPlaylist(newPlaylist);

        // Then
        assertThat(playlistDTO).isEqualTo(dto);
        assertThat(newPlaylist.getMusicas()).hasSameElementsAs(musicas);
        assertThat(newPlaylist.getMusicas()).hasSize(1);
        assertThat(musicas).hasSize(1);
        assertThat(musicas.get(0).getTitulo()).isEqualTo(song1.titulo());
    }

    @Test
    @DisplayName("Deve lançar ValidationException ao criar uma Playlist com nome existente")
    void testInvalidCreate() {
        // Given
        SongDTO song1 = new SongDTO("Musica1", "Artista1", "Album1", "Ano1", "Genero1");
        PlaylistDTO dto = new PlaylistDTO("Playlist1", "Descricao Playlist1", List.of(song1));

        // When / Then
        assertThrows(ValidationException.class, () -> service.create(dto));
    }

    @Test
    @DisplayName("Deve excluir elemento normalmente")
    void testDelete() {
        // Given
        String nomeValido = playlist.getNome();

        // When
        service.delete(nomeValido);

        // Then
        assertThat(repository.findById(playlist.getId())).isNotPresent();
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao tentar excluir elemento usando nome invalido")
    void testInvalidDelete() {
        // Given
        String nomeInvalido = "NOME_INVALIDO";

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> service.delete(nomeInvalido));
    }

    private String buildHref(String listName) {
        return UriComponentsBuilder
                    .newInstance()
                    .scheme("http")
                    .host("localhost:8080")
                    .path("/lists/{listName}").encode()
                    .buildAndExpand(listName)
                    .toString();
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

}