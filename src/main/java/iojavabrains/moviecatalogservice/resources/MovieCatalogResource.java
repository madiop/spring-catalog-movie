package iojavabrains.moviecatalogservice.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import iojavabrains.moviecatalogservice.models.CatalogItem;
import iojavabrains.moviecatalogservice.models.Movie;
import iojavabrains.moviecatalogservice.models.Rating;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	
	@GetMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

		RestTemplate restTemplate =  new RestTemplate();
		
		// get all rated movies
		List<Rating> ratings = Arrays.asList(
				new Rating("1234", 4),
				new Rating("5678", 5)
			);
		
		// for each movie Id, call movie info service and get details
		return ratings.stream().map(rating -> {
			Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
			return new CatalogItem(movie.getName(), "Test", rating.getRating());
			})
			.collect(Collectors.toList());
		
	    // Put them all together
	}

}