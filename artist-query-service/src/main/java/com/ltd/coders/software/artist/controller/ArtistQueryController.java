package com.ltd.coders.software.artist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ltd.coders.software.artist.entity.Artist;
import com.ltd.coders.software.artist.service.ArtistQueryService;

@RequestMapping("/artists")
@RestController
public class ArtistQueryController {

	@Autowired
	private ArtistQueryService queryService;

	@GetMapping
	public List<Artist> fetchAllArtists() {
		return queryService.getArtists();
	}
}
