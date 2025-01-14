package com.patrykpalka.portfolio.service;

import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class AudioPlaybackService {

    public void playAudioWithClip(byte[] audioData) {
        if (audioData == null || audioData.length == 0) {
            System.out.println("Audio data is null or empty");
            return;
        }

        try {
            InputStream inputStream = new ByteArrayInputStream(audioData);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.drain();

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (UnsupportedAudioFileException e) {
            System.out.println("UnsupportedAudioFileException: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.out.println("LineUnavailableException: " + e.getMessage());
        }
    }
}
