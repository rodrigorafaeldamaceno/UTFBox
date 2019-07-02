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
public class ConexaoSqliteArquivo {

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

    public void inserirArquivo(String nome, String dono, String dt_modificacao, String dir) {
        Statement stmt = null;
        try {
            String url = "jdbc:sqlite:banco_de_dados/banco_sqlite.db";
            this.conexao = DriverManager.getConnection(url);
            this.conexao.setAutoCommit(false);
            // System.out.println("Conexao estabelecida");

            stmt = this.conexao.createStatement();
            String sql = "INSERT INTO ARQUIVO(NOME, DONO, DATA_MODIFICACAO, DIRETORIO) VALUES(" + "'" + nome + "','"
                    + dono + "','" + dt_modificacao + "','" + dir + "');";

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

    public ArrayList buscarArquivosUser(String usuario) {

        Statement stmt = null;
        boolean achou = false;

        ArrayList<Arquivo> arquivos = new ArrayList<Arquivo>();

        try {
            String url = "jdbc:sqlite:banco_de_dados/banco_sqlite.db";
            this.conexao = DriverManager.getConnection(url);
            this.conexao.setAutoCommit(false);
            //System.out.println("Conexao estabelecida");

            stmt = this.conexao.createStatement();
            //String sqlQuery = "SELECT NOME, DONO, DATA_MODIFICACAO FROM ARQUIVO WHERE UPPER(DONO)='" + dono + "';";
            String sqlQuery = "SELECT US.NOME AS USUARIO, ARQ.NOME AS ARQUIVO, ARQ.DATA_MODIFICACAO AS DATA_MODIFICACAO FROM USER AS US, ARQUIVO AS ARQ" +
                                "WHERE ARQ.DONO=US.NOME AND ARQ.DONO='"+ usuario + "'"+
                                "UNION ALL"+
                                "select ARQCOMP.USER AS USUARIO, ARQ.NOME AS ARQUIVO, ARQ.DATA_MODIFICACAO AS DATA_MODIFICACAO "+
                                "from ARQUIVO ARQ, USER_ARQUIVO_COMPARTILHADO AS ARQCOMP"+
                                "WHERE ARQ.ID=ARQCOMP.ARQUIVO_IDAND ARQCOMP.USER='"+usuario+"');";
            
            
            ResultSet rs = stmt.executeQuery(sqlQuery);

            while (rs.next()) {
                Arquivo arq = new Arquivo();
                arq.setNome(rs.getString("ARQUIVO"));
                arq.setUsuario(rs.getString("DONO"));
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
