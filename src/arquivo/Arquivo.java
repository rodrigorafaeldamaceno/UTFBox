/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Rodri
 */
public class Arquivo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private byte[] conteudo;
    private transient long tamanhoKB;
    private String usuario;
    private transient Date dataHoraUpload;
    private transient String ipDestino;
    private transient String portaDestino;
    
    //private String diretorioDestino;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public byte[] getConteudo() {
        return conteudo;
    }

    public void setConteudo(byte[] conteudo) {
        this.conteudo = conteudo;
    }

    public long getTamanhoKB() {
        return tamanhoKB;
    }

    public void setTamanhoKB(long tamanhoKB) {
        this.tamanhoKB = tamanhoKB;
    }

    public Date getDataHoraUpload() {
        return dataHoraUpload;
    }

    public void setDataHoraUpload(Date dataHoraUpload) {
        this.dataHoraUpload = dataHoraUpload;
    }

    public String getIpDestino() {
        return ipDestino;
    }

    public void setIpDestino(String ipDestino) {
        this.ipDestino = ipDestino;
    }

    public String getPortaDestino() {
        return portaDestino;
    }

    public void setPortaDestino(String portaDestino) {
        this.portaDestino = portaDestino;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    

}
