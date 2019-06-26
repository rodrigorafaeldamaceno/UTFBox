/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import cliente.Arquivo;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Rodri
 */
public class Server {

    public static void main(String args[]) {
        //String user = "talysson";
        try {
            // 1
            ServerSocket srvSocket = new ServerSocket(5566);
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

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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
