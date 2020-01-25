package com.triggerh.utils;

import org.joml.Vector4f;

import static java.lang.Math.pow;

public class ColorVector4f extends Vector4f{
    public static final Vector4f sRGBToLinear(Vector4f color,double GAMMA){
        color.x=(float)pow(color.x,GAMMA);
        color.y=(float)pow(color.y,GAMMA);
        color.z=(float)pow(color.z,GAMMA);
        return color;
    }
}
