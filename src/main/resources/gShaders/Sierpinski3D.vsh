#version 330

layout(triangles) in ;
layout(triangle_strip, max_vertices = 3) out;

in VS_OUT{
vec3 color;
}gs_in[];
out GS_OUT{
vec3 color;
}gs_out[1];

void main()
{
    gl_Position = gl_in[0].gl_Position;
    gl_PointSize = gl_in[0].gl_PointSize;
    gs_out[0].color=gs_in[0].color;
    EmitVertex();
    gl_Position = gl_in[1].gl_Position;
    gl_PointSize = gl_in[1].gl_PointSize;
    gs_out[0].color=gs_in[1].color;
    EmitVertex();
    gl_Position = gl_in[2].gl_Position;
    gl_PointSize = gl_in[2].gl_PointSize;
    gs_out[0].color=gs_in[2].color;
    EmitVertex();
    EndPrimitive();
}