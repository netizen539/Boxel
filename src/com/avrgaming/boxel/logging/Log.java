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

package com.avrgaming.boxel.logging;

public class Log {
    public static void debug(String message) {
        System.out.println("[DEBUG]"+message);
    }
    
    public static void error(String message) {
        System.out.println("[ERROR]"+message);
    }
    
    public static void info(String message) {
        System.out.println("[INFO]"+message);
    }
}
