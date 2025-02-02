package com.ltd.coders.software.artist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ltd.coders.software.artist.entity.Artist;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
}