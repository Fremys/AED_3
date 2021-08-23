//bibliotecas usadas
import java.text.ParseException;
import java.io.IOException;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import java.io.DataOutputStream;
import java.io.DataInputStream;

import java.text.SimpleDateFormat;
import java.util.Date;

//classe dos relatorios

public class Relatorio{

    //atributos da classe principal
    protected int Cpf;
    protected String nome;
    protected Date dataNascimento;
    protected boolean sexo;
    protected String anotation;

    //conversor de String para Date

    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); 

    //construtores

    public Relatorio(int id, int cpf, String nome, String dataNascimento, boolean sexo, String anotation)
    {
        this.cpf = cpf;
        this.nome = nome;
        this.dataNascimento = formato.parse(dataNascimento);
        this.sexo = sexo;
        this.anotation = anotation;
    }

    public Relatorio(){

        this.cpf = -1;
        this.nome = "";
        this.dataNascimento = formato.parse("00/00/0000");
        this.sexo = False;
        this.anotation = "";
    }
    
    //métodos gets e seters
    
    public int getCpf(){
        return this.Cpf;
    }

    //Metodo toString

    public String toString(){

        return "Relatorio [ CPF=" + this.cpf +
         ", nome=" + this.nome + ", dataNascimento=" + this.dataNascimento +
          ", sexo=" + this.sexo", anotation"+ this.anotation" ]"
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
        
        //definir variavrel que conversao de data para milisegundos

        long time = format.getMillis();
        //escrever dados no array de bytes

        dos.writeInt(this.cpf);
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
    
    public boolean frontByteArray(byte[] data) throws IOException {

        //criar vetor que armazena bytes

        ByteArrayInputStream bais = new ByteArrayInputStrea(data);

        //definir conversor de bytes em dados primitivos;
        DataInputStream dis = new DataInputStream(bais);


        this.cpf = dis.readInt();
        this.nome = dis.readUTF();
        this.dataNascimento = new Date(dis.readLong());
        this.sexo = dis.readBoolean();
        this.anotation = ;
    }

}