/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlcode;

import conexoes.ConexaoSQLLite;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.Scanner;

/**
 *
 * @author Willian
 */
public class _Main {

    public static void main(String args[]) {
        ConexaoSQLLite connection = new ConexaoSQLLite();
        String tables = "CREATE TABLE IF NOT EXISTS project (id INTEGER PRIMARY KEY AUTOINCREMENT, origim VARCHAR(255) NOT NULL,tipo VARCHAR(30) NOT NULL,destino VARCHAR(255) NOT NULL)";
        String insert = "INSERT INTO project(origim,tipo,destino) VALUES(?,?,?);";
        String del = "DELETE FROM project";
        if (connection.conectar()) {
            try {
                Statement stmt = connection.criarStatement();
                stmt.execute(tables);
                stmt.execute(del);
                Scanner ler = new Scanner(System.in);
                //String nome = "F:\\Desenvolvimento\\Projetos\\arqsw_20182_grupo1\\TP7\\src\\TestTP7\\src\\dependencies.txt";
                String nome = "C:\\Users\\Willian\\Documents\\NetBeansProjects\\SQLCode\\dependencies.txt";
                try {
                    FileReader arq = new FileReader(nome);
                    BufferedReader lerArq = new BufferedReader(arq);
                    String linha = lerArq.readLine(); // lê a primeira linha
                    // a variável "linha" recebe o valor "null" quando o processo
                    // de repetição atingir o final do arquivo texto
                    PreparedStatement prepareStatement = connection.preparesStatement(insert);
                    while (linha != null) {
                        System.out.println("Teste");
                        System.out.printf("%s\n", linha);                        
                        linha = lerArq.readLine(); // lê da segunda até a última linha
                        String[]dados = linha.split(",");
                        prepareStatement.setString(1,dados[0]);
                        prepareStatement.setString(2,dados[1]);
                        prepareStatement.setString(3,dados[2]);
                        prepareStatement.execute();                        
                    }

                    arq.close();
                } catch (IOException e) {
                    System.err.printf("Erro na abertura do arquivo: %s.\n",
                            e.getMessage());
                }
            } catch (Exception e) {

            } finally {

            }
        }

    }
}
