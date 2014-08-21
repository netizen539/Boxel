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

import com.jme3.math.Vector3f;

public class ChunkCoord {
    int x;
    int z;
    
    public ChunkCoord(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public ChunkCoord(Vector3f location) {
        setFromLocation(location);
    }
    
    public ChunkCoord(BlockCoord bcoord) {
        setFromBlockCoord(bcoord);
    }
    
    @Override
    public int hashCode() {
        String cord = x+","+z;
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
        if (!(obj instanceof ChunkCoord)) {
            return false;
        }
        
        ChunkCoord other = (ChunkCoord)obj;
        if ((other.x == this.x) && 
            (other.z == this.z)) {
            return true;
        }
        
        return false;
    }
    
    public static int getChunkX(Vector3f loc) {
        return (int)Math.floor(loc.x / Chunk.CHUNK_SIZE); 
    }
    
    
    public static int getChunkZ(Vector3f loc) {
        return (int)Math.floor(loc.z / Chunk.CHUNK_SIZE); 
    }
    
    public static int getChunkX(int x) {
        return (int)Math.floor(x / Chunk.CHUNK_SIZE); 
    }
    
    public static int getChunkZ(int z) {
        return (int)Math.floor(z / Chunk.CHUNK_SIZE); 
    }

    public final void setFromLocation(Vector3f location) {
        this.x = getChunkX(location);
        this.z = getChunkZ(location);
    }
    
    public final void setFromBlockCoord(BlockCoord bcoord) {
        this.x = getChunkX(bcoord.x);
        this.z = getChunkZ(bcoord.z);
    }
    
}
