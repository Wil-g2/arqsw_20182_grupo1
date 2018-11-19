/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package descoberta;

import conexoes.ConexaoSQLLite;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 *
 * @author Willian
 */
public class Descoberta {

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

                String queryPacotes = "select pacote1 from pacotes";
                String verificarAcesso = "select count(*) from pacotes where pacote1= ?";
                String verificarModel = "select count(*) from project where origem like ? and destino like \"java.sql%\" ";
                String verificarView = "select count(*) from project where origem like ? and destino like \"javax.swing%\" ";
                String verificarCamada = "select count(*) from pacotes where pacote1=? and pacote2=?";
                String verificarCamada2 = "select count(*) from pacotes where pacote2=?";

                prepareStatement = connection.preparesStatement(queryPacotes);
                ResultSet rsPacotes = prepareStatement.executeQuery();
                while (rsPacotes.next()) {
                    prepareStatement = connection.preparesStatement(verificarAcesso);
                    prepareStatement.setString(1, rsPacotes.getString(1));
                    ResultSet rsAcesso = prepareStatement.executeQuery();
                    if (rsAcesso.getInt(1) == 1) {
                        prepareStatement = connection.preparesStatement(verificarCamada);
                        prepareStatement.setString(1, rsPacotes.getString(1) + "%");
                        prepareStatement.setString(2, rsPacotes.getString(1) + "%");
                        ResultSet rsCamda = prepareStatement.executeQuery();
                        if (rsCamda.getInt(1) == 1) {
                            System.out.println("Camada ou Pipeline");
                        } else {
                            prepareStatement = connection.preparesStatement(verificarModel);
                            prepareStatement.setString(1, rsPacotes.getString(1) + "%");
                            ResultSet rsModel = prepareStatement.executeQuery();
                            if (rsModel.getInt(1) > 0) {
                            }
                            prepareStatement = connection.preparesStatement(verificarView);
                            prepareStatement.setString(1, rsPacotes.getString(1) + "%");
                            ResultSet rsView = prepareStatement.executeQuery();
                            if (rsView.getInt(1) > 0) {
                                String viewModel = "select count(*) from project where origem like ? and tipo in (\"declare\",\"extend\",\"create\") and destino like \"java.sql%\" ";
                                prepareStatement = connection.preparesStatement(viewModel);
                                prepareStatement.setString(1, rsPacotes.getString(1) + "%");
                                ResultSet rsViewModel = prepareStatement.executeQuery();
                                if (rsViewModel.getInt(1) > 0) {
                                    System.out.println("MVC");
                                } else {
                                    System.out.println("MVP");
                                }

                            }
                        }
                    } else if (rsAcesso.getInt(1) >= 2) {
                        prepareStatement = connection.preparesStatement(verificarModel);
                        prepareStatement.setString(1, rsPacotes.getString(1) + "%");
                        ResultSet rsModel = prepareStatement.executeQuery();
                        if (rsModel.getInt(1) > 0) {
                            System.out.println("MVP");
                        }
                        prepareStatement = connection.preparesStatement(verificarView);
                        prepareStatement.setString(1, rsPacotes.getString(1) + "%");
                        ResultSet rsView = prepareStatement.executeQuery();
                        if (rsView.getInt(1) > 0) {
                            System.out.println("MVC");
                        }
                    } else {
                        prepareStatement = connection.preparesStatement(verificarCamada2);
                        prepareStatement.setString(1, rsPacotes.getString(1) + "%");
                        ResultSet rsCamada = prepareStatement.executeQuery();
                        if (rsCamada.getInt(1)>0){
                            System.out.println("Camada ou Pipeline");
                        }
                    }
                }

                String queryPacotesAccess = "select pacote1,pacote2 from pacotes";
                prepareStatement = connection.preparesStatement(queryPacotesAccess);
                ResultSet rsPacotesAccess = prepareStatement.executeQuery();
                while (rsPacotesAccess.next()) {
                    System.out.println(rsPacotesAccess.getString(1) + " -> " + rsPacotesAccess.getString(2));
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            } finally {
                connection.desconectar();
            }
        }
    }
}
