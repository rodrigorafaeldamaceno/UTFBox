/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

/**
 *
 * @author Rodrigo rafael
 */
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.*;

import arquivo.Arquivo;

public class Cliente extends javax.swing.JFrame {

    private long tamanhoPermitidoKB = 5120; // Igual a 5MB
    private Arquivo arquivo;
    private String usuario;
    
    
    private static final long serialVersionUID = 1L;

    public Cliente(String nome) {
        this.usuario = nome;
        initComponents();
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jTextFieldNome = new javax.swing.JTextField();
        jButtonArquivo = new javax.swing.JButton();
        jLabelTamanho = new javax.swing.JLabel();
        jButtonEnviar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldIP = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldPorta = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jLabel1.setText("Arquivo Carregado");

        jTextFieldNome.setEnabled(false);

        jButtonArquivo.setText("Selecionar Arquivo");
        jButtonArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonArquivoActionPerformed(evt);
            }
        });

        jLabelTamanho.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabelTamanho.setText("Tamanho:");

        jButtonEnviar.setText("Enviar");
        jButtonEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEnviarActionPerformed(evt);
            }
        });

        jLabel2.setText("IP");

        jLabel3.setText("Usuario: " + this.usuario);

        jLabel4.setText("Porta");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
                .createSequentialGroup().addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTextFieldNome, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                        .addComponent(jLabel1).addComponent(jButtonEnviar).addComponent(jButtonArquivo)
                        .addComponent(jLabelTamanho)
                        .addGroup(layout.createSequentialGroup().addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104,
                                        Short.MAX_VALUE)
                                .addComponent(jTextFieldIP, javax.swing.GroupLayout.PREFERRED_SIZE, 162,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup().addGroup(layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                        layout.createSequentialGroup().addComponent(jLabel4)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jTextFieldPorta, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        162, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                        layout.createSequentialGroup().addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                ))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(37, 37, 37)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
                .createSequentialGroup().addContainerGap().addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldNome, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2)
                        .addComponent(jTextFieldIP, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4)
                        .addComponent(jTextFieldPorta, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3)
                )
                .addGap(16, 16, 16).addComponent(jButtonArquivo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabelTamanho)
                .addGap(25, 25, 25).addComponent(jButtonEnviar).addGap(139, 139, 139)));
        pack();
    }

    private void jButtonEnviarActionPerformed(java.awt.event.ActionEvent evt) {
        enviarArquivoServidor();
    }

    private void jButtonArquivoActionPerformed(java.awt.event.ActionEvent evt) {
        FileInputStream fis;
        try {

            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setDialogTitle("Escolha o arquivo");

            if (chooser.showOpenDialog(this) == JFileChooser.OPEN_DIALOG) {
                File fileSelected = chooser.getSelectedFile();

                byte[] bFile = new byte[(int) fileSelected.length()];
                fis = new FileInputStream(fileSelected);
                fis.read(bFile);
                fis.close();

                long kbSize = fileSelected.length() / 1024;
                jTextFieldNome.setText(fileSelected.getName());
                jLabelTamanho.setText(kbSize + " KB");

                arquivo = new Arquivo();
                arquivo.setConteudo(bFile);
                arquivo.setDataHoraUpload(new Date());
                arquivo.setNome(fileSelected.getName());
                arquivo.setTamanhoKB(kbSize);
                arquivo.setIpDestino(jTextFieldIP.getText());
                arquivo.setPortaDestino(jTextFieldPorta.getText());
                arquivo.setUsuario(this.usuario);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enviarArquivoServidor() {
        if (validaArquivo()) {
            try {
                Socket socket = new Socket(jTextFieldIP.getText().trim(),
                        Integer.parseInt(jTextFieldPorta.getText().trim()));

                BufferedOutputStream bf = new BufferedOutputStream(socket.getOutputStream());

                byte[] bytea = serializarArquivo();
                bf.write(bytea);
                bf.flush();
                bf.close();
                socket.close();
                JOptionPane.showMessageDialog(null, "Arquivo enviado com sucesso!");
            } catch (UnknownHostException e) {
                //System.out.print("falha 1");
                JOptionPane.showMessageDialog(null, "Erro ao acessar o servidor");
                e.printStackTrace();
            } catch (IOException e) {
                //System.out.print("falha 2");
                JOptionPane.showMessageDialog(null, "Erro ao acessar o servidor");
                e.printStackTrace();
            }
        }
    }

    private byte[] serializarArquivo() {
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ObjectOutputStream ous;
            ous = new ObjectOutputStream(bao);
            ous.writeObject(arquivo);
            return bao.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean validaArquivo() {
        if (arquivo.getTamanhoKB() > tamanhoPermitidoKB) {
            JOptionPane.showMessageDialog(this,
                    "Tamanho máximo permitido atingido (" + (tamanhoPermitidoKB / 1024) + ")");
            return false;
        } else {
            return true;
        }
    }
/*
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Cliente().setVisible(true);
            }
        });
    }
*/

    private javax.swing.JButton jButtonArquivo;
    private javax.swing.JButton jButtonEnviar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelTamanho;
    //private javax.swing.JTextField jTextFieldDiretorio;
    private javax.swing.JTextField jTextFieldIP;
    private javax.swing.JTextField jTextFieldNome;
    private javax.swing.JTextField jTextFieldPorta;
}// </editor-fold>//GEN-END:initComponents
