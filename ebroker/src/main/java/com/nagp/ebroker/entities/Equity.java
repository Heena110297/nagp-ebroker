package com.nagp.ebroker.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Equity {
	@Id
	int id;
	String name;
	double nav;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getNav() {
		return nav;
	}

	public void setNav(double nav) {
		this.nav = nav;
	}

}
