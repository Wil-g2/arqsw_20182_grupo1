package view;

import model.Test;
import presenter.TestPresenter;

/**
 * interface do programa
 */
public class Interface {

	public static void main(String args[]){
        Interface principal = new Interface();
        TestPresenter tpCorrect = new TestPresenter();
        System.out.println(tpCorrect);
        //Test t = new Test(10, "Jose");
        //t.test();
    }
}
