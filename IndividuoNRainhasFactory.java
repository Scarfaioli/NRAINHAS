package principal;

import java.util.ArrayList;
import java.util.List;

public class IndividuoNRainhasFactory implements IndividuoFactory {

    private int nIndividuos, nRainhas;

    public IndividuoNRainhasFactory(int nIndividuos, int nRainhas) {
        this.nIndividuos = nIndividuos;
        this.nRainhas = nRainhas;
    }

    @Override
    public List<Individuo> getIndividuos() {
        List<Individuo> individuos = new ArrayList<>();
        for (int i = 0; i < nIndividuos; i++) {
            individuos.add(new IndividuoNRainhas(this.nRainhas));
        }
        return individuos;
    }

}