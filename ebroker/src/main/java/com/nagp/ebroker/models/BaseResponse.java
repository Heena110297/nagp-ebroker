package com.nagp.ebroker.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nagp.ebroker.entities.Customer;

public class BaseResponse {

	String status;
	String message;
	
	@JsonIgnore
	Customer customer;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
