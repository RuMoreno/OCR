package iut;

import ij.process.ImageProcessor;

public class Profile extends OCRAlgo {

    public Profile() {
        super(27);
    }

    @Override
    protected int[] getVector(ImageProcessor image) {
        int[] result = new int[image.getWidth() + image.getHeight()];

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // si le pixel est noir (=0) alors on ajoute 1 au resultat de cette ligne
                if ((image.getPixel(j, i) & 0xff) == 0)
                    result[i]++;
            }
        }

        for (int j = 0; j < image.getWidth(); j++) {
            for (int i = 0; i < image.getHeight(); i++) {
                if ((image.getPixel(i, j) & 0xff) == 0)
                    result[image.getWidth() + i]++;
            }
        }

        return result;
    }

}
