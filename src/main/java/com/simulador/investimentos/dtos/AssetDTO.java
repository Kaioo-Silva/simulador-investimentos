package com.simulador.investimentos.dtos;

public record AssetDTO(String symbol, String shortName, Double regularMarketPrice) { //ESSE É O ÚNICO QUE MANTEM COMO DOUBLE, POIS RECEBE DA API EXTERNA

}
