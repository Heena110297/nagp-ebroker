package com.nagp.ebroker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nagp.ebroker.entities.Equity;

@Repository
public interface EquityRepository extends JpaRepository<Equity, Integer> {

}
