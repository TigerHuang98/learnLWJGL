package com.triggerh.engine.graphics;

import com.triggerh.engine.GameItem;
import com.triggerh.engine.Window;

import static org.lwjgl.opengl.GL33.*;

public abstract class AbstractRender{
    protected float FOV;
    protected float Z_NEAR;
    protected float Z_FAR;
    protected final Transformation transformation;
    protected ShaderProgram shaderProgram;

    protected AbstractRender(){
        transformation=new Transformation();
    }

    abstract public void init() throws Exception;
    abstract public void render(Window window,Camera camera,GameItem[] gameItems);

    public void clear(){
        glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup(){
        if(shaderProgram!=null){
            shaderProgram.cleanup();
        }
    }
}
