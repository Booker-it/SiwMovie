package it.uniroma3.siw.controller.utility;

import java.util.Collection;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	
	private MultipartFile image;
	
	private Collection<MultipartFile> scenesMovie;

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public Collection<MultipartFile> getSceneMovie() {
		return scenesMovie;
	}

	public void setSceneMovie(Collection<MultipartFile> sceneMovie) {
		this.scenesMovie = sceneMovie;
	}
	
}
