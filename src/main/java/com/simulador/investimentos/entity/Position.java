package com.simulador.investimentos.entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "positions")
public class Position {

	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "position_id")
	@Id
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "wallet_id")
	private Wallet wallet;
	
	@ManyToOne
	@JoinColumn(name = "asset_symbol")
	private Asset asset;
	
	@OneToMany(mappedBy = "position", cascade = CascadeType.ALL)
    private List<Order> orders;
	
	@OneToMany(mappedBy = "position", cascade = CascadeType.ALL)
    private List<LotAllocation> lots;

	

	private Integer totalQuantity;
	private BigDecimal averagePrice;

	public Position() {
	}
	
	public Position(Wallet wallet, Asset asset, Integer totalQuantity, BigDecimal averagePrice) {
		this.wallet = wallet;
		this.asset = asset;
		this.totalQuantity = totalQuantity;
		this.averagePrice = averagePrice;
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

	public Integer getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(Integer totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public BigDecimal getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(BigDecimal averagePrice) {
		this.averagePrice = averagePrice;
	}
	
	
	
}
