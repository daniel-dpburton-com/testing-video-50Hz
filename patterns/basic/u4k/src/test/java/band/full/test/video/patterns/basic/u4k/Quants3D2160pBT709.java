package band.full.test.video.patterns.basic.u4k;

import static band.full.test.video.encoder.EncoderParameters.UHD4K_MAIN8;
import static band.full.test.video.generator.GeneratorFactory.HEVC;

import band.full.test.video.executor.GenerateVideo;
import band.full.test.video.generator.Quants3DBase;

/**
 * Testing color bands separation / quantization step uniformity.
 *
 * @author Igor Malinin
 */
@GenerateVideo
public class Quants3D2160pBT709 extends Quants3DBase {
    public Quants3D2160pBT709() {
        super(HEVC, UHD4K_MAIN8, "UHD4K/BT709", "U4K");
    }
}
