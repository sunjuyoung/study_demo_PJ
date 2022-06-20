package com.demo.publisher;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AnotherHandler {


    @EventListener
    @Async
    public void handler(MyEvent myEvent){
        System.out.println(Thread.currentThread().toString());
        System.out.println("두번째 핸들러" + myEvent.getData());
    }
}
