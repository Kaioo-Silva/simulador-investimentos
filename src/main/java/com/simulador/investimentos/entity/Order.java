package com.simulador.investimentos.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Users user;

    @ManyToOne
    private Asset asset;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private OrderType type;

    private Double priceAtExecution;

    private LocalDateTime timestamp;

    public Order() {}

    public Order(Users user, Asset asset, Integer quantity, OrderType type, Double priceAtExecution) {
        this.user = user;
        this.asset = asset;
        this.quantity = quantity;
        this.type = type;
        this.priceAtExecution = priceAtExecution;
        this.timestamp = LocalDateTime.now();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
	}

	public Double getPriceAtExecution() {
		return priceAtExecution;
	}

	public void setPriceAtExecution(Double priceAtExecution) {
		this.priceAtExecution = priceAtExecution;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

}
