package iut;

import java.io.File;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

public abstract class OCRAlgo {

    protected Integer imageSize;
    
    protected int[][] matrix;
    
    public OCRAlgo(int imageSize) {
        this.imageSize = imageSize;
    }

    protected abstract int evaluateImage(File file);

    /**
     * Permet de trouver les meilleurs réglages de préprocessing pour l'image
     * @return cet instance de l'algo
     */
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

    /**
     * Permet d'évaluer l'algo à partir du jeu de test d'images
     * @return
     */
    public OCRAlgo evaluate() {
        this.matrix = new int[10][10];
        File[] files = listFiles("images");
        for (File file : files) {
            var result = evaluateImage(file);
            var key = Integer.parseInt(file.getName().split("_")[0]);

            System.out.println(file.getName() + " -> Result : " + result + " | Expected : " + key);
            matrix[key][result]++;
        }
        return this;
    }

    protected ImageProcessor preprocessImage(File file) {
        return preprocessImagePlus(file).getProcessor();
    }
    
    public ImagePlus preprocessImagePlus(File file) {
        ImagePlus baseImg = IJ.openImage(file.getPath());

        // Conversion en niveaux de gris
        new ImageConverter(baseImg).convertToGray8();

        // Conversion en noir et blanc
        convertToBlackAndWhite(baseImg.getProcessor());

        baseImg.setProcessor(baseImg.getProcessor().resize(imageSize, imageSize));
        return baseImg;
    }

    protected void convertToBlackAndWhite(ImageProcessor ip) {
        byte[] pixels = (byte[]) ip.getPixels();
        for (int i = 0; i < pixels.length; i++)
            pixels[i] = (pixels[i] & 0xff) < 120 ? (byte) 0 : (byte) 255;
    }

    protected double vectorDistance(int[] v1, int[] v2) {
        int sum = 0;
        for (int i = 0; i < v1.length; i++) {
            // carré de la valeur absolue de la différence des deux vecteurs
            sum += Math.pow(Math.abs((v1[i] - v2[i])), 2);
        }
        return Math.sqrt(sum);
    }

    protected int matrixRecognitionRate() {
        int rate = 0;
        for (int i = 0; i < matrix.length; i++) {
            rate += matrix[i][i];
        }
        return rate;
    }

    /**
     * Affiche la dernière évaluation de l'algo
     */
    protected void showMatrix() {
        String message = "\n     0    1    2    3    4    5    6    7    8    9\n"
                + "----------------------------------------------------\n";
        for (int i = 0; i < matrix.length; i++) {
            message += i + " | ";
            for (int j = 0; j < matrix.length; j++) {
                if (j == 0)
                    message += matrix[i][j] > 9 ? matrix[i][j] : " " + matrix[i][j];
                else
                    message += matrix[i][j] > 9 ? "   " + matrix[i][j] : "    " + matrix[i][j];
            }
            message += "\n";
        }

        message += "----------------------------------------------------\n";
        message += "Taux de reconnaissance : " + matrixRecognitionRate() + "%\n";
        message += "----------------------------------------------------\n";
        IJ.showMessage(message);
    }

    protected File[] listFiles(String directoryPath) {
        File[] files = null;
        File directoryToScan = new File(directoryPath);
        files = directoryToScan.listFiles();
        return files;
    }
}
