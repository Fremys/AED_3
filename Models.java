//Bibliotecas usadas
import java.text.ParseException;
import java.io.IOException;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import java.io.DataOutputStream;
import java.io.DataInputStream;

import java.io.FileOutputStream;
import java.io.FileInputStream;

import java.util.Scanner;



public class Models{

    //definir arquivo de dados

    String arquivo = ("./lelatorios.db");

    /* ===== DEFINIR METODOS DE LEITURA E ESCRITA ===== */

    //definir variável para escrita
    static FileOutputStream fos;    
    //definir auxiliar variável para escrita
    static DataOutputStream dos;

    //definir variável para leitura
    static FileInputStream fis;
    //definir auxiliar variável para escrita
    static DataInputStream dis;

    //Definir Array Auxiliar
    static byte[] bytes;



    //Metodo de inserção de relatórios em arquivo
    public static boolean insertRelatorio(int cpf, String nome, String dataNascimento, boolean sexo, String anotation){
        
        try {       
            //criar uma nova classe de Relatório

            Relatorio relatorio = new Relatorio(cpf, nome, dataNascimento, sexo, anotation);

            //definir variável para escrita
            fos = new FileOutputStream("./lelatorios.db");

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

            dos.writeInt(bytes.length);
            dos.write(bytes);

            //fechar arquivo

            fos.close();

            return true;
        }catch (Exception e){
            return false;
        }

    }

    //Método de recuperação de arquivos
    public static Relatorio getRelatorio(int cpf){

        try{

            //criar uma nova classe de Relatório

            Relatorio relatorio = new Relatorio();

            //definir variável para escrita
            fis = new FileInputStream("./lelatorios.db");

            //definir variável auxiliar para escrita
            dis = new DataInputStream(fis);

            //pular um numero inteiro no ponteiro
            //int tmp = dis.readInt();

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

                    switch(op){

                    case 1:

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

                            switch(op2){

                                case 1:
                                    System.out.println("");
                                    System.out.println("INSERIR CPF:");
                                    cpf = ler.nextInt();
                                    System.out.println("");
                                break;
                                case 2:
                                    System.out.println("");
                                    System.out.println("INSERIR NOME:");
                                    nome = ler.nextLine();
                                    System.out.println("");
                                break;
                                case 3:
                                    System.out.println("");
                                    System.out.println("INSERIR DATA DE NASCIMENTO:");
                                    dataNascimento = ler.nextLine();
                                    System.out.println("");
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
                                    System.out.println("");
                                    System.out.println("ANOTAÇÃO:");
                                    anotation = ler.nextLine();
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
                            ler.nextLine();
                        }while(sair == false);
                    break;

                    case 2:
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
                    break;
                    case 5:
                        sairG = true;
                        System.out.println("");
                        System.out.println("OBRIGADO POR USAR NOSSO SISTEMA");
                        System.out.println("");
                    break;
                    default:
                        System.out.println("");
                        System.out.println("Selecione apenas um dos números");
                        System.out.println("");
                }

            }while(sairG == false);

        

    }

    public static void main(String[] args){

        interfaces();

    }
}