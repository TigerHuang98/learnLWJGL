package com.triggerh.engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL33.*;

public class ShaderProgram{
    private static final int MAX_INFO_LOG_LENGTH=1024;
    protected final int programId;//TODO:Back to private
    private int vertexShaderId;
    private int fragmentShaderId;
    private final Map<String,Integer> uniforms;

    public ShaderProgram() throws Exception{
        programId=glCreateProgram();
        if(programId==0){
            throw new Exception("Could not create Shader");
        }
        uniforms=new HashMap<>();
    }

    protected int createShader(String shaderCode,int shaderType) throws Exception{
        int shaderId=glCreateShader(shaderType);
        if(shaderId==0){
            throw new Exception("Error creating shader. Type: "+shaderType);
        }
        glShaderSource(shaderId,shaderCode);
        glCompileShader(shaderId);
        if(glGetShaderi(shaderId,GL_COMPILE_STATUS)==0){
            throw new Exception("Error compiling Shader code: "+glGetShaderInfoLog(shaderId,MAX_INFO_LOG_LENGTH));
        }
        glAttachShader(programId,shaderId);
        return shaderId;
    }

    public void createUniform(String uniformName) throws Exception{
        int uniformLocation=glGetUniformLocation(programId,uniformName);
        if(uniformLocation<0){
            throw new Exception("Could not find uniform:"+uniformName);
        }
        uniforms.put(uniformName,uniformLocation);
    }

    public void createPointLightUniform(String uniformName) throws Exception{
        createUniform(uniformName+".colour");
        createUniform(uniformName+".position");
        createUniform(uniformName+".intensity");
        createUniform(uniformName+".att.constant");
        createUniform(uniformName+".att.linear");
        createUniform(uniformName+".att.exponent");
    }

    public void createMaterialUniform(String uniformName) throws Exception{
        createUniform(uniformName+".ambient");
        createUniform(uniformName+".diffuse");
        createUniform(uniformName+".specular");
        createUniform(uniformName+".reflectance");
    }

    public void setUniform(String uniformName,Matrix4f value){
        try(MemoryStack stack=MemoryStack.stackPush()){
            FloatBuffer fb=stack.mallocFloat(16);
            value.get(fb);
            glUniformMatrix4fv(uniforms.get(uniformName),false,fb);
        }
    }

    public void setUniform(String uniformName, float value) {
        glUniform1f(uniforms.get(uniformName), value);
    }


    public void setUniform(String uniformName, Vector3f value) {
        glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }

    public void setUniform(String uniformName, Vector4f value) {
        glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
    }

    public void setUniform(String uniformName,PointLight pointLight){
        setUniform(uniformName+".colour",pointLight.getColor());
        setUniform(uniformName+".position",pointLight.getPosition());
        setUniform(uniformName+".intensity",pointLight.getIntensity());
        PointLight.Attenuation att=pointLight.getAttenuation();
        setUniform(uniformName+".att.constant",att.getConstant());
        setUniform(uniformName+".att.linear",att.getLinear());
        setUniform(uniformName+".att.exponent",att.getExponent());
    }

    public void setUniform(String uniformName,Material material){
        setUniform(uniformName+".ambient",material.getAmbientColour());
        setUniform(uniformName+".diffuse",material.getDiffuseColour());
        setUniform(uniformName+".specular",material.getSpecularColour());
        setUniform(uniformName+".reflectance",material.getReflectance());
    }

    public void createVertexShader(String shaderCode) throws Exception{
        vertexShaderId=createShader(shaderCode,GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception{
        fragmentShaderId=createShader(shaderCode,GL_FRAGMENT_SHADER);
    }

    public void bind(){
        glUseProgram(programId);
    }

    public void unbind(){
        glUseProgram(0);
    }

    public void link() throws Exception{
        glLinkProgram(programId);
        if(glGetProgrami(programId,GL_LINK_STATUS)==0){
            throw new Exception("Error linking Shader code: "+glGetProgrami(programId,MAX_INFO_LOG_LENGTH)+"\nProgram info log"+glGetProgramInfoLog(programId,MAX_INFO_LOG_LENGTH));
        }
        if(vertexShaderId!=0){
            glDetachShader(programId,vertexShaderId);
        }
        if(fragmentShaderId!=0){
            glDetachShader(programId,fragmentShaderId);
        }

        glValidateProgram(programId);
        if(glGetProgrami(programId,GL_VALIDATE_STATUS)==0){
            throw new Exception("Warning validating Shader code: "+glGetProgrami(programId,MAX_INFO_LOG_LENGTH)+"\nProgram info log"+glGetProgramInfoLog(programId,MAX_INFO_LOG_LENGTH));
        }
    }

    public void cleanup(){
        unbind();
        if(programId!=0){
            glDeleteProgram(programId);
        }
    }
}
