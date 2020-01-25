package com.triggerh.engine.graphics;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;


import static org.lwjgl.opengl.GL33.*;

public class Mesh{
    private final int vaoId;
    private final int positionVboId;
    private final int colorVboId;
    private final int indexVboId;
    private final int normalVboId;
    private final int vertexCount;
    private Material material;

    public Mesh(float[] positions,float[]colors,float[] normals,int[] indices){
        FloatBuffer positionVBO=null;
        FloatBuffer colorVBO=null;
        FloatBuffer vecNormalVBO=null;
        IntBuffer indexVBO=null;

        try{
            vertexCount=indices.length;

            vaoId=glGenVertexArrays();
            glBindVertexArray(vaoId);

            positionVboId=glGenBuffers();
            positionVBO=MemoryUtil.memAllocFloat(positions.length);
            positionVBO.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER,positionVboId);
            glBufferData(GL_ARRAY_BUFFER,positionVBO,GL_STATIC_DRAW);
            glVertexAttribPointer(0,3,GL_FLOAT,false,0,0);

            colorVboId=glGenBuffers();
            colorVBO=MemoryUtil.memAllocFloat(colors.length);
            colorVBO.put(colors).flip();
            glBindBuffer(GL_ARRAY_BUFFER,colorVboId);
            glBufferData(GL_ARRAY_BUFFER,colorVBO,GL_STATIC_DRAW);
            glVertexAttribPointer(1,3,GL_FLOAT,false,0,0);

            normalVboId=glGenBuffers();
            vecNormalVBO=MemoryUtil.memAllocFloat(normals.length);
            vecNormalVBO.put(normals).flip();
            glBindBuffer(GL_ARRAY_BUFFER,normalVboId);
            glBufferData(GL_ARRAY_BUFFER,vecNormalVBO,GL_STATIC_DRAW);
            glVertexAttribPointer(2,3,GL_FLOAT,false,0,0);

            indexVboId=glGenBuffers();
            indexVBO=MemoryUtil.memAllocInt(indices.length);
            indexVBO.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,indexVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER,indexVBO,GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER,0);
            glBindVertexArray(0);
        }finally{
            if(positionVBO!=null){
                MemoryUtil.memFree(positionVBO);
            }
            if(colorVBO!=null){
                MemoryUtil.memFree(colorVBO);
            }
            if(vecNormalVBO!=null){
                MemoryUtil.memFree(vecNormalVBO);
            }
            if(indexVBO!=null){
                MemoryUtil.memFree(indexVBO);
            }
        }
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void render(){
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glDrawElements(GL_LINES_ADJACENCY,getVertexCount(),GL_UNSIGNED_INT,0);
//        glDrawElements(GL_POINTS,getVertexCount(),GL_UNSIGNED_INT,0);
//        glDrawArrays(GL_LINES,0,2);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    public void cleanup(){
        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER,0);
        glDeleteBuffers(positionVboId);
        glDeleteBuffers(colorVboId);
        glDeleteBuffers(normalVboId);
        glDeleteBuffers(indexVboId);

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public int getVaoId(){
        return vaoId;
    }

    public int getVertexCount(){
        return vertexCount;
    }
}
