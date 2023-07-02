package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.ImageRepository;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.ReviewRepository;
import jakarta.transaction.Transactional;

@Service
public class MovieService {

	@Autowired 
	private MovieRepository movieRepository;

	@Autowired
	private ArtistRepository artistRepository;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private UserService userService;

	@Transactional
	public Movie createNewMovie (Movie movie, MultipartFile fileUploadLocandina, Collection<MultipartFile> fileUploadScenes) throws IOException{

		/* viene salvata l'immagine della locandina del film*/
		movie.setPoster(this.imageRepository.save(new Image(fileUploadLocandina.getName(),fileUploadLocandina.getBytes())));

		/* vengono salvate alcune immagini/scene del film */
		Set<Image> scenesMovie = new HashSet<>();

		for(MultipartFile scene : fileUploadScenes) {
			if(!scene.isEmpty()) {
				scenesMovie.add(this.imageRepository.save(new Image(scene.getName(),scene.getBytes())));
			}
		}
		movie.setScenes(scenesMovie);
		return this.movieRepository.save(movie);
	}


	//sulle operazioni di sola lettura andrebbe usato @Transactional(readOnly =true)
	public Movie findMovieById(Long id) {
		return this.movieRepository.findById(id).orElse(null);
	}

	@Transactional
	public Iterable<Movie> findAllMovie () {
		return this.movieRepository.findAll();
	}

	@Transactional
	public Movie saveMovie(Movie movie) {
		return this.movieRepository.save(movie);
	}

	@Transactional
	public Movie saveDirectorToMovie(Long movieId,Long directorId) {
		Movie res = null;
		Artist director = this.artistRepository.findById(directorId).orElse(null);
		Movie movie = this.movieRepository.findById(movieId).orElse(null);
		if(movie!=null && director !=null) {
			movie.setDirector(director);
			this.movieRepository.save(movie);
			res=movie;
		}
		return res;
	}

	public List<Movie> findMovieByYear(int year) {
		return this.movieRepository.findByYear(year);
	}

	@Transactional
	public Movie saveActorToMovie(Long actorId, Long movieId) {
		Movie movie = this.movieRepository.findById(movieId).orElse(null);
		Artist actor = this.artistRepository.findById(actorId).orElse(null);
		if(movie!=null && actor!=null) {
			Set<Artist> actors = movie.getActors();
			actors.add(actor);
			this.movieRepository.save(movie);
		}
		return movie;
	}

	@Transactional
	public Movie removeActorFromMovie(Long actorId, Long movieId) {
		Movie movie = this.movieRepository.findById(movieId).orElse(null);
		Artist actor = this.artistRepository.findById(actorId).orElse(null);
		if(movie!=null && actor!=null) {
			Set<Artist> actors = movie.getActors();
			actors.remove(actor);
			this.movieRepository.save(movie);
		}
		return movie;
	}

	@Transactional
	public Movie removeDirectorFromMovie(Long directorId, Long movieId) {
		Movie movie = this.movieRepository.findById(movieId).orElse(null);
		Artist director = this.artistRepository.findById(directorId).orElse(null);
		if(movie!=null && director!=null) {
			movie.setDirector(null);
			this.movieRepository.save(movie);
		}
		return movie;
	}

	@Transactional
	public boolean existMovieByTitleAndYear(String title, Integer year) {
		return this.movieRepository.existsByTitleAndYear(title, year);
	}


	@Transactional
	public void removeMovie(Long id) {
		Movie movie = this.movieRepository.findById(id).orElse(null);
		Artist director = movie.getDirector();
		Image poster  = movie.getPoster();

		if(director!=null)
			director.getDirectedMovies().remove(movie);

		if(poster!=null)
			this.imageRepository.delete(poster);

		Set<Artist> attori = movie.getActors();
		for(Artist attore : attori) {
			attore.getStarredMovies().remove(movie);
		}

		this.movieRepository.delete(movie);
	}



	@Transactional
	public Movie newReviewToMovie(Review review,Long movieId) {
		Movie movie = this.movieRepository.findById(movieId).orElse(null);

		review.setAuthor(this.userService.getCurrentUser());
		review.setReviewed(movie);

		movie.getReviews().add(review);

		this.reviewRepository.save(review);
		this.movieRepository.save(movie);


		return movie;
	}

	@Transactional
	public Movie updatePosterToMovie(Long movieId, MultipartFile fileUpload) throws IOException{
		Movie movie = this.movieRepository.findById(movieId).orElse(null);

		Image poster = movie.getPoster();

		if(!fileUpload.isEmpty()) {
			if(poster == null) {
				movie.setPoster(new Image(fileUpload.getName(),fileUpload.getBytes()));
			}
			else {
				poster.setName(fileUpload.getName());
				poster.setData(fileUpload.getBytes());
			}
		}

		return this.movieRepository.save(movie);
	}


	@Transactional
	public Movie updateScenesToMovie(Long movieId, Collection<MultipartFile> fileUpload) throws IOException{
		Movie movie = this.movieRepository.findById(movieId).orElse(null);
		movie.getScenes().clear();
		
		for(MultipartFile scene : fileUpload) {

			if(!scene.isEmpty()) {
				if(movie.getScenes() == null) {
					movie.getScenes().add(new Image(scene.getName(),scene.getBytes()));
				}
				else {
					movie.getScenes().add(new Image(scene.getName(),scene.getBytes()));

				}

			}

		}

		return this.movieRepository.save(movie);
	}
}


