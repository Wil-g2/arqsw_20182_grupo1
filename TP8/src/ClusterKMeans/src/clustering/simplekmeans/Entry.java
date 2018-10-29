package clustering.simplekmeans;

public class Entry implements Comparable<Entry> {
	private String class1;
	private String class2;
	private float value;

	public Entry(String classe1, String classe2, float valor) {
		this.class1 = classe1;
		this.class2 = classe2;
		this.value = valor;
	}

    public Entry() {}

	@Override
	public int compareTo(Entry other){
		if (this.value < other.value) {
			return 1;
		}
		if (this.value > other.value) {
			return -1;
		}
	      return 0;
	}

	public String getClasse1() {
		return class1;
	}

	public void setClasse1(String classe1) {
		this.class1 = classe1;
	}

	public String getClasse2() {
		return class2;
	}

	public void setClasse2(String classe2) {
		this.class2 = classe2;
	}

	public float getValor() {
		return value;
	}

	public void setValor(float valor) {
		this.value = valor;
	}

	
	

	
    
    
    
    
}
