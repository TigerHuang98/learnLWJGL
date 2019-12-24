#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 inColor;

out VS_OUT{
    vec3 color;
} vs_out;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main(){
    gl_PointSize=10;
    gl_Position=projectionMatrix*modelViewMatrix*vec4(position,1.0);
    vs_out.color=inColor;
}