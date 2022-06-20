package com.demo.aop;

import org.springframework.stereotype.Service;

@Service
public class SimpleEventService implements EventService{

    @PerLogging
    @Override
    public void createEvent() {
        try {
            Thread.sleep(1000);

        }catch (InterruptedException e){

        }
        System.out.println("create event");

    }

    @PerLogging
    @Override
    public void publishEvent() {
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){

        }
        System.out.println("publish event");
    }

    public void deleteEvent(){
        System.out.println("delete event");
    }
}
