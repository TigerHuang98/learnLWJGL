package com.triggerh.engine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window{
    private final String title;
    private int width;
    private int height;
    private long windowHandle;
    private boolean resized;
    private boolean vSync;

    public Window(String title,int width,int height,boolean vSync){
        this.title=title;
        this.width=width;
        this.height=height;
        this.resized=false;
        this.vSync=vSync;
    }

    public void init(){
        GLFWErrorCallback.createPrint(System.err).set();
        boolean glfwInitSuccess=glfwInit();
        if(!glfwInitSuccess){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR,3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,3);
        glfwWindowHint(GLFW_OPENGL_PROFILE,GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT,GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GL_TRUE);

        windowHandle=glfwCreateWindow(width,height,title,NULL,NULL);
        if(windowHandle==NULL){
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetFramebufferSizeCallback(
                windowHandle,
                (window,width,height)->{
                    this.width=width;
                    this.height=height;
                    setResized(true);
                }
        );

        glfwSetKeyCallback(
                windowHandle,
                (window,key,scanCode,action,mods)->{
                    if(key==GLFW_KEY_ESCAPE&&action==GLFW_RELEASE){
                        glfwSetWindowShouldClose(window,true);
                    }
                }
        );

        GLFWVidMode vidMode=glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(windowHandle,(vidMode.width()-width)/2,(vidMode.height()-height)/2);
        glfwMakeContextCurrent(windowHandle);
        if(isvSync()){
            glfwSwapInterval(1);
        }
        glfwShowWindow(windowHandle);
        GL.createCapabilities();
        glEnable(GL_FRAMEBUFFER_SRGB);
        glEnable(GL_DEPTH_TEST);
//        glEnable(GL_CULL_FACE);
//        glCullFace(GL_BACK);
        glEnable(GL_PROGRAM_POINT_SIZE);
        setClearColor(0.0f,0.0f,0.0f,0.0f);
    }

    public void setClearColor(float r,float g, float b, float alpha){
        glClearColor(r,g,b,alpha);
    }

    public boolean isKeyPressed(int KeyCode){
        return glfwGetKey(windowHandle,KeyCode)==GLFW_PRESS;
    }

    public boolean windowShouldClose(){
        return glfwWindowShouldClose(windowHandle);
    }

    public void update(){
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

//getter&setter

    public String getTitle(){
        return title;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public long getWindowHandle(){
        return windowHandle;
    }

    public boolean isResized(){
        return resized;
    }

    public void setResized(boolean resized){
        this.resized=resized;
    }

    public boolean isvSync(){
        return vSync;
    }

    public void setvSync(boolean vSync){
        this.vSync=vSync;
    }
}
