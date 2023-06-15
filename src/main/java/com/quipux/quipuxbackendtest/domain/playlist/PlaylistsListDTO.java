package com.quipux.quipuxbackendtest.domain.playlist;

import java.util.List;

public record PlaylistsListDTO(
        String nome,
        String descricao,
        List<Link> links
) {
    public PlaylistsListDTO(Playlist playlist, String href) {
        this(playlist.getNome(), playlist.getDescricao(), List.of(new Link("self", href)));
    }
}