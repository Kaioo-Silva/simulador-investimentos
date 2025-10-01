package com.simulador.investimentos.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "assets")
public class Asset {

    @Id
    private String symbol; 
    private String name;
    
    
    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL)
    private List<LotAllocation> lotAllocation;

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();
    
    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL)
	private List<Position> positions;

    public Asset(){
    }

	public Asset(String symbol, String name, List<Order> orders) {
		super();
		this.symbol = symbol;
		this.name = name;
		this.orders = orders;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	
	

}
