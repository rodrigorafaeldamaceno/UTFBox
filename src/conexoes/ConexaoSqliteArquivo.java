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
 * @author Rodrigo Rafael
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

    public void inserirArquivo(String nome, String usuario, String dt_modificacao, String dir) {
        Statement stmt = null;
        try {
            String url = "jdbc:sqlite:banco_de_dados/banco_sqlite.db";
            this.conexao = DriverManager.getConnection(url);
            this.conexao.setAutoCommit(false);
            // System.out.println("Conexao estabelecida");

            stmt = this.conexao.createStatement();
            String sql = "INSERT INTO ARQUIVO(NOME, USUARIO, DATA_MODIFICACAO, DIRETORIO) VALUES(" + "'" + nome + "','"
                    + usuario + "','" + dt_modificacao + "','" + dir + "');";

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

    public void inserirArquivoComp(String user, int arquivo_id) {
        Statement stmt = null;
        try {
            String url = "jdbc:sqlite:banco_de_dados/banco_sqlite.db";
            this.conexao = DriverManager.getConnection(url);
            this.conexao.setAutoCommit(false);
            //System.out.println("Conexao estabelecida");

            stmt = this.conexao.createStatement();
            String sql = "INSERT INTO USER_ARQUIVO_COMPARTILHADO(USER, ARQUIVO_ID) VALUES(" + "'" + user + "','" + arquivo_id + "');";

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
            //String sqlQuery = "SELECT NOME, USUARIO, DATA_MODIFICACAO FROM ARQUIVO WHERE UPPER(USUARIO)='" + USUARIO + "';";
            String sqlQuery = "SELECT US.NOME AS USUARIO, ARQ.ID AS ID_ARQUIVO, ARQ.NOME AS ARQUIVO, ARQ.DATA_MODIFICACAO AS DATA_MODIFICACAO, ARQ.DIRETORIO AS DIRETORIO " 
                    +"FROM USER AS US, ARQUIVO AS ARQ "
                    + "WHERE ARQ.USUARIO=US.NOME AND ARQ.USUARIO='" + usuario + "' "
                    + "UNION ALL "
                    + "SELECT ARQ.USUARIO AS USUARIO, ARQ.ID AS ID_ARQUIVO, ARQ.NOME AS ARQUIVO, ARQ.DATA_MODIFICACAO AS DATA_MODIFICACAO, ARQ.DIRETORIO AS DIRETORIO "
                    + "FROM ARQUIVO ARQ, USER_ARQUIVO_COMPARTILHADO AS ARQCOMP "
                    + "WHERE ARQ.ID=ARQCOMP.ARQUIVO_ID AND ARQCOMP.USER='" + usuario + "';";

            ResultSet rs = stmt.executeQuery(sqlQuery);

            while (rs.next()) {
                Arquivo arq = new Arquivo();
                arq.setId(Integer.parseInt(rs.getString("ID_ARQUIVO")));
                arq.setNome(rs.getString("ARQUIVO"));
                arq.setUsuario(rs.getString("USUARIO"));
                arq.setDataModificacao(rs.getString("DATA_MODIFICACAO"));
                arq.setDiretorio(rs.getString("DIRETORIO"));

                arquivos.add(arq);
                System.out.println("Arquivo: "+ arq.getId() + " - " + arq.getNome() + " USUARIO: "  + arq.getUsuario());
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
