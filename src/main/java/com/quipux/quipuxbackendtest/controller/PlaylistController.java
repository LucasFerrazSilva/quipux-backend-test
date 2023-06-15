package com.quipux.quipuxbackendtest.controller;

import com.quipux.quipuxbackendtest.domain.playlist.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lists")
public class PlaylistController {

    private PlaylistService service;

    public PlaylistController(PlaylistService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity list(UriComponentsBuilder uriBuilder) {
        List<PlaylistsListDTO> playlists = service.listPlaylists(uriBuilder);
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/{listName}")
    public ResponseEntity describeList(@PathVariable String listName) {
        PlaylistDTO dto = service.getPlaylistDTO(listName);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody @Valid PlaylistDTO dto, UriComponentsBuilder uriBuilder) {
        PlaylistDTO createdDTO = service.create(dto);
        URI uri = uriBuilder.path("/lists/{listName}").buildAndExpand(createdDTO.nome()).toUri();
        return ResponseEntity.created(uri).body(createdDTO);
    }

    @DeleteMapping("/{listName}")
    @Transactional
    public ResponseEntity delete(@PathVariable String listName) {
        service.delete(listName);
        return ResponseEntity.noContent().build();
    }

}
