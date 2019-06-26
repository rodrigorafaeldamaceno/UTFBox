/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import conexoes.ConexaoSQLite;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author bruno
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void Main() {
        //ConexaoSQLite conexaoSQLite = new ConexaoSQLite();
        //conexaoSQLite.conectar();
        //conexaoSQLite.inserir("Rodrigo", "123456");

        String nome = "Rodrigo";
        String password = "123456789";
        password = stringHexa(gerarHash(password));
        nome = nome.toUpperCase();

        System.out.println(password);
        //registrar(nome,password);

        System.out.println(fazerLogin(nome, password));
        //conexaoSQLite.desconectar();
    }

    public static void registrar(String nome, String password) throws NoSuchAlgorithmException {

        ConexaoSQLite conexaoSQLite = new ConexaoSQLite();
        boolean existe = conexaoSQLite.existeUserName(nome);
        if (!existe) {
            System.out.println("usuario ja cadastrado");
        } else {
            conexaoSQLite.inserir(nome, password);
        }
    }

    public static boolean fazerLogin(String nome, String password) {
        ConexaoSQLite conexaoSQLite = new ConexaoSQLite();
        conexaoSQLite.select(nome.toUpperCase(), password);
        if (conexaoSQLite.select(nome, password)) {
            return true;
        } else {
            return false;
        }
    }

    public static byte[] gerarHash(String frase) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(frase.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private static String stringHexa(byte[] bytes) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
            int parteBaixa = bytes[i] & 0xf;
            if (parteAlta == 0) {
                s.append('0');
            }
            s.append(Integer.toHexString(parteAlta | parteBaixa));
        }
        return s.toString();
    }

}
