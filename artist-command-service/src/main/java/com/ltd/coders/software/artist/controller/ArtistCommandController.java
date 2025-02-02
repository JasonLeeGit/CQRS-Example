package com.ltd.coders.software.artist.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ltd.coders.software.artist.dto.ArtistEvent;
import com.ltd.coders.software.artist.entity.Artist;
import com.ltd.coders.software.artist.service.ArtistCommandService;

@RestController
@RequestMapping("/artist")
public class ArtistCommandController {

	private ArtistCommandService commandService;

	public ArtistCommandController(ArtistCommandService commandService) {
		this.commandService = commandService;
	}

	@PostMapping
	public ResponseEntity<Artist> createArtist(@RequestBody ArtistEvent artistEvent) {
		Artist createdArtist = null;
		if (isArtistValid(artistEvent.getArtist())) {
			createdArtist = commandService.createArtist(artistEvent);
		}
		if (createdArtist != null) {
			return ResponseEntity.ok(createdArtist);
		} else {
			MultiValueMap<String, String> headers = new HttpHeaders();
			headers.add("Error message", "Error failed to insert new artist");
			return new ResponseEntity<Artist>(headers, HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Artist> updateArtist(@PathVariable long id, @RequestBody ArtistEvent artistEvent) {
		ResponseEntity<Artist> response = null;
		if (isArtistValid(artistEvent.getArtist()) && id > 0L) {
			Artist updatedArtist = commandService.updateArtist(id, artistEvent);
			if (updatedArtist != null) {
				response = ResponseEntity.ok(updatedArtist);
			} else {
				MultiValueMap<String, String> headers = new HttpHeaders();
				headers.add("Error message", "Error failed to update artist");
				response = new ResponseEntity<Artist>(headers, HttpStatus.BAD_REQUEST);
			}
		} else {
			MultiValueMap<String, String> headers = new HttpHeaders();
			headers.add("Error message", "Error artist failed validation invalid id");
			response = new ResponseEntity<Artist>(headers, HttpStatus.BAD_REQUEST);
		}
		return response;
	}

	private boolean isArtistValid(Artist artist) {
		if (artist.getArtistName() != null && !artist.getArtistName().isEmpty() 
				&& artist.getAlbumName() != null && !artist.getAlbumName().isEmpty() 
				&& artist.getAlbumReleaseYear() != null && !artist.getAlbumReleaseYear().isEmpty() 
				&& artist.getPrice() > 0.0) {
			return true;
		} else {
			return false;
		}

	}
}
