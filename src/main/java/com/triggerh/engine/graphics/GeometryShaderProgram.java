package com.triggerh.engine.graphics;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL33.GL_GEOMETRY_SHADER;

public class GeometryShaderProgram extends ShaderProgram{
    private int geometryShaderId;

    public GeometryShaderProgram() throws Exception{
    }

    public void createGeometryShader(String shaderCode) throws Exception{
        geometryShaderId=createShader(shaderCode,GL_GEOMETRY_SHADER);
    }

    public void link() throws Exception{
        super.link();
        if(geometryShaderId!=0){
            glDetachShader(programId,geometryShaderId);
        }

        glValidateProgram(programId);
        if(glGetProgrami(programId,GL_VALIDATE_STATUS)==0){
            throw new Exception("Warning validating Shader code: "+glGetProgrami(programId,1024));
        }
    }

}
