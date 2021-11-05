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

import java.security.*;
import java.math.*;

public class Hash {
    
    //definir arquivo de dados

    String arquivo = ("./Relatorios.db");

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

    //Definir profundidade global
    int profundiadeGlobal = 0;
    int quantMax = 254;
    boolean bucketCheio = false;

    //metodo construtor
    public Hash(){
        
        this.fos = null;
        this.fos = null;
        this.arq = null;
        this.fis = null;
        this.dis = null;
        this.bytes = null;
        this.bytesLixo = null;
    }

    //FUNÇÃO DE DISPERSÃO
    public long funHash(long cpf, int div){

        long n;
        String cpfText = Long.toString(cpf);

        try{
    
            MessageDigest m = MessageDigest.getInstance("MD5");
    
            m.update(cpfText.getBytes(),0,cpfText.length());
    
            BigInteger b1 = new BigInteger( 1 , m.digest());
    
            long longValueOfb1 = b1.longValue();
    
            n = longValueOfb1 % div;
    
        }catch(Exception e){
            n = -1;
        }
    
        return n;
    }
    
    //FUNÇÃO PARA CRIAR UM BUCKET VAZIO
    public byte[] newBucket(int tamanhoBucket){
        byte[] lixo = new byte[quantMax * tamanhoBucket];
        return lixo; 
    }
    //ARQUIVO AUXILIAR DA TABELA HASH
    public void diretorio(Long cpf, long ender){

        //Definir estado da operação
        boolean sucesso = false;
        long enderHash = 8;
    

        //Variaveis de verificações primarias

        boolean existeIdInicial = false;

        /*
        =========================================== EXECUTAR PELA PRIMEIRA VEZ ====================================
        * Essa parte do código verifica se há um arquivo binário criado em disco antes de realizar qualquer operação
        * Caso exista, ele executa o insert na tabela hash normalmente, porém caso não exista ele cria o arquivo.
        */

        try{

            //Definir variável para escrita e leitura aleatoria
            arq = new RandomAccessFile("./Diretorio.db", "rw");

            //Fechar arquivo
            arq.close();
            
            try{
                //definir variável para leitura
                fis = new FileInputStream("./Diretorio.db");
                
                //definir variável auxiliar para leitura
                dis = new DataInputStream(fis);
                
                //metodo get para conferir se existe um registro com o ultimo id salvo
                profundiadeGlobal = dis.readInt();
                enderHash = dis.readLong();
                
                //confirmar a existencia 
                existeIdInicial = true;
                
                //fechar arquivo
                fis.close();
                
            }catch (Exception e){
            }

            //=========================================== FIM DO EXECUTAR PELA PRIMEIRA VEZ ===========================================

            //============= DEFINÇOES ===================
            
            //definir variável para escrita e leitura aleatoria
            arq = new RandomAccessFile("./Diretorio.db", "rw");

            //salvar endereço inicial do arquivos
            long p1 = arq.getFilePointer();

            //salvar em variável o tamanho do arquivo final
            long tamArq = arq.length();

            //============ FIM DAS DEFINIÇÕES ==============

            if(existeIdInicial == false)
            {
                arq.writeInt(0);
            }

            //Calcular o hash da função
            long pos = funHash(cpf, ( (int) Math.round(Math.pow(2, profundiadeGlobal)) ) );

            long posCalculada = 4 + ((pos - 1) * 8);

            //posicionar o ponteiro no lugar adequado
            arq.seek(posCalculada);

            //Ler endereço apontado

            long insertHash = insertHash(cpf, ender, enderHash);



            //posicionar
            
            //Salvar a classe em um array dinâmico
           // bytes = relatorio.toByteArray();
            
            //Salvar na variável o tamanho do array
            int tamanho = bytes.length;
            


        }catch (Exception e){
            sucesso = false;
        }

    }

    //Método de inserção de relatorios
    public long insertHash(long cpf, long ender, long enderApotadoDir){

        //criar uma classe para salvar o bucket
        Bucket bucket = new Bucket(cpf, ender);

        //Definir estado da operação
        boolean sucesso = false;

        //Variaveis de verificações primarias
        int profundiadeLocal = 0;
        int quantidadeBucket = 0;
        boolean existeIdInicial = false;

        long EnderHash = 0;

        /*
        =========================================== EXECUTAR PELA PRIMEIRA VEZ ====================================
        * Essa parte do código verifica se há um arquivo binário criado em disco antes de realizar qualquer operação
        * Caso exista, ele executa o insert na tabela hash normalmente, porém caso não exista ele cria o arquivo.

        */      
        
        try{

            //Definir variável para escrita e leitura aleatoria
            arq = new RandomAccessFile("./Indice.db", "rw");

            //Fechar arquivo
            arq.close();
            
            try{
                //definir variável para leitura
                fis = new FileInputStream("./Indice.db");
                
                //definir variável auxiliar para leitura
                dis = new DataInputStream(fis);
                
                //metodo get para conferir se existe um registro com o ultimo id salvo
                profundiadeLocal = dis.readInt();
                quantidadeBucket = dis.readInt();
                
                //confirmar a existencia 
                existeIdInicial = true;
                
                //fechar arquivo
                fis.close();
                
            }catch (Exception e){
            }

            //=========================================== FIM DO EXECUTAR PELA PRIMEIRA VEZ ===========================================

            //============= DEFINÇOES ===================
            
            //definir variável para escrita e leitura aleatoria
            arq = new RandomAccessFile("./Indice.db", "rw");

            //salvar endereço inicial do arquivos
            long p1 = arq.getFilePointer();

            
            /*
            //posicionar o ponteiro no final do arquivo]
            long posFinalBucket = ;
            arq.seek(tamArq);
            */
            
            //============ FIM DAS DEFINIÇÕES ==============
            
            //Salvar a classe em um array dinâmico
            bytes = bucket.toByteArray();

            //salvar em variável o tamanho do arquivo final
            long tamArq = arq.length();

            
            EnderHash = tamArq;
            
            //Salvar na variável o tamanho do array
            int tamanhoBucket = bytes.length;
            
            if(existeIdInicial == false)
            {
                arq.writeInt(profundiadeGlobal);
                arq.writeInt(0);
                arq.write(newBucket(tamanhoBucket));
            }

            
            //Verificar se o bucket esta cheio
            
            if(quantidadeBucket < quantMax)
            {
                //Posicionar o ponteiro no lugar desejado para o início da leitura
                arq.seek( (enderApotadoDir) + 8);

                //calcular posição de escrita dentro do bucket
                long posDeEscrita = (8 + (quantidadeBucket * tamanhoBucket) );

                //Posicionar o ponteiro no lugar calculado
                arq.seek(posDeEscrita);

                //escrever bucket no arquivo
                arq.write(bytes);

                return -1;
            }
            else
            {
                //Posicionar o ponteiro no final do arquivo
                arq.seek(tamArq);

                //Aumentar a profundidade global
                profundiadeGlobal = profundiadeGlobal + 1;

                //Igualar a profundidade global
                profundiadeLocal = profundiadeGlobal;

                //Escrever profundidade e quantidade
                arq.writeInt(profundiadeLocal);
                arq.writeInt(0);

                //Criar bucket Vazio
                arq.write(newBucket(tamanhoBucket));

                //Arq 

                //Caso o bucket esteja cheio
                bucketCheio = true;
                profundiadeGlobal = profundiadeGlobal + 1;
                profundiadeLocal = profundiadeGlobal;


            }


            
        }catch (Exception e){
            sucesso = false;
        }
            
        return sucesso;
    }
}
    