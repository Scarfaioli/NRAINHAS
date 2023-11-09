package principal;

import java.util.ArrayList;
import java.util.List;
//import java.util.Random;
import java.util.Random;

public class AG {
    public Individuo executar(IndividuoFactory factory, int nGer, int nInd, int elitismo) {
        // preencher lista abaixo com n individuos, lista inicial com primeira geração
        List<Individuo> popIni = new ArrayList<>(nInd);

        popIni.addAll(factory.getIndividuos());
        Individuo melhor = null;
        for (int g = 0; g < nGer; g++) {
            // Populacao de filhos e mutantes respectivamente
            List<Individuo> filhos = getFilhos(popIni);
            List<Individuo> mutantes = getMutantes(popIni);

            // Populacao de todos os individuos para a selecao dos mais aptos
            List<Individuo> popTotal = new ArrayList<>();
            popTotal.addAll(popIni);
            popTotal.addAll(filhos);
            popTotal.addAll(mutantes);

            popIni = selecao(popTotal, nInd, elitismo);
            melhor = getMelhor(popIni);
            imprimeIndividuos(popTotal);
            imprimirMelhor(melhor, g);
            if(melhor.getAvaliacao()==0){
                return melhor;
            }else if(isSeturado(popIni)){
                Individuo fixado = popIni.get(0);
                popIni.clear();
                popIni.addAll(factory.getIndividuos());
                popIni.remove(nInd-1);
                popIni.add(fixado);
            }
        }
        return melhor;
    }

    private boolean isSeturado(List<Individuo> popIni) {
        Individuo fixado = popIni.get(0);
        float tam=popIni.size();
        float dif=0;
        for (Individuo individuo : popIni) {
            if(!isIgual(individuo, fixado)){
                dif++;
            }
        }
        if((dif/tam)>0.2){
            return false;
        }
        return true;
    }

    private boolean isIgual(Individuo i1, Individuo i2) {
        for(int i=0; i<i1.getGenes().length; i++){
            if(i1.getGene(i)!=i2.getGene(i)){
                return false;
            }
        }
        return true;
    }

    private void imprimeIndividuos(List<Individuo> pop) {
        for (Individuo individuo : pop) {

            System.out.print("individuo: ");
            for (int i = 0; i < individuo.getGenes().length; i++) {
                System.out.print(individuo.getGene(i) + " | ");
            }
            System.out.println();
        }
    }

    private void imprimirMelhor(Individuo melhor, int g) {
        System.out.print("melhor individuo geração: " + (g + 1) + "  tem " + melhor.getAvaliacao() + " colisões");
        System.out.print(" Melhor individuo: ");
        for (int i = 0; i < melhor.getGenes().length; i++) {
            System.out.print(melhor.getGene(i) + " | ");
        }
        System.out.println();
    }

    private Individuo getMelhor(List<Individuo> popIni) {
        Individuo melhor = popIni.get(0);
        for (Individuo individuo : popIni) {
            if (individuo.getAvaliacao() < melhor.getAvaliacao()) {
                melhor = individuo;
            }
        }
        return melhor;
    }

    private List<Individuo> selecao(List<Individuo> popAux, int nInd, int elitismo) {
        // Criar metodo em duas fases, primeiro realizar o elitismo
        // verificar se o problema é de minimazaçao ou maximizacao e ordenar popaux

        List<Individuo> newPop = new ArrayList<>();

        popAux.addAll(ordena(popAux));
        // individuos mais bem avaliados
        for (int i = 0; i < elitismo; i++) {
            newPop.add(popAux.remove(i));
        }

        // selecionar os n melhores individuos do elitismo do inicio da lista
        /**
         * ===================================================================
         * Roleta viciada
         * ===================================================================
         * minimização:
         * 1-inverter avaliações e somar todas
         * 2-sortear um numero entre 0 e a soma das avaliações
         * 3-Selecionar individuo da roleta percorrendo toda a população
         * 4-remover individuo selecionado para a new pop retornar ao passo 1
         * 
         **/

        while (newPop.size() < nInd) {
            Random rd = new Random();
            double sum = 0;

            for (Individuo individuo : popAux) {
                sum = sum + (1.0 / individuo.getAvaliacao());
            }
            double roleta = rd.nextDouble() * sum;
            for (Individuo individuo : popAux) {
                if (1.0 / individuo.getAvaliacao() > roleta) {
                    newPop.add(popAux.remove(popAux.indexOf(individuo)));
                    break;
                } else {
                    roleta -= (1.0 / individuo.getAvaliacao());
                }
            }
        }
        return newPop;
    }

    private List<Individuo> ordena(List<Individuo> popAux) {
        List<Individuo> ordenado = new ArrayList<>();
        int tam=popAux.size();

        for(int i=0; i<tam; i++){
            ordenado.add(removeMelhor(popAux));    
        }
        return ordenado;
    }

    private Individuo removeMelhor(List<Individuo> popIni) {
        Individuo melhor = popIni.get(0);
        for (Individuo individuo : popIni) {
            if (individuo.getAvaliacao() < melhor.getAvaliacao()) {
                melhor = individuo;
            }
        }

        return popIni.remove(popIni.indexOf(melhor));
    }

    private List<Individuo> getMutantes(List<Individuo> popIni) {
        List<Individuo> listaMut = new ArrayList<>();
        for (int i = 0; i < popIni.size(); i++) {
            listaMut.add(popIni.get(i));
            listaMut.get(i).mutar();
        }
        return listaMut;
    }

    private List<Individuo> getFilhos(List<Individuo> popIni) {
        List<Individuo> listaFilhos = new ArrayList<>();
        List<Individuo> pais = new ArrayList<>();
        pais.addAll(popIni);

        Random rand = new Random();
        for (int i = 0; i < popIni.size() / 2; i++) {
            int r1 = rand.nextInt(pais.size());
            Individuo p1 = pais.remove(r1);
            Individuo p2 = null;
            int j=0;
            do{
            int r2 = rand.nextInt(pais.size());
            p2 = pais.get(r2);
            j++;
            }while(isSimilar(p1, p2) && j<5);

            listaFilhos.addAll(p1.recombinar(p2));
        }
        return listaFilhos;
    }

    private boolean isSimilar(Individuo p1, Individuo p2) {
        double igual = 0;
        for(int i=0; i<p1.getGenes().length; i++){
            if(p1.getGene(i)==p2.getGene(i)){
                igual++;
            }
        }
        if((igual/p1.getGenes().length)> 0.66){
            return true;
        }
        return false;
    }

}
