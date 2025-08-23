package com.simulador.investimentos.service;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;

import com.simulador.investimentos.client.BrapiClient;
import com.simulador.investimentos.dtos.OrderDTO;
import com.simulador.investimentos.dtos.QuoteResponseDTO;
import com.simulador.investimentos.entity.Asset;
import com.simulador.investimentos.entity.Order;
import com.simulador.investimentos.entity.OrderType;
import com.simulador.investimentos.entity.User;
import com.simulador.investimentos.entity.Wallet;
import com.simulador.investimentos.repository.OrderRepository;

@Service
public class OrderService {

	private OrderRepository orderRepository;
	private BrapiClient brapiClient;
	private UserService userService;
	private WalletService walletService;
	private AssetService assetService;

	public OrderService(OrderRepository orderRepository, BrapiClient brapiClient, UserService userService,
			WalletService walletService, AssetService assetService) {
		this.orderRepository = orderRepository;
		this.brapiClient = brapiClient;
		this.userService = userService;
		this.walletService = walletService;
		this.assetService = assetService;
	}

	public OrderDTO buy(Long id, String symbol, Integer quantity) {
		User user = userService.findUser(id);
		Double userBalance = user.getWallet().getBalance();
		Wallet userWallet = user.getWallet();
		Long walletId = userWallet.getId();

		QuoteResponseDTO brapiStockData = brapiClient.getQuote(symbol);
		String stockName = brapiStockData.results().get(0).shortName();
		Double stockPrice = brapiStockData.results().get(0).regularMarketPrice();

		Double userBalanceUpdated = updateUserBalanceOrThrowInsufficientBalanceException(userBalance, stockPrice, quantity);
		userWallet.setBalance(userBalanceUpdated);
		walletService.saveUpdatedBalanceInWallet(userWallet);

		Asset assetData = assetService.findOrCreate(symbol, stockName, stockPrice); 

		Order order = new Order(userWallet, assetData, quantity, OrderType.BUY, stockPrice);
		orderRepository.save(order);

		return new OrderDTO(order.getId(), symbol, walletId, order.getType(), order.getQuantity(), order.getPriceAtExecution(), order.getTradeTime());
	}

	public Double updateUserBalanceOrThrowInsufficientBalanceException(Double userBalance, Double stockPrice,
			Integer quantity) {

		Double stockPriceValue = stockPrice * quantity;
		if (userBalance < stockPriceValue) {
			throw new IllegalArgumentException("Saldo insuficiente para operação");
		}

		BigDecimal stockPriceInBigDecimal = BigDecimal.valueOf(stockPriceValue);
		BigDecimal userBalanceBeforeBuyBigDecimal = BigDecimal.valueOf(userBalance);
		BigDecimal userBalanceUpdated = userBalanceBeforeBuyBigDecimal.subtract(stockPriceInBigDecimal);

		return userBalanceUpdated.doubleValue();

	}

}
