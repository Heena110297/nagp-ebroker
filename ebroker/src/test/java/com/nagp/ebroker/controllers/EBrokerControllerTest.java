package com.nagp.ebroker.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.nagp.ebroker.entities.Customer;
import com.nagp.ebroker.entities.Equity;
import com.nagp.ebroker.models.BaseResponse;
import com.nagp.ebroker.services.EBrokerService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(EBrokerController.class)
public class EBrokerControllerTest {

	@MockBean
	private EBrokerService eBrokerService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void shouldGetFunds() throws Exception {
		Double expectedFund = (double) 500;
		Mockito.when(eBrokerService.getFunds(Mockito.anyInt())).thenReturn(expectedFund);
		mockMvc.perform(MockMvcRequestBuilders.get("/funds/1")).andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().string("Updated Wallet Amount: 500.0"));

	}

	@Test
	public void shouldThrowExceptionWhileFetchingFunds() throws Exception {
		Mockito.when(eBrokerService.getFunds(Mockito.anyInt())).thenThrow(new Exception("Could not fetch funds"));
		mockMvc.perform(MockMvcRequestBuilders.get("/funds/1"))
				.andExpect(MockMvcResultMatchers.status().is5xxServerError()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().string("Could not fetch funds"));

	}

	@Test
	public void shouldBuyEquity() throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setMessage("DONE");
		baseResponse.setStatus("SUCCESS");
		Mockito.when(eBrokerService.buyEquity(Mockito.anyInt(), Mockito.anyInt(), Mockito.any()))
				.thenReturn(baseResponse);
		mockMvc.perform(MockMvcRequestBuilders.post("/buy/1/1")).andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().string("{\"status\":\"SUCCESS\",\"message\":\"DONE\"}"));
	}

	@Test
	public void shouldGetExceptionWHileBuyingEquity() throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setMessage("DONE");
		baseResponse.setStatus("SUCCESS");
		Mockito.when(eBrokerService.buyEquity(Mockito.anyInt(), Mockito.anyInt(), Mockito.any()))
				.thenThrow(new Exception("OperationFailed"));
		mockMvc.perform(MockMvcRequestBuilders.post("/buy/1/1"))
				.andExpect(MockMvcResultMatchers.status().is5xxServerError()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content()
						.string("{\"status\":\"FAILURE\",\"message\":\"OperationFailed\"}"));
	}

	@Test
	public void shouldSellEquity() throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setMessage("DONE");
		baseResponse.setStatus("SUCCESS");
		Mockito.when(eBrokerService.sellEquity(Mockito.anyInt(), Mockito.anyInt(), Mockito.any()))
				.thenReturn(baseResponse);
		mockMvc.perform(MockMvcRequestBuilders.post("/sell/1/1")).andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().string("{\"status\":\"SUCCESS\",\"message\":\"DONE\"}"));
	}

	@Test
	public void shouldGetExceptionWHileSellEquity() throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setMessage("DONE");
		baseResponse.setStatus("SUCCESS");
		Mockito.when(eBrokerService.sellEquity(Mockito.anyInt(), Mockito.anyInt(), Mockito.any()))
				.thenThrow(new Exception("OperationFailed"));
		mockMvc.perform(MockMvcRequestBuilders.post("/sell/1/1"))
				.andExpect(MockMvcResultMatchers.status().is5xxServerError()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content()
						.string("{\"status\":\"FAILURE\",\"message\":\"OperationFailed\"}"));
	}

	@Test
	public void shouldAddFunds() throws Exception {
		Mockito.when(eBrokerService.addFunds(Mockito.anyInt(), Mockito.anyDouble())).thenReturn((double) (10000));
		mockMvc.perform(MockMvcRequestBuilders.put("/funds/1/500")).andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().string("Updated Wallet Amount: 10000.0"));
	}

	@Test
	public void shouldThrowExceptionWHileAddingFunds() throws Exception {
		Mockito.when(eBrokerService.addFunds(Mockito.anyInt(), Mockito.anyDouble()))
				.thenThrow(new Exception("Cannot add funds"));
		mockMvc.perform(MockMvcRequestBuilders.put("/funds/1/500"))
				.andExpect(MockMvcResultMatchers.status().is5xxServerError()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().string("Cannot add funds"));
	}

	@Test
	public void shouldSaveCustomer() throws Exception {
		Customer customer = new Customer();
		customer.setWalletAmount(5000.0);
		customer.setName("Heena");
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setMessage("DONE");
		baseResponse.setStatus("SUCCESS");
		baseResponse.setCustomer(customer);
		Mockito.when(eBrokerService.addCustomer(Mockito.any())).thenReturn(customer);
		mockMvc.perform(MockMvcRequestBuilders.post("/customer").contentType(MediaType.APPLICATION_JSON)
				.content("{\r\n" + "    \"name\":\"Heena\",\r\n" + "    \"wallet_amount\":5000\r\n" + "}"))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"status\":\"SUCCESS\",\"message\":\"Customer saved successfully\"}"));
	}

	@Test
	public void shouldDeleteCustomer() throws Exception {
		Mockito.when(eBrokerService.deleteCustomer(Mockito.any())).thenReturn("SUCCESS");
		mockMvc.perform(MockMvcRequestBuilders.delete("/customer/1"))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().string("SUCCESS"));
	}
	
	@Test
	public void shouldSaveEquity() throws Exception {
		Equity equity = new Equity();
		equity.setName("ABC");
		equity.setNav(1000.0);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setMessage("DONE");
		baseResponse.setStatus("SUCCESS");
		Mockito.when(eBrokerService.addEquity(Mockito.any())).thenReturn(equity);
		mockMvc.perform(MockMvcRequestBuilders.post("/equity").contentType(MediaType.APPLICATION_JSON)
				.content("{\r\n" + "    \"name\":\"ABC\",\r\n" + "    \"nav\":1000.0\r\n" + "}"))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"status\":\"SUCCESS\",\"message\":\"Equity saved successfully\"}"));
	}
	
	@Test
	public void shouldDeleteEquity() throws Exception {
		Mockito.when(eBrokerService.deleteEquity(Mockito.any())).thenReturn("SUCCESS");
		mockMvc.perform(MockMvcRequestBuilders.delete("/equity/1"))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().string("SUCCESS"));
	}
}
