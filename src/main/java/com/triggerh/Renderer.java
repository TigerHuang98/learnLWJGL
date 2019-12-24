package com.triggerh;

import com.triggerh.engine.GameItem;
import com.triggerh.engine.Window;
import com.triggerh.engine.graphics.*;
import com.triggerh.utils.ResourceLoader;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL33.*;

public class Renderer extends AbstractRender{

    @Override
    public void init() throws Exception{

        FOV=(float) Math.toRadians(60.0f);
        Z_NEAR=0.01f;
        Z_FAR=1000.0f;

//        shaderProgram=new ShaderProgram();
//        shaderProgram.createVertexShader(ResourceLoader.loadResource("/vertex.vsh"));
//        shaderProgram.createFragmentShader(ResourceLoader.loadResource("/fragment.fsh"));
        shaderProgram=new GeometryShaderProgram();
        shaderProgram.createVertexShader(ResourceLoader.loadResource("/gShaders/vertex.vsh"));
        shaderProgram.createFragmentShader(ResourceLoader.loadResource("/gShaders/fragment.fsh"));

        if(shaderProgram instanceof GeometryShaderProgram){
            ((GeometryShaderProgram) shaderProgram).createGeometryShader(ResourceLoader.loadResource("/gShaders/Sierpinski3D.vsh"));
        }

        shaderProgram.link();

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
    }

    @Override
    public void render(Window window,Camera camera,GameItem[] gameItems){
        FOV=90;
        clear();
        if(window.isResized()){
            glViewport(0,0,window.getWidth(),window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        Matrix4f projectionMatrix=transformation.getProjectionMatrix(FOV,window.getWidth(),window.getHeight(),Z_NEAR,Z_FAR);
        shaderProgram.setUniform("projectionMatrix",projectionMatrix);

        Matrix4f viewMatrix=transformation.getViewMatrix(camera);

        for(GameItem gameItem:gameItems){
            Matrix4f worldMatrix=transformation.getModelViewMatrix(gameItem,viewMatrix);
            shaderProgram.setUniform("modelViewMatrix",worldMatrix);
            gameItem.getMesh().render();
        }
        shaderProgram.unbind();
    }
}
