/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlcode;

import conexoes.ConexaoSQLLite;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.Pack200;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import jdk.nashorn.internal.objects.NativeString;

/**
 *
 * @author Willian
 */
public class teste {

    public static void main(String[] args) {
        ConexaoSQLLite connection = new ConexaoSQLLite();
        String classes = "select origem from project";
        String query = "select distinct origem,destino from project where origem like ? and tipo in (\"declare\",\"extend\",\"create\") and destino in (select distinct origem from project where tipo in (\"declare\",\"extend\",\"create\")) ";
        Set<String> pacote = new HashSet<>();
        Set<String> pacoteAccess = new HashSet<>();
        String tables = "CREATE TABLE IF NOT EXISTS pacotes (id INTEGER PRIMARY KEY AUTOINCREMENT, pacote1 VARCHAR(255) NOT NULL,pacote2 VARCHAR(255) NOT NULL)";
        String insert = "INSERT INTO pacotes(pacote1,pacote2) VALUES(?,?);";
        String del = "DELETE FROM pacotes";

        if (connection.conectar()) {
            try {
                Statement stmt = connection.criarStatement();
                stmt.execute(tables);
                stmt.execute(del);
                PreparedStatement prepareStatement = null;
                String[] parameters = null;
                prepareStatement = connection.preparesStatement(classes);
                ResultSet rs = prepareStatement.executeQuery();                
                while (rs.next()) {
                    String classe = rs.getString(1);
                    int index = classe.lastIndexOf(".");
                    pacote.add(classe.substring(0, index));
                }
                for (String s : pacote) {
                    prepareStatement = connection.preparesStatement(query);
                    prepareStatement.setString(1, s.trim() + "%");
                    ResultSet rsQuery = prepareStatement.executeQuery();
                    prepareStatement = connection.preparesStatement(insert);
                    while (rsQuery.next()) {
                        int index = rsQuery.getString(1).lastIndexOf(".");
                        int index1 = rsQuery.getString(2).lastIndexOf(".");
                        String a = rsQuery.getString(1).substring(0, index);
                        String b = rsQuery.getString(2).substring(0, index1);
                        if (!a.equals(b)) {
                            try {
                                prepareStatement.setString(1, a);
                                prepareStatement.setString(2, b);                                
                                prepareStatement.execute();
                                pacoteAccess.add(a + " access " + b);
                            } catch (Exception e) {

                            }
                        }

                    }
                }

                for (String s : pacoteAccess) {
                    System.out.println(s);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            } finally {
                connection.desconectar();
            }
        }
    }
}
