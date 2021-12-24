package com.nagp.ebroker.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagp.ebroker.entities.Customer;
import com.nagp.ebroker.entities.Equity;
import com.nagp.ebroker.models.BaseResponse;
import com.nagp.ebroker.models.CustomerModel;
import com.nagp.ebroker.repositories.CustomerRepository;
import com.nagp.ebroker.repositories.EquityRepository;
import com.nagp.ebroker.services.EBrokerService;
import com.nagp.ebroker.utils.Helper;

@Service
public class EBrokerServiceImpl implements EBrokerService {

	@Autowired
	CustomerRepository customerRepo;

	@Autowired
	EquityRepository equityRepo;

	@Override
	public BaseResponse buyEquity(int customerId, int equityId, LocalDateTime currentDate) throws Exception {
		if (Helper.checkTime(currentDate)) {
			Optional<Customer> customer = customerRepo.findById(customerId);
			if (customer.isPresent()) {
				Optional<Equity> equity = equityRepo.findById(equityId);
				if (equity.isPresent()) {
					double currentWalletAmount = customer.get().getWalletAmount();
					double transactionAmount = equity.get().getNav();
					if (currentWalletAmount >= transactionAmount) {
						customer.get().setWalletAmount(currentWalletAmount - transactionAmount);
						List<Equity> equityList = customer.get().getEquities();
						if (Objects.isNull(equityList)) {
							equityList = new ArrayList<>();
						}
						equityList.add(equity.get());
						customer.get().setEquities(equityList);
						customerRepo.save(customer.get());
						BaseResponse baseResponse = new BaseResponse();
						baseResponse.setStatus("SUCCESS");
						baseResponse.setMessage("Equity Purchased successfully");
						return baseResponse;
					} else
						throw new Exception("Insufficient wallet amount");
				} else
					throw new Exception("Invalid equity Id");
			} else
				throw new Exception("Invalid Customer Id");
		} else
			throw new Exception("Please try between 9AM to 5PM (MON-FRI)");
	}

	@Override
	public BaseResponse sellEquity(int customerId, int equityId, LocalDateTime currentDate) throws Exception {
		if (Helper.checkTime(currentDate)) {
			Optional<Customer> customer = customerRepo.findById(customerId);
			if (customer.isPresent()) {
				Customer customerFromDB = customer.get();
				Optional<Equity> equityFromDB = equityRepo.findById(equityId);
				if (equityFromDB.isPresent()) {
					List<Equity> equities = customerFromDB.getEquities();
					if (Objects.nonNull(equities)) {
						for (Equity equity : equities) {
							if (equity.getId() == equityId) {
								double newWalletAmount = customerFromDB.getWalletAmount() + equityFromDB.get().getNav();
								customerFromDB.setWalletAmount(newWalletAmount);
								equities.remove(equity);
								customerRepo.save(customerFromDB);
								BaseResponse baseResponse = new BaseResponse();
								baseResponse.setMessage("Equity sold successfully");
								baseResponse.setStatus("SUCCESS");
								return baseResponse;
							}
						}
						throw new Exception("You must hold the equity in order to sell it.");
					} else
						throw new Exception("You do not hold any equity");
				} else
					throw new Exception("No Equity found with the given id");
			} else
				throw new Exception("Customer Doesn't exist");
		} else
			throw new Exception("Please try between 9AM to 5PM (MON-FRI)");
	}

	@Override
	public Double addFunds(int customerId, Double funds) throws Exception {
		Optional<Customer> customer = customerRepo.findById(customerId);
		if (customer.isPresent()) {
			Double currentWalletAmount = customer.get().getWalletAmount();
			Double updatedWalletAmount = currentWalletAmount + funds;
			customer.get().setWalletAmount(updatedWalletAmount);
			return updatedWalletAmount;
		} else {
			throw new Exception("Invalid Customer ID");
		}
	}

	@Override
	public Double getFunds(int customerId) throws Exception {
		Optional<Customer> customer = customerRepo.findById(customerId);
		if (customer.isPresent()) {
			return customer.get().getWalletAmount();
		} else {
			throw new Exception("Invalid Customer ID");
		}
	}

	@Override
	public Customer addCustomer(CustomerModel customer) {
		Customer customerEntity = new Customer();
		BeanUtils.copyProperties(customer, customerEntity);
		return customerRepo.save(customerEntity);
	}

}
