package band.full.testing.video.generate.hevc.u4k;

import static band.full.testing.video.encoder.EncoderParameters.UHD4K_MAIN8;
import static band.full.testing.video.generator.GeneratorFactory.HEVC;

import band.full.testing.video.executor.GenerateVideo;
import band.full.testing.video.generator.BasicSetupBase;

/**
 * @author Igor Malinin
 */
@GenerateVideo
public class BasicSetup2160pBT709 extends BasicSetupBase {
    public BasicSetup2160pBT709() {
        super(HEVC, UHD4K_MAIN8, "UHD4K/BT709/Calibrate/Basic", "U4K");
    }
}