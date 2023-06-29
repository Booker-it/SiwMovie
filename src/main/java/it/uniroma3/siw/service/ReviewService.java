package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ReviewRepository;
import jakarta.transaction.Transactional;

@Service
public class ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	
	public boolean existsReviewByTitleAndAuthorAndTextAndMovieReviewed(String title, User author, String text, Movie movieReviewed) {
		return this.reviewRepository.existsByTitleAndAuthorAndTextAndMovieReviewed(title, author, text, movieReviewed);
	}
	

	@Transactional
	public void createNewReview (Review review) {
		this.createNewReview(review);
	}
	
	
	@Transactional
	public Review findReviewById(Long reviewId) {
		return this.reviewRepository.findById(reviewId).orElse(null);
	}
	
	
	@Transactional
	public void removeReviewByAdmin(Long reviewId) {
		this.reviewRepository.deleteById(reviewId);
	}
	

}
