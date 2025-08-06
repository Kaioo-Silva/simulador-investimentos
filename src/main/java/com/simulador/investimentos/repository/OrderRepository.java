package com.simulador.investimentos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simulador.investimentos.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
