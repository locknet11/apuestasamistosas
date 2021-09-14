package com.apuestasamistosas.app.errors;

public class ErrorTransaccion extends Exception{
	
	public ErrorTransaccion(String message) {
		super(message);
	}
	
	public static final String SALDO_INSUFICIENTE = "Saldo insuficiente";
	
}
