package com.quipux.quipuxbackendtest.controller;

import com.quipux.quipuxbackendtest.domain.playlist.Playlist;
import com.quipux.quipuxbackendtest.domain.playlist.PlaylistDTO;
import com.quipux.quipuxbackendtest.domain.playlist.PlaylistRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/lists")
public class PlaylistController {

    private PlaylistRepository repository;

    public PlaylistController(PlaylistRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity list() {
        List<PlaylistDTO> playlists = repository.findAll().stream().map(PlaylistDTO::new).toList();

        return ResponseEntity.ok(playlists);
    }

}
