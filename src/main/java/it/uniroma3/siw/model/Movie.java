package it.uniroma3.siw.model;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Movie {
    
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
    
        @NotBlank
        private String title;
        
        @NotNull
        @Min(1900)
        @Max(2023)
        private Integer year;
        
        
        @ManyToOne
        private Artist director;
        
        @ManyToMany
        private Set<Artist> actors;
        
        @OneToMany(mappedBy = "movieReviewed", cascade = CascadeType.ALL)
        private Set<Review> reviews;
        
        @OneToOne(cascade = CascadeType.ALL)
        private Image poster;
        
        @OneToMany(cascade = CascadeType.ALL)
        @JoinColumn(name = "scenes_id")
        private Set<Image> scenes;
        
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
    
        public Integer getYear() {
            return year;
        }
    
        public void setYear(Integer year) {
            this.year = year;
        }
        
        public Image getPoster() {
			return poster;
		}

		public void setPoster(Image poster) {
			this.poster = poster;
		}

		public Artist getDirector() {
            return director;
        }
    
        public void setDirector(Artist director) {
            this.director = director;
        }
    
        public Set<Artist> getActors() {
            return actors;
        }
    
        public void setActors(Set<Artist> actors) {
            this.actors = actors;
        }
        
		public Set<Review> getReviews() {
			return reviews;
		}

		public void setReviews(Set<Review> reviews) {
			this.reviews = reviews;
		}

		public Set<Image> getScenes() {
			return scenes;
		}

		public void setScenes(Set<Image> scenes) {
			this.scenes = scenes;
		}

		@Override
        public int hashCode() {
            return Objects.hash(title, year);
        }
    
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Movie other = (Movie) obj;
            return Objects.equals(title, other.title) && year.equals(other.year);
        }
    }
