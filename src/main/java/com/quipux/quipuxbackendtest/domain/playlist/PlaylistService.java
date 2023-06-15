package com.quipux.quipuxbackendtest.domain.playlist;

import com.quipux.quipuxbackendtest.infra.exception.ValidationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class PlaylistService {

    private PlaylistRepository repository;

    public PlaylistService(PlaylistRepository repository) {
        this.repository = repository;
    }

    public List<PlaylistsListDTO> listPlaylists(UriComponentsBuilder uriBuilder) {
        UriComponentsBuilder builder = uriBuilder.path("/lists/{listName}").encode();

        List<PlaylistsListDTO> playlists =
            repository.findAll().stream().map(playlist -> {
                String href = builder.buildAndExpand(playlist.getNome()).toString();
                return new PlaylistsListDTO(playlist, href);
            }).toList();

        return playlists;
    }

    public PlaylistDTO getPlaylistDTO(String listName) {
        Playlist playlist = repository.findByNome(listName).orElseThrow(EntityNotFoundException::new);
        PlaylistDTO dto = new PlaylistDTO(playlist);
        return dto;
    }

    public PlaylistDTO create(PlaylistDTO dto) {
        if (repository.findByNome(dto.nome()).isPresent())
            throw new ValidationException("nome", "Essa playlist já existe");

        Playlist playlist = new Playlist(dto);
        repository.save(playlist);
        PlaylistDTO createdDTO = new PlaylistDTO(playlist);
        return createdDTO;
    }

    public void delete(String listName) {
        Playlist playlist = repository.findByNome(listName).orElseThrow(EntityNotFoundException::new);
        repository.delete(playlist);
    }
}
