package com.simulador.investimentos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.simulador.investimentos.entity.Users;
import com.simulador.investimentos.entity.Wallet;
import com.simulador.investimentos.repository.UsersRepository;
import com.simulador.investimentos.repository.WalletRepository;

@Service
public class UserService {
	
	private final UsersRepository usersRepository;
	
	private final WalletRepository walletRepository;
	
	
	
	public UserService(UsersRepository usersRepository, WalletRepository walletRepository) {
		this.usersRepository = usersRepository;
		this.walletRepository = walletRepository;
	}


	public Users criarUsuario(Users user) {
	    return usersRepository.save(user); 

	   
	}
	
	public List<Users> getAll(){
		return usersRepository.findAll();
	}

}
