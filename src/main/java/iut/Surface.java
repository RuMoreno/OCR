package iut;

import ij.process.ImageProcessor;

public class Surface extends OCRAlgo {
    public Surface() {
        super(34);
    }

    @Override
    protected int[] getVector(ImageProcessor image) {
        byte[] pixels = (byte[]) image.getPixels();
        int[] result = new int[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            result[i] = (pixels[i] & 0xff) == 0 ? 1 : 0;
        }
        return result;
    }
}
