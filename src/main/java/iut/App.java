package iut;

import java.io.File;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

public class App {
    public static void main(String[] args) {
        ///////////////////////////////////////////
        // String path = "images/1_7.png";
        // System.out.println("Le chiffre est : " + findNumber(path));
        ///////////////////////////////////////////

        ///////////////////////////////////////////
        int[][] matrix = createMatrix();
        showMatrix(matrix);
        ///////////////////////////////////////////
    }

    private static int findNumber(String filePath) {
        int CELL_COUNT = 5;

        ImagePlus baseImg = IJ.openImage(filePath);
        new ImageConverter(baseImg).convertToGray8();
        ImageProcessor img = baseImg.getProcessor();
        convertToBlackAndWhite(img);

        int[] vector = createImageVector(img, CELL_COUNT);
        File[] files = listFiles("images");

        double min = Double.MAX_VALUE;
        String found = null;

        for (File imgFile : files) {
            if (filePath.equals(imgFile.getPath()))
                continue;

            ImagePlus other = IJ.openImage(imgFile.getAbsolutePath());
            new ImageConverter(other).convertToGray8();

            ImageProcessor otherProcessor = other.getProcessor();
            convertToBlackAndWhite(otherProcessor);
            int[] otherVector = createImageVector(otherProcessor, CELL_COUNT);

            var dist = vectorDistance(vector, otherVector);

            if (dist < min) {
                min = dist;
                found = imgFile.getName();
            }
        }

        return Integer.parseInt(found.split("_")[0]);
    }

    public static int percentage(int[][] matrix) {
        int pourcentage = 0;
        for (int i = 0; i < matrix.length; i++) {
            pourcentage += matrix[i][i];
        }
        return pourcentage;
    }

    public static int[][] createMatrix() {
        int[][] matrix = new int[10][10];
        for (File file : listFiles("images")) {
            ImagePlus other = IJ.openImage(file.getAbsolutePath());
            new ImageConverter(other).convertToGray8();

            ImageProcessor otherProcessor = other.getProcessor();
            convertToBlackAndWhite(otherProcessor);
            matrix[Integer.parseInt(file.getName().split("_")[0])][findNumber(file.getPath())]++;
        }
        return matrix;
    }

    private static void showMatrix(int[][] matrix) {
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
        message += "Taux de reconnaissance : " + percentage(matrix) + "%\n";
        message += "----------------------------------------------------\n";
        IJ.showMessage(message);
    }


    public static File[] listFiles(String directoryPath) {
        File[] files = null;
        File directoryToScan = new File(directoryPath);
        files = directoryToScan.listFiles();
        return files;
    }

    public static double vectorDistance(int[] v1, int[] v2) {
        int sum = 0;
        for (int i = 0; i < v1.length; i++) {
            // carré de la valeur absolue de la différence des deux vecteurs
            sum += Math.pow(Math.abs((v1[i] - v2[i])), 2);
        }
        return Math.sqrt(sum);
    }

    private static int[] createImageVector(ImageProcessor image, int cellCount) {
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

    private static void convertToBlackAndWhite(ImageProcessor ip) {
        byte[] pixels = (byte[]) ip.getPixels();
        for (int i = 0; i < pixels.length; i++)
            pixels[i] = (pixels[i] & 0xff) < 120 ? (byte) 0 : (byte) 255;
    }

    private static float greyAverage(ImageProcessor ip) {
        byte[] pixels = (byte[]) ip.getPixels();
        float avg = 0;
        for (byte b : pixels)
            avg += b & 0xff;
        return (avg / pixels.length);
    }

}


