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

package com.avrgaming.boxel.world;

import com.avrgaming.boxel.Boxel;
import com.avrgaming.boxel.logging.Log;
import com.avrgaming.boxel.util.TextureManager;
import java.util.ArrayList;
import java.util.HashMap;

public class BlockType {
    public int id;
    public String name;
    public String texture;
    
    public static ArrayList<BlockType> types = new ArrayList<BlockType>();
    public static HashMap<String, BlockType> typesByName = new HashMap<String, BlockType>();

    public BlockType(int id, String name, String texture) {
        this.id = id;
        this.name = name;
        this.texture = texture;
    }
    
    public static void init() {
        BlockType air = new BlockType(0, "Air", "");
        types.add(air);
        typesByName.put(air.name.toLowerCase(), air);
        TextureManager.textures.add(Boxel.getInstance().getAssetManager().loadTexture("Textures/blocks/sponge.png"));

        initBlockType("Bedrock", "Textures/blocks/bedrock.png");
        initBlockType("Grass", "Textures/blocks/grass_green.png");
        initBlockType("Stone", "Textures/blocks/stone.png");
        initBlockType("Sand", "Textures/blocks/sand.png");
        initBlockType("Dirt", "Textures/blocks/dirt.png");
    }
    
    public static int idIter = 1;
    public static void initBlockType(String name, String texture) {
        BlockType t = new BlockType(idIter++, name, texture);
        types.add(t);
        typesByName.put(name.toLowerCase(), t);
        TextureManager.textures.add(Boxel.getInstance().getAssetManager().loadTexture(t.texture));
        Log.debug("id:"+t.id+" name:"+t.name+" tex:"+t.texture);
    }
    
    public static int getBlockId(String name) {
        BlockType type = null;
        
        for (BlockType t : types) {
            if (t.name.equalsIgnoreCase(name)) {
                type = t;
                break;
            }
        }
        
        if (type == null) {
            return 1;
        }
        return type.id;
    }
    
    public static BlockType getType(String name) {
        BlockType t = typesByName.get(name.toLowerCase());
        if (t == null) {
            Log.error("No block type with name '"+name+"'");
            return typesByName.get("Air");
        }
        return t;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
}
