package com.simulador.investimentos.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class LotAllocation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "asset_symbol")
	private Asset asset;

	@OneToOne
	@JoinColumn(name = "order_id")
	private Order buyOrder;

	@ManyToOne
	@JoinColumn(name = "position_id")
	private Position position;
	private Integer quantity;
	private BigDecimal buyPrice;

	public LotAllocation() {

	}
	
	public LotAllocation(Order buyOrder, Position position) {
		this.position = position;
		this.buyOrder = buyOrder;
	}

	public LotAllocation(Asset asset, Order buyOrder, Position position,  Integer quantity, BigDecimal buyPrice) {
		this.asset = asset;
		this.position = position;
		this.buyOrder = buyOrder;
		this.quantity = quantity;
		this.buyPrice = buyPrice;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public BigDecimal getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Order getBuyOrder() {
		return buyOrder;
	}

	public void setBuyOrder(Order buyOrder) {
		this.buyOrder = buyOrder;
	}

}
