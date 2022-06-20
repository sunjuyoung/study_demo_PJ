package com.demo.publisher;

import org.springframework.context.ApplicationEvent;

public class MyEvent {

    private int data;

    public MyEvent(int data) {
        this.data = data;
    }

    public int getData(){
       return data;
   }

}
