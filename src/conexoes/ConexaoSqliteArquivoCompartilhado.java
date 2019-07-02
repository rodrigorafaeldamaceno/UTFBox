/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexoes;

import arquivo.Arquivo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rodri
 */
public class ConexaoSqliteArquivoCompartilhado {

    private Connection conexao;

    public boolean conectar() {

        try {
            String url = "jdbc:sqlite:banco_de_dados/banco_sqlite.db";
            this.conexao = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
        System.out.println("conectou!!!");
        return true;
    }

    public boolean desconectar() {

        try {
            if (this.conexao.isClosed() == false) {
                this.conexao.close();
            }

        } catch (SQLException e) {

            System.err.println(e.getMessage());
            return false;
        }
        System.out.println("desconectou!!!");
        return true;
    }

    public void inserirArquivoComp(String user, String arquivo, String dir) {
        Statement stmt = null;
        try {
            String url = "jdbc:sqlite:banco_de_dados/banco_sqlite.db";
            this.conexao = DriverManager.getConnection(url);
            this.conexao.setAutoCommit(false);
            //System.out.println("Conexao estabelecida");

            stmt = this.conexao.createStatement();
            String sql = "INSERT INTO USER_ARQUIVO_COMPARTILHADO(USER, ARQUIVO, DIRETORIO) VALUES(" + "'" + user + "','" + arquivo + dir + "');";

            stmt.executeUpdate(sql);
            stmt.close();
            this.conexao.commit();
            this.conexao.close();
        } catch (Exception e) {
            System.out.println("erro");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Arquivo salvo com sucesso no BD");

    }

    public ArrayList buscarArquivosUserComp(String usuario) {

        Statement stmt = null;

        ArrayList<Arquivo> arquivos = new ArrayList<Arquivo>();

        try {
            String url = "jdbc:sqlite:banco_de_dados/banco_sqlite.db";
            this.conexao = DriverManager.getConnection(url);
            this.conexao.setAutoCommit(false);
            //System.out.println("Conexao estabelecida");

            stmt = this.conexao.createStatement();
            String sqlQuery = "SELECT ARQUIVO, USER, DIRETORIO, DATA_MODIFICACAO FROM USER_ARQUIVO_COMPARTILHADO WHERE UPPER(USER)='" + usuario + "';";
            ResultSet rs = stmt.executeQuery(sqlQuery);

            while (rs.next()) {
                Arquivo arq = new Arquivo();
                arq.setNome(rs.getString("ARQUIVO"));
                arq.setUsuario(rs.getString("USER"));
                arq.setDataModificacao(rs.getString("DATA_MODIFICACAO"));

                arquivos.add(arq);
                System.out.println("Arquivo: " + arq.getNome() + " Dono: " + arq.getUsuario());
            }

            rs.close();
            stmt.close();
            this.conexao.close();
            return arquivos;

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            //System.exit(0);
        }

        //System.out.println("Select executado");  
        return null;
    }
}
