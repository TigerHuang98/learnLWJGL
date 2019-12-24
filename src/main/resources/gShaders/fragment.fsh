#version 330

//in vec3 GStoFSColor[];
in GS_OUT{
vec3 color;
} fs_in[1];
out vec4 fragColor;

void main(){
    fragColor=vec4(fs_in[0].color,1.0);
}