package teste;

public class Cliente {
	
	private String id; 
	private String cpf; 
	private String nome; 
	
	private String rua; 
	private String numero;
	private String cidade; 
	private String bairro;
	
	private String infoCli;
	private String tipoCli;
	
	public void addCliente() {	
		System.out.println(this.id + this.nome + this.cpf + 		this.cidade);
	}
	
	public void addEnderecoCliente() {		
		System.out.println(this.rua + this.numero + this.bairro + 	this.cidade);		
	}
	
	public void addContaCliente() {		
		System.out.println(this.tipoCli + this.infoCli + 			this.cidade);		
	}
}
