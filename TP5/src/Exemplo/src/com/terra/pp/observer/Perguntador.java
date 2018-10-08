package com.terra.pp.observer;

public class Perguntador implements Runnable {

	private final String nome;
	
	public Perguntador(String nome) {
		this.nome = nome;
	}
	
	@Override
	public void run() {
		String msg = EntidadeSuperior.getInstancia().getMensagem();
		System.out.println(nome + ": " + msg);
		
		while(true){
			try {
				Thread.sleep((long) (Math.random()*10000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!msg.equals(EntidadeSuperior.getInstancia().getMensagem())){
				msg = EntidadeSuperior.getInstancia().getMensagem();
				System.out.println(nome + ": " + msg);
			}
		}
		
	}

}
