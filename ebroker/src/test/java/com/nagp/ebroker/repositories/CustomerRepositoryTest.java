package com.nagp.ebroker.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nagp.ebroker.entities.Customer;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CustomerRepositoryTest {

	@Autowired
	CustomerRepository customerRepo;

	@Test
	public void shouldCreateAndReadCustomer() {

		Customer customer = new Customer();
		customer.setWalletAmount((double) 10000);
		customerRepo.save(customer);

		Iterable<Customer> customers = customerRepo.findAll();
		Assertions.assertThat(customers).extracting((c) -> c.getWalletAmount()).contains((double) 10000);
	}

	@Test
	public void shouldCreateAndFindCustomerById() {
		Customer customer = new Customer();
		customer.setId(100);
		customer.setWalletAmount((double) 10000);
		customerRepo.save(customer);
		Optional<Customer> customerFetchedById = customerRepo.findById(100);
		assertEquals(100, customerFetchedById.get().getId());
		assertEquals((double) 10000, customerFetchedById.get().getWalletAmount());
	}

	@Test
	public void shouldCreateAndDeleteEquityById() {
		Customer customer = new Customer();
		customer.setId(100);
		customer.setWalletAmount((double) 10000);
		customerRepo.save(customer);
		customerRepo.deleteById(100);
		Optional<Customer> customerFetchedById = customerRepo.findById(100);
		assertTrue(customerFetchedById.isEmpty());
	}
}
