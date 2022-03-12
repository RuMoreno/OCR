package iut;

import java.io.File;

public class Profile extends OCRAlgo {

    public Profile() {
        super(10); // TODO déterminer la meilleur taille d'image
    }

    @Override
    protected int evaluateImage(File file) {
        // TODO
        return 0;
    }

    @Override
    public OCRAlgo findBestProcessing() {
        // TODO -> force brute le evaluate
        return this;
    }
    
}
