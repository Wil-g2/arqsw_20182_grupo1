package com.terra.pp.observer;

public class EntidadeSuperior {

	private static EntidadeSuperior instancia = null;
	
	private EntidadeSuperior(){}
	
	public static synchronized EntidadeSuperior getInstancia() {
		if (instancia==null){
			instancia = new EntidadeSuperior();
		}
		return instancia;
	}
	
	private String mensagem = "Deus te ilumine!";
	
	public String getMensagem() {
		return mensagem;
	}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
}
