package com.triggerh.engine;

import com.triggerh.utils.LoopTimer;

public class GameEngine implements Runnable{
    public final int target_FPS;
    public final int target_UPS;
    private final Window window;
    private final LoopTimer loopTimer;
    private final AbstractGameLogic gameLogic;
    private final MouseInput mouseInput;

    public GameEngine(int target_FPS,int target_UPS,String windowTitle,int width,int height,boolean vSync,AbstractGameLogic gameLogic){
        this.target_FPS=target_FPS;
        this.target_UPS=target_UPS;
        this.window=new Window(windowTitle,width,height,vSync);
        this.loopTimer=new LoopTimer();
        this.gameLogic=gameLogic;
        this.mouseInput=new MouseInput();
    }

    protected void init() throws Exception{
        window.init();
        gameLogic.init();
        mouseInput.init(window);
    }

    @Override
    public void run(){
        try{
            init();
            gameLoop();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            cleanUp();
        }
    }

    protected void gameLoop(){
        float elapsedTime;
        float accumulator=0f;
        float interval=1f/target_UPS;
        while(!window.windowShouldClose()){
            elapsedTime=loopTimer.getElapsedTime();
            accumulator+=elapsedTime;

            mouseInput.input(window);
            gameLogic.input(window);

            while(accumulator>=interval){
                gameLogic.update(interval,mouseInput);
                accumulator-=interval;
            }

            gameLogic.render(window);
            window.update();

            if(!window.isvSync()){
                float loopSlot=1f/target_FPS;
                double endTime=loopTimer.getLastFetchTime()+loopSlot;
                while(loopTimer.getTime()<endTime){
                    try{
                        Thread.sleep(1);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    protected void cleanUp(){
        gameLogic.cleanup();
    }
}
