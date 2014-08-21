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
package com.avrgaming.boxel.threading;

import com.avrgaming.boxel.Boxel;
import com.avrgaming.boxel.logging.Log;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class Task implements Callable {
    public static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(16);
    public static Object emptyObject = new Object();
    
    private Future future = null;
    
   
    
    public void start() {
        if (future != null) {
            Log.error("Task("+this+") already started!");
            return;
        }
        
        future = executor.submit(this);
    }
    
    public boolean isFinished() {
        if (future == null) {
            return true;
        }
        
        return future.isDone() || future.isCancelled();
    }
    
    public Object getResult() {
        if (future == null || !isFinished()) {
            /* Return an empty object if we're not finished. */
            return emptyObject;
        }
        try {
            return future.get();
        } catch (InterruptedException ex) {
            Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return emptyObject;
    }
    
    public Boxel getBoxel() {
        return Boxel.getInstance();
    }

}
