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

import com.avrgaming.boxel.logging.Log;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.TreeMap;

public class ChunkManager {
    
    public HashMap<ChunkCoord, Chunk> chunks = new HashMap<ChunkCoord, Chunk>();
    
   // public Queue<Chunk> chunkLoadList = new LinkedList<Chunk>();
    public TreeMap<Float, Chunk> chunkLoadList = new TreeMap<Float, Chunk>();
    public Queue<Chunk> chunkUnloadList = new LinkedList<Chunk>();
    public ArrayList<Chunk> chunkVisibilityList = new ArrayList<Chunk>();
    public ArrayList<Chunk> chunkRenderList = new ArrayList<Chunk>();
    
    public static final int VIEW_DISTANCE = 7;
    
    private ChunkCoord lastChunk = null;
    private ChunkCoord currentChunk = new ChunkCoord(0,0);
    
    public void update(Vector3f location) {
        
        
        /* 1) Check if any chunks need to be loaded.
         * 2) Check if any chunks have been loaded, but need to be setup (voxel configuration, set active blocks, etc...).
         * 3) Check if any chunks need to be rebuilt (i.e. a chunk was modified last frame and needs mesh rebuild).
         * 4) Check if any chunks need to be unloaded.
         * 5) Update the chunk visibility list (this is a list of all chunks that could potentially be rendered)
         * 6) Update the render list.
         */
     //   if (lastChunk != null) {
   //         currentChunk.setFromLocation(location);
            
  //          if (currentChunk.equals(lastChunk)) {
  //              return;
  //          }
  //      } else {
  //          lastChunk = new ChunkCoord(location);
   //     }
        
        updateVisibiltyList(location);
        updateLoadList();
        updateUnloadList();
        
        
    //    lastChunk.setFromLocation(location);
    }
   
    /*
     * Iterates through chunks on the load list and loads them in from the filesystem.
     */
    public void updateLoadList() {
       for (int i = 0; i < 10; i++) {
            Entry<Float,Chunk> entry = chunkLoadList.firstEntry();
            if (entry == null) {
                return;
            }
            
            Chunk chunk = chunkLoadList.firstEntry().getValue();
            if (chunk == null) {
                return;
            }

            if (!chunk.isLoading()) {
                chunk.loadAsync();
            }
            chunkLoadList.remove(chunkLoadList.firstKey());
       }
    }
     /*
     * Iterates through chunks on the unload list and saves them to the filesystem.
     * TODO free/reuse allocated chunk memory by putting it on to a free list.
     */
    public void updateUnloadList() {
//       Chunk chunk = chunkUnloadList.poll();
//       if (chunk == null) {
//           return;
//       }
//
//       chunk.unload();
    }
    
    /*
     * Places chunks on a visibility list if they are within a specific radius
     * of a location.
     */
    ChunkCoord coord = new ChunkCoord(0,0);
    public void updateVisibiltyList(Vector3f location) {
        int cx = ChunkCoord.getChunkX(location);
        int cz = ChunkCoord.getChunkZ(location);
        

//        for (Chunk chunk : chunkVisibilityList) {
//            if (chunk.x < (cx-VIEW_DISTANCE) || chunk.x > (cx+VIEW_DISTANCE)) {
//                chunkUnloadList.add(chunk);
//                chunk.visible = false;
//            }
//            
//            if (chunk.z < (cz-VIEW_DISTANCE) || chunk.z > (cz+VIEW_DISTANCE)) {
//                chunkUnloadList.add(chunk);
//                chunk.visible = false;
//            }
//        }
        
        chunkVisibilityList.clear();
        for (int x = (cx-VIEW_DISTANCE); x < (cx+VIEW_DISTANCE); x++) {
            for (int z = (cz-VIEW_DISTANCE); z < (cz+VIEW_DISTANCE); z++) {
                coord.x = x; coord.z = z;
                Chunk chunk = chunks.get(coord);
                if (chunk == null) {
                    /* Chunk is not loaded. */
                    chunk = new Chunk(x,z);
                    
                    Float distanceSqd = Float.valueOf((float)Math.pow(cx-x, 2) + (float)Math.pow(cz-z, 2));
                    while (chunkLoadList.containsKey(distanceSqd)) {
                        distanceSqd += 0.01f;
                    }
                    
                    chunkLoadList.put(distanceSqd, chunk);
                    chunks.put(coord, chunk);
                }
                                    
                chunkVisibilityList.add(chunk);
                chunk.visible = true;             


                
//                if (chunk == null) {
//                    chunk = new Chunk(x,z);
//                    /* 
//                     * Chunk isn't loaded, create a new chunk 
//                     * and add it to the load list.
//                     */
//                    chunkVisibilityList.add(chunk);
//                    chunkLoadList.add(chunk);
//                    chunk.visible = true;
//                } else if (!chunk.loaded && !chunk.visible) {
//                    chunkVisibilityList.add(chunk);
//                    chunkLoadList.add(chunk);   
//                    chunk.visible = true;
//                } else if (!chunk.visible) {
//                    /* Chunk is visible, add it to list. */
//                    chunkVisibilityList.add(chunk);
//                    chunk.visible = true;
//                }
            }
        }
    }
    
}
