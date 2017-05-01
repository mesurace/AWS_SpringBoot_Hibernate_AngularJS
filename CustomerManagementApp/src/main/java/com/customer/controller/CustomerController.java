package com.customer.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.customer.domain.Address;
import com.customer.domain.Customer;
import com.customer.domain.Image;
import com.customer.repository.CustomerRepository;
import com.customer.service.FileArchiveService;

@RestController
public class CustomerController {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private FileArchiveService fileArchiveService;

	@RequestMapping(value = "/customers", method = RequestMethod.POST)
	public @ResponseBody Customer createCustomer(@RequestParam(value = "firstName", required = true) String firstName,
			@RequestParam(value = "lastName", required = true) String lastName,
			@RequestParam(value = "dateOfBirth", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOfBirth,
			@RequestParam(value = "street", required = true) String street,
			@RequestParam(value = "town", required = true) String town,
			@RequestParam(value = "county", required = true) String county,
			@RequestParam(value = "postcode", required = true) String postcode,
			@RequestParam(value = "image", required = true) MultipartFile image) throws Exception {

		Image customerImage = fileArchiveService.saveFileToS3(image);
		Customer customer = new Customer(firstName, lastName, dateOfBirth, customerImage,
				new Address(street, town, county, postcode));

		customerRepository.save(customer);
		return customer;
	}

}
