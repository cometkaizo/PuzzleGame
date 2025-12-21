package com.cometkaizo.screen;

import com.cometkaizo.Main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class Sound {
    private static final Set<Sound> PLAYED_THIS_TICK = new HashSet<>();
    protected final AudioFormat format;
    protected final byte[] audio;
    public Sound(InputStream in) {
        try {
            var audioIn = AudioSystem.getAudioInputStream(in);
            this.format = audioIn.getFormat();
            this.audio = audioIn.readAllBytes();
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void play() {
        if (!PLAYED_THIS_TICK.add(this)) return;
        try {
            var clip = AudioSystem.getClip();
            clip.open(format, audio, 0, audio.length);

            trySetVolume(clip, -15);
            clip.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void trySetVolume(Clip clip, float volume) {
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            var gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            // ensure the value is within the control's limits
            float minGain = gainControl.getMinimum();
            float maxGain = gainControl.getMaximum();
            float effectiveGain = Math.min(maxGain, Math.max(minGain, volume));

            // set the volume in decibels
            gainControl.setValue(effectiveGain);
        } else {
            Main.log("Volume control not supported for audio");
        }
    }

    public static void tick() {
        PLAYED_THIS_TICK.clear();
    }
}
