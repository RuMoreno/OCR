package iut;

import java.io.File;
import ij.process.ImageProcessor;

public class Zonning extends OCRAlgo {
    private final static int CELL_COUNT = 5;

    @Override
    protected int evaluateImage(File file) {
        var image = preprocessImage(file);

        int[] vector = createImageVector(image, CELL_COUNT);

        double min = Double.MAX_VALUE;
        String found = null;

        for (File otherFile : listFiles("images")) {
            if (file.getPath().equals(otherFile.getPath())) // On skip soit même
                continue;

            var other = preprocessImage(otherFile);

            int[] otherVector = createImageVector(other, CELL_COUNT);
            var dist = vectorDistance(vector, otherVector);

            if (dist < min) {
                min = dist;
                found = otherFile.getName();
            }
        }

        return Integer.parseInt(found.split("_")[0]);
    }

    @Override
    public OCRAlgo findBestProcessing() {
        int best = 15;
        int lastRate = -1;
        for (int i = best; i < 60; i++) {
            System.out.println("Testing img size : " + i);
            imageSize = i;
            evaluate();
            var rate = matrixRecognitionRate();
            if (rate > lastRate) {
                lastRate = rate;
                best = i;
            }
        }
        imageSize = best;
        System.out.println("Best image size = " + best);
        return this;
    }

    private int[] createImageVector(ImageProcessor image, int cellCount) {
        int[] vector = new int[cellCount * cellCount];
        int cellHeight = image.getHeight() / cellCount;
        int cellWidth = image.getWidth() / cellCount;
        int compteur = 0;
        for (int a = 0; a < cellCount; a++) {
            for (int b = 0; b < cellCount; b++) {
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

    private double vectorDistance(int[] v1, int[] v2) {
        int sum = 0;
        for (int i = 0; i < v1.length; i++) {
            // carré de la valeur absolue de la différence des deux vecteurs
            sum += Math.pow(Math.abs((v1[i] - v2[i])), 2);
        }
        return Math.sqrt(sum);
    }
}
