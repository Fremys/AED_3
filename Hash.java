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
    protected byte[] bytesAux;
    protected byte[] bytess;
    protected byte[] bytesLixo;

    //Definir profundidade global
    int profundiadeGlobal = 0;
    int profundiadeLocal = 0;
    int quantMax;
    boolean bucketCheio = false;
    long enderDirGemeo = -1;
    int oldProfundidadeColidida = -1;

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
    public static long funHash(long cpf, int div)
    {

        long n;
        String cpfText = Long.toString(cpf);

        try{
    
            MessageDigest m = MessageDigest.getInstance("MD5");
    
            m.update(cpfText.getBytes(),0,cpfText.length());
    
            BigInteger b1 = new BigInteger( 1 , m.digest());
    
            long longValueOfb1 = b1.longValue();

            if(longValueOfb1 < 0){
                longValueOfb1 = longValueOfb1 * -1;
            }
    
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

    
    //Método para aumentar o arquivo do diretório
    public boolean aumentarDiret(long chaveColidida, long newEnder){

        //definir variável para salvar o resultado da operação
        boolean sucesso = false;

        try{

            //Definir variável para escrita e leitura aleatoria
            arq = new RandomAccessFile("./Diretorio.db", "rw");

            //salvar ponteiro no começo do arquivo
            long p1 = 0;

            //Ler profundidade salva
            int oldProf = arq.readInt();
            int oldTamanho = ( (int) Math.round(Math.pow(2, oldProf)) );

            //Copiar valores
            int newTamanho = ( (int) Math.round(Math.pow(2, profundiadeGlobal)) );

            long antigo;

            long posComp = 4;

            System.out.println("Dividiu");
            System.out.println("Tamanho: " + oldTamanho);
            System.out.println("chaveColidida: " + chaveColidida);

            for(long i = 0; i < oldTamanho; i++){

                arq.seek(posComp);

                antigo = arq.readLong();

                arq.seek(arq.length());

                if(antigo == chaveColidida){
                    arq.writeLong(newEnder);
                }
                else{
                    arq.writeLong(antigo);
                }
                //passar para o proximo endereço
                posComp = posComp + 8;
            }

            // for(int i = 0; i < oldTamanho; i++){
            //     cop[i] = arq.readLong();
            // }
            
            // arq.seek(arq.length());

            // for(int i = 0; i < (oldTamanho); i++){

            //     if(cop[i] == chaveColidida){
            //         //System.out.println(newEnder);
            //         arq.writeLong(newEnder);
            //         //enderDirGemeo = newEnder;
            //     }
            //     else{
            //         //System.out.println(cop[i]);
            //         arq.writeLong(cop[i]);
            //     }

            // }
            //System.out.println("");
            
            arq.seek(p1);

            //Escrever profundidade do arquivo
            arq.writeInt(profundiadeGlobal);

            arq.seek(p1);

            arq.close();

            sucesso = true;

        }catch (Exception e){
            sucesso = false;
        }
        return sucesso;
    }
    

    //CRIAR BUCKETS VAZIOS
    public void createBucketsVazios(int p, int no){

        quantMax = no;

        try{

            //abrir o arquivo
            arq = new RandomAccessFile("./Bucket.db", "rw");

            arq.writeInt(0);
            arq.writeInt(0);
            arq.writeLong(-1);
            arq.write(newBucket(16));

            arq.close();
            
        }catch (Exception e){}

        //criar diretório inicial
        try{
            arq = new RandomAccessFile("./Diretorio.db", "rw");

            arq.writeInt(0);
            arq.writeLong(0);

            arq.close();

        }catch (Exception e){
        }


        //Definir dados

        int profLocal = 0;
        
        long enderAnterior = 0;

        do{
            try{

                //abrir o arquivo
                arq = new RandomAccessFile("./Bucket.db", "rw");

                //Definir dados
                long newHash = 0;
                //PREPARAÇÃO DOS ARQUIVOS
                    
                //Igualar profundidades
                profundiadeGlobal = profundiadeGlobal + 1;
                profLocal = profundiadeGlobal;

                //salvar em variável o tamanho do arquivo final
                newHash = arq.length();

                arq.seek(newHash);
                arq.writeInt(profLocal);
                arq.writeInt(0);
                arq.writeLong(-1);
                arq.write(newBucket(16));


                //Arquivo primário/original
                arq.seek(enderAnterior);
                arq.writeInt(profLocal);
                arq.writeInt(0);
                arq.writeLong(newHash);
                arq.write(newBucket(16));

                arq.close();

                //Aumentar diretório
                boolean sucessAuemnt = aumentarDiret(enderAnterior, newHash);

                arq.close();

                enderAnterior = newHash;
            

            }catch (Exception e){}

        }while( profundiadeGlobal < p);
    }
        
    
    //ARQUIVO AUXILIAR DA TABELA HASH
    
    public void diretorio(Long cpf, long ender, int no){

        //Definir estado da operação
        boolean sucesso = false;
        long enderHash = 0;
        quantMax = no;

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
                
                //confirmar a existencia 
                existeIdInicial = true;
                
                //fechar arquivo
                fis.close();
                
            }catch (Exception e){
            }

            //=========================================== FIM DO EXECUTAR PELA PRIMEIRA VEZ ===========================================

            
            //definir variável para escrita para leitura aleatoria
            arq = new RandomAccessFile("./Diretorio.db", "rw");

            //salvar endereço inicial do arquivos
            long p1 = arq.getFilePointer();

            //salvar em variável o tamanho do arquivo final
            long tamArq = arq.length();


            if(existeIdInicial == false)
            {
                arq.writeInt(0);
                arq.writeLong(0);
            }
            
            //Calcular o hash da função
            long pos = funHash(cpf, ( (int) Math.round(Math.pow(2, profundiadeGlobal)) ) );

            // System.out.println("");
            // System.out.println("profundiadeGlobal = " + profundiadeGlobal);
            // System.out.println("CPF = " + cpf);
            // System.out.println("fun = " + ( (int) Math.round(Math.pow(2, profundiadeGlobal)) ));
            // System.out.println("pos = " + pos);

            long posCalculada = 4 + ((pos) * 8);

            // System.out.println("posCalculada = " + posCalculada);
            
            //posicionar o ponteiro no lugar adequado
            arq.seek(posCalculada);
            
            enderHash = arq.readLong();
            
            // System.out.println("enderHash = " + enderHash);
            // System.out.println("=========================");

            //Ler endereço apontado
            boolean fun = insertHash(cpf, ender, enderHash);
            sucesso = !sucesso && fun;

            //posicionar o ponteiro para salvar o endereço do hash

            //posicionar
            
            //Salvar a classe em um array dinâmico
           // bytes = relatorio.toByteArray();
            
            //Salvar na variável o tamanho do array
            // int tamanho = bytes.length;
            
            arq.close();

        }catch (Exception e){
            sucesso = false;
        }

    }
  
    //Método de inserção de relatórios
    public boolean insertHash(long cpf, long ender, long enderApotadoDir){   
        //Definir dados
        Bucket bucket = new Bucket(cpf, ender);

        int profLocal = 0;
        int quantidade = 0;
        long enderProxBucket = 0;

        boolean inicial = false;

        boolean sucesso = false;


        try{

            //Abrir arquivo para sua criação
            arq = new RandomAccessFile("./Bucket.db", "rw");
            arq.close();

            //Tentar ler dados iniciais
            try{
                arq = new RandomAccessFile("./Bucket.db", "rw");

                //posicionar o ponteiro no bucket adequado
                arq.seek(enderApotadoDir);

                //Ler dados inciais
                profLocal = arq.readInt();
                quantidade = arq.readInt();
                enderProxBucket = arq.readLong();

                inicial = true;

                arq.close();

            }catch(Exception e){
            }

            //Abrir arquivo
            arq = new RandomAccessFile("./Bucket.db", "rw");


            if(inicial == false){
                arq.writeInt(0);
                arq.writeInt(0);
                arq.writeLong(-1);
                arq.write(newBucket(16));
            }

            if(quantidade < quantMax && profLocal <= profundiadeGlobal){

                // System.out.println("");
                // System.out.println("Inseriu");
                // System.out.println("");

                //calcular a posição do arquivo
                long pos =  (8 + 8 + (quantidade * 16) ) + enderApotadoDir ;

                arq.seek(pos);

                //System.out.println("Posicao insert = "+ pos);

                bytes = bucket.toByteArray();
                
                arq.write(bytes);

                quantidade = quantidade + 1;

                //Recalcular a quantidade
                
                arq.seek(4 + enderApotadoDir);
                
                arq.writeInt(quantidade);
                
                arq.close();
                
                sucesso = true;
                
            }
            else{
    
                    //Definir dados
                    long newHash = -1;
                    //PREPARAÇÃO DOS ARQUIVOS
    
                    //Igualar profundidades
                    profundiadeGlobal = profundiadeGlobal + 1;
                    profLocal = profundiadeGlobal;
    
    
                    //Arquivo secundário
                    newHash = arq.length();
    
                    arq.seek(newHash);
                    arq.writeInt(profLocal);
                    arq.writeInt(0);
                    arq.writeLong(-1);
                    arq.write(newBucket(16));
    
                    //Arquivo primário/original
                    arq.seek(enderApotadoDir);
                    arq.writeInt(profLocal);
                    arq.writeInt(0);
                    arq.writeLong(newHash);
                    // System.out.println("enderProxBucket: " + newHash);
    
                    arq.close();
                    //Aumentar diretório
                    boolean sucessAuemnt = aumentarDiret(enderApotadoDir, newHash);
    
                    if(sucessAuemnt)
                    {
                        Bucket bucketAux = new Bucket();
    
                        long position = enderApotadoDir + 8 + 8;
    
                        for(int i=0; i< quantMax; i++){
    
                            arq = new RandomAccessFile("./Bucket.db", "rw");
    
                            arq.seek(position);
    
                            bytesAux = new byte[16];
    
                            arq.read(bytesAux);
    
                            bucketAux.frontByteArray(bytesAux);
    
                            arq.close();
    
                            position = position + 16;
    
                            diretorio(bucketAux.getChave(), bucketAux.getEndereco(), quantMax);
    
    
                            sucesso = true;
                        }

                        diretorio(cpf, ender, quantMax);
                    }
                    else
                    {
                        System.out.println("Error");
                        sucesso = false;
                    }
                //}


            }


        }catch (Exception e){

        }

        return sucesso;
    }

    //método de busca de relatórios
    public long getHash(Long cpf){

        //Definir dados diretorio
        long enderBucket = -1;
        long pos = -1;
        long posHash = -1;

        //Definir dados Bucket
        int profLocal = -1;
        int quantidade = -1;
        long enderProxBucket = -1;

        //endereço no arquivo principal
        long enderEncontrado = -1;

        boolean econtrou = false;

        try{

            //Abrir diretorio
            arq = new RandomAccessFile("./Diretorio.db", "rw");

            //Ler profundidade e endereço
            profundiadeGlobal = arq.readInt();
            
            if(profundiadeGlobal >= 0)
            {
                //Receber valor da função hash
                pos = (funHash(cpf, ( (int) Math.round(Math.pow(2, profundiadeGlobal)) )));
            }

            if(pos >= 0)
            {
                posHash = 4 + ((pos) * 8);
                arq.seek(posHash);
                enderBucket = arq.readLong();
            }

            // System.out.println("pos: "+ pos);
            // System.out.println("posHashos: "+ posHash);




            arq.close();

            
            
            //Procurar registro no bucket
            if(enderBucket >= 0)
            {   
                //abrir buckets
                arq = new RandomAccessFile("./Bucket.db", "rw");
                Bucket bucket = new Bucket();
                
                //posicionar no bucket procurado
                arq.seek(enderBucket);
                
                //ler dados
                profLocal = arq.readInt();
                quantidade = arq.readInt();
                enderProxBucket = arq.readLong();

                // System.out.println("profLocal: "+ profLocal);
                // System.out.println("enderProxBucket: "+ enderProxBucket);
                // System.out.println("quantidade: "+ quantidade);
                // System.out.println("profundiadeGlobal: "+ profundiadeGlobal);
                
                for(int i = 0; i < quantidade; i++)
                {
                    bytesAux = new byte[16];

                    arq.read(bytesAux);

                    bucket.frontByteArray(bytesAux);

                    // System.out.println("chave: "+ bucket.getChave());

                    if(bucket.getChave() != -1 && bucket.getChave() == cpf)
                    {
                        // System.out.println("Encontrou");
                        enderEncontrado = bucket.getEndereco();
                        i = quantidade;
                        econtrou = true;
                    }
                }

                // if(!econtrou)
                // {
                //     arq.seek(enderProxBucket);

                //     //ler dados
                //     profLocal = arq.readInt();
                //     quantidade = arq.readInt();
                //     enderProxBucket = arq.readLong();

                //     for(int i = 0; i < quantidade; i++)
                //     {
                //         bytesAux = new byte[16];

                //         arq.read(bytesAux);

                //         bucket.frontByteArray(bytesAux);

                //         System.out.println("chave: "+ bucket.getChave());

                //         if(bucket.getChave() != -1 && bucket.getChave() == cpf)
                //         {
                //             System.out.println("Encontrou");
                //             enderEncontrado = bucket.getEndereco();
                //             i = quantidade;
                //             econtrou = true;
                //         }
                //     }

                // }

                arq.close();
            }

        }catch(Exception e){
        }

        // System.out.println("enderEncontrado = " + enderEncontrado);
        return enderEncontrado;
    }

    //método para deletar de relatórios
    public long deleteHash(Long cpf){
        
        //Definir dados diretorio
        long enderBucket = -1;
        long pos = -1;
        long posHash = -1;
        
        //Definir dados Bucket
        int profLocal = -1;
        int quantidade = -1;
        long enderProxBucket = -1;
        
        boolean sucesso = false;
        boolean encontrou = false;
        long enderEncontrado = -1;
 
         try{
 
            //Abrir diretorio
            arq = new RandomAccessFile("./Diretorio.db", "rw");
 
            //Ler profundidade e endereço
            profundiadeGlobal = arq.readInt();
             
            if(profundiadeGlobal >= 0)
            {
                //Receber valor da função hash
                pos = (funHash(cpf, ( (int) Math.round(Math.pow(2, profundiadeGlobal)) )));
            }
 
            if(pos >= 0)
            {
                posHash = 4 + ((pos) * 8);
                arq.seek(posHash);
                enderBucket = arq.readLong();
            }
 
            arq.close();
 
             
             
            //Procurar registro no bucket
            if(enderBucket >= 0)
            {   
                //abrir buckets
                arq = new RandomAccessFile("./Bucket.db", "rw");
                Bucket bucket = new Bucket();
                 
                //posicionar no bucket procurado
                arq.seek(enderBucket);
                 
                //ler dados
                profLocal = arq.readInt();
                quantidade = arq.readInt();
                enderProxBucket = arq.readLong();
 
                 
                for(int i = 0; i < quantidade; i++)
                {
                    bytesAux = new byte[16];

                    long b1 = arq.getFilePointer();
 
                    arq.read(bytesAux);
 
                    bucket.frontByteArray(bytesAux);
 
                    // System.out.println("chave: "+ bucket.getChave());
 
                    if(bucket.getChave() != -1 && bucket.getChave() == cpf)
                    {
                        // System.out.println("Encontrou");
                        //atualizar variável de controle
                        i = quantidade;

                        //deletar chave encontrada
                        bucket.setChave(-1);
                        enderEncontrado = bucket.getEndereco();

                        //ir para o início do arquivo para salvar o processo
                        arq.seek(b1);
                        bytes = bucket.toByteArray();
                        arq.write(bytes);

                        encontrou = true;
                        sucesso = true;
                    }
                }
 
                // if(!encontrou)
                // {
                //     arq.seek(enderProxBucket);
 
                //     //ler dados
                //     profLocal = arq.readInt();
                //     quantidade = arq.readInt();
                //     enderProxBucket = arq.readLong();
 
                //     for(int i = 0; i < quantidade; i++)
                //     {
                //         bytesAux = new byte[16];

                //         long b1 = arq.getFilePointer();
 
                //         arq.read(bytesAux);
 
                //         bucket.frontByteArray(bytesAux);
 
                //         // System.out.println("chave: "+ bucket.getChave());
 
                //         if(bucket.getChave() != -1 && bucket.getChave() == cpf)
                //         {
                //             // System.out.println("Encontrou");
                //             i = quantidade;
                //             //deletar chave encontrada
                //             bucket.setChave(-1);
                //             enderEncontrado = bucket.getEndereco();

                //             //ir para o início do arquivo para salvar o processo
                //             arq.seek(b1);
                //             bytes = bucket.toByteArray();
                //             arq.write(bytes);

                //             sucesso = true;
                //         }
                //     }
 
                // }
 
                arq.close();
            }
 
        }catch(Exception e){
        }

        return enderEncontrado;
    }


}

    