package com.terra.pp.abstractfactory;

import java.util.List;

public class Main {

	public static void main(String[] args) {
		List<String> lista = new Fabrica().getList();
		
		lista.add("Maria");
		lista.add("Mario");
		lista.add("Mário");
		lista.add("Alexandre");
		lista.add("Elena");
		lista.add("Christian");
		lista.add("Juliana");
		lista.add("Hoyama");
		lista.add("Helio");
		lista.add("Heleno");
		
		
		System.out.println(lista);
		

	}

}
