package com.terra.pp.singleton;

public class SabotarSingleton implements Runnable {

	@Override
	public void run() {
		DateUtil du = DateUtil.getInstance();
		System.out.println(du.strToDate("13/09/2018 14:34", "dd/MM/yyyy HH:mm"));
	}

}
