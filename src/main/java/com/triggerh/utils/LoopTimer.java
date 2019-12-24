package com.triggerh.utils;

public class LoopTimer{
    private double lastFetchTime;

    public LoopTimer(){
        reset();
    }

    public void reset(){
        lastFetchTime=getTime();
    }

    public double getTime(){
        return System.nanoTime()/1e9;
    }

    public double getLastFetchTime(){
        return lastFetchTime;
    }

    public float getElapsedTime(){
        double time=getTime();
        double elapsedTime=time-lastFetchTime;
        lastFetchTime=time;
        return (float) elapsedTime;
    }
}
