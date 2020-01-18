#version 330

layout(lines_adjacency) in ;
layout(triangle_strip, max_vertices=256) out;//max_vertices=192 can create half level-3 Sierpinski3D

in VS_OUT{
vec3 color;
}gs_in[];

out GS_OUT{
vec3 color;
}gs_out[1];

struct StackFrame{
    vec4 p0;vec4 p1;vec4 p2;vec4 p3;vec3 c0;vec3 c1;vec3 c2;vec3 c3;int level;int branch_to_go;
};

void tetrahedron(vec4 p0,vec4 p1,vec4 p2,vec4 p3,vec3 c0,vec3 c1,vec3 c2,vec3 c3){
    gl_Position=p0;
    gs_out[0].color=c0;
    EmitVertex();
    gl_Position=p2;
    gs_out[0].color=c2;
    EmitVertex();
    gl_Position=p1;
    gs_out[0].color=c1;
    EmitVertex();
    gl_Position=p3;
    gs_out[0].color=c3;
    EmitVertex();
    gl_Position=p0;
    gs_out[0].color=c0;
    EmitVertex();
    gl_Position=p2;
    gs_out[0].color=c2;
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
            stack[current_frame].c0,stack[current_frame].c1,stack[current_frame].c2,stack[current_frame].c3);
            current_frame--;
            stack[current_frame].branch_to_go++;
        }
    }
}