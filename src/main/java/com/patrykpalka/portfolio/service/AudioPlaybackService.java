package com.patrykpalka.portfolio.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class AudioPlaybackService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AudioPlaybackService.class);

    public void playAudioWithClip(byte[] audioData) {
        if (audioData == null || audioData.length == 0) {
            LOGGER.error("Audio data is null or empty");
            return;
        }

        try {
            InputStream inputStream = new ByteArrayInputStream(audioData);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.drain();

        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
