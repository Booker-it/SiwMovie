package it.uniroma3.siw.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.utility.FileUploadUtil;

@Component
public class FileValidator implements Validator{


	@Override
	public void validate(Object o, Errors errors) {
		FileUploadUtil fileUpload = (FileUploadUtil) o;
		if( fileUpload.getImage()==null && fileUpload.getImage().isEmpty()){

			errors.reject("fileUpload.noFile");
		}

		if(!fileUpload.getImage().getOriginalFilename().endsWith(".png") &&
				!fileUpload.getImage().getOriginalFilename().endsWith(".jpg") && !fileUpload.getImage().getOriginalFilename().endsWith(".jpeg")){

			errors.reject("fileUpload.invalidFormat");
		}

		if(fileUpload.getScenesMovie() != null) { //aggiunto perché sull'upload della foto dell'artista non ci sono scene multiple e andava in errore
			for(MultipartFile sceneMovies: fileUpload.getScenesMovie()) {

				if(!sceneMovies.getOriginalFilename().endsWith(".jpg") && !sceneMovies.getOriginalFilename().endsWith(".png") && !sceneMovies.getOriginalFilename().endsWith(".jpeg"))
					errors.reject("fileUpload.sceneMovies.invalidFormat");
			}
		}
	}
	@Override
	public boolean supports(Class<?> clazz) {
		return FileUploadUtil.class.equals(clazz);
	}
}


