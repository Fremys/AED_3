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

        /*
        =========================================== EXECUTAR PELA PRIMEIRA VEZ ====================================
        * Essa parte do código verifica se há um arquivo binário criado em disco antes de realizar qualquer operação
        * Caso exista, ele executa o insert normalmente, porém caso não exista ele cria o arquivo.
        */
        

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
            }

            //=========================================== FIM DO EXECUTAR PELA PRIMEIRA VEZ ===========================================

            //============= DEFINÇOES ===================
            
            //definir variável para escrita aleatoria
            arq = new RandomAccessFile("./Relatorios.db", "rw");

            //salvar enderço inicial do arquivossssss
            long p1 = arq.getFilePointer();

            //salvar em variável o tamanho do arquivo final
            long tamArq = arq.length();

            //posicionar o ponteiro no final do arquivo
            arq.seek(tamArq);

            //============ FIM DAS DEFINIÇÕES ==============

            if(existeIdInicial == false)
            {
                arq.writeInt(cpf);
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
    
                //escrever non começo do arquivo o ultimo id escrito
                arq.writeInt((idUltimo + 1));
            }

            //fechar arquivo
            arq.close();
            
            //return
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

            arq = new RandomAccessFile("./Relatorios.db", "rw");

            //pular um numero inteiro no ponteiro
            int tmp = arq.readInt();

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
    public static void deleteRelatorio(int cpf){

        try{
            //criar uma nova classe de Relatório

            Relatorio relatorio = new Relatorio();

            //definir variável para escrita

            arq = new RandomAccessFile("./Relatorios.db", "rw");

            //pular um numero inteiro no ponteiro
            int tmp = arq.readInt();

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

                //definir dados
                int resp = -1;

                //mostrar
                System.out.println(relatorio.toString());

                //confirmar com o usuário
                System.out.print("\n");
                System.out.println("Tem certeza que deseja excluir esse relatório?");
                System.out.print("\n");

                //exibir menu
                System.out.println("\n");
                System.out.println("1 - SIM");
                System.out.println("2 - NÃO");
                System.out.println("\n");
                
                //salvar mensagem

                do{
                    //ler resposta
                    resp = ler.nextInt();
                    ler.nextLine();
                    
                    //verificar se a resposta esta incorreta
                    if(resp != 1 && resp != 2){
                        System.out.println("\n");
                        System.out.println("Resposta incorreta, insira apenas um das opções disponíveis");
                        
                        //exibir menu
                        System.out.println("\n");
                        System.out.println("1 - SIM");
                        System.out.println("2 - NÃO");
                        System.out.println("\n");
                    }

                }while(resp != 1 && resp != 2);

                //testar resposta recebida
                if(resp == 1)
                {
                    //mover o ponteiro para o arquivo inicial
                    arq.seek(posArq);

                    //salvar o id como excluído
                    arq.writeInt(-1);

                    System.out.print("\r\n");
                    System.out.print("PACIENTE DELETADO COM SUCESSO");
                    System.out.print("\n");

                }

                //saltar linhas
                System.out.print("\r\n");

            }
            else{
                //mostrar na tela
                System.out.println("\n\n\n");
                System.out.println("CPF não registrado");
                System.out.println("\n\n\n");
            }


        }catch (Exception e){
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
                                        if(anotation.length() > 540)
                                        {
                                            System.out.println("Anotação muito grande, tente novamente!");
                                        }
                                        
                                    }while(anotation.length() > 540);
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
                        //chamar menu principal
                        menu();
                    break;
                    case 4:
                        System.out.print("\r\n");
                        //DEFINIR DADOS
                        int cpfDigitadoDelete;

                        //menu
                        System.out.println("");
                        System.out.println("DIGITE O CPF DO PACIENTE:");
                        cpfDigitadoDelete = ler.nextInt();
                        System.out.println("");

                        //chamar metodo de delete
                        deleteRelatorio(cpfDigitadoDelete);

                        //chamar menu principal
                        menu();

                    break;
                    case 5:
                        System.out.print("\r\n");
                        System.out.print("OBRIGADO POR USAR NOSSO SISTEMA :)");
                        System.out.print("\r\n");

                        //Modificar a variavel de saida
                        sairG = true;
                    break;
                    default:
                        System.out.print("\r\n");
                        System.out.println("");
                        System.out.println("Selecione apenas um dos números");
                        System.out.println("");

                        //chamnar menu principal
                        menu();
                    break;
                }

            }while(sairG == false);

        

    }
    public static void main(String[] args){
        //Chamar a interface do sistema
        interfaces();
    }
}