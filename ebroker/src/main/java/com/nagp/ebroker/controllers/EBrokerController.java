package com.nagp.ebroker.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nagp.ebroker.entities.Customer;
import com.nagp.ebroker.models.BaseResponse;
import com.nagp.ebroker.models.CustomerModel;
import com.nagp.ebroker.services.EBrokerService;

@RestController
public class EBrokerController {

	@Autowired
	EBrokerService ebrokerService;

	@PostMapping("/customer")
	public ResponseEntity<BaseResponse> addCustomer(@RequestBody CustomerModel customer) {
		BaseResponse baseResponse = new BaseResponse();
		try {
			baseResponse.setMessage("Customer saved successfully");
			baseResponse.setStatus("SUCCESS");
			Customer savedCustomer = ebrokerService.addCustomer(customer);
			baseResponse.setCustomer(savedCustomer);
			return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			baseResponse.setMessage(e.getMessage());
			baseResponse.setStatus("FAILURE");
			return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/buy/{customerId}/{equityId}")
	public ResponseEntity<BaseResponse> buyEquity(@PathVariable("customerId") int customerId,
			@PathVariable("equityId") int equityId) {
		BaseResponse baseResponse = new BaseResponse();
		try {
			baseResponse = ebrokerService.buyEquity(customerId, equityId, LocalDateTime.now());
			return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			baseResponse.setMessage(e.getMessage());
			baseResponse.setStatus("FAILURE");
			return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/sell/{customerId}/{equityId}")
	public ResponseEntity<BaseResponse> sellEquity(@PathVariable("customerId") int customerId,
			@PathVariable("equityId") int equityId) {
		BaseResponse baseResponse = new BaseResponse();
		try {
			baseResponse = ebrokerService.sellEquity(customerId, equityId, LocalDateTime.now());
			return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			baseResponse.setMessage(e.getMessage());
			baseResponse.setStatus("FAILURE");
			return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/funds/{customerId}/{fundAmt}")
	public ResponseEntity<String> addFunds(@PathVariable int customerId, @PathVariable Double fundAmt) {
		Double updatedWalletAmount;
		try {
			updatedWalletAmount = ebrokerService.addFunds(customerId, fundAmt);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("Updated Wallet Amount: " + updatedWalletAmount, HttpStatus.OK);
	}

	@GetMapping("/funds/{id}")
	public ResponseEntity<String> getFunds(@PathVariable(value = "id") Integer customerId) {
		Double updatedWalletAmount;
		try {
			updatedWalletAmount = ebrokerService.getFunds(customerId);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("Updated Wallet Amount: " + updatedWalletAmount, HttpStatus.OK);

	}
}
