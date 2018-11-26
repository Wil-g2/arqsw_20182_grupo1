package presenter;

import model.Model;

public class Presenter {
	
	public String TestView() {
		Model t = new Model();	     
		return t.toString();	
	}
}
