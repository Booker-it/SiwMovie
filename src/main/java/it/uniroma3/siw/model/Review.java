package it.uniroma3.siw.model;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String title;

	@Min(1)
	@Max(10)
	private Integer rating;

	@Length(max = 200)
	@NotBlank
	private String text;

	@ManyToOne
	private Movie movieReviewed;

	@ManyToOne
	private User author;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Movie getReviewed() {
		return movieReviewed;
	}

	public void setReviewed(Movie reviewed) {
		this.movieReviewed = reviewed;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

//	@Override
//	public int hashCode() {
//		return Objects.hash(author, rate, text, title);
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Review other = (Review) obj;
//		return Objects.equals(author, other.author) && Objects.equals(rate, other.rate)
//				&& Objects.equals(text, other.text) && Objects.equals(title, other.title);
//	}

	
}
