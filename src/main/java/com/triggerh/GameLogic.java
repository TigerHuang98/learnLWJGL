package com.triggerh;

import com.triggerh.engine.AbstractGameLogic;
import com.triggerh.engine.GameItem;
import com.triggerh.engine.MouseInput;
import com.triggerh.engine.Window;
import com.triggerh.engine.graphics.AbstractRender;
import com.triggerh.engine.graphics.Camera;
import com.triggerh.engine.graphics.Material;
import com.triggerh.engine.graphics.PointLight;
import com.triggerh.utils.ColorVector4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public class GameLogic extends AbstractGameLogic{

    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float CAMERA_POS_STEP = 0.03f;

    private final AbstractRender renderer=new Renderer();
    private final Camera camera=new Camera();
    private final Vector3f cameraInc=new Vector3f(0,0,0);
    private GameItem[] gameItems;
    private Vector3f ambientLight;
    private PointLight pointLight;

    @Override
    public void init() throws Exception{
        renderer.init();
//        GameItem gameItem=new GameItem(
//                new Mesh(
//                        new float[]{//position
//                                -0.5f, 0.5f, 0.0f,
//                                -0.5f, -0.5f, 0.0f,
//                                0.5f, -0.5f, 0.0f,
//                                0.5f, 0.5f, 0.0f
//                        },
//                        new float[]{//color
//                                1.0f,0.0f,0.0f,
//                                0.5f,0.5f,0.0f,
//                                0.0f,1.0f,0.0f,
//                                0.0f,0.0f,1.0f
//                        },
//                        new int[]{//index
//                                0,1,3,3,1,2
//                        }
//                )
//        );
//        gameItem.setPosition(0,0,5f);
//        gameItems=new GameItem[]{gameItem};


        GameItem Tetrahedron=new GameItem(
                Sierpinski3D.getSierpinski3DMesh(
                        new Vector3f(-1,-1,-1),
                        new Vector3f(-1,1,1),
                        new Vector3f(1,-1,1),
                        new Vector3f(1,1,-1),
                        new Vector3f(1.0f,0.0f,0.0f),
                        new Vector3f(1.0f,1.0f,0.0f),
                        new Vector3f(0.0f,1.0f,0.0f),
                        new Vector3f(0.0f,0.0f,1.0f),

                        new Vector3f(-1,-1,1),
                        new Vector3f(-1,1,-1),
                        new Vector3f(1,1,1),
                        new Vector3f(1,-1,-1),
                        0
                        )
//                new Mesh(
//                        new float[]{//position
//                                -1,-1,-1,
//                                -1,1,1,
//                                1,-1,1,
//                                1,1,-1,
//                                -5,-5,-5,
//                                -5,5,5,
//                                5,-5,5,
//                                5,5,-5
//                        },
//                        new float[]{//color
//                                1.0f,0.0f,0.0f,
//                                1.0f,1.0f,0.0f,
//                                0.0f,1.0f,0.0f,
//                                0.0f,0.0f,1.0f
//                        },
//                        new int[]{//0,1,2,3
////                                0,  2,  1,
////                                1,  3,  0,
////                                2,  3,  1,
////                                3,  2,  0
//
//                                0,3,1,3,2,3,//switch 1&2 can get in side out
//                                4,7,5,7,6,7
////                                1,2,3,2,0,2,
////                                2,0,3,0,1,0,
////                                3,1,2,1,0,1,
//                        }
//
//                )
        );
        Tetrahedron.setScale(1);
        Tetrahedron.getMesh().setMaterial(
                new Material(
                        ColorVector4f.sRGBToLinear(new Vector4f(0.2125f,0.1275f,0.054f,1f),2.2f),
                        ColorVector4f.sRGBToLinear(new Vector4f(0.714f,0.4284f,0.18144f,1f),2.2f),
                        ColorVector4f.sRGBToLinear(new Vector4f(0.393548f,0.271906f,0.166721f,1f),2.2f),
                        25.6f)
        );
//        Tetrahedron.getMesh().setMaterial(
//                new Material(
//                        new Vector4f(0.19125f,0.0735f,0.0225f,1f),
//                        new Vector4f(0.7038f,0.27048f,0.0828f,1f),
//                        new Vector4f(0.256777f,0.137622f,0.086014f,1f),
//                12.8f)
//        );
        gameItems=new GameItem[]{Tetrahedron};

        ambientLight = new Vector3f(0.4f, 0.4f, 0.4f);
        Vector3f lightColour =
//                new Vector3f(0.2f, 0.1f, 0.7f);
                new Vector3f(0.8f, 0.8f, 0.8f);
        Vector3f lightPosition = new Vector3f(0,0,0);
        float lightIntensity = 1f;
        pointLight = new PointLight(lightColour, lightPosition, lightIntensity);
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
        pointLight.setPosition(camera.getPosition());
    }

    @Override
    public void render(Window window){
        window.setClearColor(0.0f,0.0f,0.0f,0.0f);
        renderer.render(window,camera,gameItems,ambientLight, pointLight);
    }

    @Override
    public void cleanup(){
        renderer.cleanup();
        for(GameItem gameItem:gameItems){
            gameItem.getMesh().cleanup();
        }
    }
}
