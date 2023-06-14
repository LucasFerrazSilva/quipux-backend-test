package com.quipux.quipuxbackendtest.domain.playlist;

import com.quipux.quipuxbackendtest.domain.song.SongDTO;

import java.util.List;

public record PlaylistDTO(

        String nome,
        String descricao,
        List<SongDTO> musicas
) {
    public PlaylistDTO(Playlist playlist) {
        this(playlist.getNome(), playlist.getDescricao(), playlist.getMusicasDTOs());
    }
}
