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

public class Block {
    public int x;
    public int y;
    public int z;
    public BlockShape shape;
    public BlockType type;
    public float[] points;
    
    public enum BlockShape {
            BOX,
            SLOPE_NORTH,
            SLOPE_SOUTH,
            SLOPE_EAST,
            SLOPE_WEST,
            SLOPE_NORTH_EAST,
            SLOPE_NORTH_WEST,
            SLOPE_SOUTH_EAST,
            SLOPE_SOUTH_WEST
    }
        
    /*
         * NORTH = z+1
         * SOUTH = z-1;
         * EAST = x+1;
         * WEST = x-1;
    */
    public enum BlockSide {
        NORTH,
        SOUTH,
        EAST,
        WEST,
        TOP,
        BOTTOM
    }
    
    public static final float BLOCK_RENDER_SIZE = 1.0f;
    
    public Block(int x, int y, int z, float[] points, BlockType type) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.points = points;
        this.type = type;
    }
    
    public Block(int x, int y, int z, float[] points, String typeName) {
        this(x,y,z, points, BlockType.getType(typeName));
    }
    
    @Override
    public String toString() {
        return x+","+y+","+z+" type:"+this.type.name;
    }
    
    public static Block getBlock(int x, int y, int z) {
        BlockCoord bcoord = new BlockCoord(x, y, z);
        ChunkCoord coord = new ChunkCoord(bcoord);
        
        Chunk chunk = Boxel.chunkManager.chunks.get(coord);
        if (chunk == null) {
            return new Block(x, y, z, new float[4], "Air");
        }
        
        Block b = chunk.blocks.get(bcoord);
        if (b == null) {
            return new Block(x,y,z, new float[4], "Air");
        } else {
            return b;
        }
    }
    
    public int getBlockTextureId() {        
        return this.type.id;
    }
    
    boolean isAir() {
        return (this.type.id == 0);
    }
    
}
