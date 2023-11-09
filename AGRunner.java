package principal;

public class AGRunner {
    public static void main(String[] args) {
        int n = 20, e = n * 20 / 100, nRainhas = 16;
        IndividuoFactory factory = new IndividuoNRainhasFactory(n, nRainhas);
        AG ag = new AG();
        Individuo indList = ag.executar(factory, 10000, n, e);
        System.out.println("\nColisões do melhor individuo final " + indList.getAvaliacao());
    }
}