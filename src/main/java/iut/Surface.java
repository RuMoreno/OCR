package iut;

import java.io.File;
import ij.process.ImageProcessor;

public class Surface extends OCRAlgo {
    public Surface() {
        super(34);
    }

    @Override
    protected int evaluateImage(File file) {
        ImageProcessor image = preprocessImage(file);

        var vector = surface(image);

        double min = Double.MAX_VALUE;
        String found = null;

        for (File otherFile : listFiles("images")) {
            if (file.getPath().equals(otherFile.getPath())) // On skip soit même
                continue;

            var other = preprocessImage(otherFile);

            int[] otherVector = surface(other);
            var dist = vectorDistance(vector, otherVector);

            if (dist < min) {
                min = dist;
                found = otherFile.getName();
            }
        }

        return Integer.parseInt(found.split("_")[0]);
    }

    private int[] surface(ImageProcessor ip) {
        byte[] pixels = (byte[]) ip.getPixels();
        int[] result = new int[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            result[i] = (pixels[i] & 0xff) == 0 ? 1 : 0;
        }
        return result;
    }
}
