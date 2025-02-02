package com.ltd.coders.software.artist;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltd.coders.software.artist.dto.ArtistEvent;
import com.ltd.coders.software.artist.entity.Artist;
import com.ltd.coders.software.artist.service.ArtistCommandService;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = ArtistCommandServiceApplication.class)
//@ActiveProfiles({"test"})
public abstract class ControllerJsonMapper {
    
	@Autowired
	protected WebApplicationContext webApplicationContext;
	protected MockMvc mockMvc;
	
	@MockitoBean
	protected ArtistCommandService mockArtistCommandService;
	protected Artist artist;
	protected ArtistEvent artistEvent;

	@BeforeEach
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		artist = new Artist();
		artist.setArtistName("ArtistName");
		artist.setAlbumName("AlbumName");
		artist.setAlbumReleaseYear("1234");
		artist.setPrice(10.99);
		artistEvent = new ArtistEvent("CreateArtist", artist);
	}

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}
}