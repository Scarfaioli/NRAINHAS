package principal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IndividuoNRainhas extends Individuo {

    private int nRainhas;
    private int[] genes;

    public void setGene(int i, int gene) {
        this.genes[i] = gene;
    }

    public int getGene(int i) {
        return genes[i];
    }

    public int[] getGenes() {
        return genes;
    }

    public int getNRainhas() {
        return nRainhas;
    }

    public IndividuoNRainhas(int nRainhas) {
        this.nRainhas = nRainhas;
        this.genes = new int[nRainhas];
        Random rdn = new Random();
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < this.nRainhas; i++) {
            values.add(i);
        }
        for (int i = 0; i < genes.length; i++) {
            this.genes[i] = values.remove(rdn.nextInt(values.size()));
        }
    }

    /**
     * finalizar metodo
     */
    @Override
    public List<Individuo> recombinar(Individuo p2) {
        Random rd = new Random();
        List<Individuo> filhos = new ArrayList<>();
        Individuo filho1 = new IndividuoNRainhas(nRainhas);
        Individuo filho2 = new IndividuoNRainhas(nRainhas);
        int corteIni, corteFin;

        corteIni = rd.nextInt(nRainhas);
        corteFin = (corteIni + (nRainhas / 3)) % nRainhas;

        if (corteFin < corteIni) {
            int aux = corteFin;
            corteFin = corteIni;
            corteIni = aux;
        }
        for (int i = 0; i < corteIni; i++) {
            filho1.setGene(i, this.getGene(i));
            filho2.setGene(i, p2.getGene(i));
        }
        for (int i = corteIni; i < corteFin; i++) {
            filho2.setGene(i, this.getGene(i));
            filho1.setGene(i, p2.getGene(i));
        }
        for (int i = corteFin; i < nRainhas; i++) {
            filho1.setGene(i, this.getGene(i));
            filho2.setGene(i, p2.getGene(i));
        }

        filhos.add(filho1);
        filhos.add(filho2);
        return filhos;
    }

    @Override
    public Individuo mutar() {
        double chance = 0.15;
        Individuo mutante = new IndividuoNRainhas(nRainhas);
        Random rd = new Random();
        for (int i = 0; i < nRainhas; i++) {
            mutante.setGene(i, this.getGene(i));
        }
        for (int i = 0; i < nRainhas; i++) {
            if (rd.nextDouble() < chance) {
                int pos = rd.nextInt(nRainhas);
                int valor = mutante.getGene(i);
                mutante.setGene(i, mutante.getGene(pos));
                mutante.setGene(pos, valor);
            }
        }
        for (int i = 0; i < mutante.getNRainhas(); i++) {
        }
        return mutante;
    }

    @Override
    public double avaliar() {
        double resultado = 0;

        for (int i = 0; i < genes.length - 1; i++) {
            for (int j = i + 1; j < genes.length; j++) {
                if ((genes[j] == genes[i] + (j - i)) || (genes[j] == genes[i] - (j - i)) || (genes[i] == genes[j])) {
                    resultado++;
                }
            }
        }
        return resultado;
    }

}
