package com.quipux.quipuxbackendtest.domain.playlist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, String> {

    Optional<Playlist> findByNome(String nome);

}
