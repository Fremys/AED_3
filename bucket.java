import java.text.ParseException;
import java.io.IOException;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import java.io.DataOutputStream;
import java.io.DataInputStream;

import java.io.FileOutputStream;
import java.io.FileInputStream;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.text.DateFormat;

public class bucket {
    //Definições dos dados principais

    private long chave;
    private long endereco;

    //============ MÉTODOS CONSTRUTORES ============
    
    public bucket() {
        this.chave = -1;
        this.endereco = -1;
    }

    public bucket(long chave, long endereco) {
        this.chave = chave;
        this.endereco = endereco;
    }

    //============ GETS E SETS ===============

    public void setChave(long chave) {
        this.chave = chave;
    }

    public void setEndereco(long endereco) {
        this.endereco = endereco;
    }

    public long getChave() {
        return this.chave;
    }

    public long getEndereco() {
        return this.endereco;
    }

    //Metodo toString

    public String ToString(){
        return (
         "\nChave: " + this.chave +
         "\nEndereço: " + this.endereco
        );
    }

    //MÉTODOS PARA LER E ESCREVER DE FORMA BINÁRIA NOS ARQUIVOS


    /*
        Método auxiliar para escrita de dados.
        É por meio desse método que se é escrito
        os dados da classe em um array de bytes e enviado
        através de seu @return  
    */

    public byte[] toByteArray() throws IOException{

        //criar um vetor de bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //definir conversor de dados primitivos para bytes
        DataOutputStream dos = new DataOutputStream(baos);

        //escrever dados no array de bytes
        dos.writeLong(this.chave);
        dos.writeLong(this.endereco);

        return baos.toByteArray();
    }

    /*
        Método auxiliar para a leitura de dados.
        É por meio desse método que se é lido e salvo os dados
        dessa classe.
    */

    public void frontByteArray(byte[] data)throws IOException {

        //Criar um vetor de bytes
        ByteArrayInputStream bis = new ByteArrayInputStream(data);

        //Definir conversor de bytes para dados primitivos
        DataInputStream dis = new DataInputStream(bis);

        //Salvar dados na classe

        this.chave = dis.readLong();
        this.endereco = dis.readLong();

    }
    
}
