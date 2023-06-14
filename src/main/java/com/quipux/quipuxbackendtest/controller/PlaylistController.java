package com.quipux.quipuxbackendtest.controller;

import com.quipux.quipuxbackendtest.domain.playlist.Playlist;
import com.quipux.quipuxbackendtest.domain.playlist.PlaylistDTO;
import com.quipux.quipuxbackendtest.domain.playlist.PlaylistRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity list() {
        List<PlaylistDTO> playlists = repository.findAll().stream().map(PlaylistDTO::new).toList();

        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/{listName}")
    public ResponseEntity describeList(@PathVariable String listName) {
        System.out.println(listName);
        Optional<Playlist> optional = repository.findByNome(listName);

        if (optional.isPresent()) {
            Playlist playlist = optional.get();
            PlaylistDTO dto = new PlaylistDTO(playlist);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
