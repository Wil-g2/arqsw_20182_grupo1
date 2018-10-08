package model;

import presenter.TestPresenter;

public class Test {

	private int idade; 
	private String nome; 
	
	public Test(int idade, String nome) {
		this.idade = idade; 
		this.nome = nome;
		TestPresenter tp = new TestPresenter();
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String test() {
		return "teste";
	}
}
