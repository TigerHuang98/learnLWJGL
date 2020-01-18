package com.triggerh;

import com.triggerh.engine.graphics.Mesh;
import org.joml.Vector3f;

import java.util.ArrayList;

public class Sierpinski3D{

    private static class Sierpinski3DMesh{
        ArrayList<Float> positions=new ArrayList<>();
        ArrayList<Float> colors=new ArrayList<>();
        ArrayList<Integer> indices=new ArrayList<>();
    }

    public static Mesh getSierpinski3DMesh(
            Vector3f p0,Vector3f p1,Vector3f p2,Vector3f p3,
            Vector3f c0,Vector3f c1,Vector3f c2,Vector3f c3,
            int level
    ){
        Sierpinski3DMesh s3m=calculateMesh(
                p0,p1,p2,p3,
                c0,c1,c2,c3,
                level
        );
        float[] positionArray=new float[s3m.positions.size()];
        int i=0;

        for(Float f:s3m.positions){
            positionArray[i++]=(f!=null?f:Float.NaN);
        }
        float[] colorArray=new float[s3m.positions.size()];
        i=0;

        for(Float f:s3m.colors){
            colorArray[i++]=(f!=null?f:Float.NaN);
        }
        return new Mesh(
                positionArray,
                colorArray,
                s3m.indices.stream().mapToInt(num_i->num_i).toArray()
        );
    }

    public static Sierpinski3DMesh calculateMesh(
            Vector3f p0,Vector3f p1,Vector3f p2,Vector3f p3,
            Vector3f c0,Vector3f c1,Vector3f c2,Vector3f c3,
            int level
    ){
        Sierpinski3DMesh sierpinski3DMesh=new Sierpinski3DMesh();
        return calculateMesh(
                p0,p1,p2,p3,
                c0,c1,c2,c3,
        level,sierpinski3DMesh
        );
    }

    public static Sierpinski3DMesh calculateMesh(
            Vector3f p0,Vector3f p1,Vector3f p2,Vector3f p3,
            Vector3f c0,Vector3f c1,Vector3f c2,Vector3f c3,
            int level,Sierpinski3DMesh mesh
    ){
        if(level>0){
            Vector3f temp=new Vector3f();
            Vector3f p01=new Vector3f();
            Vector3f p02=new Vector3f();
            Vector3f p03=new Vector3f();
            Vector3f p12=new Vector3f();
            Vector3f p13=new Vector3f();
            Vector3f p23=new Vector3f();
            Vector3f c01=new Vector3f();
            Vector3f c02=new Vector3f();
            Vector3f c03=new Vector3f();
            Vector3f c12=new Vector3f();
            Vector3f c13=new Vector3f();
            Vector3f c23=new Vector3f();
            p1.sub(p0,temp);
            p0.add(temp.div(2),p01);
            p2.sub(p0,temp);
            p0.add(temp.div(2),p02);
            p3.sub(p0,temp);
            p0.add(temp.div(2),p03);
            p2.sub(p1,temp);
            p1.add(temp.div(2),p12);
            p3.sub(p1,temp);
            p1.add(temp.div(2),p13);
            p3.sub(p2,temp);
            p2.add(temp.div(2),p23);

            c1.sub(c0,temp);
            c0.add(temp.div(2),c01);
            c2.sub(c0,temp);
            c0.add(temp.div(2),c02);
            c3.sub(c0,temp);
            c0.add(temp.div(2),c03);
            c2.sub(c1,temp);
            c1.add(temp.div(2),c12);
            c3.sub(c1,temp);
            c1.add(temp.div(2),c13);
            c3.sub(c2,temp);
            c2.add(temp.div(2),c23);

            calculateMesh(p0,p01,p02,p03,c0,c01,c02,c03,level-1,mesh);
            calculateMesh(p01,p1,p12,p13,c01,c1,c12,c13,level-1,mesh);
            calculateMesh(p02,p12,p2,p23,c02,c12,c2,c23,level-1,mesh);
            calculateMesh(p03,p13,p23,p3,c03,c13,c23,c3,level-1,mesh);
        }else{
            int vertexCount=mesh.positions.size()/3;
            mesh.positions.add(p0.x);
            mesh.positions.add(p0.y);
            mesh.positions.add(p0.z);
            mesh.positions.add(p1.x);
            mesh.positions.add(p1.y);
            mesh.positions.add(p1.z);
            mesh.positions.add(p2.x);
            mesh.positions.add(p2.y);
            mesh.positions.add(p2.z);
            mesh.positions.add(p3.x);
            mesh.positions.add(p3.y);
            mesh.positions.add(p3.z);
            mesh.colors.add(c0.x);
            mesh.colors.add(c0.y);
            mesh.colors.add(c0.z);
            mesh.colors.add(c1.x);
            mesh.colors.add(c1.y);
            mesh.colors.add(c1.z);
            mesh.colors.add(c2.x);
            mesh.colors.add(c2.y);
            mesh.colors.add(c2.z);
            mesh.colors.add(c3.x);
            mesh.colors.add(c3.y);
            mesh.colors.add(c3.z);
            mesh.indices.add(vertexCount);
            mesh.indices.add(vertexCount+1);
            mesh.indices.add(vertexCount+2);
            mesh.indices.add(vertexCount+3);
        }
        return mesh;
    }
}
