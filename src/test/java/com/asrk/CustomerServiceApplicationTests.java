package com.asrk;

import com.asrk.client.MovieClient;
import com.asrk.domain.Genre;
import com.asrk.dto.CustomerDto;
import com.asrk.dto.GenreUpdateRequest;
import com.asrk.dto.MovieDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.ProblemDetail;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;

@Import(TestcontainersConfiguration.class)
@MockitoBean(types= {RestClient.class, MovieClient.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerServiceApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(CustomerServiceApplicationTests.class);

	@Autowired
	TestRestTemplate template;

	@Autowired
	private MovieClient client;

	@Test
	void contextLoads() {
	}

	@Test
	public void health(){
		var responseEntity = this.template.getForEntity("/actuator/health", Object.class);
		Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
	}

	@Test
	void customerWithMovies() {
		Mockito.when(client.getMovies(Mockito.any(Genre.class))).thenReturn(List.of(
			new MovieDto(1, "movie-1", 1990, Genre.ACTION),
			new MovieDto(2,"movie-2", 1991, Genre.ACTION)
		));

		var responseEntity = this.template.getForEntity("/api/customers/1", CustomerDto.class);
		Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		var customerDto = responseEntity.getBody();
		Assertions.assertNotNull(customerDto);
		Assertions.assertEquals("sam", customerDto.name());
		Assertions.assertEquals(2, customerDto.recommendedMovies().size());
	}

	@Test
	public void customerNotFoundScenario(){
		var responseEntity = this.template.getForEntity("/api/customers/10", ProblemDetail.class);
		Assertions.assertTrue(responseEntity.getStatusCode().is4xxClientError());
		var problemDetail = responseEntity.getBody();
		log.info("problem detail : {}", problemDetail);
		Assertions.assertNotNull(problemDetail);
		Assertions.assertEquals("Customer not found ", problemDetail.getTitle());
	}

	@Test
	public void updateGenre()
	{
		var genreUpdateRequest = new GenreUpdateRequest(Genre.DRAMA);
		var requestEntity = new RequestEntity<>(genreUpdateRequest, HttpMethod.PATCH, URI.create("/api/customers/1/genre"));
		var responseEntity = this.template.exchange(requestEntity, Void.class);
		Assertions.assertEquals(204, responseEntity.getStatusCode().value());
	}


}
