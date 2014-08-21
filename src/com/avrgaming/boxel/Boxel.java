/*
 ************************************************************************
 * 
 * AVRGAMING LLC
 * __________________
 * 
 *  [2014] AVRGAMING LLC
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of AVRGAMING LLC and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to AVRGAMING LLC
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from AVRGAMING LLC.
 ************************************************************************
 */
package com.avrgaming.boxel;

import com.avrgaming.boxel.gui.GuiManager;
import com.avrgaming.boxel.logging.Log;
import com.avrgaming.boxel.threading.Task;
import com.avrgaming.boxel.util.DepthBlurFilter;
import com.avrgaming.boxel.util.TextureManager;
import com.avrgaming.boxel.world.BlockType;
import com.avrgaming.boxel.world.Chunk;
import com.avrgaming.boxel.world.ChunkCoord;
import com.avrgaming.boxel.world.ChunkManager;
import com.avrgaming.boxel.world.World;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.util.SkyFactory;
import com.jme3.water.SimpleWaterProcessor;
import com.jme3.water.WaterFilter;
import java.util.concurrent.ScheduledThreadPoolExecutor;


public class Boxel extends SimpleApplication {
    
    public static Boxel instance;
    public static ChunkManager chunkManager;
    public static BitmapText debugInfo;
    public static GuiManager guiManager;
    public static AmbientLight ambient;
    public static Node mainScene = new Node(); /* Main scene node for landscape. */
    public static FilterPostProcessor fpp;

    
    public static void main(String[] args) {
        Boxel app = new Boxel();
        chunkManager = new ChunkManager();
        instance = app;
        app.start();
    }

    public static Boxel getInstance() {
        return instance;
    }
   
    @Override
    public void destroy() {
        super.destroy();
        Task.executor.shutdown();
    }
    
    public void setupWater() {
        
      //  FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
      //  WaterFilter water = new WaterFilter(rootNode, new Vector3f(-4.9f, -1.3f, 5.9f));
      //  water.setWaterHeight(Chunk.WATER_LEVEL);
        //water.setWaveScale(0.005f); //default
       // water.setWaveScale(0.008f);

        //Log.debug("scale:"+water.getWaveScale());

    //    fpp.addFilter(water);
     //   viewPort.addProcessor(fpp);
        
       // water.setLightColor(ColorRGBA.Pink);
//        // we create a water processor
        SimpleWaterProcessor waterProcessor = new SimpleWaterProcessor(assetManager);
        waterProcessor.setReflectionScene(mainScene);
        //waterProcessor.setWaterTransparency(0.0f);

        // we set the water plane
        Vector3f waterLocation=new Vector3f(0,Chunk.WATER_LEVEL,0);
        waterProcessor.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));
        viewPort.addProcessor(waterProcessor);

        // we set wave properties
        waterProcessor.setWaterDepth(40);         // transparency of water
        waterProcessor.setDistortionScale(0.07f); // strength of waves
        waterProcessor.setWaveSpeed(0.05f);       // speed of waves

        // we define the wave size by setting the size of the texture coordinates
        Quad quad = new Quad(40,40);
        quad.scaleTextureCoordinates(new Vector2f(6f,6f));

        // we create the water geometry from the quad
        Geometry water=new Geometry("water", quad);
        water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        water.setLocalTranslation(-30, Chunk.WATER_LEVEL, 0);
        water.setShadowMode(ShadowMode.Receive);
        water.setMaterial(waterProcessor.getMaterial());
        rootNode.attachChild(water);
    }
    
    public void setupPostProcessor() {
        fpp = new FilterPostProcessor(assetManager);
        viewPort.addProcessor(fpp);
        
        DepthBlurFilter df = new DepthBlurFilter();
        fpp.addFilter(df);
          
    }
    
    @Override
    public void simpleInitApp() {
        //NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        //guiManager = new GuiManager(niftyDisplay);
        //guiViewPort.addProcessor(niftyDisplay);
        setupPostProcessor();
        //setupWater();
        
        mainScene.attachChild(SkyFactory.createSky(
            assetManager, "Textures/sky.png", true));
        
        
        flyCam.setMoveSpeed(10);
        BlockType.init();
        TextureManager.loadTextures();
        
        inputManager.addMapping("ToggleWireframe",  new KeyTrigger(KeyInput.KEY_X));
        inputManager.addListener(actionListener, "ToggleWireframe");

        int size = 1;
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                Chunk c = new Chunk(x,z);
                c.loadAsync();
            }
        }
        
        ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White.mult(1.5f));
        rootNode.addLight(ambient); 
        
        rootNode.attachChild(mainScene); 
        
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        debugInfo = new BitmapText(guiFont, false);
        debugInfo.setSize(guiFont.getCharSet().getRenderedSize());
        debugInfo.setText("Loading....");
        debugInfo.setLocalTranslation(0, settings.getHeight(), 0);
        debugInfo.setColor(ColorRGBA.Green);
        guiNode.attachChild(debugInfo);
        
    }
    
    private ActionListener actionListener = new ActionListener() {
    public void onAction(String name, boolean keyPressed, float tpf) {
      if (name.equals("ToggleWireframe") && !keyPressed) {
          for (Material mat : Chunk.materials) {
             mat.getAdditionalRenderState().setWireframe(!mat.getAdditionalRenderState().isWireframe());
          }
      }
    }
  };

    public static float daylight = 3.5f;
    public static boolean day = false;
    @Override
    public void simpleUpdate(float tpf) {
       chunkManager.update(cam.getLocation());
                
//        if (day) {
//            daylight -= 0.2f*tpf;
//        } else {
//           daylight += 0.2f*tpf;
//        }
//        
//        if (daylight > 10.0f) {
//            daylight = 10.0f;
//            day = true;
//        } else if (daylight < 1.5f) {
//            daylight = 1.5f;
//            day = false;
//        }
        
        Runtime runtime = Runtime.getRuntime();
        int mb = 1024*1024;
        float usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / mb;
        float freeMemory = runtime.freeMemory() / mb;
        float totalMemory = runtime.totalMemory() / mb;
                
        String out = "Location x:"+cam.getLocation().x+" y:"+cam.getLocation().y+" z:"+cam.getLocation().z+"\n";
        out += "Chunk X:"+ChunkCoord.getChunkX(cam.getLocation())+" Chunk Z:"+ChunkCoord.getChunkZ(cam.getLocation())+"\n";
        out += "Chunks: Loading:"+chunkManager.chunkLoadList.size()+" Visible:"+chunkManager.chunkVisibilityList.size()+"\n";
        out += "Memory: Used:"+usedMemory+" Free:"+freeMemory+" Total:"+totalMemory+"\n";
        out += "Daylight:"+daylight;
        
       
        ambient.setColor(ColorRGBA.White.mult(daylight));
        debugInfo.setText(out);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code       
        

        
        /** Write text on the screen (HUD) */

        
    }
    
  
        
}
