package com.nagp.ebroker.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer {

	@Id
	int id;

	@OneToMany(cascade = CascadeType.DETACH)
	List<Equity> equities;

	String name;

	double walletAmount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Equity> getEquities() {
		return equities;
	}

	public void setEquities(List<Equity> equities) {
		this.equities = equities;
	}

	public double getWalletAmount() {
		return walletAmount;
	}

	public void setWalletAmount(double walletAmount) {
		this.walletAmount = walletAmount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
