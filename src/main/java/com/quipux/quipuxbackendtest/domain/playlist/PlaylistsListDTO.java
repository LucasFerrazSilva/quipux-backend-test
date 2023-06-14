package com.quipux.quipuxbackendtest.domain.playlist;

public record PlaylistsListDTO(
        String nome,
        String descricao,
        String href
) {
    public PlaylistsListDTO(Playlist playlist, String href) {
        this(playlist.getNome(), playlist.getDescricao(), href);
    }
}
