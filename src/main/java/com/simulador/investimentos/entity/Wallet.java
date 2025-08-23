package com.simulador.investimentos.entity;

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

	private Double balance;


	@OneToOne
	@JoinColumn(name = "user_wallet")
	@JsonBackReference
	private User user;

	@OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
	private List<Order> orders;


	public Wallet() {
	}

	public Wallet(double balance) {
		
		this.balance = balance;
	}
	

	public Wallet(double balance, User user, List<Order> orders) {
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

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
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
