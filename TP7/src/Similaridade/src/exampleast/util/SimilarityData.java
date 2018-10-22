package exampleast.util;

public class SimilarityData {

	
	
	private String namePackage;
	private String nameClass;
	private String valueSimilarity;
	
	public SimilarityData(String namePackage, String namePackage2, String namePackage3, String nameClass) {
		super();
		this.namePackage = namePackage;
		this.namePackage2 = namePackage2;
		//this.namePackage3 = namePackage3;
		this.nameClass = nameClass;
		
	}
	public SimilarityData() {}
	
	public SimilarityData(String namePackage, String nameClass, String valueSimilarity) {
		super();
		this.namePackage = namePackage;
		this.nameClass = nameClass;
		this.valueSimilarity = valueSimilarity;
	}


	public String getNamePackage() {
		return namePackage;
	}
	public void setNamePackage(String namePackage) {
		this.namePackage = namePackage;
	}
	public String getNameClass() {
		return nameClass;
	}
	public void setNameClass(String nameClass) {
		this.nameClass = nameClass;
	}
	public String getValueSimilarity() {
		return valueSimilarity;
	}
	public void setValueSimilarity(String valueSimilarity) {
		this.valueSimilarity = valueSimilarity;
	}
	
	private String namePackage2;
	//private String namePackage3;
	
	public String getNamePackage2() {
		return namePackage2;
	}

	public void setNamePackage2(String namePackage2) {
		this.namePackage2 = namePackage2;
	}

	/*public String getNamePackage3() {
		return namePackage3;
	}

	public void setNamePackage3(String namePackage3) {
		this.namePackage3 = namePackage3;
	}*/
}
