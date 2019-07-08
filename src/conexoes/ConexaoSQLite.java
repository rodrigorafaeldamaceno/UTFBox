package conexoes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

/**
 *
 * @author Rodrigo Rafael
 */
public class ConexaoSQLite {

    private Connection conexao;

    /**
     * Conecta a um banco de dados (cria o banco se ele não existir)
     *
     * @author Rodrigo Rafael
     */
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

    public void inserirUsuario(String name, String pass) {
        Statement stmt = null;
        try {
            String url = "jdbc:sqlite:banco_de_dados/banco_sqlite.db";
            this.conexao = DriverManager.getConnection(url);
            this.conexao.setAutoCommit(false);
            //System.out.println("Conexao estabelecida");

            stmt = this.conexao.createStatement();
            String sql = "INSERT INTO user(nome, password) VALUES(" + "'" + name + "','" + pass + "');";

            stmt.executeUpdate(sql);
            stmt.close();
            this.conexao.commit();
            this.conexao.close();
        } catch (Exception e) {
            System.out.println("erro");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Salvo com sucesso");

    }

    public boolean selectUsuario(String name, String pass) {
        Statement stmt = null;
        boolean achou = false;
        try {
            String url = "jdbc:sqlite:banco_de_dados/banco_sqlite.db";
            this.conexao = DriverManager.getConnection(url);
            this.conexao.setAutoCommit(false);
            //System.out.println("Conexao estabelecida");

            stmt = this.conexao.createStatement();
            String sqlQuery = "select id, upper(nome) as nome from user where upper(nome)='" + name + "' and password='" + pass + "';";
            ResultSet rs = stmt.executeQuery(sqlQuery);

            while (rs.next()) {
                if (rs.getString("nome") != null) {
                    achou = true;
                }else{
                    achou=false;
                };
            }

            rs.close();
            stmt.close();
            this.conexao.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            //System.exit(0);
        }

        //System.out.println("Select executado");  
        return achou;
    }

    public int existeUserName(String name) {
        Statement stmt = null;
        //boolean achou = true;
        int user_id = -1;
        try {
            String url = "jdbc:sqlite:banco_de_dados/banco_sqlite.db";
            this.conexao = DriverManager.getConnection(url);
            this.conexao.setAutoCommit(false);
            //System.out.println("Conexao estabelecida");

            stmt = this.conexao.createStatement();
            String sqlQuery = "select ID, upper(nome) as nome from user where upper(nome)='" + name + "';";
            ResultSet rs = stmt.executeQuery(sqlQuery);

            while (rs.next()) {
                if (rs.getString("nome") != null) {
                    user_id = Integer.parseInt(rs.getString("ID"));
                    System.out.println(user_id);
                };
                
            }

            rs.close();
            stmt.close();
            this.conexao.close();

        } catch (Exception e) {
            user_id = -1;
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            //System.exit(0);
        }

        //System.out.println("Select executado");  
        return user_id;
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
}
