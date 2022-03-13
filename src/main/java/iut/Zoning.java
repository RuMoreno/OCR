package iut;

import ij.process.ImageProcessor;

public class Zoning extends OCRAlgo {
    
    private final static int CELL_COUNT = 5;
    
    public Zoning() {
        super(36);
    }

    @Override
    protected int[] getVector(ImageProcessor image) {
        int[] vector = new int[CELL_COUNT * CELL_COUNT];
        int cellHeight = image.getHeight() / CELL_COUNT;
        int cellWidth = image.getWidth() / CELL_COUNT;
        int compteur = 0;
        for (int a = 0; a < CELL_COUNT; a++) {
            for (int b = 0; b < CELL_COUNT; b++) {
                for (int i = a * cellHeight; i < a * cellHeight + cellHeight; i++) {
                    for (int j = b * cellWidth; j < b * cellWidth + cellWidth; j++) {
                        if (image.getPixelValue(j, i) == 0.0f) {
                            vector[compteur]++;
                        }
                    }
                }
                compteur++;
            }
        }
        return vector;
    }
}
