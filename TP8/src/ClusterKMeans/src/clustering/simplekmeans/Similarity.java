package clustering.simplekmeans;

public class Similarity  {
	private String nameClass1;
	private String nameClass2;
	private float valueSimilarity;
	
	public Similarity() {}
	
	public Similarity(String nameClass1, String nameClass2, float valueSimilarity) {
		super();
		this.nameClass1 = nameClass1;
		this.nameClass2 = nameClass2;
		this.valueSimilarity = valueSimilarity;
	}


	public String getNameClass1() {
		return nameClass1;
	}
	public void setNameClass1(String namePackage) {
		this.nameClass1 = namePackage;
	}
	public String getNameClass2() {
		return nameClass2;
	}
	public void setNameClass2(String nameClass) {
		this.nameClass2 = nameClass;
	}
	public float getValueSimilarity() {
		return valueSimilarity;
	}
	public void setValueSimilarity(float valueSimilarity) {
		this.valueSimilarity = valueSimilarity;
	}
	
	private String namePackage2;
	private String namePackage3;
	
	public String getNamePackage2() {
		return namePackage2;
	}

	public void setNamePackage2(String namePackage2) {
		this.namePackage2 = namePackage2;
	}

	public String getNamePackage3() {
		return namePackage3;
	}

	public void setNamePackage3(String namePackage3) {
		this.namePackage3 = namePackage3;
	}

	
}
