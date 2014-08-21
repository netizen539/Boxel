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
import com.avrgaming.boxel.threading.Task;
import java.util.concurrent.Callable;

public class TaskChunkLoad extends Task {
    private Chunk chunk;
    
    public TaskChunkLoad(Chunk chunk) {
       this.chunk = chunk;
    }
    
    public Boolean call() {
        try {
        if (chunk.loaded) {
            Log.error(chunk+" already loaded but async load task called.");
            return false;
        }
        
        /* Load the chunk's mesh/normals/textures. */
        chunk.load();
        
        /* Add to scene graph synchronously. */
        getBoxel().enqueue(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                if (chunk.meshHelper == null) {
                    return false;
                }
                
                chunk.meshHelper.addMeshToScene();
                //chunk.addWaterToScene();
                chunk.finalizeLoad();
                return true;
            }
        });

        return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
