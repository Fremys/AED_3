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
    static FileOutputStream fos;    
    //definir auxiliar variável para escrita
    static DataOutputStream dos;

    //definir variável para escrita aleatória de dados
    static RandomAccessFile arq;

    //definir variável para leitura
    static FileInputStream fis;
    //definir auxiliar variável para escrita
    static DataInputStream dis;

    //Definir Array Auxiliar
    static byte[] bytes;
    static byte[] bytesLixo;


    //Metodo de inserção de relatórios em arquivo
    public static boolean insertRelatorio(int cpf, String nome, String dataNascimento, boolean sexo, String anotation){
        
        //deifnir tamanho
        int tamanhoPadrao = 600;
        int idUltimo = 0;
        boolean existeIdInicial = false;

        try{       
            //criar uma nova classe de Relatório
            
            Relatorio relatorio = new Relatorio(cpf, nome, dataNascimento, sexo, anotation);
            
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
                idUltimo = dis.readInt();
                
                //confirmar a existencia 
                existeIdInicial = true;
                
                //fechar arquivo
                fis.close();

            }catch (Exception e){
                System.out.println("Não há para adicionar");
            }

            //============= DEFINÇOES ===================
            
            //definir variável para escrita aleatoria
            arq = new RandomAccessFile("./Relatorios.db", "rw");

            //============ FIM DAS DEFINIÇÕES ==============

            if(existeIdInicial == false)
            {
                arq.writeInt(cpf);
            }
            //salvar enderço inicial do arquivo
            long p1 = arq.getFilePointer();

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
                bytesLixo = new byte[((tamanhoPadrao - tamanho) - 1)];
                arq.write(bytesLixo);
            }

            //salvar enderço final do arquivo
            long p2 = arq.getFilePointer();
            
            if(existeIdInicial == true)
            {
                //voltar com o ponteiro para o começo do arquivo
                arq.seek(p1);
    
                //escrever non começo do arqyuivo o ultimo id escrito
                arq.writeInt((idUltimo + 1));
            }
            arq.seek(p2);
            //fechar arquivo
            arq.close();
            
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    //Método de recuperação de arquivos
    public static Relatorio getRelatorio(int cpf){

        try{
            //criar uma nova classe de Relatório

            Relatorio relatorio = new Relatorio();

            //definir variável para escrita

            fis = new FileInputStream("./Relatorios.db");

            //definir variável auxiliar para escrita

            dis = new DataInputStream(fis);

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

                relatorio.frontByteArray(bytes);

                //mostrar

                System.out.println(relatorio.toString());

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






    public static void menu(){
        //printar menu

        System.out.println("------------RELATÓRIOS MÉDICOS--------");
        System.out.println("");
        System.out.println("");
        System.out.println("1 - NOVO RELATÓRIO");
        System.out.println("2 - VISUALIZAR RELATÓRIO");
        System.out.println("3 - EDITAR RELATÓRIO");
        System.out.println("4 - DELETAR RELATÓRIO");
        System.out.println("5 - SAIR");
        System.out.println("");
        System.out.println("");
    }

    public static void menu2(){
        //printar menu

        System.out.println("");
        System.out.println("");
        System.out.println("1 - CPF:");
        System.out.println("2 - NOME:");
        System.out.println("3 - DATA DE NASCIMENTO:");
        System.out.println("4 - SEXO:");
        System.out.println("5 - ANOTAÇÕES:");
        System.out.println("6 - MOSTRAR:");
        System.out.println("7 - SALVAR:");
        System.out.println("8 - VOLTAR:");
        System.out.println("");
        System.out.println("");
    }

    public static void interfaces(){
        //definir auxiliar de leitura
        Scanner ler = new Scanner(System.in);
        String cont = "";

        //intergface
        System.out.println("OLA");
        System.out.println("");
        System.out.println("BEM VINDO AO SISTEMA DE MEDITÓRIO");
        System.out.println("");
        System.out.println("");
        System.out.println("Aperte" +'"'+ " enter " +'"'+ " para continuar...");
        cont = ler.nextLine();

            //chamnar menu principal
            menu();

            //definir dados
            int op;
            boolean sairG = false;

            do{
                System.out.println("Opção:");
                op = ler.nextInt();
                System.out.print("\r\n");
                
                    switch(op){

                    case 1:
                        System.out.print("\r\n");
                        //chamar menu
                        menu2();
                        //definir dados
                        Relatorio relatorio;
                        int cpf = -1;
                        String nome = " ";
                        String dataNascimento = "";
                        boolean sexoBol = false;
                        String anotation = "";
                        boolean sair = false;
                        int sexo = 0;

                        //dividir telas
                        int op2;

                        do{
                            
                            System.out.println("Opção:");
                            
                            op2 = ler.nextInt();
                            
                            //esvaziar buffer
                            ler.nextLine();
                            
                            System.out.print("\r\n");
                            switch(op2){

                                case 1:
                                    System.out.println("");
                                    System.out.println("INSERIR CPF:");
                                    cpf = ler.nextInt();
                                    System.out.println("");
                                break;
                                case 2:
                                    do{
                                        System.out.println("");
                                        System.out.println("INSERIR NOME:");
                                        nome = ler.nextLine();

                                        //testar se o nome do paciente tem mais que 40 caracteres
                                        if(nome.length() > 40)
                                        {
                                            System.out.println("Nome muito grande, tente novamente!");
                                        }
                                        System.out.println("");

                                    }while(nome.length() > 40);
                                break;
                                case 3:
                                    do{
                                        System.out.println("");
                                        System.out.println("INSERIR DATA DE NASCIMENTO:");
                                        dataNascimento = ler.nextLine();
    
                                        //testar se o nome do paciente tem mais que 40 caracteres
                                        if(nome.length() > 40)
                                        {
                                            System.out.println("Anotação muito grande, tente novamente!");
                                        }
                                        System.out.println("");

                                    }while(nome.length() > 40);
                                break;
                                case 4:
                                    //tratar erro com o repetição
                                    do{
                                        System.out.println("");
                                        System.out.println("1 - MASCULINO");
                                        System.out.println("2 - FEMININO");
                                        System.out.println("");
                                        sexo = ler.nextInt();

                                        //exibir mensgaem de erro

                                        if(sexo != 1 && sexo != 2)
                                        {
                                            System.out.println("");
                                            System.out.println("Selecione apenas uma das letras");
                                            System.out.println("");
                                        }
                                    }while(sexo != 1 && sexo != 2);

                                    //conveter para o tipo de dado adequado para a memoria

                                    if(sexo == 1){
                                        sexoBol = true;
                                    }
                                    else if(sexo == 2){
                                        sexoBol = false;
                                    }
                                break;
                            
                                case 5:
                                    do{
                                        System.out.println("");
                                        System.out.println("ANOTAÇÃO:");
                                        anotation = ler.nextLine();
                                        
                                        //exibir mensgaem de erro
                                        if(anotation.length() > 340)
                                        {
                                            System.out.println("Anotação muito grande, tente novamente!");
                                        }
                                        
                                    }while(anotation.length() > 340);
                                    System.out.println("");
                                break;
                                case 6:
                                    System.out.println("");
                                    System.out.println("CPF: "+ cpf +
                                    "\nNome: " + nome +
                                    "\nData de Nascimento: "+ dataNascimento +
                                    "\nSexo: "+ sexo +
                                    "\nAnotação: \n" +
                                    anotation);
                                    System.out.println("");
                                break;                         
                                case 7:
                                    boolean sucesso = insertRelatorio(cpf, nome, dataNascimento, sexoBol, anotation);

                                    //TESTAR SE A OPERAÇÃO FOI BEM SUCEDIDA
                                    if(sucesso){
                                        System.out.println("");
                                        System.out.println("SALVADO COM SUCESSO");
                                        System.out.println("");
                                    }
                                    else{
                                        System.out.println("");
                                        System.out.println("ERRO, O ARQUIVO NÃO PODE SER SALVO");
                                        System.out.println("");
                                    }
                                break;
                                case 8:
                                    sair = true;
                                    //chamar o menu de opções
                                    menu();
                                break;
                                default:
                                    System.out.println("");
                                    System.out.println("Selecione apenas um dos números");
                                    System.out.println("");
                            }
                            if (op2 != 8){
                                menu2();
                            }
                            System.out.print("\r\n");
                            ler.nextLine();
                        }while(sair == false);
                    break;

                    case 2:
                        System.out.print("\r\n");
                        //DEFINIR DADOS
                        int cpfDigitado;
                        boolean sucesso = false;
                        Relatorio relatorios;

                        //menu
                        System.out.println("");
                        System.out.println("DIGITE O CPF DO PACIENTE:");
                        cpfDigitado = ler.nextInt();
                        System.out.println("");

                        //pegar relatorio do paciente
                        relatorios = getRelatorio(cpfDigitado);
                        

                        //testar se o cpf foi cadastrado
                        if(relatorios != null){
                            System.out.println("");
                            System.out.println(relatorios.toString());;
                            System.out.println("");
                        }
                        else{
                            System.out.println("");
                            System.out.println("CPF NÃO REGISTRADO");
                            System.out.println("");
                        }
                        //chamnar menu principal
                        menu();
                    break;
                    case 5:
                        System.out.print("\r\n");
                        sairG = true;
                        System.out.println("");
                        System.out.println("OBRIGADO POR USAR NOSSO SISTEMA");
                        System.out.println("");
                        //chamnar menu principal
                        menu();
                    break;
                    default:
                        System.out.print("\r\n");
                        System.out.println("");
                        System.out.println("Selecione apenas um dos números");
                        System.out.println("");
                        //chamnar menu principal
                        menu();
                }

            }while(sairG == false);

        

    }
    public static void main(String[] args){

        interfaces();

    }
}