package com.quipux.quipuxbackendtest.domain.playlist;

import com.quipux.quipuxbackendtest.domain.song.SongDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PlaylistDTO(

        @NotBlank
        String nome,
        @NotBlank
        String descricao,
        @Valid
        List<SongDTO> musicas
) {
    public PlaylistDTO(Playlist playlist) {
        this(playlist.getNome(), playlist.getDescricao(), playlist.getMusicasDTOs());
    }
}
