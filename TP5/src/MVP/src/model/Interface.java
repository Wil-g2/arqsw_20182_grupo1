package view;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.Model;
import presenter.Presenter;

/**
 * interface do programa
 */
public class Interface {

	private JLabel lbl1;
	private JLabel lbl2;
	private JLabel lbl3;
	private JTextField txt1;
	private JTextField txt2;
	private JTextField txt3;
	private JPanel panel; 
	private JTextArea area;
	public static void main(String args[]){     
        //TestPresenter tpCorrect = new TestPresenter();        
        JOptionPane.showMessageDialog(null, "teste");
        Model model = new Model();
        //t.test();
    }
}
