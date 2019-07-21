package com.app.validator;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.model.Customer;

@Component
public class CustomerValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		//supports only customer class objects
		return Customer.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		//downcasting
		Customer c=(Customer) target;
		
		//text input validation
		if(!Pattern.matches("[A-Za-z0-9]{5,15}", c.getCustCode())) {
			errors.rejectValue("custCode", null, "Please Enter Valid Name First Letter UpperCase 5-15");
		}
		
		if(!Pattern.matches("[A-Z]{4,25}", c.getCustName())) {
			errors.rejectValue("custName", null, "Please Enter Valid Name  Each Letter in UppperCase 4-25");
		}
		
		// should not  be(null,empty,spaces)
		
		if(!StringUtils.hasText(c.getCustType())) {
			errors.rejectValue("custType", null, "Please Select One Valid Option");
		}
		
		// text area
		
		if(!Pattern.matches("[A-Za-z0-9\\s]{10,250}", c.getNote())) {
			errors.rejectValue("note", null, "Please Enter Valid Note 10-250");
		}
	}

}
