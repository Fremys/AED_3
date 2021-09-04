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
    public static boolean insertRelatorio(long cpf, String nome, String dataNascimento, boolean sexo, String anotation){
        
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
    
                //escrever non começo do arquivo o ultimo id escrito
                arq.writeLong((idUltimo + 1));
            }

            //fechar arquivo
            arq.close();
            
            //return
            return true;

        }catch (Exception e){
            return false;
        }

    }

    //Método de recuperação de arquivos
    public static Relatorio getRelatorio(long cpf){

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
    public static boolean deleteRelatorio(long cpf){

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
    public static boolean updateRelatorio(Relatorio relatorio){
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

    public static void menuEdit(){
        //printar menu

        System.out.println("");
        System.out.println("");
        System.out.println("1 - NOME:");
        System.out.println("2 - DATA DE NASCIMENTO:");
        System.out.println("3 - SEXO:");
        System.out.println("4 - ANOTAÇÕES:");
        System.out.println("5 - MOSTRAR:");
        System.out.println("6 - SALVAR:");
        System.out.println("7 - VOLTAR:");
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
                        long cpf = -1;
                        String nome = " ";
                        String dataNascimento = " ";
                        boolean sexoBol = false;
                        String anotation = " ";
                        boolean sair = false;
                        int sexo = 0;

                        //dividir telas
                        int op2;

                        do{
                            
                            System.out.println("Opção:");
                            
                            op2 = ler.nextInt();
                            
                            //esvaziar buffer
                            System.out.println("Caso necessário, aperte ENTER para continuar...");
                            ler.nextLine();

                            //saltar espaço
                            System.out.print("\r\n");
                            switch(op2){

                                case 1:
                                    System.out.println("");
                                    System.out.println("INSERIR CPF:");
                                    cpf = ler.nextLong();
                                    System.out.println("");
                                break;
                                case 2:
                                    do{
                                        System.out.println("");
                                        System.out.println("INSERIR NOME:");
                                        nome = ler.nextLine();

                                        //testar se o nome do paciente tem mais que 40 caracteres
                                        if(nome.length() > 50)
                                        {
                                            System.out.println("Nome muito grande, Abrevie!");
                                        }
                                        System.out.println("");

                                    }while(nome.length() > 50);
                                break;
                                case 3:
                                    do{
                                        System.out.println("");
                                        System.out.println("INSERIR DATA DE NASCIMENTO:");
                                        dataNascimento = ler.nextLine();
    
                                        //testar se o nome do paciente tem mais que 40 caracteres
                                        if(dataNascimento.length() > 10 || dataNascimento.length() < 10)
                                        {
                                            System.out.println("Data inválida, tente novamente!");
                                        }
                                        System.out.println("");

                                    }while(dataNascimento.length() > 10 || dataNascimento.length() < 10);
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
                                        if(anotation.length() > (2932))
                                        {
                                            System.out.println("Anotação muito grande, tente novamente!");
                                            System.out.println(anotation.length());
                                        }
                                        
                                    }while((anotation.length()) > (2932));
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
                                    //definir um classe auxiliar para verificação
                                    Relatorio aux = new Relatorio();

                                    //procurar algum usuário já registrado com aquele cpf
                                    aux = getRelatorio(cpf);

                                    //testar se encontrou
                                    if(aux == null) {
                                        
                                        //EXECUTAR O METODO DE INSERÇÃO
                                        boolean sucesso = insertRelatorio(cpf, nome, dataNascimento, sexoBol, anotation);
    
                                        //TESTAR SE A OPERAÇÃO FOI BEM SUCEDIDA
                                        if(sucesso){
                                            System.out.println("");
                                            System.out.println("SALVO COM SUCESSO");
                                            System.out.println("");
                                        }
                                        else{
                                            System.out.println("");
                                            System.out.println("ERRO, O ARQUIVO NÃO PODE SER SALVO");
                                            System.out.println("");
                                        }
                                    }
                                    else
                                    {
                                        System.out.println("");
                                        System.out.println("CPF JÁ CADASTRADO ANTERIORMENTE");
                                        System.out.println("");
                                    }

                                break;
                                case 8:
                                    //atualizar variavel
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
                            System.out.println("Caso necessário, aperte ENTER para continuar...");
                            ler.nextLine();
                            System.out.println("");
                        }while(sair == false);
                    break;

                    case 2:
                        //DEFINIR DADOS
                        System.out.print("\r\n");
                        long cpfDigitado;
                        boolean sucesso = false;
                        Relatorio relatorios;

                        //menu
                        System.out.println("");
                        System.out.println("DIGITE O CPF DO PACIENTE:");
                        cpfDigitado = ler.nextLong();
                        System.out.println("");

                        //pegar relatorio do paciente
                        relatorios = getRelatorio(cpfDigitado);

                        if(relatorios != null){

                            System.out.println("");
                            System.out.println(relatorios.toString());
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
                    case 3:
                        //DEFINIR DADOS
                        System.out.print("\r\n");
                        long cpfDigitadoUpdate;
                        boolean sucessoUpdate = false;
                        Relatorio relatorioUpdate;

                        //menu
                        System.out.println("");
                        System.out.println("DIGITE O CPF DO PACIENTE:");
                        cpfDigitadoUpdate = ler.nextLong();
                        System.out.println("");

                        //mostrar na tela o usuário antes de deleta-lo
                        relatorioUpdate = getRelatorio(cpfDigitadoUpdate);  

                        //testar se o cpf foi cadastrado
                        if(relatorioUpdate != null){

                            //definir dados
                            int numU = -1;

                            do{
                                
                                System.out.println("");
                                System.out.println(relatorioUpdate.toString());
                                System.out.println("");
    
                                //MOSTRAR TELA 
                                System.out.println("");
                                System.out.println("ESSE É USUÁRIO QUE VOCÊ DESEJA EDITAR? ");
                                System.out.println("1 - SIM");
                                System.out.println("2 - NÃO");
                                System.out.print("Opção: ");

                                //ler do usuário
                                numU = ler.nextInt();

                                //testar se houve erro na entrada do usuário
                                if(numU != 1 && numU != 2){
                                    System.out.println("");
                                    System.out.println("Digite apenas uma das opções");
                                    System.out.println("");
                                }

                            }while(numU != 1 && numU != 2);
                            
                            //Testar se o usuário deseja deletar
                            if(numU == 1){
                                
                                System.out.print("\r\n");

                                //chamar menu
                                menuEdit();
                                
                                //definir dados
                                long cpfMostrar = -1;
                                String nomeMostrar = " ";
                                String dataNascimentoMostrar = " ";
                                boolean sexoBolMostrar = false;
                                String anotationMostrar = " ";
                                boolean sairMostrar = false;
                                int sexoMostrar = 0;
        
                                //dividir telas
                                int op3;
        
                                do{
                                    
                                    System.out.println("Opção:");
                                    
                                    //ler opção do usuario
                                    op3= ler.nextInt();

                                    //esvaziar buffer
                                    System.out.println("Caso necessário, aperte ENTER para continuar...");
                                    ler.nextLine();
                                    System.out.print("\r\n");
                                    switch(op3){

                                        case 1:
                                            do{
                                                System.out.println("");
                                                System.out.println("NOME: " + relatorioUpdate.getNome());
                                                System.out.println("EDITAR NOME: ");
                                                nomeMostrar = ler.nextLine();
        
                                                //testar se o nome do paciente tem mais que 40 caracteres
                                                if(nomeMostrar.length() > 50)
                                                {
                                                    System.out.println("Nome muito grande, tente abreviar!");
                                                }
                                                System.out.println("");
                                            }while(nomeMostrar.length() > 50);

                                            relatorioUpdate.setNome(nomeMostrar);
                                        break;
                                        case 2:
                                            do{
                                                System.out.println("");
                                                System.out.println("DATA DE NASCIMNETO: " + relatorioUpdate.getDataNascimento());
                                                System.out.println("EDITAR DATA DE NASCIMENTO:");
                                                dataNascimentoMostrar = ler.nextLine();
            
                                                //testar se o nome do paciente tem mais que 40 caracteres
                                                if(dataNascimentoMostrar.length() > 10 || dataNascimentoMostrar.length() < 10)
                                                {
                                                    System.out.println("Data inválida");
                                                }
                                                System.out.println("");
        
                                            }while(dataNascimentoMostrar.length() > 10 || dataNascimentoMostrar.length() < 10);

                                            relatorioUpdate.setData(dataNascimentoMostrar);
                                            
                                        break;
                                        case 3:
                                            //tratar erro com o repetição
                                            do{
                                                System.out.println("");
                                                System.out.println("SEXO: " + relatorioUpdate.getSexo());
                                                System.out.println("1 - MASCULINO");
                                                System.out.println("2 - FEMININO");
                                                System.out.println("");
                                                sexoMostrar = ler.nextInt();
        
                                                //exibir mensgaem de erro
        
                                                if(sexoMostrar != 1 && sexoMostrar != 2)
                                                {
                                                    System.out.println("");
                                                    System.out.println("Selecione apenas uma das letras");
                                                    System.out.println("");
                                                }
                                            }while(sexoMostrar != 1 && sexoMostrar != 2);
        
                                            //conveter para o tipo de dado adequado para a memoria
        
                                            if(sexoMostrar == 1){
                                            
                                                sexoBolMostrar = true;
                                            }
                                            else if(sexoMostrar == 2){

                                                sexoBolMostrar = false;
                                            }
                                            
                                            relatorioUpdate.setSexo(sexoBolMostrar);

                                        break;
                                    
                                        case 4:
                                            do{
                                                System.out.println("");
                                                System.out.println("ANOTAÇÃO:\n" + relatorioUpdate.getAnotation());
                                                System.out.println("");
                                                System.out.println("ANOTAÇÃO:");
                                                anotationMostrar = ler.nextLine();
                                                
                                                //exibir mensgaem de erro
                                                if(anotationMostrar.length() > 2932)
                                                {
                                                    System.out.println("Anotação muito grande, tente novamente!");
                                                    System.out.println((anotationMostrar.length()));
                                                }
                                                
                                            }while(anotationMostrar.length() > 2932);

                                            relatorioUpdate.setAnotation(anotationMostrar);

                                            System.out.println("");
                                        break;
                                        case 5:
                                            System.out.println("");
                                            System.out.println(relatorioUpdate.toString());
                                            System.out.println("");
                                        break;                         
                                        case 6:
                                            boolean sucessoInUpdate = updateRelatorio(relatorioUpdate);
        
                                            //TESTAR SE A OPERAÇÃO FOI BEM SUCEDIDA
                                            if(sucessoInUpdate){
                                                System.out.println("");
                                                System.out.println("ATUALIZADO COM SUCESSO");
                                                System.out.println("");
                                            }   
                                            else{
                                                System.out.println("");
                                                System.out.println("ERRO, O ARQUIVO NÃO PODE SER ATUALIZADO");
                                                System.out.println("");
                                            }
                                        break;
                                        case 7:
                                            //Ataulizar variavel
                                            sairMostrar = true;
                                            //chamar o menu de opções
                                            menu();
                                        break;
                                        default:
                                            System.out.println("");
                                            System.out.println("Selecione apenas um dos números");
                                            System.out.println("");
                                    }
                                    //mostrar menu novamente
                                    if (op3 != 7){
                                        menuEdit();
                                    }

                                    //esvaziar buffer
                                    System.out.print("\r\n");

                                    System.out.println("Caso necessário, aperte ENTER para continuar...");
                                    ler.nextLine();
                                    System.out.println("");
                                }while(sairMostrar == false);
                            }
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
                        //DEFINIR DADOS
                        System.out.print("\r\n");
                        long cpfDigitadoDelete;
                        boolean sucessoDelete = false;
                        Relatorio relatoriosDelete;
        
                        //menu
                        System.out.println("");
                        System.out.println("DIGITE O CPF DO PACIENTE:");
                        cpfDigitadoDelete = ler.nextLong();
                        System.out.println("");
        
                        //pegar relatorio do paciente
                        relatoriosDelete = getRelatorio(cpfDigitadoDelete);
        
                        if(relatoriosDelete != null){
                            //definir dados
                            int confirmDelete = -1;

                            do{
                                
                                System.out.println("");
                                System.out.println(relatoriosDelete.toString());
                                System.out.println("");
                                
                                System.out.println("ESTE É O USUÁRIO QUE VOCÊ DESEJA DELETAR?");
                                System.out.println("");
                                System.out.println("1 - SIM");
                                System.out.println("2 - NÃO");
                                System.out.println("");
                                
                                System.out.println("Opção:");
                                confirmDelete = ler.nextInt();
                                System.out.println("");

                                if(confirmDelete != 1 && confirmDelete != 2){
                                    System.out.println("");
                                    System.out.println("Digite apenas um das opções válidas");
                                    System.out.println("");
                                }

                            }while(confirmDelete != 1 && confirmDelete != 2);

                            //testar se o usuario realemnte deseja deletar o usuario
                            if(confirmDelete == 1){

                                sucessoDelete = deleteRelatorio(cpfDigitadoDelete);

                                //Testar se o usuário foi deletado com sucesso
                                if(sucessoDelete){
                                    System.out.println("");
                                    System.out.println("USUÁRIO DELETADO COM SUCESSO");
                                    System.out.println("");
                                }
                                else{
                                    System.out.println("");
                                    System.out.println("NÃO FOI POSSÍVEL DELETAR O USUÁRIO");
                                    System.out.println("");
                                }
                            }
                        }
                        else{
                            System.out.println("");
                            System.out.println("CPF NÃO REGISTRADO");
                            System.out.println("");
                        }
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