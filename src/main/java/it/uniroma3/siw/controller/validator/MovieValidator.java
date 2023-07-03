package it.uniroma3.siw.controller.validator;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.service.MovieService;

@Component
public class MovieValidator implements Validator {

	private final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);
	
	private final LocalDate MAX_DATE = LocalDate.now();


	@Autowired
	private MovieService movieService;

	@Override
	public void validate(Object o, Errors errors) {
		Movie movie = (Movie)o;
		if (movie.getTitle()!=null && movie.getYear()!=null 
				&& movieService.existMovieByTitleAndYear(movie.getTitle(), movie.getYear())) {
			errors.reject("movie.duplicate");
		}
		if(movie.getYear() < MIN_DATE.getYear())
			errors.reject("movie.minDate");
		
		if(movie.getYear() > MAX_DATE.getYear())
			errors.reject("movie.maxDate");
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Movie.class.equals(aClass);
	}
}