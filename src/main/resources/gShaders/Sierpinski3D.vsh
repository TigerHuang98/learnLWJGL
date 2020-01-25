#version 330

layout(lines_adjacency) in ;
layout(triangle_strip, max_vertices=192) out;

in VS_OUT{
    vec3 color;
    vec3 vertexNormal;
    vec3 mvVertexPosition;
}gs_in[];

out GS_OUT{
    vec3 color;
    vec3 modelViewVertexNormal;
    vec3 modelViewVertexposition;
}gs_out[1];

struct StackFrame{
    vec4 p0;vec4 p1;vec4 p2;vec4 p3;vec3 c0;vec3 c1;vec3 c2;vec3 c3;vec3 n0;vec3 n1;vec3 n2;vec3 n3;vec3 mvP0;vec3 mvP1;vec3 mvP2;vec3 mvP3;int level;int branch_to_go;
};

void tetrahedron(vec4 p0,vec4 p1,vec4 p2,vec4 p3,vec3 c0,vec3 c1,vec3 c2,vec3 c3,vec3 n0,vec3 n1,vec3 n2,vec3 n3,vec3 mvP0,vec3 mvP1,vec3 mvP2,vec3 mvP3){
    gl_Position=p0;
    gs_out[0].color=c0;
    gs_out[0].modelViewVertexNormal=n0;
    gs_out[0].modelViewVertexposition=mvP0;
    EmitVertex();
    gl_Position=p2;
    gs_out[0].color=c2;
    gs_out[0].modelViewVertexNormal=n0;
    gs_out[0].modelViewVertexposition=mvP2;
    EmitVertex();
    gl_Position=p1;
    gs_out[0].color=c1;
    gs_out[0].modelViewVertexNormal=n0;
    gs_out[0].modelViewVertexposition=mvP1;
    EmitVertex();
    EndPrimitive();

    gl_Position=p0;
    gs_out[0].color=c0;
    gs_out[0].modelViewVertexNormal=n1;
    gs_out[0].modelViewVertexposition=mvP0;
    EmitVertex();
    gl_Position=p1;
    gs_out[0].color=c1;
    gs_out[0].modelViewVertexNormal=n1;
    gs_out[0].modelViewVertexposition=mvP1;
    EmitVertex();
    gl_Position=p3;
    gs_out[0].color=c3;
    gs_out[0].modelViewVertexNormal=n1;
    gs_out[0].modelViewVertexposition=mvP3;
    EmitVertex();
    EndPrimitive();

    gl_Position=p1;
    gs_out[0].color=c1;
    gs_out[0].modelViewVertexNormal=n2;
    gs_out[0].modelViewVertexposition=mvP1;
    EmitVertex();
    gl_Position=p2;
    gs_out[0].color=c2;
    gs_out[0].modelViewVertexNormal=n2;
    gs_out[0].modelViewVertexposition=mvP2;
    EmitVertex();
    gl_Position=p3;
    gs_out[0].color=c3;
    gs_out[0].modelViewVertexNormal=n2;
    gs_out[0].modelViewVertexposition=mvP3;
    EmitVertex();
    EndPrimitive();

    gl_Position=p0;
    gs_out[0].color=c0;
    gs_out[0].modelViewVertexNormal=n3;
    gs_out[0].modelViewVertexposition=mvP0;
    EmitVertex();
    gl_Position=p3;
    gs_out[0].color=c3;
    gs_out[0].modelViewVertexNormal=n3;
    gs_out[0].modelViewVertexposition=mvP3;
    EmitVertex();
    gl_Position=p2;
    gs_out[0].color=c2;
    gs_out[0].modelViewVertexNormal=n3;
    gs_out[0].modelViewVertexposition=mvP2;
    EmitVertex();
    EndPrimitive();
}

void main(){
    const int total_level=2;

    int current_frame=0;//the frame index that have frame in.
    StackFrame[total_level+1] stack;//main stack,
    stack[0]=StackFrame(//init frame
    gl_in[0].gl_Position,
    gl_in[1].gl_Position,
    gl_in[2].gl_Position,
    gl_in[3].gl_Position,
    gs_in[0].color,
    gs_in[1].color,
    gs_in[2].color,
    gs_in[3].color,
    gs_in[0].vertexNormal,
    gs_in[1].vertexNormal,
    gs_in[2].vertexNormal,
    gs_in[3].vertexNormal,
    gs_in[0].mvVertexPosition,
    gs_in[1].mvVertexPosition,
    gs_in[2].mvVertexPosition,
    gs_in[3].mvVertexPosition,
    total_level,
    0
    );

    while(!(stack[current_frame].level==total_level&&stack[current_frame].branch_to_go==4)){//<--total_branch_number
        if(stack[current_frame].level!=0){
            if(stack[current_frame].branch_to_go==0){
                StackFrame nextFrame=StackFrame(
                stack[current_frame].p0,
                (stack[current_frame].p0+stack[current_frame].p1)/2,
                (stack[current_frame].p0+stack[current_frame].p2)/2,
                (stack[current_frame].p0+stack[current_frame].p3)/2,

                stack[current_frame].c0,
                (stack[current_frame].c0+stack[current_frame].c1)/2,
                (stack[current_frame].c0+stack[current_frame].c2)/2,
                (stack[current_frame].c0+stack[current_frame].c3)/2,

                stack[current_frame].n0,
                stack[current_frame].n1,
                stack[current_frame].n2,
                stack[current_frame].n3,

                stack[current_frame].mvP0,
                (stack[current_frame].mvP0+stack[current_frame].mvP1)/2,
                (stack[current_frame].mvP0+stack[current_frame].mvP2)/2,
                (stack[current_frame].mvP0+stack[current_frame].mvP3)/2,

                stack[current_frame].level-1,
                0
                );
                stack[current_frame+1]=nextFrame;
                current_frame++;
            }else if(stack[current_frame].branch_to_go==1){
                StackFrame nextFrame=StackFrame(
                (stack[current_frame].p1+stack[current_frame].p0)/2,
                stack[current_frame].p1,
                (stack[current_frame].p1+stack[current_frame].p2)/2,
                (stack[current_frame].p1+stack[current_frame].p3)/2,

                (stack[current_frame].c1+stack[current_frame].c0)/2,
                stack[current_frame].c1,
                (stack[current_frame].c1+stack[current_frame].c2)/2,
                (stack[current_frame].c1+stack[current_frame].c3)/2,

                stack[current_frame].n0,
                stack[current_frame].n1,
                stack[current_frame].n2,
                stack[current_frame].n3,

                (stack[current_frame].mvP1+stack[current_frame].mvP0)/2,
                stack[current_frame].mvP1,
                (stack[current_frame].mvP1+stack[current_frame].mvP2)/2,
                (stack[current_frame].mvP1+stack[current_frame].mvP3)/2,

                stack[current_frame].level-1,
                0
                );
                stack[current_frame+1]=nextFrame;
                current_frame++;
            }else if(stack[current_frame].branch_to_go==2){
                StackFrame nextFrame=StackFrame(
                (stack[current_frame].p2+stack[current_frame].p0)/2,
                (stack[current_frame].p2+stack[current_frame].p1)/2,
                stack[current_frame].p2,
                (stack[current_frame].p2+stack[current_frame].p3)/2,

                (stack[current_frame].c2+stack[current_frame].c0)/2,
                (stack[current_frame].c2+stack[current_frame].c1)/2,
                stack[current_frame].c2,
                (stack[current_frame].c2+stack[current_frame].c3)/2,

                stack[current_frame].n0,
                stack[current_frame].n1,
                stack[current_frame].n2,
                stack[current_frame].n3,

                (stack[current_frame].mvP2+stack[current_frame].mvP0)/2,
                (stack[current_frame].mvP2+stack[current_frame].mvP1)/2,
                stack[current_frame].mvP2,
                (stack[current_frame].mvP2+stack[current_frame].mvP3)/2,

                stack[current_frame].level-1,
                0
                );
                stack[current_frame+1]=nextFrame;
                current_frame++;
            }else if(stack[current_frame].branch_to_go==3){
                StackFrame nextFrame=StackFrame(
                (stack[current_frame].p3+stack[current_frame].p0)/2,
                (stack[current_frame].p3+stack[current_frame].p1)/2,
                (stack[current_frame].p3+stack[current_frame].p2)/2,
                stack[current_frame].p3,

                (stack[current_frame].c3+stack[current_frame].c0)/2,
                (stack[current_frame].c3+stack[current_frame].c1)/2,
                (stack[current_frame].c3+stack[current_frame].c2)/2,
                stack[current_frame].c3,

                stack[current_frame].n0,
                stack[current_frame].n1,
                stack[current_frame].n2,
                stack[current_frame].n3,

                (stack[current_frame].mvP3+stack[current_frame].mvP0)/2,
                (stack[current_frame].mvP3+stack[current_frame].mvP1)/2,
                (stack[current_frame].mvP3+stack[current_frame].mvP2)/2,
                stack[current_frame].mvP3,

                stack[current_frame].level-1,
                0
                );
                stack[current_frame+1]=nextFrame;
                current_frame++;
            }else{//this level finished
                current_frame--;
                stack[current_frame].branch_to_go++;
            }
        }else{//leaf nodes of the recursive tree
            tetrahedron(stack[current_frame].p0,stack[current_frame].p1,stack[current_frame].p2,stack[current_frame].p3,
            stack[current_frame].c0,stack[current_frame].c1,stack[current_frame].c2,stack[current_frame].c3,
            stack[current_frame].n0,stack[current_frame].n1,stack[current_frame].n2,stack[current_frame].n3,
            stack[current_frame].mvP0,stack[current_frame].mvP1,stack[current_frame].mvP2,stack[current_frame].mvP3);
            current_frame--;
            stack[current_frame].branch_to_go++;
        }
    }
}