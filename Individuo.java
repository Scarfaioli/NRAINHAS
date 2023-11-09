package principal;

import java.util.List;

public abstract class Individuo {
    private double avaliacao;
    private boolean avaliado;
    private boolean maximo;

    public abstract List<Individuo> recombinar(Individuo p2);

    public abstract Individuo mutar();

    public abstract double avaliar();

    public abstract void setGene(int i, int gene);

    public abstract int getGene(int i);

    public abstract int[] getGenes();

    public abstract int getNRainhas();

    public final double getAvaliacao() {
        if (!avaliado) {
            avaliacao = avaliar();
        }
        return avaliacao;
    }

    public final boolean isMaximo() {
        return maximo;
    }

}