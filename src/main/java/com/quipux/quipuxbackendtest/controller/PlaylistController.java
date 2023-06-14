package com.quipux.quipuxbackendtest.controller;

import com.quipux.quipuxbackendtest.domain.playlist.Playlist;
import com.quipux.quipuxbackendtest.domain.playlist.PlaylistDTO;
import com.quipux.quipuxbackendtest.domain.playlist.PlaylistRepository;
import com.quipux.quipuxbackendtest.domain.playlist.PlaylistsListDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lists")
public class PlaylistController {

    private PlaylistRepository repository;

    public PlaylistController(PlaylistRepository repository) {
        this.repository = repository;
    }

    @GetMapping()
    public ResponseEntity list(UriComponentsBuilder uriBuilder) {
        UriComponentsBuilder builder = uriBuilder.path("/lists/{listName}").encode();
        List<PlaylistsListDTO> playlists = repository.findAll().stream().map(playlist -> {
            String href = builder.buildAndExpand(playlist.getNome()).toString();
            return new PlaylistsListDTO(playlist, href);
        }).toList();

        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/{listName}")
    public ResponseEntity describeList(@PathVariable String listName) {
        Optional<Playlist> optional = repository.findByNome(listName);

        if (optional.isPresent()) {
            Playlist playlist = optional.get();
            PlaylistDTO dto = new PlaylistDTO(playlist);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity save(@RequestBody @Valid PlaylistDTO dto, UriComponentsBuilder uriBuilder) {
        Playlist playlist = new Playlist(dto);
        repository.save(playlist);
        URI uri = uriBuilder.path("/lists/{listName}").buildAndExpand(playlist.getNome()).toUri();
        return ResponseEntity.created(uri).body(new PlaylistDTO(playlist));
    }

}
