package io.hhplus.tdd.point.message;

import org.apache.logging.log4j.message.Message;

public interface MessageHandler {
    void handle(final Message message);
}
