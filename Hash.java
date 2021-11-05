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
                System.out.println("Primeira vez");
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
                arq.writeInt(0);
            }
            
            //Calcular o hash da função
            long pos = funHash(cpf, ( (int) Math.round(Math.pow(2, profundiadeGlobal)) ) );

            long posCalculada = 4 + ((pos) * 8);

            //posicionar o ponteiro no lugar adequado
            arq.seek(posCalculada);

            System.out.println("Ender Hash: "+ enderHash);

            //Ler endereço apontado
            boolean fun = insertHash(cpf, ender, enderHash);
            sucesso = !sucesso && fun;


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

    //Método para aumentar o arquivo do diretório
    public boolean aumentarDiret(long chaveColidida, long newEnder){

        try{

            //Definir variável para escrita e leitura aleatoria
            arq = new RandomAccessFile("./Diretorio.db", "rw");

            //salvar ponteiro no começo do arquivo
            long p1 = arq.getFilePointer();

            //Ler profundidade salva
            int oldTamanho = arq.readInt(); 

            //Copiar valores
            int newTamanho = ( (int) Math.pow(2, profundiadeGlobal) );

            //criar vetor para armazenar as copias
            long cop[] = new long[oldTamanho];

            for(int i = 0; i < oldTamanho; i++){
                cop[i] = arq.readLong();
            }


            for(int i = 0; i < (newTamanho - oldTamanho); i++){
                arq.seek(arq.length());
                if(cop[i] == chaveColidida){
                    arq.writeLong(newEnder);
                }
                arq.writeLong(cop[i]);
            }
            
            arq.seek(p1);

            //Escrever profundidade do arquivo
            arq.writeInt(profundiadeGlobal);

        }catch (Exception e){
        }

        return false;
    };

    //Método de inserção de relatorios
    public boolean insertHash(long cpf, long ender, long enderApotadoDir){

        //criar uma classe para salvar o bucket
        Bucket bucket = new Bucket(cpf, ender);

        //Salvar posição do indice no bucket
        long bucketPos = -1;

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
                //calcular posição de escrita dentro do bucket
                long posDeEscrita = (8 + (quantidadeBucket * tamanhoBucket) );

                System.out.println("quantidadeBucket: " + quantidadeBucket);
                System.out.println("tamanhoBucket: " + tamanhoBucket);
                
                //Posicionar o ponteiro no lugar calculado
                arq.seek(posDeEscrita);
                
                //escrever bucket no arquivo
                arq.write(bytes);
                
                //aumentar a variável de quantidade
                quantidadeBucket = quantidadeBucket + 1;
                arq.seek(4);
                arq.writeInt(quantidadeBucket);

                sucesso = true;

                return sucesso;
            }
            else
            {   
                //====== Preparação do primeiro arquivo ===========

                //Endereço inicial de cada bucket
                long EnderOrigin = enderApotadoDir;
                long EnderOriginEscrita = EnderOrigin + 8;

                //========= Preparação do segundo arquivo ===========
                long EnderNewEnder = tamArq;
                long varPosEcritaNewBucket;

                //Posicionar o ponteiro no final do arquivo
                arq.seek(tamArq);

                bucketPos = tamArq;

                //Aumentar a profundidade global
                profundiadeGlobal = profundiadeGlobal + 1;

                //Igualar a profundidade global
                profundiadeLocal = profundiadeGlobal;

                //Escrever profundidade e quantidade
                arq.writeInt(profundiadeLocal);
                arq.writeInt(0);

                //Criar bucket Vazio
                arq.write(newBucket(tamanhoBucket));

                //============ Fim da preparação ================

                //Zerar quantidade
                long enderEscritaQuantOrigin = EnderOrigin + 4;
                arq.seek(enderEscritaQuantOrigin);
                arq.write(0);

                //===============================================

                //Apagar as variáveis escritas
                arq.seek(EnderOriginEscrita);

                //Criar bucket temporário para edição
                Bucket bucketEdit = new Bucket();

                for(int i = 0; i < quantMax; i++){

                    //Criar um vetro de bytes para leitura dos arquivos
                    bytes = new byte[16];
    
                    //Ler dados
                    arq.read(bytes);
                    
                    //Salvar na classe auxiliar
                    bucketEdit.frontByteArray(bytes);

                    //Aumentar o arquivo do diretorio
                    boolean sucessoAuemnt = aumentarDiret(enderApotadoDir, EnderNewEnder);

                    if(sucessoAuemnt){

                        //Recalcular
                        diretorio(bucketEdit.getChave(), bucketEdit.getEndereco());
    
                        //Chamar a função novamente com ao aumento feito
                        diretorio(cpf, ender);

                        sucesso = true;

                        System.out.println("Inserido no hash com sucesso");
                        
                    }
                    else{
                        System.out.println("Erro");
                    }
                }

                //definir variável

                // //Salvar posição de escrita do novo bucket
                // varPosEcritaNewBucket = tamArq + 8;

                // arq.seek(varPosEcritaNewBucket);

                // //escrever bucket no arquivo
                // arq.write(bytes)

                return sucesso;

            }


            
        }catch (Exception e){
            sucesso = false;
        }
            
        return sucesso;
    }
}
    