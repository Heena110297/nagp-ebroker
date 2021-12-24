package com.nagp.ebroker.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nagp.ebroker.entities.Equity;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class EquityRepositoryTest {

	@Autowired
	EquityRepository equityRepo;

	@Autowired
	CustomerRepository customerRepo;

	@Test
	public void shouldCreateAndReadEquity() {

		Equity equity = new Equity();
		equity.setNav(1000);
		equity.setName("Equity100");
		equityRepo.save(equity);
		Iterable<Equity> equitiesFetched = equityRepo.findAll();
		Assertions.assertThat(equitiesFetched).extracting((e) -> e.getName()).contains("Equity100");
	}
	
	@Test
	public void shouldCreateAndFindEquityById() {
		Equity equity = new Equity();
		equity.setNav(1000);
		equity.setName("Equity100");
		Equity savedEquity = equityRepo.save(equity);
		Optional<Equity> equityFetchedById = equityRepo.findById(savedEquity.getId());
        assertEquals(savedEquity.getId(), equityFetchedById.get().getId());
        assertEquals("Equity100", equityFetchedById.get().getName());
	}
	
	@Test
	public void shouldCreateAndDeleteEquityById() {
		Equity equity = new Equity();
		equity.setId(100);
		equity.setNav(1000);
		equity.setName("Equity100");
		equityRepo.save(equity);
		equityRepo.deleteById(100);
		Optional<Equity> equityFetchedById = equityRepo.findById(100);
        assertTrue(equityFetchedById.isEmpty());
	}
}
