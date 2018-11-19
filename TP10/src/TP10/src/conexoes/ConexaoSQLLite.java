/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexoes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Willian
 */
public class ConexaoSQLLite {

    private Connection conexao;

    public boolean conectar() {
        try {
            String url = "jdbc:sqlite:banco/bd.db";
            this.conexao = DriverManager.getConnection(url);
            System.out.println("conectou!!");
            return true;
        } catch (SQLException e) {
            System.err.print(e.getMessage());
            return false;
        }        
    }

    public boolean desconectar() {
        try {
            if (this.conexao.isClosed()) {
                this.conexao.close();
                System.out.println("desconectou!!");
            }

        } catch (SQLException e) {
            System.err.print(e.getMessage());
            return false;
        }
        return true;
    }

    public Statement criarStatement() {
        try {
            return this.conexao.createStatement();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
    
    public PreparedStatement preparesStatement(String Sql) throws SQLException{
        PreparedStatement preparedStatement = this.conexao.prepareStatement(Sql);        
        return preparedStatement; 
    }
    
    
    public Connection getConnection(){
        return this.conexao;
    }
}
