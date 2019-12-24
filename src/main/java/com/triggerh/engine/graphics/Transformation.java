package com.triggerh.engine.graphics;

import com.triggerh.engine.GameItem;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation{
    private final Matrix4f projectionMatrix;
    private final Matrix4f modelViewMatrix;
    private final Matrix4f viewMatrix;

    public Transformation(){
        projectionMatrix=new Matrix4f();
        modelViewMatrix=new Matrix4f();
        viewMatrix=new Matrix4f();
    }

    public Matrix4f getProjectionMatrix(float fov,float width,float height,float zNear,float zFar){
        projectionMatrix.identity();
        projectionMatrix.perspective(fov,width/height,zNear,zFar);
        return projectionMatrix;
    }

    public Matrix4f getViewMatrix(Camera camera){
        Vector3f cameraPos=camera.getPosition();
        Vector3f cameraRot=camera.getRotation();

        viewMatrix.identity();
        viewMatrix.rotate((float)Math.toRadians(cameraRot.x),new Vector3f(1,0,0))
                .rotate((float)Math.toRadians(cameraRot.y),new Vector3f(0,1,0));
        viewMatrix.translate(-cameraPos.x,-cameraPos.y,-cameraPos.z);
        return viewMatrix;
    }

    public Matrix4f getModelViewMatrix(GameItem gameItem,Matrix4f viewMatrix){
        Vector3f rotation=gameItem.getRotation();
        modelViewMatrix.identity().
                translate(gameItem.getPosition()).
                rotateX((float)Math.toRadians(-rotation.x)).
                rotateY((float)Math.toRadians(-rotation.y)).
                rotateZ((float)Math.toRadians(-rotation.z)).
                scale(gameItem.getScale())
        ;
        Matrix4f viewBackup=new Matrix4f(viewMatrix);
        return viewBackup.mul(modelViewMatrix);
    }
}
