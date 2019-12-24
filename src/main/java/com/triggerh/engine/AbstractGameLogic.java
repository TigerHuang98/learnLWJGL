package com.triggerh.engine;

public abstract class AbstractGameLogic{
    public abstract void init() throws Exception;
    public abstract void input(Window window);
    public abstract void update(float interval,MouseInput mouseInput);
    public abstract void render(Window window);
    public abstract void cleanup();
}
