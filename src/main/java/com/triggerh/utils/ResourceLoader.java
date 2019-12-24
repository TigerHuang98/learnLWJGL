package com.triggerh.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ResourceLoader{
    public static String loadResource(String filename) throws IOException{
    String result;
    try(
            InputStream in =ResourceLoader.class.getResourceAsStream(filename);
            Scanner scanner=new Scanner(in,StandardCharsets.UTF_8)
            ){
        result = scanner.useDelimiter("\\A").next();//"\A" Matches the beginning of the string
    }
    return result;
    }
}
