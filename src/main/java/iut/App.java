package iut;

public class App {
    public static void main(String[] args) {

        /////////////////////////////////////////////////////////////////
        new Zoning()
                // .findBestProcessing() // Prend beaucoup de temps
                .evaluate()
                .showMatrix();
        /////////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////////
        // new Profile()
        //         // .findBestProcessing() // Prend beaucoup de temps
        //         .evaluate()
        //         .showMatrix();
        /////////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////////
        // new Surface()
        //         // .findBestProcessing() // Prend beaucoup de temps
        //         .evaluate()
        //         .showMatrix();
        /////////////////////////////////////////////////////////////////




        ////////////////// VISUALISATION CHAINE TRAITEMENT ////////////////
        // var notProcessed  = new ij.ImagePlus("images/1_7.png");
        // notProcessed.show();
        // ij.WindowManager.addWindow(notProcessed.getWindow());

        // var image  = new Surface().preprocessImagePlus(new java.io.File("images/1_7.png"));
        // image.show();
        // ij.WindowManager.addWindow(image.getWindow());
        /////////////////////////////////////////////////////////////////

    }
}
