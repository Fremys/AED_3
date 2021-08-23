//Bibliotecas usadas
import java.text.ParseException;
import java.io.IOException;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import java.io.DataOutputStream;
import java.io.DataInputStream;

public class Model {

    //definir arquivo de dados

    String arquivo = ("./lelatorios.db");

    /* ===== DEFINIR METODOS DE LEITURA E ESCRITA ===== */

    //definir variável para escrita
    FileOutputStream fos;    
    //definir auxiliar variável para escrita
    DataOutputStream dos;

    //definir variável para leitura
    FileInputStream fis;
    //definir auxiliar variável para escrita
    DataInputStream dis;

    //Definir Array Auxiliar

    byte[] bytes;



    //Metodo de inserção de relatórios em arquivo
    public boolean insertRelatorio(int id, int cpf, String nome, String dataNascimento, boolean sexo, String anotation){
        
        try {       
            //criar uma nova classe de Relatório

            Relatorio relatorio = new Relatorio(id, cpf, nome, dataNascimento, sexo, anotation);

            //definir variável para escrita
            fos = new FileOutputStream(arquivo);

            //definir variável auxiliar para escrita
            dos = new DataOutputStream(fos);

            //metodo get para conferir se existe um registro com o ultimo id salvo
            
            //Metodo ===================
            //Metodo ===================

            //Salvar a classe em um array dinâmico

            bytes = relatorio.toByteArray();

            //Salvar na variável o tamanho do array

            int tamanho = bytes.length;

            //============= ESCRITA ======================

            //Escrever tamanho do registro

            dos.writeInt(ba.length);
            dos.write(bytes);

            //fechar arquivo

            fos.close();

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    //Método de recuperação de arquivos
    public Relatorio getRelatorio(int cpf){

        try{

            //criar uma nova classe de Relatório

            Relatorio relatorio = new Relatorio(id, cpf, nome, dataNascimento, sexo, anotation);

            //definir variável para escrita
            fis = new FileInputStream(arquivo);

            //definir variável auxiliar para escrita
            dis = new DataInputStream(fos);

            //pular um numero inteiro no ponteiro
            int tmp = dis.readInt();

            //definr variavel de encontro
            boolean encontrou = false;

            //entrar em um looping até encontrar o cpf solicitado
            do{

                //Salvar na variável o tamanho do array

                int tamanho = dis.readInt();

                //Salvar a classe em um array dinâmico

                bytes = new byte[tamanho];

                //salvar os dados em um vetor

                dis.read(bytes);

                //integrar dados salvo a uma Classe

                relatorio.fromBytes(bytes);

                //verificar busca

                if(cpf == relatorio.getCpf()){
                    encontrou = true;
                }


            }while(encontrou == false);

            if(encontrou == true){
                return relatorio;
            }
            else{
                return null;
            }


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}