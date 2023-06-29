package it.uniroma3.siw.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.ImageRepository;
import jakarta.transaction.Transactional;

@Service
public class ArtistService {
	@Autowired
	private ArtistRepository artistRepository;
	
	@Autowired
	private ImageRepository imageRepository;


	@Transactional
	public Artist createNewArtist(Artist artist, MultipartFile fileUploadPhoto) throws IOException {
		artist.setPhoto(this.imageRepository.save(new Image(fileUploadPhoto.getName(), fileUploadPhoto.getBytes())));
		return this.artistRepository.save(artist);
	}

	@Transactional
	public Iterable<Artist> findAllArtist(){
		return this.artistRepository.findAll();
	}

	public List<Artist> findActorsNotInMovie(Long movieId) {
		List<Artist> actorsToAdd = new ArrayList<>();

		for (Artist a : this.artistRepository.findActorsNotInMovie(movieId)) {
			actorsToAdd.add(a);
		}
		return actorsToAdd;
	}
	
	@Transactional
	public Artist findArtistById(Long artistId){
		return this.artistRepository.findById(artistId).orElse(null);
	}

	@Transactional
	public boolean existArtistByByNameAndSurnameAndDateOfBirth(String name, String surname, LocalDate data) {
		return this.artistRepository.existsByNameAndSurnameAndDateOfBirth(name, surname,data);
	}
}
