package it.uniroma3.siw.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.controller.utility.FileUploadUtil;

@Component
public class FileValidator implements Validator{

	@Override
	public void validate(Object o, Errors errors) {
		FileUploadUtil fileUpload = (FileUploadUtil) o;
		if( fileUpload.getImage()!=null && !fileUpload.getImage().isEmpty() &&  !fileUpload.getImage().getOriginalFilename().endsWith(".png") &&
				!fileUpload.getImage().getOriginalFilename().endsWith(".jpg") && !fileUpload.getImage().getOriginalFilename().endsWith(".jpeg")){

			errors.reject("fileUpload.invalidFormat.image");
		}
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return FileUploadUtil.class.equals(clazz);
	}
}


