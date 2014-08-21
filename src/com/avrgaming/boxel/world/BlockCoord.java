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

import com.avrgaming.boxel.world.Block.BlockSide;

public class BlockCoord {
    public int x;
    public int y;
    public int z;
    
    public BlockCoord(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
   @Override
    public int hashCode() {
        String cord = x+","+","+y+","+z;
        return cord.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BlockCoord)) {
            return false;
        }
        
        BlockCoord other = (BlockCoord)obj;
        if ((other.x == this.x) && 
            (other.y == this.y) &&
            (other.z == this.z)) {
            return true;
        }
        
        return false;
    }
    /*
         * NORTH = z+1
         * SOUTH = z-1;
         * EAST = x+1;
         * WEST = x-1;
    */
    public static BlockCoord getAdjacentBlockCoord(BlockCoord center, BlockSide side) {
        switch (side) {
         case NORTH:
             return new BlockCoord(center.x, center.y, center.z+1);
         case SOUTH:
             return new BlockCoord(center.x, center.y, center.z-1);
         case EAST:
             return new BlockCoord(center.x+1, center.y, center.z);
         case WEST:
             return new BlockCoord(center.x-1, center.y, center.z);
         case TOP:
             return new BlockCoord(center.x, center.y+1, center.z);
         case BOTTOM:
             return new BlockCoord(center.x-1, center.y-1, center.z);
        }
        return center;
    }
}
