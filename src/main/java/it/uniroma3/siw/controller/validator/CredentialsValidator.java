package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.service.CredentialsService;

@Component
public class CredentialsValidator implements Validator{
	
	private final Integer MAX_LENGTH_USERNAME = 20;
	private final Integer MIN_LENGTH_USERNAME = 4;
	
	private final Integer MAX_LENGTH_PASSWORD = 30;
	private final Integer MIN_LENGTH_PASSWORD = 9;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Override
	public void validate(Object o, Errors errors) {
		Credentials credentials = (Credentials)o;
		
		//Aggiunto il .trim(), altrimenti venivano considerati anche gli spazi
		String credenzialiUsername = credentials.getUsername().trim();
		String credenzialiPassword = credentials.getPassword().trim();
		
		
		if( credenzialiUsername != null && credentialsService.existCredentialsByUsername(credenzialiUsername)){
			errors.reject("credentials.duplicate");
		}
		
		if(credenzialiUsername.isEmpty() || credenzialiPassword.isEmpty()) {
			errors.reject("credentials.cannotBeBlank");
		}
		
		if (credenzialiUsername.length() < MIN_LENGTH_USERNAME || credenzialiUsername.length() > MAX_LENGTH_USERNAME) {
			errors.reject("credentials.username");
		}
		
		if (credenzialiPassword.length() < MIN_LENGTH_PASSWORD || credentials.getUsername().length() > MAX_LENGTH_PASSWORD) {
			errors.reject("credentials.password");
		}
		
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return CredentialsValidator.class.equals(clazz);
	}
	
}
