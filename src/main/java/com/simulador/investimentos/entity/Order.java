package com.simulador.investimentos.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name="user_orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "asset_symbol")
    private Asset asset;
    
    @ManyToOne
    @JoinColumn(name = "user_wallet")
    private Wallet wallet;
    
    @Enumerated(EnumType.STRING)
    private OrderType type;

    private Integer quantity;

    private Double priceAtExecution;

    private LocalDateTime tradeTime;
    

    
    public Order() {}

    public Order(Wallet wallet, Asset asset, Integer quantity, OrderType type, Double priceAtExecution) {
        this.wallet = wallet;
        this.asset = asset;
        this.quantity = quantity;
        this.type = type;
        this.priceAtExecution = priceAtExecution;
        this.tradeTime = LocalDateTime.now();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
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

	public LocalDateTime getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(LocalDateTime tradeTime) {
		this.tradeTime = tradeTime;
	}

}
