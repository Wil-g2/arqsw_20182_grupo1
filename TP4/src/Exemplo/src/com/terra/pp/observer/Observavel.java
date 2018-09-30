package com.terra.pp.observer;

public interface Observavel {

	void addObservador(Observador o);
	
	void notificaTodos();
	
}
