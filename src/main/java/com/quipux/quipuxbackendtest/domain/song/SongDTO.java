package com.quipux.quipuxbackendtest.domain.song;

public record SongDTO(
        String titulo,
        String artista,
        String album,
        String ano,
        String genero
) {
    public SongDTO(Song song) {
        this(song.getTitulo(), song.getArtista(), song.getAlbum(), song.getAno(), song.getGenero());
    }
}
