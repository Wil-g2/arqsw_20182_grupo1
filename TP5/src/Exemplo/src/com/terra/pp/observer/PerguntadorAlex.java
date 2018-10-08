package com.terra.pp.observer;

public class PerguntadorAlex implements Observador {

	
	@Override
	public void notifica() {
		System.out.println("Alex você ignorou uma mensagem de Deus");
	}

}
