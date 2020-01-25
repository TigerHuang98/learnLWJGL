#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 inColor;
layout (location=2) in vec3 vertexNormal;

out VS_OUT{
    vec3 color;
    vec3 vertexNormal;
    vec3 mvVertexPosition;
} vs_out;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main(){
    gl_PointSize=10;
    vec4 mvPos=modelViewMatrix*vec4(position,1.0);
    gl_Position=projectionMatrix*mvPos;
    vs_out.color=inColor;
    vs_out.vertexNormal=normalize(modelViewMatrix * vec4(vertexNormal, 0.0)).xyz;
    vs_out.mvVertexPosition=mvPos.xyz;
}