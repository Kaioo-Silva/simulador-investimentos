package com.simulador.investimentos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simulador.investimentos.entity.Users;


@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

}
