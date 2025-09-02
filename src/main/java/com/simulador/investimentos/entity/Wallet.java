package com.simulador.investimentos.entity;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "wallets")
public class Wallet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private BigDecimal balance; // BIG DECIMAL

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	
	@OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
	private List<Position> positions;

	@OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
	private List<Order> orders;

	public Wallet() {
	}

	public Wallet(BigDecimal balance) {

		this.balance = balance;
	}

	public Wallet(BigDecimal balance, User user, List<Order> orders) { // BIG DECIMAL
		super();
		this.balance = balance;
		this.user = user;
		this.orders = orders;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getBalance() { // BIG DECIMAL
		return balance;
	}

	public void setBalance(BigDecimal balance) { // BIG DECIMAL
		this.balance = balance;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

}
