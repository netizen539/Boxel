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

public class VoxelFace {
    
        public Block block;
        public boolean transparent;
        public VoxelSide side;
        
        public enum VoxelSide {
               SOUTH,
               NORTH,
               EAST,
               WEST,
               BOTTOM,
               TOP
        }
 
        public VoxelFace(int x, int y, int z, Block block, VoxelSide side) {
            this.block = block;
            this.side = side;
        }
        
        public boolean equals(final VoxelFace face) {
            return face.transparent == this.transparent && 
                   this.block.type.equals(face.block.type) &&
                   this.block.shape == face.block.shape;
        }
        
}

