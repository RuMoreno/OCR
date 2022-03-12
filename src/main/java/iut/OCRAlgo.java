package iut;

import java.io.File;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

public abstract class OCRAlgo {

    protected int imageSize = 36;

    protected int[][] matrix;

    protected abstract int evaluateImage(File file);

    /**
     * Permet de trouver les meilleurs réglages de préprocessing pour l'image
     * @return cet instance de l'algo
     */
    public abstract OCRAlgo findBestProcessing();

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
        ImagePlus baseImg = IJ.openImage(file.getPath());

        // Conversion en niveaux de gris
        new ImageConverter(baseImg).convertToGray8();

        // Conversion en noir et blanc
        ImageProcessor img = baseImg.getProcessor();
        convertToBlackAndWhite(img);

        return img.resize(imageSize, imageSize);
    }

    protected void convertToBlackAndWhite(ImageProcessor ip) {
        byte[] pixels = (byte[]) ip.getPixels();
        for (int i = 0; i < pixels.length; i++)
            pixels[i] = (pixels[i] & 0xff) < 120 ? (byte) 0 : (byte) 255;
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
