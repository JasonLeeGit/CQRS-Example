package com.ltd.coders.software.artist.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.ltd.coders.software.artist.ControllerJsonMapper;
import com.ltd.coders.software.artist.entity.Artist;

@Testcontainers
public class ArtistCommandControlleRestTest extends ControllerJsonMapper {

	static final MySQLContainer<?> MY_SQL_CONTAINER;
	static {
		MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest");
		MY_SQL_CONTAINER.start();
	}

	@DynamicPropertySource
	static void configureTestProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", () -> MY_SQL_CONTAINER.getJdbcUrl());
		registry.add("spring.datasource.username", () -> MY_SQL_CONTAINER.getUsername());
		registry.add("spring.datasource.password", () -> MY_SQL_CONTAINER.getPassword());
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
	}

	@BeforeEach
	public void setUp() throws Exception {
		super.setUp();
	}

	@Order(1)
	@Test
	public void createArtistWithValidTest() throws Exception {
		when(mockArtistCommandService.createArtist(artistEvent)).thenReturn(artist);
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.post("/artist").content(asJsonString(artistEvent))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		String content = mvcResult.getResponse().getContentAsString();
		Artist results = mapFromJson(content, Artist.class);

		verify(mockArtistCommandService, times(1)).createArtist(artistEvent);

		assertNotNull(results);

		assertEquals(results.getAlbumName(), "AlbumName");
		assertEquals(results.getArtistName(), "ArtistName");
		assertEquals(results.getAlbumReleaseYear(), "1234");
		assertEquals(results.getPrice(), 10.99);
	}

	@Order(2)
	@Test
	public void createArtistWithInvalidTest() throws Exception {
		artistEvent.getArtist().setArtistName("");
		when(mockArtistCommandService.createArtist(artistEvent)).thenReturn(artist);
		mockMvc.perform(MockMvcRequestBuilders.post("/artist").content(asJsonString(artistEvent))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Order(3)
	@Test
	public void updateArtistWithValidTest() throws Exception {
		artistEvent.getArtist().setArtistName("ArtistName");
		when(mockArtistCommandService.updateArtist(1L, artistEvent)).thenReturn(artist);
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.put("/artist/1").content(asJsonString(artistEvent))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		String content = mvcResult.getResponse().getContentAsString();

		Artist results = mapFromJson(content, Artist.class);

		verify(mockArtistCommandService, times(1)).updateArtist(1L, artistEvent);

		assertEquals(results.getAlbumName(), "AlbumName");
		assertEquals(results.getArtistName(), "ArtistName");
		assertEquals(results.getAlbumReleaseYear(), "1234");
	}

	@Order(4)
	@Test
	public void updateArtistWithInvalidTest() throws Exception {
		artistEvent.getArtist().setArtistName("");
		when(mockArtistCommandService.updateArtist(0L, artistEvent)).thenReturn(null);
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.put("/artist/0").content(asJsonString(artistEvent))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError()).andReturn();

		verify(mockArtistCommandService, times(0)).updateArtist(0, artistEvent);
	
		List<Object> header = mvcResult.getResponse().getHeaderValues("Error message");
		assertEquals(header.get(0), "Error artist failed validation");
	}

}
