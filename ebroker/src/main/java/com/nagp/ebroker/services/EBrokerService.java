package com.nagp.ebroker.services;

import java.time.LocalDateTime;

import com.nagp.ebroker.entities.Customer;
import com.nagp.ebroker.entities.Equity;
import com.nagp.ebroker.models.BaseResponse;
import com.nagp.ebroker.models.CustomerModel;
import com.nagp.ebroker.models.EquityModel;

public interface EBrokerService {

	public BaseResponse buyEquity(int customerId, int equityId, LocalDateTime currentDate) throws Exception;

	public BaseResponse sellEquity(int customerId, int equityId, LocalDateTime currentDate) throws Exception;

	public Double addFunds(int customerId, Double funds) throws Exception;

	public Double getFunds(int customerId) throws Exception;
	
	public Customer addCustomer(CustomerModel customer);
	
	public String deleteCustomer(Integer id);
	
	public Equity addEquity(EquityModel equityModel);

	String deleteEquity(Integer id);
}
