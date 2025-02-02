package com.ltd.coders.software.artist.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.ltd.coders.software.artist.ArtistCommandServiceApplication;
import com.ltd.coders.software.artist.dto.ArtistEvent;
import com.ltd.coders.software.artist.entity.Artist;
import com.ltd.coders.software.artist.repository.ArtistRepository;
import com.ltd.coders.software.artist.service.ArtistCommandService;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = { ArtistCommandServiceApplication.class })
@Testcontainers
public class ArtistCommandControllerServiceRepositoryTest {

	static final MySQLContainer<?> MY_SQL_CONTAINER;
    static {
        MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest");
        MY_SQL_CONTAINER.start();
    }    
    
    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url",() -> MY_SQL_CONTAINER.getJdbcUrl());
        registry.add("spring.datasource.username",() -> MY_SQL_CONTAINER.getUsername());
        registry.add("spring.datasource.password",() -> MY_SQL_CONTAINER.getPassword());
        registry.add("spring.jpa.hibernate.ddl-auto",() -> "create");
    }
	
	@SuppressWarnings("deprecation")
	@Container
	@ServiceConnection
	static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
	
	@SuppressWarnings("deprecation")
	@DynamicPropertySource
	public static void initKafkaProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
	}
    
    @Autowired
    private ArtistRepository repository;
	@Autowired
	private ArtistCommandService service;
	private ArtistEvent artistEvent;
	private Artist artist = new Artist();

	@BeforeEach
	public void setUp() throws Exception {
		artist.setArtistName("ArtistName");
		artist.setAlbumName("AlbumName");
		artist.setAlbumReleaseYear("1234");
		artist.setPrice(10.99);
	}

	@Order(1)
	@Test
	public void createArtistTest() {
		artistEvent = new ArtistEvent("CreateArtist", artist);

		Artist savedArtist = service.createArtist(artistEvent);
		Artist existingArtist = repository.findById(savedArtist.getId()).get();

		assertNotNull(existingArtist);
		assertEquals(existingArtist.getAlbumName(), "AlbumName");
		assertEquals(existingArtist.getArtistName(), "ArtistName");
		assertEquals(existingArtist.getAlbumReleaseYear(), "1234");

		repository.deleteById(existingArtist.getId());
	}

	@Order(2)
	@Test
	public void updateArtistTest() {
		artistEvent = new ArtistEvent("UpdateArtist", artist);

		Artist savedArtist = service.createArtist(artistEvent);

		artistEvent.getArtist().setArtistName("UpdatedArtistName");
		Artist updatedArtist = service.updateArtist(savedArtist.getId(), artistEvent);

		assertNotNull(updatedArtist);
		assertEquals(updatedArtist.getAlbumName(), "AlbumName");
		assertEquals(updatedArtist.getArtistName(), "UpdatedArtistName");
		assertEquals(updatedArtist.getAlbumReleaseYear(), "1234");

		repository.deleteById(updatedArtist.getId());
	}

}
