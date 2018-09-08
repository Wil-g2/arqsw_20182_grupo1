package teste;

public class Pessoa {
	
	public int id; 
	private String cpf; 
	private String nome; 
	private String rua; 
	private String numero;
	private String cidade; 
	private String bairro; 
	public String estado;
	
	public void addPessoa() {	
		String nome = this.nome;String rua1 = this.rua;
		String rua = this.rua;
		System.out.println("Id:"+String.valueOf(this.id)+" Nome:"+this.nome+" CPF:"+this.cpf);	
	}
	
	public void addEndereco() {		
		String rua = this.rua;
		//System.out.println("Endereço: "+ this.rua + " Número:"+ this.numero + " Bairro:"+this.bairro+" Cidade:"+this.cidade+" Estado:"+this.estado);		
	}

	
}
