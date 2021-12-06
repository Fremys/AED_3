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

import java.util.Random;

public class Interface {
    //definir classe global
    static Models models = new Models();

    public static void interfaceInsertTest(){
        System.out.println("------------TESTE EFICIENCIA DOS RELATORIOS--------\n\n\n1 - NUMERO DE CHAVES:\n2 - PROFUNDIDADE GLOBAL:\n3 - NUMERO DE ENTRADAS POR BUCKET:\n4 - TAMANHO ADICONAL DO RESGISTRO:\n5 - MEDIR TEMPO:\n6 - Simulacao 1 Gb:\n7 - Recuperar Dados:\n8 - Sair:\n\n\n");
    }

    //Interface de Inserção para teste
    public static void insertTeste(){

        interfaceInsertTest();

        //definir auxiliar de leitura
        Scanner ler = new Scanner(System.in);
        String cont = "";

        //limpar buffer
        System.out.println("Aperte" +'"'+ " enter " +'"'+ " para continuar...");
        cont = ler.nextLine();

        int op = -1;
        boolean out = false;
        
        //DEFINIR VARIÁVEIS USADAS PARA AS INSERÇÕES
        long k = -1;
        int p = -1;
        int n = -1;
        int m = -1;

        do{
            System.out.print("Opção:");
            op = ler.nextInt();

            System.out.print("\r\n");

            switch (op) {
                
                case 1:
                    System.out.println("");
                    System.out.print("Numero de chaves: "); 
                    k = ler.nextLong();
                    System.out.println("");
                break;
                case 2:
                    System.out.println("");
                    System.out.print("Numero de profundidade: ");
                    p = ler.nextInt();
                    System.out.println("");

                break;
                
                case 3:
                    System.out.println("");
                    System.out.print("Quantidade por Bucket: ");
                        n = ler.nextInt();
                    if(p > - 1 && n > -1){
                        //Criar os Buckets vazios para armazenamento
                        models.createBuckets(p, n);
                        System.out.println("Buckets vazios criado com sucesso");
                        System.out.println("");
                    }else{
                        System.out.println("ERRO: Algyum campo não foi preenchido");
                        System.out.println("");
                    }

                break;

                case 4:
                    System.out.println("");
                    System.out.print("Tamanho arquivo adicional: ");
                        m = ler.nextInt();
                    System.out.println("");
                break;
                    
                case 5:

                    //Testar se todos os campos fora preenchidos com sucesso
                    if(k > -1 && p > -1 && n > -1 && m > -1){

                        //Definir dados
                        Random random = new Random();
                        int tamanhoTotalRegistro = 66 + m;
                        String nome = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
                        String arquivoAdicional = "";
                        long tempoTotal = 0;

                        //Criar os Buckets vazios para armazenamento
                        //models.createBuckets(p, n);


                        //Preparar arquivo adicional
                        for(int j = 0; j < m; j++){
                            arquivoAdicional = arquivoAdicional + "B";
                        }
                        
                        //Realizar as inserções desejadas
                        for(int i = 0; i < k; i++){

                            System.out.println("inserindo : "+ i);

                            long tempoInicial = System.currentTimeMillis();
                            //random.nextInt(999999999)
                            boolean sucesso = models.insertRelatorio(i, nome, "21/02/2002", true, arquivoAdicional, n, tamanhoTotalRegistro);
                       
                            tempoTotal = tempoTotal + (System.currentTimeMillis() - tempoInicial);
                        }  
                        
                        System.out.println("Tempo total das Inserções: " + tempoTotal);
                        System.out.println("");
                    }
                    else{
                        System.out.println("");
                        System.out.println("ERRO: Algum campo não foi preenchido");
                        System.out.println("");
                    }
                break;
                case 6:

                if(p > -1 && n > -1 && m > -1){
                    //Definir dados
                    Random random = new Random();
                    long tempoTotal = 0;
                    int tamanhoTotalRegistro = 66 + m;
                    long quantidaNecessariaRegistros = 1000000000/tamanhoTotalRegistro;
                    String nome = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
                    String arquivoAdicional = "";
                    k = quantidaNecessariaRegistros;

                    //Preparar arquivo adicional
                    //models.createBuckets(p, n);

                    //Preparar arquivo adicional
                    for(long j = 0; j < m; j++){
                        arquivoAdicional = arquivoAdicional + "B";
                    }
                    System.out.println("quantidaNecessariaRegistros: "+quantidaNecessariaRegistros);
                    //Realizar as inserções desejadas
                    for(long i = 0; i < quantidaNecessariaRegistros; i++){

                        System.out.println("Inserido: "+i);
                        
                        long tempoInicial = System.currentTimeMillis();
                        
                        boolean sucesso = models.insertRelatorio(i, nome, "21/02/2002", true, arquivoAdicional, n, tamanhoTotalRegistro);
                        
                        tempoTotal = tempoTotal + (System.currentTimeMillis() - tempoInicial);
                    }
                    System.out.println("Tempo total das Inserções: " + tempoTotal);
                    System.out.println("Quantidade de arquivos inseridos: " + quantidaNecessariaRegistros);
                    System.out.println("Tamanho de Cada registro: " + tamanhoTotalRegistro);
                    System.out.println("");

                }else{
                    System.out.println("");
                    System.out.println("ERRO: Algum campo não foi preenchido");
                    System.out.println("");
                }
                break;
                case 7:

                if(p > -1 && n > -1 && m > -1){
                    long tempoTotal = 0;
                    
                    for(long i = 0; i < k; i++){
                        
                        System.out.println("Inserido: "+i);
                        
                        long tempoInicial = System.currentTimeMillis();
                        
                        Relatorio sucesso = models.getRelatorio(i);
                        
                        tempoTotal = tempoTotal + (System.currentTimeMillis() - tempoInicial);
                    }

                    System.out.println("Tempo total das Recuperações: " + tempoTotal);
                }
                else{
                    System.out.println("");
                    System.out.println("ERRO: Algum campo não foi preenchido");
                    System.out.println("");
                }
                break;

                case 8:
                out = true;
                break;
                
                default:
                break;
                
            }
            System.out.println("");
            interfaceInsertTest();
            System.out.println("Aperte" +'"'+ " enter " +'"'+ " para continuar...");
            cont = ler.nextLine();
            System.out.println("");
            System.out.println("");

        }while(!out);
    }

    public static void interfaceTest(){
        System.out.println("------------RELATORIOS MÉDICOS--------");
        System.out.println("");
        System.out.println("");
        System.out.println("1 - Comecar Testes");
        System.out.println("2 - SAIR");
        System.out.println("");
        System.out.println("");
    }

    //Interface para teste
    public static void testesEficiencia(){

        interfaceTest();

        //definir auxiliar de leitura
        Scanner ler = new Scanner(System.in);
        String cont = "";

        //limpar buffer
        System.out.println("Aperte" +'"'+ " enter " +'"'+ " para continuar...");
        cont = ler.nextLine();

        int op = -1;
        boolean out = false;

        do{
            System.out.println("Opção:");
            op = ler.nextInt();
            System.out.print("\r\n");


            switch (op) {
                
                case 1:
                    insertTeste();
                break;

                case 2:
                    out = true;
                break;
                
                default:
                break;
                
            }
            interfaceTest();
            //limpar buffer
            System.out.println("Aperte" +'"'+ " enter " +'"'+ " para continuar...");
            cont = ler.nextLine();
        }while(!out);
            
            
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

                                        //testar se o nome do paciente tem mais que 50 caracteres
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
                                    aux = models.getRelatorio(cpf);

                                    //testar se encontrou
                                    if(aux == null) {
                                        
                                        //EXECUTAR O METODO DE INSERÇÃO
                                        boolean sucesso = models.insertRelatorio(cpf, nome, dataNascimento, sexoBol, anotation, 2, 3000);
    
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
                                        System.out.println("CPF JA CADASTRADO ANTERIORMENTE");
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
                        relatorios = models.getRelatorio(cpfDigitado);

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
                        relatorioUpdate = models.getRelatorio(cpfDigitadoUpdate);  

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
                                System.out.println("ESSE E USUARIO QUE VOCÊ DESEJA EDITAR? ");
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
                                            boolean sucessoInUpdate = models.updateRelatorio(relatorioUpdate, 3000);
        
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
                        relatoriosDelete = models.getRelatorio(cpfDigitadoDelete);
        
                        if(relatoriosDelete != null){
                            //definir dados
                            int confirmDelete = -1;

                            do{
                                
                                System.out.println("");
                                System.out.println(relatoriosDelete.toString());
                                System.out.println("");
                                
                                System.out.println("ESTE E O USUARIO QUE VOCÊ DESEJA DELETAR?");
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

                                sucessoDelete = models.deleteRelatorio(cpfDigitadoDelete);

                                //Testar se o usuário foi deletado com sucesso
                                if(sucessoDelete){
                                    System.out.println("");
                                    System.out.println("USUARIO DELETADO COM SUCESSO");
                                    System.out.println("");
                                }
                                else{
                                    System.out.println("");
                                    System.out.println("NÃO FOI POSSIVEL DELETAR O USUARIO");
                                    System.out.println("");
                                }
                            }

                            //chamar o menu de opções
                            menu();
                        }
                        else{
                            System.out.println("");
                            System.out.println("CPF NÃO REGISTRADO");
                            System.out.println("");
                            //chamar o menu de opções
                            menu();
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
        testesEficiencia();
    }
}
