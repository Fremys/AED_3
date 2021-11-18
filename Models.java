//Bibliotecas usadas
import java.text.ParseException;
import java.io.IOException;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import java.io.DataOutputStream;
import java.io.DataInputStream;

import java.io.FileOutputStream;
import java.io.FileInputStream;

import java.io.RandomAccessFile;

import java.util.Scanner;

public class Models{

    //definir arquivo de dados

    String arquivo = ("./lelatorios.db");

    /* ===== DEFINIR METODOS DE LEITURA E ESCRITA ===== */

    //definir variável para escrita
    protected FileOutputStream fos;    
    //definir auxiliar variável para escrita
    protected DataOutputStream dos;

    //definir variável para escrita aleatória de dados
    protected RandomAccessFile arq;

    //definir variável para leitura
    protected FileInputStream fis;
    //definir auxiliar variável para leitura
    protected DataInputStream dis;

    //Definir Array Auxiliar
    protected byte[] bytes;
    protected byte[] bytesLixo;

    //Definir função hash
    Hash hash = new Hash();

    
    public Models(){
        
        this.fos = null;
        this.fos = null;
        this.arq = null;
        this.fis = null;
        this.dis = null;
        this.bytes = null;
        this.bytesLixo = null;
    }


    //Metodo de inserção de relatórios em arquivo
    public  boolean insertRelatorio(long cpf, String nome, String dataNascimento, boolean sexo, String anotation){
        
        //deifnir tamanho
        int tamanhoPadrao = 3000;
        long idUltimo = 0;
        boolean existeIdInicial = false;

        /*
        =========================================== EXECUTAR PELA PRIMEIRA VEZ ====================================
        * Essa parte do código verifica se há um arquivo binário criado em disco antes de realizar qualquer operação
        * Caso exista, ele executa o insert normalmente, porém caso não exista ele cria o arquivo.
        */
        

        try{       
            //criar uma nova classe de Relatório
            
            Relatorio relatorio = new Relatorio(cpf, nome, dataNascimento, sexo, anotation);
            Hash hash = new Hash();
            
            //definir variável para escrita aleatoria
            arq = new RandomAccessFile("./Relatorios.db", "rw");
            //fechar arquivo
            arq.close();
            
            try{
                //definir variável para escrita
                fis = new FileInputStream("./Relatorios.db");
    
                //definir variável auxiliar para escrita
                dis = new DataInputStream(fis);

                //metodo get para conferir se existe um registro com o ultimo id salvo
                idUltimo = dis.readLong();
                
                //confirmar a existencia 
                existeIdInicial = true;
                
                //fechar arquivo
                fis.close();

            }catch (Exception e){
            }

            //=========================================== FIM DO EXECUTAR PELA PRIMEIRA VEZ ===========================================

            //============= DEFINÇOES ===================
            
            //definir variável para escrita aleatoria
            arq = new RandomAccessFile("./Relatorios.db", "rw");

            //salvar enderço inicial do arquivos
            long p1 = arq.getFilePointer();

            //salvar em variável o tamanho do arquivo final
            long tamArq = arq.length();

            //posicionar o ponteiro no final do arquivo
            arq.seek(tamArq);

            //============ FIM DAS DEFINIÇÕES ==============

            if(existeIdInicial == false)
            {
                arq.writeLong(cpf);
            }

            //Salvar a classe em um array dinâmico
            bytes = relatorio.toByteArray();
            
            //Salvar na variável o tamanho do array
            int tamanho = bytes.length;
            
            //============= ESCRITA ======================
            
            //Escrever tamanho do registro
            arq.writeInt(tamanhoPadrao);
            arq.write(bytes);
            
            //inserir lixo até completar o tamanho padrão
            if(tamanho < tamanhoPadrao)
            {
                //definir lixo
                bytesLixo = new byte[((tamanhoPadrao - tamanho))];
                arq.write(bytesLixo);
            }
            
            //Atualizar id do inicio do arquivo
            if(existeIdInicial == true)
            {
                //voltar com o ponteiro para o começo do arquivo
                arq.seek(p1);
    
                //escrever no começo do arquivo o ultimo id escrito
                arq.writeLong((idUltimo + 1));
            }

            //fechar arquivo
            arq.close();

            //inserir na tabela hash
            hash.diretorio(cpf, tamArq);
            
            //return
            return true;

        }catch (Exception e){
            return false;
        }

    }

    //Método de recuperação de arquivos
    public  Relatorio getRelatorio(long cpf){

        try{
            //criar uma nova classe de Relatório

            Relatorio relatorio = new Relatorio();

            //definir variável para escrita

            arq = new RandomAccessFile("./Relatorios.db", "rw");

            //pular o ultimo id salvo no ponteiro
            long tmp = arq.readLong();

            //definir variavel de encontro
            boolean encontrou = false;

            //entrar em um looping até encontrar o cpf solicitado
            do{

                //Salvar na variável o tamanho do array

                int tamanho = arq.readInt();

                //Salvar a classe em um array dinâmico

                bytes = new byte[tamanho];

                //salvar os dados em um vetor

                arq.read(bytes);

                //integrar dados salvo a uma Classe

                relatorio.frontByteArray(bytes);

                //verificar busca

                if(cpf == relatorio.getCpf() && cpf != -1){
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
            return null;
        }
    }

    //Método de delete
    public  boolean deleteRelatorio(long cpf){

        try{
            //criar uma nova classe de Relatório

            Relatorio relatorio = new Relatorio();

            //definir variável para escrita

            arq = new RandomAccessFile("./Relatorios.db", "rw");

            //pular um numero inteiro no ponteiro
            long tmp = arq.readLong();

            //definir variavel de encontro
            boolean encontrou = false;

            //variavel que salva o ponteiro do arquivo desejado
            long posArq = -1;

            //entrar em um looping até encontrar o cpf solicitado
            do{
                //Salvar na variável o tamanho do array
                int tamanho = arq.readInt();

                //salvar posição do arquivo
                posArq = arq.getFilePointer();

                //Salvar a classe em um array dinâmico
                bytes = new byte[tamanho];

                //salvar os dados em um vetor
                arq.read(bytes);

                //integrar dados salvo a uma Classe
                relatorio.frontByteArray(bytes);

                //verificar busca
                if(cpf == relatorio.getCpf()){
                    encontrou = true;
                }

            }while(encontrou == false);
            
            //verificar se o relatorio existe
            if(encontrou == true){

                //definir auxiliar de leitura
                Scanner ler = new Scanner(System.in);

                //mover o ponteiro para o arquivo inicial
                arq.seek(posArq);

                //salvar o id como excluído
                arq.writeLong(-1);

                return true;
            }
            else{
                return false;
            }
        }catch (Exception e){
            return false;
        }

    }

    //Método Update
    public  boolean updateRelatorio(Relatorio relatorio){
        try{
            //deifnir tamanho
            int tamanhoPadrao = 3000;

            //definr classe e variavel auxilixar
            Relatorio aux = new Relatorio();
            byte[] bytesaux;

            //definir variável para escrita
            arq = new RandomAccessFile("./Relatorios.db", "rw");

            //pular um numero inteiro no ponteiro
            long tmp = arq.readLong();

            //definir variavel de encontro
            boolean encontrou = false;

            //Salvar a classe em um array dinâmico
            bytes = relatorio.toByteArray();

            //definir tamanho
            int tamanho = bytes.length;

            //variavel que salva o ponteiro do arquivo desejado
            long posArq = -1;

            //entrar em um looping até encontrar o cpf solicitado
            do{
                //salvar posição do arquivo
                posArq = arq.getFilePointer();
                
                //Salvar na variável o tamanho do array
                int tamanhoArq = arq.readInt();

                //Salvar a classe em um array dinâmico
                bytesaux = new byte[tamanhoArq];

                //salvar os dados em um vetor
                arq.read(bytesaux);

                //integrar dados salvo a uma Classe
                aux.frontByteArray(bytesaux);

                //verificar busca
                if(aux.getCpf() == relatorio.getCpf()){
                    encontrou = true;
                }

            }while(encontrou == false);
            
            //verificar se o relatorio existe
            if(encontrou == true){

                //mover o ponteiro para o arquivo inicial
                arq.seek(posArq);

                //salvar o id como excluído
                arq.writeInt(tamanhoPadrao);

                //SALVAR DADOS
                arq.write(bytes);

                //inserir lixo até completar o tamanho padrão
                if(tamanho < tamanhoPadrao)
                {
                    //definir lixo
                    bytesLixo = new byte[((tamanhoPadrao - tamanho))];
                    arq.write(bytesLixo);
                }

                return true;

            }
            else{
                
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }
}