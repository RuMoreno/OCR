package iut;

public class App {
    public static void main(String[] args) {
        new Zoning()
                .findBestProcessing() // Prend beaucoup de temps
                .evaluate()
                .showMatrix();
    }
}
