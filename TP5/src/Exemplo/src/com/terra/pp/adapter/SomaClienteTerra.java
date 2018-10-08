package com.terra.pp.adapter;

public class SomaClienteTerra implements SomaCliente {

	private Matematica m = new Matematica();
	
	@Override
	public int soma(int x, int y) {
		return (int) m.soma(x, y);
	}

	@Override
	public int sub(int x, int y) {
		return (int) m.soma(x, -1*y);
	}

	@Override
	public int mult(int x, int y) {
		if (y==0) {
			return 0;
		}
		int soma = x;
		for (int i=1; i<y; i++) {
			soma  = this.soma(soma, x);
		}
		return soma;
		
	}

}
