package com.patrykpalka.portfolio.service;

import com.patrykpalka.portfolio.exception.AudioDataEmptyException;
import com.patrykpalka.portfolio.exception.AudioPlaybackException;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class AudioPlaybackService {

    public void playAudioWithClip(byte[] audioData) {
        if (audioData == null || audioData.length == 0) {
            throw new AudioDataEmptyException("Audio data is empty. Cannot play audio.");
        }

        try {
            InputStream inputStream = new ByteArrayInputStream(audioData);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.drain();

        } catch (LineUnavailableException e) {
            throw new AudioPlaybackException("Line unavailable for audio playback.", e);
        } catch (UnsupportedAudioFileException e) {
            throw new AudioPlaybackException("Unsupported audio file format.", e);
        } catch (IOException e) {
            throw new AudioPlaybackException("I/O error occurred during audio playback.", e);
        }
    }
}
