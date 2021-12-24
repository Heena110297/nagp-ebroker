package com.nagp.ebroker;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.nagp.ebroker.utils.Helper;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testCreateReadDelete() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post("/customer").contentType(MediaType.APPLICATION_JSON)
				.content("{\r\n" + "    \"name\":\"Heena\",\r\n" + "    \"walletAmount\":5000.0\r\n" + "}"))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content()
						.string("{\"status\":\"SUCCESS\",\"message\":\"Customer saved successfully\"}"));

		mockMvc.perform(MockMvcRequestBuilders.post("/equity").contentType(MediaType.APPLICATION_JSON)
				.content("{\r\n" + "    \"name\":\"ABC\",\r\n" + "    \"nav\":1000.0\r\n" + "}"))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content()
						.string("{\"status\":\"SUCCESS\",\"message\":\"Equity saved successfully\"}"));

		if (Helper.checkTime(LocalDateTime.now())) {
			mockMvc.perform(MockMvcRequestBuilders.post("/buy/0/0")).andExpect(MockMvcResultMatchers.status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.content().string("{\"status\":\"SUCCESS\",\"message\":\"DONE\"}"));

			mockMvc.perform(MockMvcRequestBuilders.post("/sell/0/0")).andExpect(MockMvcResultMatchers.status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.content().string("{\"status\":\"SUCCESS\",\"message\":\"DONE\"}"));

		} else {
			mockMvc.perform(MockMvcRequestBuilders.post("/buy/0/0"))
					.andExpect(MockMvcResultMatchers.status().is5xxServerError()).andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.content().string(
							"{\"status\":\"FAILURE\",\"message\":\"Please try between 9AM to 5PM (MON-FRI)\"}"));

			mockMvc.perform(MockMvcRequestBuilders.post("/sell/0/0"))
					.andExpect(MockMvcResultMatchers.status().is5xxServerError()).andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.content()
							.string("{\"status\":\"FAILURE\",\"message\":\"Please try between 9AM to 5PM (MON-FRI)\"}"));
		}

		mockMvc.perform(MockMvcRequestBuilders.delete("/customer/0"))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().string("SUCCESS"));

		mockMvc.perform(MockMvcRequestBuilders.delete("/equity/0"))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().string("SUCCESS"));
	}

}
