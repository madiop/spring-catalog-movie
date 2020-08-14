package io.javabrains.moviecatalogservice.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.UserRating;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

		// get all rated movies
		UserRating ratings = restTemplate.getForObject("http://movie-rating-service/rating/user/" + userId, UserRating.class);
		
		
		return ratings.getUserRating().stream().map(rating -> {
			// for each movie Id, call movie info service and get details
			Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);

		    // Put them all together			
			return new CatalogItem(movie.getName(), "Test", rating.getRating());
		})
		.collect(Collectors.toList());
		
	}

}
/*
	
	@Autowired
	private WebClient.Builder webClientBuilder;
Movie movie = webClientBuilder.build()
       .get()
       .uri("http://localhost:8082/movies/" + rating.getMovieId())
       .retrieve()
       .bodyToMono(Movie.class)
       .block();
*/