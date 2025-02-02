package com.ltd.coders.software.artist.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ARTIST_QUERY")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Artist {
	
	@Id
	private Long id;
	private String artistName;
	private String albumName;
	private String albumReleaseYear;
	private double price;
}
