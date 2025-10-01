package com.simulador.investimentos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "asset_symbol")
	private Asset asset;

	@ManyToOne
	@JoinColumn(name = "user_wallet")
	private Wallet wallet;

	@ManyToOne
	@JoinColumn(name = "position_id")
	private Position position;

	@OneToOne(mappedBy = "buyOrder", cascade = CascadeType.ALL)
	private LotAllocation lotAllocation;

	@Enumerated(EnumType.STRING)
	private OrderType type;
	private Integer quantity;
	private BigDecimal priceAtExecution;
	private LocalDateTime tradeTime;

	public Order() {
	}

	public Order(Wallet wallet, Asset asset, Integer quantity, OrderType type, BigDecimal priceAtExecution,
			Position position) {
		this.wallet = wallet;
		this.asset = asset;
		this.quantity = quantity;
		this.type = type;
		this.priceAtExecution = priceAtExecution;
		this.position = position;
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

	public BigDecimal getPriceAtExecution() {
		return priceAtExecution;
	}

	public void setPriceAtExecution(BigDecimal priceAtExecution) {
		this.priceAtExecution = priceAtExecution;
	}

	public LocalDateTime getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(LocalDateTime tradeTime) {
		this.tradeTime = tradeTime;
	}
	
	

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public int hashCode() {
		return Objects.hash(asset, position, priceAtExecution, quantity, type, wallet);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return Objects.equals(asset, other.asset) && Objects.equals(position, other.position)
				&& Objects.equals(priceAtExecution, other.priceAtExecution) && Objects.equals(quantity, other.quantity)
				&& type == other.type && Objects.equals(wallet, other.wallet);
	}

}
