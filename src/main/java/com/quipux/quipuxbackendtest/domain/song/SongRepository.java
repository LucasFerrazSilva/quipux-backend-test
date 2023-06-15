package com.quipux.quipuxbackendtest.domain.song;

import com.quipux.quipuxbackendtest.domain.playlist.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {

    List<Song> findByPlaylist(Playlist playlist);

}
