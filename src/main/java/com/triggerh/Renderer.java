package com.triggerh;

import com.triggerh.engine.GameItem;
import com.triggerh.engine.Window;
import com.triggerh.engine.graphics.*;
import com.triggerh.utils.ResourceLoader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL33.*;

public class Renderer extends AbstractRender{

    private float specularPower=1f;

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

        shaderProgram.createMaterialUniform("material");

        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createPointLightUniform("pointLight");
    }

    @Override
    public void render(Window window,Camera camera,GameItem[] gameItems,Vector3f ambientLight,PointLight pointLight){
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

        shaderProgram.setUniform("ambientLight", ambientLight);
        shaderProgram.setUniform("specularPower", specularPower);

        PointLight currPointLight = new PointLight(pointLight);
        Vector3f lightPos = currPointLight.getPosition();
        Vector4f aux = new Vector4f(lightPos, 1);
        aux.mul(viewMatrix);
        lightPos.x = aux.x;
        lightPos.y = aux.y;
        lightPos.z = aux.z;
        shaderProgram.setUniform("pointLight", currPointLight);

        for(GameItem gameItem:gameItems){
            Matrix4f worldMatrix=transformation.getModelViewMatrix(gameItem,viewMatrix);
            shaderProgram.setUniform("modelViewMatrix",worldMatrix);
            shaderProgram.setUniform("material", gameItem.getMesh().getMaterial());
            gameItem.getMesh().render();
        }
        shaderProgram.unbind();
    }
}
