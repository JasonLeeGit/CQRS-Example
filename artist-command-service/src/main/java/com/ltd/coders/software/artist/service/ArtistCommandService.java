package com.ltd.coders.software.artist.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ltd.coders.software.artist.dto.ArtistEvent;
import com.ltd.coders.software.artist.entity.Artist;
import com.ltd.coders.software.artist.repository.ArtistRepository;

@Service
public class ArtistCommandService {

	@Autowired
	private ArtistRepository repository;

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	public Artist createArtist(ArtistEvent artistEvent) {
		Artist artistDO = repository.save(artistEvent.getArtist());
		ArtistEvent event = new ArtistEvent("CreateArtist", artistDO);
		kafkaTemplate.send("artist-event-topic", event);
	
		return artistDO;
	}

	public Artist updateArtist(long id, ArtistEvent artistEvent) {
		Artist artistDO = null;
		Optional<Artist> existingArtist = repository.findById(id);
		Artist artist = artistEvent.getArtist();
		if(existingArtist.isPresent()){
			existingArtist.get().setAlbumName(artist.getAlbumName());
			existingArtist.get().setAlbumReleaseYear(artist.getAlbumReleaseYear());
			existingArtist.get().setArtistName(artist.getArtistName());
			existingArtist.get().setPrice(artist.getPrice());
		
			artistDO = repository.save(existingArtist.get());
			ArtistEvent event = new ArtistEvent("UpdateArtist", artistDO);
			kafkaTemplate.send("artist-event-topic", event);
		}

		return artistDO;
	}


}
