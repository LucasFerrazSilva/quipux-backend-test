package com.quipux.quipuxbackendtest.domain.playlist;

import com.quipux.quipuxbackendtest.domain.song.Song;
import com.quipux.quipuxbackendtest.domain.song.SongDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="TB_PLAYLISTS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of="id")
@ToString
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    @OneToMany(mappedBy = "playlist", fetch = FetchType.LAZY)
    private List<Song> musicas;

    public List<SongDTO> getMusicasDTOs() {
        return this.musicas.stream().map(SongDTO::new).toList();
    }
}
