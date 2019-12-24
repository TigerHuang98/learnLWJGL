package com.triggerh;

import com.triggerh.engine.AbstractGameLogic;
import com.triggerh.engine.GameItem;
import com.triggerh.engine.MouseInput;
import com.triggerh.engine.Window;
import com.triggerh.engine.graphics.AbstractRender;
import com.triggerh.engine.graphics.Camera;
import com.triggerh.engine.graphics.Mesh;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class GameLogic extends AbstractGameLogic{

    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float CAMERA_POS_STEP = 0.03f;

    private final AbstractRender renderer=new Renderer();
    private final Camera camera=new Camera();
    private final Vector3f cameraInc=new Vector3f(0,0,0);
    private GameItem[] gameItems;

    @Override
    public void init() throws Exception{
        renderer.init();
        GameItem gameItem=new GameItem(
                new Mesh(
                        new float[]{//position
                                -0.5f, 0.5f, 0.0f,
                                -0.5f, -0.5f, 0.0f,
                                0.5f, -0.5f, 0.0f,
                                0.5f, 0.5f, 0.0f
                        },
                        new float[]{//color
                                1.0f,0.0f,0.0f,
                                0.5f,0.5f,0.0f,
                                0.0f,1.0f,0.0f,
                                0.0f,0.0f,1.0f
                        },
                        new int[]{//index
                                0,1,3,3,1,2
                        }
                )
        );
        gameItem.setPosition(0,0,5f);
//        gameItems=new GameItem[]{gameItem};
        GameItem Tetrahedron=new GameItem(
                new Mesh(
                        new float[]{//position
                                -1,-1,-1,
                                -1,1,1,
                                1,-1,1,
                                1,1,-1
                        },
                        new float[]{//color
                                1.0f,0.0f,0.0f,
                                1.0f,1.0f,0.0f,
                                0.0f,1.0f,0.0f,
                                0.0f,0.0f,1.0f
                        },
                        new int[]{//0,1,2,3
//                                0,  2,  1,
//                                1,  3,  0,
//                                2,  3,  1,
//                                3,  2,  0

                                0,3,2,3,1,3,
//                                1,2,3,2,0,2,
//                                2,0,3,0,1,0,
//                                3,1,2,1,0,1,
                        }

                )
        );
        gameItems=new GameItem[]{Tetrahedron};
    }

    @Override
    public void input(Window window){
        cameraInc.set(0,0,0);
        if(window.isKeyPressed(GLFW_KEY_W)){
            cameraInc.z=-1;
        }else if(window.isKeyPressed(GLFW_KEY_S)){
            cameraInc.z=1;
        }
        if(window.isKeyPressed(GLFW_KEY_A)){
            cameraInc.x=-1;
        }else if(window.isKeyPressed(GLFW_KEY_D)){
            cameraInc.x=1;
        }
        if(window.isKeyPressed(GLFW_KEY_TAB)){
            cameraInc.y=-1;
        }else if(window.isKeyPressed(GLFW_KEY_SPACE)){
            cameraInc.y=1;
        }
    }

    @Override
    public void update(float interval,MouseInput mouseInput){
        camera.movePosition(cameraInc.x*CAMERA_POS_STEP,cameraInc.y*CAMERA_POS_STEP,cameraInc.z*CAMERA_POS_STEP);
        if(mouseInput.isRightButtonPressed()){
            Vector2f rotVec=mouseInput.getDisplaceVec();
            camera.moveRotation(rotVec.x*MOUSE_SENSITIVITY,rotVec.y*MOUSE_SENSITIVITY,0);
        }
    }

    @Override
    public void render(Window window){
        window.setClearColor(0.0f,0.0f,0.0f,0.0f);
        renderer.render(window,camera,gameItems);
    }

    @Override
    public void cleanup(){
        renderer.cleanup();
        for(GameItem gameItem:gameItems){
            gameItem.getMesh().cleanup();
        }
    }
}
