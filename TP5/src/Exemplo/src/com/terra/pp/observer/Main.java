package com.terra.pp.observer;

import javax.swing.JOptionPane;

public class Main {

	public static void main(String[] args) {
		new Thread(new Perguntador("Ju")).start();
		new Thread(new Perguntador("Hoy")).start();
		new Thread(new Perguntador("Elena")).start();
		new Thread(new Perguntador("Danielle")).start();
		new Thread(new Perguntador("Alexandre")).start();
		while(true){
			String msg = JOptionPane.showInputDialog("Nova mensagem");
			EntidadeSuperior.getInstancia().setMensagem(msg);
		}
	}
	
}
