package com.patrykpalka.portfolio.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AudioPlaybackServiceTest {

    @InjectMocks
    private AudioPlaybackService audioPlaybackService;

    @Mock
    private Clip clip;

    @Mock
    private AudioInputStream audioInputStream;

    private byte[] validAudioData;

    @BeforeEach
    void setUp() {
        // Create sample audio data
        validAudioData = new byte[]{0x01, 0x02, 0x03, 0x04};
    }

    @Test
    void shouldPlayAudioSuccessfully() throws Exception {
        try (MockedStatic<AudioSystem> audioSystem = mockStatic(AudioSystem.class)) {
            // given
            audioSystem.when(() -> AudioSystem.getAudioInputStream(any(ByteArrayInputStream.class)))
                    .thenReturn(audioInputStream);
            audioSystem.when(() -> AudioSystem.getClip())
                    .thenReturn(clip);

            // when
            audioPlaybackService.playAudioWithClip(validAudioData);

            // then
            verify(clip).open(audioInputStream);
            verify(clip).start();
            verify(clip).drain();
        }
    }

    @Test
    void shouldHandleNullAudioData() throws Exception {
        try (MockedStatic<AudioSystem> audioSystem = mockStatic(AudioSystem.class)) {
            // when
            audioPlaybackService.playAudioWithClip(null);

            // then
            audioSystem.verify(() -> AudioSystem.getAudioInputStream(any(ByteArrayInputStream.class)), never());
            verifyNoInteractions(clip);
        }
    }

    @Test
    void shouldHandleEmptyAudioData() throws Exception {
        try (MockedStatic<AudioSystem> audioSystem = mockStatic(AudioSystem.class)) {
            // when
            audioPlaybackService.playAudioWithClip(new byte[0]);

            // then
            audioSystem.verify(() -> AudioSystem.getAudioInputStream(any(ByteArrayInputStream.class)), never());
            verifyNoInteractions(clip);
        }
    }

    @Test
    void shouldHandleUnsupportedAudioFileException() throws Exception {
        try (MockedStatic<AudioSystem> audioSystem = mockStatic(AudioSystem.class)) {
            // given
            audioSystem.when(() -> AudioSystem.getAudioInputStream(any(ByteArrayInputStream.class)))
                    .thenThrow(new UnsupportedAudioFileException("Unsupported audio format"));

            // when
            audioPlaybackService.playAudioWithClip(validAudioData);

            // then
            verifyNoInteractions(clip);
        }
    }

    @Test
    void shouldHandleLineUnavailableException() throws Exception {
        try (MockedStatic<AudioSystem> audioSystem = mockStatic(AudioSystem.class)) {
            // given
            audioSystem.when(() -> AudioSystem.getAudioInputStream(any(ByteArrayInputStream.class)))
                    .thenReturn(audioInputStream);
            audioSystem.when(() -> AudioSystem.getClip())
                    .thenThrow(new LineUnavailableException("Audio line unavailable"));

            // when
            audioPlaybackService.playAudioWithClip(validAudioData);

            // then
            verifyNoInteractions(clip);
        }
    }

    @Test
    void shouldHandleIOException() throws Exception {
        try (MockedStatic<AudioSystem> audioSystem = mockStatic(AudioSystem.class)) {
            // given
            audioSystem.when(() -> AudioSystem.getAudioInputStream(any(ByteArrayInputStream.class)))
                    .thenReturn(audioInputStream);
            audioSystem.when(() -> AudioSystem.getClip())
                    .thenReturn(clip);
            doThrow(new IOException("IO Error")).when(clip).open(any(AudioInputStream.class));

            // when
            audioPlaybackService.playAudioWithClip(validAudioData);

            // then
            verify(clip, never()).start();
            verify(clip, never()).drain();
        }
    }
}
