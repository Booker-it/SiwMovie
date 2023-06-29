package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.utility.FileUploadUtil;
import it.uniroma3.siw.controller.validator.ArtistValidator;
import it.uniroma3.siw.controller.validator.FileValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.service.ArtistService;
import jakarta.validation.Valid;

@Controller
public class ArtistController {

	@Autowired
	private ArtistService artistService;

	@Autowired
	private ArtistValidator artistValidator;
	
	@Autowired
	private FileValidator fileValidator;
	
	@GetMapping(value="/admin/formNewArtist")
	public String formNewArtist(Model model) {
		model.addAttribute("artist", new Artist());
		return "admin/formNewArtist.html";
	}

	@GetMapping(value="/admin/indexArtist")
	public String indexArtist() {
		return "admin/indexArtist.html";
	}


	@PostMapping("/admin/artist")
	public String newArtist(@Valid @ModelAttribute("artist") Artist artist,BindingResult bindingResultArtist,
							@ModelAttribute FileUploadUtil fileUpload, BindingResult bindingResultFile, Model model) {
		this.artistValidator.validate(artist, bindingResultArtist);
		this.fileValidator.validate(fileUpload, bindingResultFile);
		if (!bindingResultArtist.hasErrors() && !bindingResultFile.hasErrors()) {
		try {
			model.addAttribute("artist", this.artistService.createNewArtist(artist, fileUpload.getImage()) );
			return "artist.html";
		} catch (IOException e) {
			// TODO: handle exception
			//aggiungere informazione per utente per l'errore
			return "admin/formNewArtist.html";
		}
			
		} else {
//			model.addAttribute("messaggioErrore", "Questo artista esiste già");
			return "admin/formNewArtist.html"; 
		}
	}


	@GetMapping("/artist/{id}")
	public String getArtist(@PathVariable("id") Long id, Model model) {
		Artist artist = this.artistService.findArtistById(id);
		if(artist != null) {
			model.addAttribute("artist", artist);
			return "artist.html";
		}
		return "artistError.html";
	}

	@GetMapping("/artist")
	public String getArtists(Model model) {
		model.addAttribute("artists", this.artistService.findAllArtist());
		return "artists.html";
	}
}
