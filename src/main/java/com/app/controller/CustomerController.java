package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.app.model.Customer;
import com.app.service.ICustomerService;
import com.app.validator.CustomerValidator;

@Controller
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	private ICustomerService service;
	
	@Autowired
	private CustomerValidator validator;
	
	//1.Display Reg with form backing object
	@RequestMapping("/reg")
	public String showReg(Model map) {
		map.addAttribute("customer", new Customer());
		return "Register";
	}
	
	//2.On click submit read data form and save into db
	@RequestMapping(value="/save", method = RequestMethod.POST)
	public String saveData(@ModelAttribute Customer customer, Model map, Errors errors) {
		//First Call validator
		
		validator.validate(customer, errors);
		
			
			if(!errors.hasErrors()) {
			//insert into db
			Integer id=service.saveCustomer(customer);
			map.addAttribute("message", "Customer '"+id+"' Created");
			//clean form backing object
			map.addAttribute("customer", new Customer());
			}else {
			//goto back same page	
			map.addAttribute("message", "Please Check All Errors");
			}
		return "Register";
	}
	
	//3.Display all records in db at UI
	@RequestMapping("/all")
	public String showAll(Model map) {
		//fetch data from db
		List<Customer> cobs=service.getAllCoustomers();
		//send data to ui
		map.addAttribute("list", cobs);
		return "Data";
	}
	
	//4.fetch Data Based on id and display
	@RequestMapping("/view/{id}")
	public String viewOne(@PathVariable Integer id, Model map) {
		Customer c=service.getOneCustomer(id);
		map.addAttribute("ob", c);
		return "View";
	}
	
	//5.delete row based on id
	@RequestMapping("/delete/{id}")
	public String deleteOne(@PathVariable Integer id, Model map) {
		service.deleteCustomer(id);
		//fetch all new data
		List<Customer> cobs=service.getAllCoustomers();
		//send data to ui
		map.addAttribute("list",cobs);
		//success message
		map.addAttribute("message", "Customer '"+id+"' deleted");
		return "Data";
	}

	//6.edit row based on id
	@RequestMapping("/edit/{id}")
	public String showEditPage(@PathVariable Integer id, Model map) {
		Customer c=service.getOneCustomer(id);
		map.addAttribute("customer", c);
		return "Edit";
	}
	
	//7.update row based on id
	@RequestMapping(value="/update", method = RequestMethod.POST)
	public String updateData(@ModelAttribute Customer customer, Model map) {
		//update into db
		Integer id = customer.getCustId();
		service.updateCustomer(id, customer);

		//response.sendRedirect
		return "redirect:all";		
	}
	
}
