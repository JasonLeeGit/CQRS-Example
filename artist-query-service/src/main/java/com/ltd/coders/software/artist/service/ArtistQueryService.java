package com.ltd.coders.software.artist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ltd.coders.software.artist.dto.ArtistEvent;
import com.ltd.coders.software.artist.entity.Artist;
import com.ltd.coders.software.artist.repository.ArtistRepository;

@Service
public class ArtistQueryService {

	@Autowired
	private ArtistRepository repository;

	public List<Artist> getArtists() {
		return repository.findAll();
	}

	@KafkaListener (topics = "artist-event-topic", groupId = "artist-event-group")
	public void processArtistEvents(ArtistEvent artistEvent) {
		
		Artist artist = artistEvent.getArtist();
		
		if (artistEvent.getEventType().equals("CreateArtist")) {
			repository.save(artist);
		}
		
		if (artistEvent.getEventType().equals("UpdateArtist")) {
			Artist existingArtist = repository.findById(artist.getId()).get();
			
			existingArtist.setAlbumName(artist.getAlbumName());
			existingArtist.setArtistName(artist.getArtistName());
			existingArtist.setAlbumReleaseYear(artist.getAlbumReleaseYear());
			existingArtist.setPrice(artist.getPrice());

			repository.save(existingArtist);
		}
	}
}
