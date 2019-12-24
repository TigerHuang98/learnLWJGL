package com.triggerh;

import com.triggerh.engine.GameEngine;

public class Main{
    public static void main(String[] args){
        try {
            boolean vSync = true;
            GameEngine gameEng = new GameEngine(75,30,"GAME",1280,720,vSync,new GameLogic());
            gameEng.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
