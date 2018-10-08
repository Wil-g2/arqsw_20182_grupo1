package com.terra.pp.adapter;

public class Main {

	public static void main(String[] args) {

		SomaCliente sc = new SomaClienteTerra();
		System.out.printf("%d\n",sc.soma(2, 3));
		System.out.printf("%d\n",sc.sub(2, 3));
		System.out.printf("%d\n",sc.mult(2, 3));
		
	}
	
}
