package com.patrykpalka.portfolio;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SpeakChuckNorrisJokesApplicationTests {

    @Test
    void shouldLogInitializationMessageWhenApplicationStarts() {
        // given
        Logger logger = (Logger) LoggerFactory.getLogger(SpeakChuckNorrisJokesApplication.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        // when
        SpeakChuckNorrisJokesApplication.main(new String[] {});

        // then
        assertThat(listAppender.list)
                .extracting(ILoggingEvent::getMessage)
                .contains("Initializing Speak Chuck Norris Jokes Application");

        assertThat(listAppender.list)
                .extracting(ILoggingEvent::getLevel)
                .contains(Level.INFO);
    }
}
