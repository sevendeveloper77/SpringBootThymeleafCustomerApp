package com.app.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.model.Customer;
import com.app.repo.CustomerRepository;
import com.app.service.ICustomerService;

@Service
public class CustomerServiceImpl implements ICustomerService {
	
	@Autowired
	private CustomerRepository repo;
	
	@Transactional
	public Integer saveCustomer(Customer c) {
 		return repo.save(c).getCustId();
	}

	@Transactional(readOnly = true)
	public List<Customer> getAllCoustomers() {
		return repo.findAll();
	}

	@Transactional(readOnly = true)
	@Cacheable(value="cust-cache", key="#custId")
	public Customer getOneCustomer(Integer custId) {
		Optional<Customer> c=repo.findById(custId);
		if(c.isPresent()) {
			return c.get();
		}
		return null;
	}
	
	@Transactional
	@CacheEvict(value="cust-cache", key="#custId")
	public void deleteCustomer(Integer custId) {
		repo.deleteById(custId);
	}
	
	
	@Transactional
	@CachePut(value="cust-cache", key="#custId")
	public Customer updateCustomer(Integer custId, Customer c) {
		 return repo.save(c);
	}

}
