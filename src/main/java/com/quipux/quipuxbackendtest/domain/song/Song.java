package com.quipux.quipuxbackendtest.domain.song;

import com.quipux.quipuxbackendtest.domain.playlist.Playlist;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="TB_SONGS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of="id")
@ToString
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String artista;
    private String album;
    private String ano;
    private String genero;
    @ManyToOne
    @JoinColumn(name="playlist_id")
    private Playlist playlist;

}
