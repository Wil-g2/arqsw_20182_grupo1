package presenter;

import model.Test;

public class TestPresenter {
	
	public String TestView() {
		Test t = new Test(10, "Jose");	     
		return t.test();	
	}
}
