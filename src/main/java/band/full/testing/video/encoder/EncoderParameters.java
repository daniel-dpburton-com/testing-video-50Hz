package band.full.testing.video.encoder;

import static band.full.testing.video.core.Framerate.FPS_23_976;
import static band.full.testing.video.core.Resolution.STD_1080p;
import static band.full.testing.video.core.Resolution.STD_2160p;
import static band.full.testing.video.core.Resolution.STD_720p;
import static band.full.testing.video.encoder.Preset.SLOW;
import static band.full.testing.video.itu.BT2020.BT2020_10bit;
import static band.full.testing.video.itu.BT709.BT709_8bit;
import static band.full.testing.video.smpte.ST2084.PQ;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import band.full.testing.video.core.Framerate;
import band.full.testing.video.core.Resolution;
import band.full.testing.video.itu.BT2020;
import band.full.testing.video.itu.BT709;
import band.full.testing.video.itu.TransferCharacteristics;
import band.full.testing.video.itu.YCbCr;

import java.util.List;
import java.util.Optional;

public class EncoderParameters {
    public static final String MASTER_DISPLAY_PRIMARIES =
            "G(13250,34500)B(7500,3000)R(34000,16000)WP(15635,16450)";

    public static final String MASTER_DISPLAY =
            MASTER_DISPLAY_PRIMARIES + "L(10000000,5)";

    public static final EncoderParameters HD_MAIN = new EncoderParameters(
            STD_720p, BT709.TRANSFER, BT709_8bit, FPS_23_976);

    public static final EncoderParameters FULLHD_MAIN8 = new EncoderParameters(
            STD_1080p, BT709.TRANSFER, BT709_8bit, FPS_23_976);

    public static final EncoderParameters UHD4K_MAIN8 = new EncoderParameters(
            STD_2160p, BT709.TRANSFER, BT709_8bit, FPS_23_976);

    public static final EncoderParameters UHD4K_MAIN10 = new EncoderParameters(
            STD_2160p, BT2020.TRANSFER_10bit, BT2020_10bit, FPS_23_976);

    public static final EncoderParameters HDR10 = new EncoderParameters(
            STD_2160p, PQ, BT2020_10bit, FPS_23_976)
                    .withMasterDisplay(MASTER_DISPLAY);

    public final Resolution resolution;
    public final TransferCharacteristics transfer;
    public final YCbCr matrix;
    public final Framerate framerate;
    public final Preset preset;
    public final Optional<String> masterDisplay; // HEVC HDR only
    public final List<String> encoderOptions;
    public final List<String> muxerOptions;

    public EncoderParameters(Resolution resolution,
            TransferCharacteristics transfer, YCbCr matrix,
            Framerate framerate) {
        this(resolution, transfer, matrix, framerate, SLOW);
    }

    public EncoderParameters(Resolution resolution,
            TransferCharacteristics transfer, YCbCr matrix,
            Framerate framerate, Preset preset) {
        this(resolution, transfer, matrix, framerate, preset, empty(),
                emptyList(), emptyList());
    }

    private EncoderParameters(Resolution resolution,
            TransferCharacteristics transfer, YCbCr matrix,
            Framerate framerate, Preset preset,
            Optional<String> masterDisplay,
            List<String> encoderOptions, List<String> muxerOptions) {
        this.resolution = resolution;
        this.transfer = transfer;
        this.matrix = matrix;
        this.framerate = framerate;
        this.preset = preset;
        this.masterDisplay = masterDisplay;
        this.encoderOptions = encoderOptions;
        this.muxerOptions = muxerOptions;
    }

    public EncoderParameters withFramerate(Framerate framerate) {
        return new EncoderParameters(resolution, transfer, matrix,
                framerate, preset, masterDisplay, encoderOptions, muxerOptions);
    }

    public EncoderParameters withPreset(Preset preset) {
        return new EncoderParameters(resolution, transfer, matrix,
                framerate, preset, masterDisplay, encoderOptions, muxerOptions);
    }

    public EncoderParameters withMasterDisplay(String masterDisplay) {
        return new EncoderParameters(resolution, transfer, matrix,
                framerate, preset, ofNullable(masterDisplay),
                encoderOptions, muxerOptions);
    }

    public EncoderParameters withEncoderOptions(String... options) {
        return withEncoderOptions(asList(options));
    }

    public EncoderParameters withEncoderOptions(List<String> options) {
        return new EncoderParameters(resolution, transfer, matrix,
                framerate, preset, masterDisplay, options, muxerOptions);
    }

    public EncoderParameters withFfmpegOptions(String... options) {
        return withFfmpegOptions(asList(options));
    }

    public EncoderParameters withFfmpegOptions(List<String> options) {
        return new EncoderParameters(resolution, transfer, matrix,
                framerate, preset, masterDisplay, encoderOptions, options);
    }
}
