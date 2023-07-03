package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.controller.utility.FileUploadUtil;
import it.uniroma3.siw.controller.validator.FileValidator;
import it.uniroma3.siw.controller.validator.MovieValidator;
import it.uniroma3.siw.controller.validator.ReviewValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;

@Controller
public class MovieController {
	@Autowired 
	private MovieService movieService;

	@Autowired
	private ArtistService artistService;

	@Autowired 
	private MovieValidator movieValidator;

	@Autowired
	private FileValidator fileValidator;
	
	@Autowired
	private ReviewValidator reviewValidator;
	
	@Autowired
	private UserService userService;

	@GetMapping(value="/admin/formNewMovie")
	public String formNewMovie(Model model) {
		model.addAttribute("movie", new Movie());
		return "admin/formNewMovie.html";
	}

	@GetMapping(value="/admin/formUpdateMovie/{id}")
	public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
		Movie movie = movieService.findMovieById(id);
		if(movie!=null)
			model.addAttribute("movie", movie);
		else {
			return "/errors/movieError.html";
		}
		return "admin/formUpdateMovie.html";
	}

	@GetMapping(value="/admin/indexMovie")
	public String indexMovie() {
		return "admin/indexMovie.html";
	}

	@GetMapping(value="/admin/manageMovies")
	public String manageMovies(Model model) {
		model.addAttribute("movies", this.movieService.findAllMovie());
		return "admin/manageMovies.html";
	}

	@GetMapping(value="/admin/setDirectorToMovie/{directorId}/{movieId}")
	public String setDirectorToMovie(@PathVariable("directorId") Long directorId, @PathVariable("movieId") Long movieId, Model model) {

		Movie movie = this.movieService.saveDirectorToMovie(movieId, directorId); 
		if(movie != null) {
			model.addAttribute("movie", movie);
			return "admin/formUpdateMovie.html";
		}
		else {
			return "/errors/movieError.html";
		}
	}


	@GetMapping(value="/admin/addDirector/{id}")
	public String addDirector(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artists", this.artistService.findAllArtist());
		Movie movie =  this.movieService.findMovieById(id);
		if(movie != null) {
			model.addAttribute("movie",movie);
			return "admin/directorsToAdd.html";
		}
		return "/errors/movieError.html";
	}

	@PostMapping("/admin/movie")
	public String newMovie(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResultMovie, 
			@Valid @ModelAttribute FileUploadUtil fileUpload, BindingResult bindingResultFileUpload, Model model) {

		this.movieValidator.validate(movie, bindingResultMovie);
		this.fileValidator.validate(fileUpload, bindingResultFileUpload);

		if (!bindingResultMovie.hasErrors() && !bindingResultFileUpload.hasErrors()) {
			try {
				model.addAttribute("movie",this.movieService.createNewMovie(movie, fileUpload.getImage(), fileUpload.getScenesMovie())); 
				return "movie.html";
			}
			catch (IOException e) {
				// TODO: handle exception
				//aggiungere informazione per utente per l'errore
				return "/errors/genericError.html";
			}
		} 
		else {
			return "admin/formNewMovie.html"; 
		}
	}


	@GetMapping("/movie/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		Movie movie = this.movieService.findMovieById(id);
		if(movie != null) {
			model.addAttribute("movie", movie);
			return "movie.html";
		}
		else
			return "/errors/movieError.html";
	}

	@GetMapping("/movie")
	public String getMovies(Model model) {		
		model.addAttribute("movies", this.movieService.findAllMovie());
		return "movies.html";
	}

	@GetMapping("/formSearchMovies")
	public String formSearchMovies() {
		return "formSearchMovies.html";
	}

	@PostMapping("/searchMovies")
	public String searchMovies(Model model, @RequestParam int year) {
		model.addAttribute("movies", this.movieService.findMovieByYear(year));
		return "foundMovies.html";
	}

	@GetMapping("/admin/updateActors/{id}")
	public String updateActors(@PathVariable("id") Long id, Model model) {

		List<Artist> actorsToAdd = this.artistService.findActorsNotInMovie(id);
		model.addAttribute("actorsToAdd", actorsToAdd);
		Movie movie =  this.movieService.findMovieById(id);
		if(movie !=null) {
			model.addAttribute("movie",movie);
			return "admin/actorsToAdd.html";
		}
		return "/errors/movieError.html";
	}

	@GetMapping(value="/admin/addActorToMovie/{actorId}/{movieId}")
	public String addActorToMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {

		Movie movie = this.movieService.saveActorToMovie(actorId, movieId);
		if(movie!=null) {
			List<Artist> actorsToAdd = this.artistService.findActorsNotInMovie(movieId);
			model.addAttribute("movie", movie);
			model.addAttribute("actorsToAdd", actorsToAdd);

			return "admin/actorsToAdd.html";
		}
		else
			return "/errors/movieError.html";
	}

	@GetMapping(value="/admin/removeActorFromMovie/{actorId}/{movieId}")
	public String removeActorFromMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
		Movie movie = this.movieService.removeActorFromMovie(actorId, movieId);

		if(movie!=null) {
			List<Artist> actorsToAdd = this.artistService.findActorsNotInMovie(movieId);

			model.addAttribute("movie", movie);
			model.addAttribute("actorsToAdd", actorsToAdd);

			return "admin/actorsToAdd.html";
		}
		else
			return "/errors/movieError.html";

	}

	@GetMapping(value="/admin/removeDirectorFromMovie/{directorId}/{movieId}")
	public String removeDirectorFromMovie(@PathVariable("directorId") Long directorId, @PathVariable("movieId") Long movieId, Model model) {
		Movie movie = this.movieService.removeDirectorFromMovie(directorId, movieId);

		if(movie!=null) {
			model.addAttribute("movie", movie);
			return "admin/formUpdateMovie.html";
		}
		else
			return "/errors/movieError.html";
	}


	@GetMapping(value="/admin/removeMovie/{movieId}")
	public String removeMovie(@PathVariable("movieId") Long movieId, Model model) {
		this.movieService.removeMovie(movieId);

		return "/admin/indexMovie.html";
	}
	
	
	/*************************** Poster Update Movie******************************/
	
	@GetMapping(value="/admin/posterChange/{movieId}")
	public String posterChange(@PathVariable("movieId") Long movieId, Model model) {
		Movie movie = movieService.findMovieById(movieId);
		if(movie!=null)
			model.addAttribute("movie", movie);
		else {
			return "/errors/movieError.html";
		}
		return "/admin/posterChange.html";
	}
	
	@PostMapping(value="/admin/updatePosterToMovie/{movieId}")
	public String updatePosterToMovie(@PathVariable("movieId") Long movieId, 
			@ModelAttribute FileUploadUtil fileUpload, BindingResult bindingResult, Model model) {

		this.fileValidator.validate(fileUpload, bindingResult);

		if (!bindingResult.hasErrors()) {
			try {
				model.addAttribute("movie",this.movieService.updatePosterToMovie(movieId,fileUpload.getImage())); 
				return "redirect:/admin/formUpdateMovie/" + movieId;
			}
			catch (IOException e) {
				return "admin/manageMovies.html";
			}
		} 
		else {
			return "admin/manageMovies.html"; 
		}
	}
	
	/*************************** Scenes Update Movie******************************/
	
	@PostMapping(value="/admin/updateScenesToMovie/{movieId}")
	public String updateScenesToMovie(@PathVariable("movieId") Long movieId, 
			@ModelAttribute FileUploadUtil fileUpload, BindingResult bindingResult, Model model) {

		this.fileValidator.validate(fileUpload, bindingResult);

		if (!bindingResult.hasErrors()) {
			try {
				model.addAttribute("movie",this.movieService.updateScenesToMovie(movieId,fileUpload.getScenesMovie())); 
				return "redirect:/admin/formUpdateMovie/" + movieId;
			}
			catch (IOException e) {
				return "admin/manageMovies.html";
			}
		} 
		else {
			return "admin/manageMovies.html"; 
		}
	}
	
	@GetMapping(value="/admin/scenesChange/{movieId}")
	public String scenesChange(@PathVariable("movieId") Long movieId, Model model) {
		Movie movie = movieService.findMovieById(movieId);
		if(movie!=null)
			model.addAttribute("movie", movie);
		else {
			return "/errors/movieError.html";
		}
		return "/admin/scenesChange.html";
	}
	
	
	/************************************************** REVIEWS **************************************************/
	
	
	@GetMapping(value="/registered/formAddReview/{idMovie}")
	public String addReview(@PathVariable("idMovie") Long id, Model model) {
		model.addAttribute("movie", this.movieService.findMovieById(id));
		model.addAttribute("review", new Review());
		return "/registered/formAddReview.html";
	}
	
	@PostMapping(value="/registered/addReviewToMovie/{movieId}")
	public String addReviewToMovieByUser(@Valid @ModelAttribute Review review, BindingResult bindingResult, @PathVariable("movieId") Long movieId, Model model) {
		if(this.userService.checkIfUserAlreadyReviewedAfilm(this.userService.getCurrentUser(), this.movieService.findMovieById(movieId)))
			return "errors/alreadyReviewed.html";
		
		this.reviewValidator.validate(review, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.movieService.newReviewToMovie(review,movieId);
			model.addAttribute("review", review);
			return "redirect:/movie/" + movieId;   //senza redirect dà errore sul templating
			
		}	
		model.addAttribute("movie", this.movieService.findMovieById(movieId));
		return "/registered/formAddReview.html";
	}
}
