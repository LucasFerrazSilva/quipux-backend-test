package com.quipux.quipuxbackendtest.domain.song;

import jakarta.validation.constraints.NotBlank;

public record SongDTO(
        @NotBlank
        String titulo,
        @NotBlank
        String artista,
        @NotBlank
        String album,
        @NotBlank
        String ano,
        @NotBlank
        String genero
) {
    public SongDTO(Song song) {
        this(song.getTitulo(), song.getArtista(), song.getAlbum(), song.getAno(), song.getGenero());
    }
}
