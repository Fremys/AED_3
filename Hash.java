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
    public long funHash(long cpf){
        long n;
        try{
    
            MessageDigest m = MessageDigest.getInstance("MD5");
    
            m.update(s.getBytes(),0,s.length());
    
            BigInteger b1 = new BigInteger( 1 , m.digest());
    
            long longValueOfb1 = b1.longValue();
    
            n = longValueOfb1;
    
        }catch(Exception e){
            n = -1;
        }
    
        return n;
    }

    //ARQUIVO AUXILIAR DA TABELA HASH
    public void insertHash(Long cpf){
        
    }

    //Método de inserção de relatorios
    public boolean insertHash(long cpf){
        //Definir sucesso da operação
        boolean sucesso = false;

        //Variaveis de verificações primarias
        long idUltimo = 0;
        boolean existeIdInicial = false;

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
                idUltimo = dis.readLong();
                
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
           // bytes = relatorio.toByteArray();
            
            //Salvar na variável o tamanho do array
            int tamanho = bytes.length;
            
            
        }catch (Exception e){
            sucesso = false;
        }
            
        return sucesso;
    }
}
    