/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flowshop;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Gleidson
 */
public class FlowShop {

    // Variavel global para definir o numero de MAQUINAS   
    public static int NUMERO_MAQUINAS = 0;
    //Variavel global para definir o tamanho de TAREFAS
    public static int NUMERO_TAREFAS = 0;

    public static void main(String[] args) {
        //Cria o scanner para leitura dos valores
        Scanner ler = new Scanner(System.in);
        System.out.println("####################################################################################################### ");

        // Informa a quantidade de Maquinas
        System.out.println("Informe o numero de Maquinas: ");
        NUMERO_MAQUINAS = ler.nextInt();

        // Informa a quantidade de Tarefas
        System.out.println("Informe o numero de Tarefas: ");
        NUMERO_TAREFAS = ler.nextInt();
        System.out.println("####################################################################################################### ");

        // vetores que receberão os nomes das maquinas e tarefas   
        String[] nomeMaquinas = new String[NUMERO_MAQUINAS];
        String[] nomeTarefas = new String[NUMERO_TAREFAS];

        //Vetor para atribuir a nomeação para fins de teste e não ter q digitar toda hora
        int[] tempoFinalPorMaquina = new int[NUMERO_MAQUINAS];
        int[] vetorPosOrdenadaLPT = new int[NUMERO_TAREFAS];

        // Popula o vetor de maquinas com os Nomes Especificados
        for (int i = 0; i < NUMERO_MAQUINAS; i++) {
            nomeMaquinas[i] = "M" + i;
        }

        // Popula o vetor de TAREFAS com os Nomes Especificados
        for (int i = 0; i < NUMERO_TAREFAS; i++) {
            nomeTarefas[i] = "Tarefa" + i;
        }

        //Inicialização e Preenchimento VetorTempoTarefa
        int vetorTempoIndividualTarefa[][] = new int[NUMERO_MAQUINAS][NUMERO_TAREFAS];
        int vetorTempoTotalTarefasMaquinas[][] = new int[NUMERO_MAQUINAS][NUMERO_TAREFAS];

        int[] posDoisMelhores = new int[2];

        System.out.println("####################################################################################################### ");
        for (int i = 0; i < NUMERO_MAQUINAS; i++) {
            for (int j = 0; j < NUMERO_TAREFAS; j++) {
                System.out.println("Informe o Tempo de Processamento da maquina: " + (nomeMaquinas[i]) + " com a: " + (nomeTarefas[j]));
                vetorTempoIndividualTarefa[i][j] = ler.nextInt();
            }
        }

        System.out.println("####################################################################################################### ");

        //Função de Calculo do Makespan sem qualquer Ordenação
        vetorTempoTotalTarefasMaquinas = calcularTempoProcessamentoGeral(tempoFinalPorMaquina, vetorTempoIndividualTarefa);

        //Função para Impressão
        imprimir(vetorTempoIndividualTarefa, vetorTempoTotalTarefasMaquinas, nomeMaquinas, nomeTarefas, tempoFinalPorMaquina);

        vetorPosOrdenadaLPT = ordenarLPT(vetorTempoIndividualTarefa);

        int melhorOpcao = 0;
        melhorOpcao = calcularTempoDuasMelhores(vetorPosOrdenadaLPT, tempoFinalPorMaquina, vetorTempoIndividualTarefa);
        System.out.println(melhorOpcao);
        /*
        imprimir2(vetorTempoIndividualTarefa, vetorTempoTotalTarefasMaquinas, nomeMaquinas, nomeTarefas, tempoFinalPorMaquina);
         */
    }

    // Fun��o para calcular o makespan
    private static int[][] calcularTempoProcessamentoGeral(int[] tempoFinalPorMaquina, int[][] vetorTempoIndividualTarefa) {
        // vetorTempoTotal receberá os Valores Acumulados dos Tempos Individuais
        // vetorFinalPorMaquina receberá os valores que cada maquina gastou para rodar todas as Atividades
        // vetorTempoIndividualTarefa trás o tempo que cada atividade gasta para executar em cada maquina
        int vetorTempoTotalTarefasMaquinas[][] = new int[NUMERO_MAQUINAS][NUMERO_TAREFAS];
        int resTmp = 0;

        for (int i = 0; i < NUMERO_MAQUINAS; i++) {
            for (int j = 0; j < NUMERO_TAREFAS; j++) {

                vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoIndividualTarefa[i][j] + resTmp;
                resTmp = vetorTempoTotalTarefasMaquinas[i][j];

                // Se já não estiver na primeira Linha, somo com as Informações com Linha Anterior          
                if (i != 0) {
                    vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoTotalTarefasMaquinas[i - 1][j] + vetorTempoIndividualTarefa[i][j];
                    resTmp = vetorTempoTotalTarefasMaquinas[i][j];

                    // Se já não estiver na primeira coluna, somo com a Coluna Anterior  
                    if (j != 0) {
                        /*Verificação para analisar se a tarefa a ser somada com a coluna anterior não é inferior a maquina da linha anterior, ou seja
                        Verificação para vê se a atividade ainda não estava em execução' na maquina anterior*/

                        //Codigo de Verificação se a tarefa não esta em execução na maquina anterior; 
                        if ((vetorTempoTotalTarefasMaquinas[i][j - 1] + vetorTempoIndividualTarefa[i][j]) > (vetorTempoTotalTarefasMaquinas[i - 1][j] + vetorTempoIndividualTarefa[i][j])) {
                            vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoTotalTarefasMaquinas[i][j - 1] + vetorTempoIndividualTarefa[i][j];
                            resTmp = vetorTempoTotalTarefasMaquinas[i][j];
                        } else {
                            vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoTotalTarefasMaquinas[i - 1][j] + vetorTempoIndividualTarefa[i][j];
                            resTmp = vetorTempoTotalTarefasMaquinas[i][j];
                        }
                    }
                }
            }
            tempoFinalPorMaquina[i] = resTmp;
        }
        return vetorTempoTotalTarefasMaquinas;
    }

    private static int calcularTempoProcessamentoMatrizMontada(int[][] vetorTempoIndividualTarefa) {
        // vetorTempoTotal receberá os Valores Acumulados dos Tempos Individuais
        // vetorFinalPorMaquina receberá os valores que cada maquina gastou para rodar todas as Atividades
        // vetorTempoIndividualTarefa trás o tempo que cada atividade gasta para executar em cada maquina
        int vetorTempoTotalTarefasMaquinas[][] = new int[NUMERO_MAQUINAS][NUMERO_TAREFAS];

        int resTmp = 0;
        /* for (int i = 0; i < NUMERO_MAQUINAS; i++) {
            for (int j = 0; j < NUMERO_TAREFAS; j++) {
                System.out.println(vetorTempoIndividualTarefa[i][j]);
            }
        }*/
        for (int i = 0; i < NUMERO_MAQUINAS; i++) {
            for (int j = 0; j < NUMERO_TAREFAS; j++) {

                vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoIndividualTarefa[i][j] + resTmp;
                resTmp = vetorTempoTotalTarefasMaquinas[i][j];

                // Se já não estiver na primeira Linha, somo com as Informações com Linha Anterior          
                if (i != 0) {
                    vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoTotalTarefasMaquinas[i - 1][j] + vetorTempoIndividualTarefa[i][j];
                    resTmp = vetorTempoTotalTarefasMaquinas[i][j];

                    // Se já não estiver na primeira coluna, somo com a Coluna Anterior  
                    if (j != 0) {
                        /*Verificação para analisar se a tarefa a ser somada com a coluna anterior não é inferior a maquina da linha anterior, ou seja
                        Verificação para vê se a atividade ainda não estava em execução' na maquina anterior*/

                        //Codigo de Verificação se a tarefa não esta em execução na maquina anterior; 
                        if ((vetorTempoTotalTarefasMaquinas[i][j - 1] + vetorTempoIndividualTarefa[i][j]) > (vetorTempoTotalTarefasMaquinas[i - 1][j] + vetorTempoIndividualTarefa[i][j])) {
                            vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoTotalTarefasMaquinas[i][j - 1] + vetorTempoIndividualTarefa[i][j];
                            resTmp = vetorTempoTotalTarefasMaquinas[i][j];
                        } else {
                            vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoTotalTarefasMaquinas[i - 1][j] + vetorTempoIndividualTarefa[i][j];
                            resTmp = vetorTempoTotalTarefasMaquinas[i][j];
                        }
                    }
                }
            }

        }
        System.out.println("O Valor desta Matriz é : " + resTmp);
        return resTmp;
    }

    // Fun��o para calcular o makespan
    private static int calcularTempoDuasMelhores(int[] vetorOrdenado, int[] tempoFinalPorMaquina, int[][] vetorTempoIndividualTarefa) {
        // vetorTempoTotal receberá os Valores Acumulados dos Tempos Individuais
        // vetorFinalPorMaquina receberá os valores que cada maquina gastou para rodar todas as Atividades
        // vetorTempoIndividualTarefa trás o tempo que cada atividade gasta para executar em cada maquina
        int looping = 2;
        int vetorTempoTotalTarefasMaquinas[][] = new int[NUMERO_MAQUINAS][NUMERO_TAREFAS];
        int resTmp = 0;
        int tempo1 = 0, tempo2 = 0;
        int[] posInvDoisMelhores = new int[2];
        int[] posDoisMelhores = new int[2];
        int x1 = 0, x2 = 0;

        for (int i = 0; i < NUMERO_MAQUINAS; i++) {
            for (int j = 0; j <= 1; j++) {
                vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoIndividualTarefa[i][vetorOrdenado[j]] + resTmp;
                resTmp = vetorTempoTotalTarefasMaquinas[i][j];

                // Se já não estiver na primeira Linha, somo com as Informações com Linha Anterior          
                if (i != 0) {
                    vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoTotalTarefasMaquinas[i - 1][j] + vetorTempoIndividualTarefa[i][vetorOrdenado[j]];
                    resTmp = vetorTempoTotalTarefasMaquinas[i][j];

                    // Se já não estiver na primeira coluna, somo com a Coluna Anterior  
                    if (j != 0) {
                        /*Verificação para analisar se a tarefa a ser somada com a coluna anterior não é inferior a maquina da linha anterior, ou seja
                        Verificação para vê se a atividade ainda não estava em execução' na maquina anterior*/

                        //Codigo de Verificação se a tarefa não esta em execução na maquina anterior; 
                        if ((vetorTempoTotalTarefasMaquinas[i][j - 1] + vetorTempoIndividualTarefa[i][vetorOrdenado[j]]) > (vetorTempoTotalTarefasMaquinas[i - 1][j] + vetorTempoIndividualTarefa[i][vetorOrdenado[j]])) {
                            vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoTotalTarefasMaquinas[i][j - 1] + vetorTempoIndividualTarefa[i][j];
                            resTmp = vetorTempoTotalTarefasMaquinas[i][j];
                        } else {
                            vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoTotalTarefasMaquinas[i - 1][j] + vetorTempoIndividualTarefa[i][vetorOrdenado[j]];
                            resTmp = vetorTempoTotalTarefasMaquinas[i][j];
                        }
                    }
                }
            }
            tempoFinalPorMaquina[i] = resTmp;
            tempo1 = resTmp;
            x1 = vetorOrdenado[0];
        }
        posDoisMelhores[0] = vetorOrdenado[0];
        posDoisMelhores[1] = vetorOrdenado[1];
        posInvDoisMelhores[0] = vetorOrdenado[1];
        posInvDoisMelhores[1] = vetorOrdenado[0];

        for (int i = 0; i < NUMERO_MAQUINAS; i++) {
            vetorTempoTotalTarefasMaquinas[i][0] = 0;
            vetorTempoTotalTarefasMaquinas[i][1] = 0;
        }
        resTmp = 0;

        for (int i = 0; i < NUMERO_MAQUINAS; i++) {
            for (int j = 0; j <= 1; j++) {
                vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoIndividualTarefa[i][posInvDoisMelhores[j]] + resTmp;
                resTmp = vetorTempoTotalTarefasMaquinas[i][j];

                // Se já não estiver na primeira Linha, somo com as Informações com Linha Anterior          
                if (i != 0) {
                    vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoTotalTarefasMaquinas[i - 1][j] + vetorTempoIndividualTarefa[i][posInvDoisMelhores[j]];
                    resTmp = vetorTempoTotalTarefasMaquinas[i][j];

                    // Se já não estiver na primeira coluna, somo com a Coluna Anterior  
                    if (j != 0) {
                        /*Verificação para analisar se a tarefa a ser somada com a coluna anterior não é inferior a maquina da linha anterior, ou seja
                        Verificação para vê se a atividade ainda não estava em execução' na maquina anterior*/

                        //Codigo de Verificação se a tarefa não esta em execução na maquina anterior; 
                        if ((vetorTempoTotalTarefasMaquinas[i][j - 1] + vetorTempoIndividualTarefa[i][posInvDoisMelhores[j]]) > (vetorTempoTotalTarefasMaquinas[i - 1][j] + vetorTempoIndividualTarefa[i][posInvDoisMelhores[j]])) {
                            vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoTotalTarefasMaquinas[i][j - 1] + vetorTempoIndividualTarefa[i][posInvDoisMelhores[j]];
                            resTmp = vetorTempoTotalTarefasMaquinas[i][j];
                        } else {
                            vetorTempoTotalTarefasMaquinas[i][j] = vetorTempoTotalTarefasMaquinas[i - 1][j] + vetorTempoIndividualTarefa[i][posInvDoisMelhores[j]];
                            resTmp = vetorTempoTotalTarefasMaquinas[i][j];
                        }
                    }
                }
            }
            tempoFinalPorMaquina[i] = resTmp;
            tempo2 = resTmp;
            x2 = vetorOrdenado[1];
        }
        System.out.println("\n\n#######################################################################################################\n");

        System.out.printf("Makespan de %d para %d: %d\n", vetorOrdenado[0], vetorOrdenado[1], tempo1);
        System.out.printf("Makespan de %d para %d: %d\n", vetorOrdenado[1], vetorOrdenado[0], tempo2);
        if (tempo1 < tempo2) {
            System.out.printf("A Sequencia Escolhida Foi %d e %d Com um Makespan de: %d\n", vetorOrdenado[0], vetorOrdenado[1], tempo1);
            
            retornarMelhorPosicao(posDoisMelhores, 2, vetorOrdenado, vetorTempoIndividualTarefa);
        } else {
            System.out.printf("A Sequencia Escolhida Foi %d e %d Com um Makespan de:\n", vetorOrdenado[1], vetorOrdenado[0], tempo2);
            retornarMelhorPosicao(posInvDoisMelhores, 2, vetorOrdenado, vetorTempoIndividualTarefa);
        }
        System.out.println("\n#######################################################################################################\n\n\n ");

        return 1;
    }

    private static int[] retornarMelhorPosicao(int[] vetorPosInicial, int posAtual, int[] vetorOrdenado, int[][] vetorTempoIndividualTarefa) {
        int melhormakespan = 0, makespanAtual = 0;
        int[] melhorSequencia = new int[NUMERO_TAREFAS];
        int[] colunaPercorrer = new int[vetorOrdenado.length + 1];

        ArrayList<int[]> colunap = new ArrayList();
        ArrayList<int[]> arrayMelhorSequencia = new ArrayList();
        int[] vet = new int[NUMERO_TAREFAS];

        vet[0] = vetorOrdenado[2];
        vet[1] = vetorPosInicial[0];
        vet[2] = vetorPosInicial[1];

        colunap.add(vet);
        System.out.println("\n#######################################################################################################\n ");
        System.out.println("\nSequencia Montada");
        System.out.printf("  " + colunap.get(0)[0]);
        System.out.printf("  " + colunap.get(0)[1]);
        System.out.printf("  " + colunap.get(0)[2]);
        for (int i = 3; i < NUMERO_TAREFAS; i++) {
            colunaPercorrer[i] = vetorOrdenado[i];
        }
        
        int pos = 0;
//        for (int pare = 2; pare < NUMERO_TAREFAS; pare++) {
            pos = 0;
            for (int loop = 0; loop < NUMERO_TAREFAS - 1; loop++) {
                int[][] permutacoes = new int[NUMERO_MAQUINAS][NUMERO_TAREFAS];
                System.out.println("\nMatriz de Tempos Individuais\n");
                for (int i = 0; i < NUMERO_MAQUINAS; i++) {
                    for (int j = 0; j <= posAtual; j++) {
                        //System.out.println("Colunap = "+ colunap.get(0)[j]);
                        permutacoes[i][j] = vetorTempoIndividualTarefa[i][colunap.get(0)[j]];
                        System.out.printf("   " + vetorTempoIndividualTarefa[i][colunap.get(0)[j]]);
                    }
                    System.out.println("\n");
                }

                int[] aux = colunap.get(loop);

                int aux2;
                if (pos < NUMERO_TAREFAS - 1) {
                    colunap.remove(0);
                    aux2 = aux[pos + 1];
                    aux[pos + 1] = aux[pos];
                    aux[pos] = aux2;
                    colunap.add(aux);
                }
                makespanAtual = calcularTempoProcessamentoMatrizMontada(permutacoes);

                System.out.println("\n#######################################################################################################\n ");
                System.out.println("\n Nova Posição Montada: ");
                for (int i = 0; i < aux.length - 1; i++) {
                    System.out.printf("  " + aux[i]);
                }
                pos++;
                if ((loop == NUMERO_TAREFAS - 1)) {
                    colunap.set(loop, colunap.get(loop + 1));
                    colunap.set(loop + 1, aux);
                }

                if (loop == 0) {
                    melhormakespan = makespanAtual;
                    arrayMelhorSequencia.add(aux);
                } else if (makespanAtual < melhormakespan) {
            //        arrayMelhorSequencia.set(0, aux);
                    melhormakespan = makespanAtual;
                }

                vet[1] = vetorOrdenado[posAtual];
                colunap.add(0, vet);

            }
            posAtual++;
            //colunap.add();

        
        System.out.println("\n\n\nMelhor Makespan: " + melhormakespan);
//        System.out.println("\nMelhor PosiçãoAtual: ");
//        for (int i = 0; i < arrayMelhorSequencia.get(0).length-1; i++) {
          //  System.out.printf("  " + arrayMelhorSequencia.get(0)[i]);
//        }
        return melhorSequencia;
    }

    private static int[] ordenarLPT(int[][] vetorTempoTarefa) {
        int k = 0;
        int[] retorno = new int[NUMERO_TAREFAS];
        int resTmp = 0;
        int[][] mediaTemposProcessamentoEPosicao = new int[2][NUMERO_TAREFAS];

        for (int i = 0; i < NUMERO_TAREFAS; i++) {
            for (int j = 0; j < NUMERO_MAQUINAS; j++) {
                resTmp = resTmp + vetorTempoTarefa[j][i];
            }

            mediaTemposProcessamentoEPosicao[0][k] = (resTmp / NUMERO_MAQUINAS);
            mediaTemposProcessamentoEPosicao[1][k] = i;
            resTmp = 0;
            k++;
        }

        System.out.println("\n\n####################################################################################################### ");
        System.out.println("\n\nMedias e Posição das Tarefas\n");
        for (int i = 0; i < NUMERO_TAREFAS; i++) {
            System.out.print("                   " + mediaTemposProcessamentoEPosicao[0][i]);
        }
        System.out.println("\n");
        for (int i = 0; i < NUMERO_TAREFAS; i++) {
            System.out.print("                   " + mediaTemposProcessamentoEPosicao[1][i]);
        }

        System.out.println("\n####################################################################################################### ");
        for (int i = 0; i <= mediaTemposProcessamentoEPosicao.length; i++) {
            for (int j = 0; j <= mediaTemposProcessamentoEPosicao.length; j++) {
                try {
                    if (mediaTemposProcessamentoEPosicao[0][j] < mediaTemposProcessamentoEPosicao[0][j + 1]) {
                        int aux = mediaTemposProcessamentoEPosicao[0][j];
                        int aux2 = mediaTemposProcessamentoEPosicao[1][j];
                        mediaTemposProcessamentoEPosicao[0][j] = mediaTemposProcessamentoEPosicao[0][j + 1];
                        mediaTemposProcessamentoEPosicao[1][j] = mediaTemposProcessamentoEPosicao[1][j + 1];
                        mediaTemposProcessamentoEPosicao[0][j + 1] = aux;
                        mediaTemposProcessamentoEPosicao[1][j + 1] = aux2;
                    }
                } catch (Exception e) {
                }

            }
        }

        System.out.println("\n####################################################################################################### ");
        System.out.println("\nMedias e Posições Ordenadas LPT\n");
        for (int i = 0; i < NUMERO_TAREFAS; i++) {
            System.out.print("                   " + mediaTemposProcessamentoEPosicao[0][i]);
        }
        System.out.println("\n");
        for (int i = 0; i < NUMERO_TAREFAS; i++) {
            System.out.print("                   " + mediaTemposProcessamentoEPosicao[1][i]);
            retorno[i] = mediaTemposProcessamentoEPosicao[1][i];
        }

        System.out.println("\n#######################################################################################################\n\n\n ");
        return retorno;

    }

    // Função de Impressão
    private static void imprimir(int[][] vetorTempoIndividualTarefa, int[][] vetorTempoTotalTarefasMaquinas, String[] nomeMaquinas, String[] nomeTarefas, int[] tempoFinalPorMaquina) {
        int i;
        for (i = 0; i < NUMERO_MAQUINAS; i++) {
            System.out.print("\nMaquina: " + nomeMaquinas[i] + "\n");
            for (int j = 0; j < NUMERO_TAREFAS; j++) {
                System.out.print("   " + nomeTarefas[j] + ":    Tempo Nesta Tarefa: " + vetorTempoIndividualTarefa[i][j] + " == " + vetorTempoTotalTarefasMaquinas[i][j]);
            }
            System.out.print("  Tempo Processamento da Maquina" + tempoFinalPorMaquina[i]);
        }
    }

}
