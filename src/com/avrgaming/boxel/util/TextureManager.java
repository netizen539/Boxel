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
package com.avrgaming.boxel.util;

import com.avrgaming.boxel.Boxel;
import com.avrgaming.boxel.logging.Log;
import com.avrgaming.boxel.world.BlockType;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.TextureArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextureManager {
    //public static HashMap<Integer, Texture> textures = new HashMap<Integer, Texture>();
    public static ArrayList<Texture> textures = new ArrayList<Texture>();
    public static HashMap<Integer, Integer> textureIDMap = new HashMap<Integer, Integer>();
    
    public static TextureArray terrianTextures;
    
    public static final int TEXTURE_MAP_SIZE = 2048;
    public static final int TEXTURE_SIZE = 128;
    public static final int TEX_MOD = TEXTURE_MAP_SIZE / TEXTURE_SIZE;
    
    public static void loadTextures() {
        
        //texMap.put(1, 0); //Bedrock
        //texMap.put(2, 2); //Dirt
        //texMap.put(3, 78); //Grass
        
        
       
       // textures.put(0,  Main.getInstance().getAssetManager().loadTexture("Textures/terrain.png"));
       // int index = 0;
       // textures.put(index++,  Main.getInstance().getAssetManager().loadTexture("Textures/stone.png"));
        //textures.put(index++,  Main.getInstance().getAssetManager().loadTexture("Textures/grass.png"));
       
       //for (BlockType type : BlockType.types) {
       //}
        
//       textures.add(Boxel.getInstance().getAssetManager().loadTexture("Textures/bedrock.png"));
//       textures.add(Boxel.getInstance().getAssetManager().loadTexture("Textures/grass.png"));
//       textures.add(Boxel.getInstance().getAssetManager().loadTexture("Textures/stone.png"));
//       textures.add(Boxel.getInstance().getAssetManager().loadTexture("Textures/dirt.png"));
//       textures.add(Boxel.getInstance().getAssetManager().loadTexture("Textures/sand.png"));

       
       List<Image> images = new ArrayList<Image>();
       for (Texture tex : textures) {
           Log.debug("Adding image:"+tex.toString());
           images.add(tex.getImage());
       }
       
       terrianTextures = new TextureArray(images);
       terrianTextures.setWrap(Texture.WrapMode.Repeat);
        
        
    }
    
//    public static TextureCoord getTextureCoords(int type) {
//        int index = texMap.get(type);
//        TextureCoord coord = new TextureCoord();
//        
//        int texX = (index % TEX_MOD)*TEXTURE_SIZE;
//        int texY = (index / TEX_MOD)*TEXTURE_SIZE;
//        int texX2 = texX + TEXTURE_SIZE;
//        int texY2 = texY + TEXTURE_SIZE;
//        
//        System.out.println("x:"+texX+" y:"+texY+" x2:"+texX2+" y2:"+texY2);
//        
//        coord.minX = ((float)texX) / TEXTURE_MAP_SIZE;
//        coord.maxX = ((float)texX2) / TEXTURE_MAP_SIZE;
//        
//        coord.minY = ((float)texY) / TEXTURE_MAP_SIZE;
//        coord.maxY = ((float)texY2) / TEXTURE_MAP_SIZE;
//        
//        System.out.println("minX:"+coord.minX+" maxX:"+coord.maxX+" minY:"+coord.minY+" maxY:"+coord.maxY);
//
//        return coord;
//    }
   
    
}
