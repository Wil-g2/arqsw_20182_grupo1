package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//import presenter.Presenter;

public class Model {
 
	private Connection con;
	private Connection con2;
	private DriverManager driver;
	
	//Presenter presenter = new Presenter();

	public Connection getConnetion() throws SQLException {
		return con; 			
	}
	
	
}
