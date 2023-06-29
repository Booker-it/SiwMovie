package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.ReviewService;

@Controller
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	

	
	
	/******************* ADMIN ********************/
	@GetMapping(value="/admin/removeReview/{reviewId}")
	public String removeReviewOfMovieByAdmin(@PathVariable("reviewId") Long reviewId){
		Review review = this.reviewService.findReviewById(reviewId);
		
		if(review == null)
			return "/errors/reviewError.html";
		
		Movie movie = review.getReviewed();
		movie.getReviews().remove(review);
		this.reviewService.removeReviewByAdmin(reviewId);
		
		
		return "redirect:/admin/formUpdateMovie/"+ movie.getId();
	}
	


}
