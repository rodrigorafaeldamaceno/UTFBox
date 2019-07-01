/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import arquivo.Arquivo;
import conexoes.ConexaoSqliteArquivo;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Rodri
 */
public class Server {

    public static void main(String args[]) {

        try {
            // 1
            ServerSocket srvSocket = new ServerSocket(5566);
            while (true) {
                System.out.println("Aguardando envio de arquivo ...");

                Socket socket = srvSocket.accept();

                // 2
                byte[] objectAsByte = new byte[socket.getReceiveBufferSize()];
                BufferedInputStream bf = new BufferedInputStream(socket.getInputStream());
                bf.read(objectAsByte);

                // 3
                Arquivo arquivo = (Arquivo) getObjectFromByte(objectAsByte);
                /*
                * // 4 String dir = arquivo.getDiretorioDestino().endsWith("/") ?
                * arquivo.getDiretorioDestino() + arquivo.getNome() :
                * arquivo.getDiretorioDestino() + "/" + arquivo.getNome();
                * System.out.println("Escrevendo arquivo " + dir);
                 */

                File diretorio = new File("C://utfboxServidor/" + arquivo.getUsuario());
                diretorio.mkdir();
                String dir = "C://utfboxServidor/" + arquivo.getUsuario() + "/" + arquivo.getNome();
                System.out.println("Escrevendo arquivo " + dir);

                FileOutputStream fos = new FileOutputStream(dir);
                fos.write(arquivo.getConteudo());
                fos.close();
                
                Date dataAtual = new Date(System.currentTimeMillis());
                SimpleDateFormat formatarDate = new SimpleDateFormat("yyyy-MM-dd");
                
                gravarArquivoBD(arquivo.getNome(), arquivo.getUsuario(),formatarDate.format(dataAtual), dir);
                
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void gravarArquivoBD(String nome,String dono,String dt_modificacao,String dir){
        ConexaoSqliteArquivo conexaoArquivo = new ConexaoSqliteArquivo();
        conexaoArquivo.inserirArquivo(nome, dono, dt_modificacao, dir);
    }

    private static Object getObjectFromByte(byte[] objectAsByte) {
        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        boolean flag = true;
        do {
            try {
                bis = new ByteArrayInputStream(objectAsByte);
                ois = new ObjectInputStream(bis);
                obj = ois.readObject();

                bis.close();
                ois.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                flag = false;
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                flag = false;
            }

            return obj;
        } while (flag);

    }
}
