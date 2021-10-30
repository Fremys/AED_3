//bibliotecas usadas
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

//classe dos relatorios

public class Relatorio{

    //atributos da classe principal
    protected long Cpf;
    protected String nome;
    protected Date dataNascimento;
    protected boolean sexo;
    protected String anotation;

    //conversor de String para Date

    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); 

    //construtores

    public Relatorio(long cpf, String nome, String dataNascimento, boolean sexo, String anotation){
        this.Cpf = cpf;
        this.nome = nome;

        //tratamento de erro obrigatorio
        try{
            this.dataNascimento = formato.parse(dataNascimento);
        }catch (Exception e){
            this.dataNascimento = null;
        }

        this.sexo = sexo;
        this.anotation = anotation;
    }

    public Relatorio(){

        this.Cpf = -1;
        this.nome = "";
        this.dataNascimento = null;
        this.sexo = false;
        this.anotation = "";
    }
    
    //métodos gets e seters
    
    public long getCpf(){
        return this.Cpf;
    }

    public String getNome(){
        return this.nome;
    }

    public Date getDataNascimento(){
        return this.dataNascimento;
    }

    public boolean getSexo(){
        return this.sexo;
    }

    public String getAnotation(){
        return this.anotation;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setData(String data){
        //tratamento de erro obrigatorio
        try{
            this.dataNascimento = formato.parse(data);
        }catch (Exception e){
            this.dataNascimento = null;
        }
    }

    public void setSexo(boolean sexo){
        this.sexo = sexo;
    }

    public void setAnotation(String anotation){
        this.anotation = anotation;
    }

    //Metodo toString

    public String toString(){
        
        //tartar sexo
        String sexoSRT = "";
        if(this.sexo == true)
        {
            sexoSRT = "Masculino";
        }
        else if(this.sexo == false){
            sexoSRT = "Feminino";
        }

        //tratar data
        
        return "==== RELATORIO ===\n" + 
         "\nCPF: " + this.Cpf +
         "\nNome: " + this.nome + 
         "\nData de Nascimento: " + formato.format(this.dataNascimento) +
          "\nSexo: " + sexoSRT +
           "\nAnotação: "+ this.anotation + "\n";
    }

    /*
        Método auxiliar para escrita de dados.
        É por meio desse método que se é escrito
        os dados da classe em um array de bytes e enviado
        através de seu @return  
    */
    public byte[] toByteArray()throws IOException {

        //criar um vetor de bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //definir conversor de dados primitivos para bytes
        DataOutputStream dos = new DataOutputStream(baos);
        
        //definir variavel que conversao de data para milisegundos
        long time = this.dataNascimento.getTime();

        //escrever dados no array de bytes

        dos.writeLong(this.Cpf);
        dos.writeUTF(this.nome);
        dos.writeLong(time);
        dos.writeBoolean(this.sexo);
        dos.writeUTF(this.anotation);

        return baos.toByteArray();
    }

    /*
        Método auxiliar para a leitura de dados.
        É por meio desse método que se é lido os dados
        da classe e salvo nessa.
        
    */
    
    public void frontByteArray(byte[] data) throws IOException {

        //criar vetor que armazena bytes
        ByteArrayInputStream bais = new ByteArrayInputStream(data);

        //definir conversor de bytes em dados primitivos;
        DataInputStream dis = new DataInputStream(bais);


        this.Cpf = dis.readLong();
        this.nome = dis.readUTF();
        this.dataNascimento = new Date(dis.readLong());
        this.sexo = dis.readBoolean();
        this.anotation = dis.readUTF();
    }

}