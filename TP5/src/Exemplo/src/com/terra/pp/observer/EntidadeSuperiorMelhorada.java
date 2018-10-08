package com.terra.pp.observer;

import java.util.HashSet;
import java.util.Set;

public class EntidadeSuperiorMelhorada implements Observavel {
	private Set<Observador> seguidores; 
	
	private static EntidadeSuperiorMelhorada instancia = null;
	
	private EntidadeSuperiorMelhorada(){
		seguidores = new HashSet<>();
	}
	
	public static synchronized EntidadeSuperiorMelhorada getInstancia() {
		if (instancia==null){
			instancia = new EntidadeSuperiorMelhorada();
		}
		return instancia;
	}
	
	private String mensagem = "Deus te ilumine!";
	
	public String getMensagem() {
		return mensagem;
	}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
		this.notificaTodos();
			
	}

	@Override
	public void addObservador(Observador o) {
		this.seguidores.add(o);
	}

	@Override
	public void notificaTodos() {
		for (Observador o : this.seguidores){
			o.notifica();
		}	
		
	}

	
}
