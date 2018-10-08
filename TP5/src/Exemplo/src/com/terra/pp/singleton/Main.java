package com.terra.pp.singleton;

public class Main {

	public static void main(String[] args) {
		/*ANTES*/
//		DateUtil du = new DateUtil();		
//		System.out.println(du.strToDate("13/09/2018 14:34", "dd/MM/yyyy HH:mm"));		
		
		/*
		DateUtil du = DateUtil.getInstance();		
		System.out.println(du.strToDate("13/09/2018 14:34", "dd/MM/yyyy HH:mm"));
		
		DateUtil du2 = DateUtil.getInstance();
		System.out.println(du2.strToDate("13/09/2018 14:34", "dd/MM/yyyy HH:mm"));
		
		DateUtil du3 = DateUtil.getInstance();
		System.out.println(du3.strToDate("13/09/2018 14:34", "dd/MM/yyyy HH:mm"));
		*/
		
		new Thread(new SabotarSingleton()).start();
		new Thread(new SabotarSingleton()).start();
		new Thread(new SabotarSingleton()).start();
		new Thread(new SabotarSingleton()).start();
		
		
	}

	


}
