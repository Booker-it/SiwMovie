package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.ReviewService;

@Component
public class ReviewValidator implements Validator{
	
	@Autowired
	private ReviewService reviewService;;
	
	
	@Override
	public void validate(Object o, Errors errors) {
		Review review = (Review)o;
		if(review.getTitle() != null && review.getText() != null && review.getAuthor() != null
			&& review.getRating() != null 
			&& this.reviewService.existsReviewByTitleAndAuthorAndTextAndMovieReviewed(review.getTitle(), review.getAuthor(), review.getText(), review.getReviewed()))
			errors.reject("review.duplicate");
		
		if(review.getRating()<1)
			errors.reject("Min.review.rating");
		if(review.getRating()>5)
			errors.reject("Max.review.rating");
		
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Review.class.equals(clazz);
	}

}
