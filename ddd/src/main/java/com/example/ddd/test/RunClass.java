package com.example.ddd.test;

public class RunClass {

    private Ainterface ainterface;

    public RunClass(Ainterface ainterface) {
        this.ainterface = ainterface;
    }

    public void gogo(){
        ainterface.test();
    }
}
