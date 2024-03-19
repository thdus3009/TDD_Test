package io.hhplus.tdd.point.message;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.logging.log4j.message.Message;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;

@Component
@ToString
@RequiredArgsConstructor
// 메세지가 담길 메세지큐
public class MessageQ {
    // Thread-safe하기 위해 ConcurrentLinkedQueue 사용
    private final Queue<Message> queue = new ConcurrentLinkedQueue<>();

    public void add(Message noti){
        queue.add(noti);
    }

    public Message poll(){
        return queue.poll();
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }

    public int size(){ return queue.size(); }

}
