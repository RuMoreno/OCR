package iut;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

public class App {
    public static void main(String[] args) {
        // Chargment d’ une image dans un objet
        ImagePlus image = IJ.openImage("images/0_1.png");

        // conversion de l’ image en NdG
        new ImageConverter(image).convertToGray8();

        // Récupération des NdG
        ImageProcessor ip = image.getProcessor();

        // On passe l'image en noir et blanc
        convertToBlackAndWhite(ip);

        // On récupère le niveau de gris moyen de l'image
        System.out.println(greyAverage(ip));
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