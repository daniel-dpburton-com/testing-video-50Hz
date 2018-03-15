package band.full.testing.video.itu;

import static band.full.testing.video.itu.ColorRange.FULL;

import band.full.testing.video.color.Matrix3x3;
import band.full.testing.video.color.Primaries;

/**
 * Color Matrix
 *
 * @author Igor Malinin
 */
public abstract class ColorMatrix {
    public final int code;
    public final TransferCharacteristics transfer;
    public final Primaries primaries;
    public final int bitdepth;
    public final ColorRange range;

    public final int VMIN, VMAX; // video data range
    public final int YMIN, YMAX; // luma range
    public final int CMIN, CMAX; // chroma range
    public final int ACHROMATIC;

    public final Matrix3x3 RGBtoXYZ;
    public final Matrix3x3 XYZtoRGB;

    protected ColorMatrix(int code, TransferCharacteristics transfer,
            Primaries primaries, int bitdepth, ColorRange range) {
        if (bitdepth < 8) throw new IllegalArgumentException(
                "bitdepth should be at least 8 but was " + bitdepth);

        this.code = code;
        this.transfer = transfer;
        this.primaries = primaries;
        this.bitdepth = bitdepth;
        this.range = range;

        int shift = bitdepth - 8;

        VMIN = range == FULL ? 0 : 1 << shift;
        VMAX = ((range == FULL ? 256 : 255) << shift) - 1;

        YMIN = range == FULL ? 0 : 16 << shift;
        YMAX = range == FULL ? (256 << shift) - 1 : 235 << shift;

        CMIN = range == FULL ? 1 : 16 << shift;
        CMAX = range == FULL ? (256 << shift) - 1 : 240 << shift;

        ACHROMATIC = 128 << shift;

        RGBtoXYZ = primaries.RGBtoXYZ;
        XYZtoRGB = primaries.XYZtoRGB;
    }

    public abstract double[] fromRGB(double[] rgb, double[] yuv);

    public abstract double[] fromLinearRGB(double[] rgb, double[] yuv);

    public abstract double[] toRGB(double[] yuv, double[] rgb);

    public abstract double[] toLinearRGB(double[] yuv, double[] rgb);

    public double[] fromCodes(double[] yuv, double[] codes) {
        codes[0] = fromLumaCode(yuv[0]);
        codes[1] = fromChromaCode(yuv[1]);
        codes[2] = fromChromaCode(yuv[2]);

        return codes;
    }

    public double[] toCodes(double[] yuv, double[] codes) {
        codes[0] = toLumaCode(yuv[0]);
        codes[1] = toChromaCode(yuv[1]);
        codes[2] = toChromaCode(yuv[2]);

        return codes;
    }

    /**
     * Input is packed 32 bit ARGB (8 bit per component).<br>
     * Output is nonlinear YUV.
     */
    public double[] fromARGB(int argb, double[] yuv) {
        yuv[0] = ((argb >> 16) & 0xff) / 255.0;
        yuv[1] = ((argb >> 8) & 0xff) / 255.0;
        yuv[2] = ((argb) & 0xff) / 255.0;

        return fromRGB(yuv, yuv);
    }

    public boolean isNominal(double[] yuv) {
        double[] rgb = new double[3];
        toRGB(yuv, rgb);
        return isNominal(rgb[0]) && isNominal(rgb[1]) && isNominal(rgb[2]);
    }

    protected boolean isNominal(double val) {
        return val >= 0.0 && val <= 1.0;
    }

    public boolean isNominal(int yCode, int uCode, int vCode) {
        double[] yuv = {yCode, uCode, vCode};
        fromCodes(yuv, yuv);
        return isNominal(yuv);
    }

    public final double toLumaCode(double y) {
        return y * (YMAX - YMIN) + YMIN;
    }

    public final double[] toLumaCode(double[] rgb, double[] dst) {
        for (int i = 0; i < rgb.length; i++) {
            dst[i] = toLumaCode(rgb[i]);
        }

        return dst;
    }

    public final double toChromaCode(double c) {
        return c * (CMAX - CMIN) + ACHROMATIC;
    }

    public final double fromLumaCode(double yCode) {
        return (yCode - YMIN) / (YMAX - YMIN);
    }

    public final double[] fromLumaCode(double[] src, double[] rgb) {
        for (int i = 0; i < rgb.length; i++) {
            rgb[i] = fromLumaCode(src[i]);
        }

        return src;
    }

    public final double fromChromaCode(double cCode) {
        return (cCode - ACHROMATIC) / (CMAX - CMIN);
    }
}
