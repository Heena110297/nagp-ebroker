package com.nagp.ebroker.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nagp.ebroker.entities.Customer;
import com.nagp.ebroker.entities.Equity;
import com.nagp.ebroker.models.BaseResponse;
import com.nagp.ebroker.models.CustomerModel;
import com.nagp.ebroker.models.EquityModel;
import com.nagp.ebroker.repositories.CustomerRepository;
import com.nagp.ebroker.repositories.EquityRepository;
import com.nagp.ebroker.utils.Helper;

@ExtendWith(MockitoExtension.class)
public class EBrokerServiceImplTest {

	@InjectMocks
	private EBrokerServiceImpl eBrokerService;

	@Mock
	private CustomerRepository customerRepo;

	@Mock
	private EquityRepository equityRepo;

	@Test
	public void shouldBeAbleToBuyEquity() throws Exception {
		Equity equity = new Equity();
		equity.setId(1);
		equity.setName("equity1");
		equity.setNav(1000);
		List<Equity> equities = new ArrayList<>();
		equities.add(equity);
		Customer customer = new Customer();
		customer.setId(1);
		customer.setWalletAmount((double) 10500);
		customer.setEquities(equities);
		Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.of(customer));
		Mockito.when(equityRepo.findById(Mockito.any())).thenReturn(Optional.of(equity));
		BaseResponse baseResponse = eBrokerService.buyEquity(1, 1, LocalDateTime.of(2021, 12, 21, 10, 1));
		assertEquals("SUCCESS", baseResponse.getStatus());
	}

	@Test
	public void shouldBeAbleToBuyEquityEvenIfHeDoesNotHoldAny() throws Exception {
		Equity equity = new Equity();
		equity.setId(1);
		equity.setName("equity1");
		equity.setNav(1000);
		Customer customer = new Customer();
		customer.setId(1);
		customer.setWalletAmount((double) 10500);
		Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.of(customer));
		Mockito.when(equityRepo.findById(Mockito.any())).thenReturn(Optional.of(equity));
		BaseResponse baseResponse = eBrokerService.buyEquity(1, 1, LocalDateTime.of(2021, 12, 21, 10, 1));
		assertEquals("SUCCESS", baseResponse.getStatus());
	}

	@Test
	public void shouldThrowExceptionIfCustomerDoesNotExist() throws Exception {
		Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
		Assertions.assertThrows(Exception.class, () -> {
			eBrokerService.buyEquity(1, 1, LocalDateTime.of(2021, 12, 21, 10, 1));
		});
	}

	@Test
	public void shouldThrowExceptionIfEquityDoesNotExist() throws Exception {
		Customer customer = new Customer();
		customer.setId(1);
		customer.setWalletAmount((double) 10500);
		Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.of(customer));
		Mockito.when(equityRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
		Assertions.assertThrows(Exception.class, () -> {
			eBrokerService.buyEquity(1, 1, LocalDateTime.of(2021, 12, 21, 10, 1));
		});
	}

	@Test
	public void shouldThrowExceptionBecauseOfInsufficientFunds() throws Exception {
		Equity equity = new Equity();
		equity.setId(1);
		equity.setName("equity1");
		equity.setNav(1000);
		List<Equity> equities = new ArrayList<>();
		equities.add(equity);
		Customer customer = new Customer();
		customer.setId(1);
		customer.setWalletAmount((double) 500);
		customer.setEquities(equities);
		Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.of(customer));
		Mockito.when(equityRepo.findById(Mockito.any())).thenReturn(Optional.of(equity));
		Assertions.assertThrows(Exception.class, () -> {
			eBrokerService.buyEquity(1, 1, LocalDateTime.of(2021, 12, 21, 10, 1));
		});
	}

	@Test
	public void shouldThrowExceptionIfCurrentDateIsNotInExpectedTimeFrame() throws Exception {
		Assertions.assertThrows(Exception.class, () -> {
			eBrokerService.buyEquity(1, 1, LocalDateTime.of(2021, 12, 21, 2, 1));
		});
	}

	@Test
	public void shouldThrowExceptionIfCurrentDateIsNotInExpectedDayOfTheWeekFrame() throws Exception {
		Assertions.assertThrows(Exception.class, () -> {
			eBrokerService.buyEquity(1, 1, LocalDateTime.of(2021, 12, 18, 10, 1));
		});
	}

	@Test
	public void shouldBeAbleToSellEquity() throws Exception {
		Equity equity = new Equity();
		equity.setId(1);
		equity.setName("equity1");
		equity.setNav(1000);
		List<Equity> equities = new ArrayList<>();
		equities.add(equity);
		Customer customer = new Customer();
		customer.setId(1);
		customer.setWalletAmount((double) 10500);
		customer.setEquities(equities);
		Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.of(customer));
		Mockito.when(equityRepo.findById(Mockito.any())).thenReturn(Optional.of(equity));
		BaseResponse baseResponse = eBrokerService.sellEquity(1, 1, LocalDateTime.of(2021, 12, 21, 10, 1));
		assertEquals("SUCCESS", baseResponse.getStatus());
	}

	@Test
	public void shouldThrowExceptionIfCurrentDateIsNotInExpectedTimeFrameWhileSelling() throws Exception {
		Assertions.assertThrows(Exception.class, () -> {
			eBrokerService.sellEquity(1, 1, LocalDateTime.of(2021, 12, 21, 2, 1));
		});
	}

	@Test
	public void shouldThrowExceptionIfCurrentDateIsNotInExpectedDayOfTheWeekFrameWhileSelling() throws Exception {
		Assertions.assertThrows(Exception.class, () -> {
			eBrokerService.sellEquity(1, 1, LocalDateTime.of(2021, 12, 18, 10, 1));
		});
	}

	@Test
	public void shouldThrowExceptionIfCurrentDateIsNotInExpectedDayOfTheWeekFrameWhileSellingLate() throws Exception {
		Assertions.assertThrows(Exception.class, () -> {
			eBrokerService.sellEquity(1, 1, LocalDateTime.of(2021, 12, 21, 19, 1));
		});
	}

	@Test
	public void shouldThrowExceptionIfCustomerDoesNotHoldAnyEquity() throws Exception {
		Customer customer = new Customer();
		customer.setId(1);
		customer.setWalletAmount((double) 10500);
		Equity equity = new Equity();
		equity.setId(1);
		equity.setName("equity1");
		equity.setNav(1000);
		Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.of(customer));
		Mockito.when(equityRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(equity));
		Assertions.assertThrows(Exception.class, () -> {
			eBrokerService.sellEquity(1, 1, LocalDateTime.of(2021, 12, 21, 10, 1));
		});
	}

	@Test
	public void shouldThrowExceptionIfCustomerDoesNotExistWhileSelling() throws Exception {
		Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
		Assertions.assertThrows(Exception.class, () -> {
			eBrokerService.sellEquity(1, 1, LocalDateTime.of(2021, 12, 21, 10, 1));
		});
	}

	@Test
	public void shouldThrowExceptionIfEquityDoesNotExistWhileSelling() throws Exception {
		Customer customer = new Customer();
		customer.setId(1);
		customer.setWalletAmount((double) 10500);
		Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.of(customer));
		Mockito.when(equityRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
		Assertions.assertThrows(Exception.class, () -> {
			eBrokerService.sellEquity(1, 1, LocalDateTime.of(2021, 12, 21, 10, 1));
		});
	}

	@Test
	public void shouldThrowExceptionIfCustomerDoesNotHoldEquity() throws Exception {
		Equity equity = new Equity();
		equity.setId(5);
		equity.setName("equity1");
		equity.setNav(1000);
		List<Equity> equities = new ArrayList<>();
		equities.add(equity);
		Customer customer = new Customer();
		customer.setId(1);
		customer.setWalletAmount((double) 10500);
		customer.setEquities(equities);
		Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.of(customer));
		Mockito.when(equityRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(equity));
		Assertions.assertThrows(Exception.class, () -> {
			eBrokerService.sellEquity(1, 1, LocalDateTime.of(2021, 12, 21, 10, 1));
		});
	}

	@Test
	public void shouldReturnFunds() throws Exception {
		Customer customer = new Customer();
		customer.setId(1);
		customer.setWalletAmount((double) 10500);
		Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.of(customer));
		double fundValue = eBrokerService.getFunds(1);
		assertEquals((double) 10500, fundValue);
	}

	@Test
	public void shouldThrowExceptionIfCustomerDoesNotExistWhileFetchingFunds() throws Exception {
		Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
		Assertions.assertThrows(Exception.class, () -> {
			eBrokerService.getFunds(1);
		});
	}

	@Test
	public void shouldThrowExceptionIfCustomerDoesNotExistWhileAddingFunds() {
		Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
		Assertions.assertThrows(Exception.class, () -> {
			eBrokerService.addFunds(1, (double) 10000);
		});
	}

	@Test
	public void shouldbeAbleToAddFunds() throws Exception {
		Customer customer = new Customer();
		customer.setId(1);
		customer.setWalletAmount((double) 10500);
		Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(Optional.of(customer));
		double fundValue = eBrokerService.addFunds(1, (double) 5000);
		assertEquals((double) 15500, fundValue);
	}

	@DisplayName("test For Helper Static Method")
	@Test
	public void testForHelperStaticMethod() {
		try (MockedStatic<Helper> utilites = Mockito.mockStatic(Helper.class)) {
			utilites.when(() -> Helper.checkTime(LocalDateTime.of(2021, 12, 21, 10, 1))).thenReturn(true);
			utilites.when(() -> Helper.checkTime(LocalDateTime.of(2021, 12, 21, 8, 1))).thenReturn(false);
			utilites.when(() -> Helper.checkTime(LocalDateTime.of(2021, 12, 21, 19, 1))).thenReturn(false);
			utilites.when(() -> Helper.checkTime(LocalDateTime.of(2021, 12, 18, 10, 1))).thenReturn(false);
			utilites.when(() -> Helper.checkTime(LocalDateTime.of(2021, 12, 18, 8, 1))).thenReturn(false);
			utilites.when(() -> Helper.checkTime(LocalDateTime.of(2021, 12, 18, 19, 1))).thenReturn(false);
			Assertions.assertEquals(true, Helper.checkTime(LocalDateTime.of(2021, 12, 21, 10, 1)));
			Assertions.assertEquals(false, Helper.checkTime(LocalDateTime.of(2021, 12, 21, 8, 1)));
			Assertions.assertEquals(false, Helper.checkTime(LocalDateTime.of(2021, 12, 21, 19, 1)));
			Assertions.assertEquals(false, Helper.checkTime(LocalDateTime.of(2021, 12, 18, 10, 1)));
			Assertions.assertEquals(false, Helper.checkTime(LocalDateTime.of(2021, 12, 18, 8, 1)));
			Assertions.assertEquals(false, Helper.checkTime(LocalDateTime.of(2021, 12, 18, 19, 1)));
		}
	}

	@Test
	public void shouldSaveCustomer() {
		CustomerModel customer = new CustomerModel();
		customer.setName("name");
		customer.setWalletAmount((double) 5000);
		Mockito.when(customerRepo.save(Mockito.any())).thenReturn(new Customer());
		Customer savedCustomer = eBrokerService.addCustomer(customer);
		assertNotNull(savedCustomer);
	}

	@Test
	public void shouldDeleteCustomer() {
		String resp = eBrokerService.deleteCustomer(1);
		assertNotNull(resp);
	}

	@Test
	public void shouldSaveEquity() {
		EquityModel equity = new EquityModel();
		equity.setName("name");
		equity.setNav(100.0);
		Mockito.when(equityRepo.save(Mockito.any())).thenReturn(new Equity());
		Equity savedEquity = eBrokerService.addEquity(equity);
		assertNotNull(savedEquity);
	}

	@Test
	public void shouldDeleteEquity() {
		String resp = eBrokerService.deleteEquity(1);
		assertNotNull(resp);
	}
}
