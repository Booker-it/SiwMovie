package it.uniroma3.siw.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.User;

@Component
public class UserValidator implements Validator {

	

	@Override
	public void validate(Object o, Errors errors) {
		User user = (User)o;
		if(user.getName().trim().isEmpty() || user.getSurname().isEmpty()) {
			errors.reject("user.cannotBeBlank");
		}
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return User.class.equals(clazz);
	}

}
