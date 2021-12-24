package com.nagp.ebroker;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.nagp.ebroker.entities.Customer;
import com.nagp.ebroker.models.BaseResponse;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void testCreateReadDelete() throws Exception {
		Customer customer = new Customer();
		customer.setWalletAmount((double) 5000);
		customer.setName("Heena");
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setMessage("DONE");
		baseResponse.setStatus("SUCCESS");
		baseResponse.setCustomer(customer);
		mockMvc.perform(MockMvcRequestBuilders.post("/customer").contentType(MediaType.APPLICATION_JSON)
				.content("{\r\n" + "    \"name\":\"Heena\",\r\n" + "    \"wallet_amount\":5000\r\n" + "}"))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"status\":\"SUCCESS\",\"message\":\"Customer saved successfully\",\"customer\":{\"id\":0,\"equities\":null,\"name\":\"Heena\",\"walletAmount\":5000.0}}"));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/buy/1/1")).andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.content().string("{\"status\":\"SUCCESS\",\"message\":\"DONE\"}"));
	}
	
}
