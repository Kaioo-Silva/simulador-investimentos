package com.simulador.investimentos.service;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import com.simulador.investimentos.client.BrapiClient;
import com.simulador.investimentos.dtos.OrderClosedDTO;
import com.simulador.investimentos.dtos.OrderDTO;
import com.simulador.investimentos.dtos.QuoteResponseDTO;
import com.simulador.investimentos.entity.Asset;
import com.simulador.investimentos.entity.Order;
import com.simulador.investimentos.entity.OrderType;
import com.simulador.investimentos.entity.User;
import com.simulador.investimentos.entity.Wallet;
import com.simulador.investimentos.exception.InsufficientBalanceException;
import com.simulador.investimentos.exception.ResourceNotFoundException;
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

		return new OrderDTO(order.getId(), symbol, walletId, order.getType(), order.getQuantity(),
				            order.getPriceAtExecution(), order.getTradeTime());
	}

	public OrderClosedDTO sellOpenPosition(Long userId, Long orderId, Integer quantityStocksWishToSell) {
		User user = userService.findUser(userId);
		Double userWalletBalance = user.getWallet().getBalance(); 
		Wallet userWallet = user.getWallet();
		Long walletId = userWallet.getId();

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("id da ordem informada não existe"));
		
		if(quantityStocksWishToSell > order.getQuantity() ) {
			throw new ResourceNotFoundException("Não é possível vender mais ações do que você possui");
		}

		QuoteResponseDTO brapiStockData = brapiClient.getQuote(order.getAsset().getSymbol());
        Double stockPrice = brapiStockData.results().get(0).regularMarketPrice(); 

		Double userBalanceBoughtOrder = order.getPriceAtExecution() * order.getQuantity(); 																					
        Double balanceUpdated = updateUserBalanceClosingOrder(stockPrice, userBalanceBoughtOrder, quantityStocksWishToSell);

		userWallet.setBalance(userWalletBalance + balanceUpdated);
		walletService.saveUpdatedBalanceInWallet(userWallet);

		Order orderClosed = new Order(userWallet, order.getAsset(), quantityStocksWishToSell, OrderType.SELL, stockPrice);
		orderRepository.save(orderClosed);

		String messageSucessOrder = "Ordem finalizada: " + quantityStocksWishToSell + " ações de " + orderClosed.getAsset().getName()
				                    + " foram vendidas no valor de " + stockPrice;

		return new OrderClosedDTO(orderClosed.getId(), orderClosed.getAsset().getSymbol(),
				walletId, OrderType.SELL, quantityStocksWishToSell, stockPrice, orderClosed.getTradeTime(), messageSucessOrder);
	}

	public Double updateUserBalanceOrThrowInsufficientBalanceException(Double userBalance, Double stockPrice,
			Integer quantity) {

		Double stockPriceValue = stockPrice * quantity;
		if (userBalance < stockPriceValue) {
			throw new InsufficientBalanceException("Saldo insuficiente para operação");
		}

		BigDecimal stockPriceInBigDecimal = BigDecimal.valueOf(stockPriceValue);
		BigDecimal userBalanceBeforeBuyBigDecimal = BigDecimal.valueOf(userBalance);
		BigDecimal userBalanceUpdated = userBalanceBeforeBuyBigDecimal.subtract(stockPriceInBigDecimal);

		return userBalanceUpdated.doubleValue();
	}

	public Double updateUserBalanceClosingOrder(Double stockPriceNow, Double userBalanceBoughtOrder,
			Integer quantityStocksWishToSell) {

		Double valueWishedToCloseOrder = stockPriceNow * quantityStocksWishToSell;
		if (userBalanceBoughtOrder <= valueWishedToCloseOrder) {
			return valueWishedToCloseOrder - userBalanceBoughtOrder;
		}
		if (userBalanceBoughtOrder > valueWishedToCloseOrder) {
			return userBalanceBoughtOrder - valueWishedToCloseOrder;
		}
		return null;
	}

}
