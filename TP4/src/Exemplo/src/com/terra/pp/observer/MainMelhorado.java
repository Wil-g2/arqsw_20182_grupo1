package com.terra.pp.observer;

import javax.swing.JOptionPane;

public class MainMelhorado {

	public static void main(String[] args) {
		EntidadeSuperiorMelhorada esm = EntidadeSuperiorMelhorada.getInstancia();
		
		esm.addObservador(new PerguntadorMelhorado("Mallu"));
		esm.addObservador(new PerguntadorMelhorado("Felipe"));
		esm.addObservador(new PerguntadorMelhorado("Lucas"));
		esm.addObservador(new PerguntadorMelhorado("Carlos"));
		esm.addObservador(new PerguntadorMelhorado("Otávio"));
		//esm.addObservador(new PerguntadorAlex());
		
		while(true){
			String msg = JOptionPane.showInputDialog("Nova mensagem");
			EntidadeSuperiorMelhorada.getInstancia().setMensagem(msg);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
