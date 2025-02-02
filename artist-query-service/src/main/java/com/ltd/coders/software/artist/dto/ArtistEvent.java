package com.ltd.coders.software.artist.dto;

import com.ltd.coders.software.artist.entity.Artist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistEvent {
	
	private String eventType;
	private Artist artist;
}
