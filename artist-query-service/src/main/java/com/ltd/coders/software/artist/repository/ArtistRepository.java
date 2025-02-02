package com.ltd.coders.software.artist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ltd.coders.software.artist.entity.Artist;

public interface ArtistRepository extends JpaRepository<Artist,Long> {
}
