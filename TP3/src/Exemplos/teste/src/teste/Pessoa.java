package teste;

public class Pessoa {
	
	private String id; 
	private String cpf; 
	private String nome; 
	private String rua; 
	private String numero;
	private String cidade; 
	private String bairro; 
	private String estado;
	private String pais;
	private String estado2;
	private String pais2;
	
	
	public void addPessoa() {	
		System.out.println(this.id + this.nome + this.cpf);
		System.out.println(this.rua + this.numero + this.bairro + this.cidade);
	}
	
	public void addEndereco() {		
		System.out.println(this.rua + this.numero + this.bairro + this.cidade);		
	}
	
	public void addEstadoPais() {		
		System.out.println(this.estado + this.pais);		
	}
	public void addEstadoPais2() {		
		System.out.println(this.estado2 + this.pais2);		
	}
}
