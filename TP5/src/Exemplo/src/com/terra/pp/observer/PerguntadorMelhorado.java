package com.terra.pp.observer;

public class PerguntadorMelhorado implements Observador {

	private final String nome;

	public PerguntadorMelhorado(String nome) {
		this.nome = nome;
	}

	@Override
	public void notifica() {
		System.out.println(nome + ": " + EntidadeSuperiorMelhorada.getInstancia().getMensagem());

	}

}
