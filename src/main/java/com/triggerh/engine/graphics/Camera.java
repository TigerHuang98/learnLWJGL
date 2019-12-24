package com.triggerh.engine.graphics;

import org.joml.Vector3f;

public class Camera{
    private final Vector3f position;
    private final Vector3f rotation;

    public Camera(){
        position=new Vector3f(0,0,0);
        rotation=new Vector3f(0,0,0);
    }

    public Camera(Vector3f position,Vector3f rotation){
        this.position=position;
        this.rotation=rotation;
    }

    public Vector3f getPosition(){
        return position;
    }

    public void setPosition(float x,float y,float z){
        position.x=x;
        position.y=y;
        position.z=z;
    }

    public void movePosition(float x,float y,float z){
        if(z!=0){
            position.x-=Math.sin(Math.toRadians(rotation.y))*z;
            position.z+=Math.cos(Math.toRadians(rotation.y))*z;
        }
        if(x!=0){
            position.x-=Math.sin(Math.toRadians(rotation.y-90))*x;
            position.z+=Math.cos(Math.toRadians(rotation.y-90))*x;
        }
        position.y+=y;
    }

    public Vector3f getRotation(){
        return rotation;
    }

    public void setRotation(float x,float y,float z){
        rotation.x=x;
        rotation.y=y;
        rotation.z=z;
    }

    public void moveRotation(float x,float y,float z){
        rotation.x+=x;
        rotation.y+=y;
        rotation.z+=z;
    }
}
