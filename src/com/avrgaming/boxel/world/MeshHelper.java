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
import static com.avrgaming.boxel.Boxel.mainScene;
import com.avrgaming.boxel.logging.Log;
import com.avrgaming.boxel.util.TextureManager;
import com.avrgaming.boxel.world.Block.BlockShape;
import static com.avrgaming.boxel.world.Block.BlockShape.SLOPE_EAST;
import static com.avrgaming.boxel.world.Block.BlockShape.SLOPE_NORTH;
import static com.avrgaming.boxel.world.Block.BlockShape.SLOPE_SOUTH;
import static com.avrgaming.boxel.world.Chunk.CHUNK_SIZE;
import static com.avrgaming.boxel.world.Chunk.YMAX;
import static com.avrgaming.boxel.world.Chunk.materials;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import com.jme3.water.SimpleWaterProcessor;
import java.util.ArrayList;
import java.util.Arrays;

    
    
/*
 * MesHelper for making those verts. 
 */

public class MeshHelper {

    ArrayList<Vector3f> meshVerts = null;
    ArrayList<Vector3f> meshTexCoords = null;
    ArrayList<Vector3f> meshNormals = null;
    ArrayList<Integer> meshIndexes = null;
    Chunk chunk;
    
    
    
    public MeshHelper(Chunk chunk) {
        this.chunk = chunk;
        meshNormals = new ArrayList<Vector3f>();
        meshTexCoords = new ArrayList<Vector3f>();
        meshVerts = new ArrayList<Vector3f>();
        meshIndexes = new ArrayList<Integer>();

    }
    
    public void cleanup() {
        meshVerts = null;
        meshTexCoords = null;
        meshIndexes = null;
        meshNormals = null;
    }
  
    /**
     * Build a quad for a BOX shape.
     */
    void quad( Vector3f bottomLeft, Vector3f topLeft,  Vector3f topRight,
               Vector3f bottomRight, int width, int height, VoxelFace voxel,
               boolean backFace, boolean water) {
           
        final Vector3f [] vertices = new Vector3f[4];
        
        vertices[2] = topLeft.multLocal(Block.BLOCK_RENDER_SIZE);
        vertices[3] = topRight.multLocal(Block.BLOCK_RENDER_SIZE);
        vertices[0] = bottomLeft.multLocal(Block.BLOCK_RENDER_SIZE);
        vertices[1] = bottomRight.multLocal(Block.BLOCK_RENDER_SIZE);

        final Vector3f [] texCoords = new Vector3f[4];
        Vector3f normalA;
        
        //System.out.println("VoxShape:"+voxel.block.shape.name());
        
        if (backFace) {
            texCoords[3] = new Vector3f(0,0, voxel.block.getBlockTextureId());
            texCoords[2] = new Vector3f(1*height, 0, voxel.block.getBlockTextureId());
            texCoords[1] = new Vector3f(0,1*width, voxel.block.getBlockTextureId());
            texCoords[0] = new Vector3f(1*height,1*width, voxel.block.getBlockTextureId());
            
            Vector3f point1 = new Vector3f(vertices[2]).subtract(vertices[3]);
            Vector3f point2 = new Vector3f(vertices[1]).subtract(vertices[3]);
            normalA = point1.cross(point2).normalize();
                        
        } else {
            texCoords[2] = new Vector3f(0,0, voxel.block.getBlockTextureId());
            texCoords[3] = new Vector3f(1*height,0, voxel.block.getBlockTextureId());
            texCoords[0] = new Vector3f(0,1*width, voxel.block.getBlockTextureId());
            texCoords[1] = new Vector3f(1*height,1*width, voxel.block.getBlockTextureId());
                    
            Vector3f point1 = new Vector3f(vertices[2]).subtract(vertices[0]);
            Vector3f point2 = new Vector3f(vertices[1]).subtract(vertices[0]);
            normalA = point1.cross(point2).normalize();
            
        }

        meshNormals.add(normalA);
        meshNormals.add(normalA);
        meshNormals.add(normalA);
        meshNormals.add(normalA);
        meshTexCoords.addAll(Arrays.asList(texCoords));

        final int [] indexes = backFace ? 
                new int[] { 2,0,1, 1,3,2 } : //Back Face
                new int[] { 2,3,1, 1,0,2 }; // Front Face
        

        for (int i : indexes) {
            this.meshIndexes.add(meshVerts.size() + i);
        }
        
        this.meshVerts.addAll(Arrays.asList(vertices));
    }
    
    private void processSlopeShape(Block block) {
        int[] indexes = null;
        Vector3f [] vertices = null;
        Vector3f normalA = null;             
        Vector3f point1 = null;
        Vector3f point2 = null;
        
        switch (block.shape) {
        case SLOPE_SOUTH:
             vertices = new Vector3f[18];
            
            // Bottom Plane
            vertices[0] = new Vector3f(block.x,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[1] = new Vector3f(block.x+1,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[2] = new Vector3f(block.x,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[3] = new Vector3f(block.x+1,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            meshTexCoords.add(new Vector3f(0,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(0,0, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,0, block.getBlockTextureId()));

//            texCoords[2] = new Vector3f(0,0, block.getBlockTextureId());
//        texCoords[3] = new Vector3f(1,0, block.getBlockTextureId());
//        texCoords[0] = new Vector3f(0,1, block.getBlockTextureId());
//        texCoords[1] = new Vector3f(1,1, block.getBlockTextureId());
//        
//        texCoords[6] = new Vector3f(0,0, block.getBlockTextureId());
//        texCoords[7] = new Vector3f(1,0, block.getBlockTextureId());
//        texCoords[4] = new Vector3f(0,1, block.getBlockTextureId());
//        texCoords[5] = new Vector3f(1,1, block.getBlockTextureId());        
            //Top Sloped Plane
            vertices[4] = new Vector3f(block.x,block.y+1,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[5] = new Vector3f(block.x+1,block.y+1,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[6] = new Vector3f(block.x,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[7] = new Vector3f(block.x+1,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            meshTexCoords.add(new Vector3f(0,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(0,0, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,0, block.getBlockTextureId()));
            
            // Right Side Plane
            vertices[8] = new Vector3f(block.x+1,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[9] = new Vector3f(block.x+1,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[10] = new Vector3f(block.x+1,block.y+1,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            meshTexCoords.add(new Vector3f(0,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(0,0, block.getBlockTextureId()));

            // Left Side Plane
            vertices[11] = new Vector3f(block.x,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[12] = new Vector3f(block.x,block.y+1,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[13] = new Vector3f(block.x,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            meshTexCoords.add(new Vector3f(0,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(0,0, block.getBlockTextureId()));

            // Back Plane
            vertices[14] = new Vector3f(block.x,block.y+1,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[15] = new Vector3f(block.x,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[16] = new Vector3f(block.x+1,block.y+1,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[17] = new Vector3f(block.x+1,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            meshTexCoords.add(new Vector3f(0,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(0,0, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,0, block.getBlockTextureId()));
            
            indexes = new int[] {
                3,2,0, 0,1,3, //Bottom Plane
                4,5,6, 7,6,5, //Top sloped Plane
                8,9,10, //Right Side Plane
                11,12,13, // Left Side Plane
                14,15,16, 17,16,15 //Back Plane
            };
                        
            point1 = new Vector3f(vertices[2]).subtract(vertices[0]);
            point2 = new Vector3f(vertices[1]).subtract(vertices[0]);
            normalA = point1.cross(point2).normalize();
            for (int i = 0; i < vertices.length; i++) {
                meshNormals.add(normalA);
            }
            
            break;
        case SLOPE_NORTH:
            vertices = new Vector3f[18];
            
            //Bottom Plane
            vertices[0] = new Vector3f(block.x+1,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[1] = new Vector3f(block.x,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[2] = new Vector3f(block.x,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[3] = new Vector3f(block.x+1,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            meshTexCoords.add(new Vector3f(0,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(0,0, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,0, block.getBlockTextureId()));         

            // Top Sloped Plane
            vertices[4] = new Vector3f(block.x,block.y+1,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[5] = new Vector3f(block.x,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[6] = new Vector3f(block.x+1,block.y+1,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[7] = new Vector3f(block.x+1,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            meshTexCoords.add(new Vector3f(0,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(0,0, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,0, block.getBlockTextureId()));
            
            // Right Side Plane
            vertices[8] = new Vector3f(block.x+1,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[9] = new Vector3f(block.x+1,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[10] = new Vector3f(block.x+1,block.y+1,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            meshTexCoords.add(new Vector3f(0,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(0,0, block.getBlockTextureId()));
            
            // Left Side Plane
            vertices[11] = new Vector3f(block.x,block.y+1,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[12] = new Vector3f(block.x,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[13] = new Vector3f(block.x,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            meshTexCoords.add(new Vector3f(0,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(0,0, block.getBlockTextureId()));
            
            // Back Plane
            vertices[14] = new Vector3f(block.x+1,block.y+1,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[15] = new Vector3f(block.x,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[16] = new Vector3f(block.x,block.y+1,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[17] = new Vector3f(block.x+1,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            meshTexCoords.add(new Vector3f(0,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(0,0, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,0, block.getBlockTextureId()));            

            indexes = new int[] {
                0,1,2, 2,3,0, //Bottom Plane
                4,5,6, 5,7,6, //Top sloped Plane
                8,9,10, //Right Side Plane
                11,12,13, // Left Side Plane
                14,15,16, 17,15,14 //Back Plane
            };
            
            point1 = new Vector3f(vertices[2]).subtract(vertices[0]);
            point2 = new Vector3f(vertices[1]).subtract(vertices[0]);
            normalA = point1.cross(point2).normalize();
            for (int i = 0; i < vertices.length; i++) {
                meshNormals.add(normalA);
            }
            break;
        case SLOPE_EAST:
           vertices = new Vector3f[18];
            
            // Bottom Plane
            vertices[0] = new Vector3f(block.x,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[1] = new Vector3f(block.x,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[2] = new Vector3f(block.x+1,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[3] = new Vector3f(block.x+1,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            meshTexCoords.add(new Vector3f(0,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(0,0, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,0, block.getBlockTextureId()));
            
            // Top Sloped Plane
            vertices[4] = new Vector3f(block.x,block.y+1,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[5] = new Vector3f(block.x,block.y+1,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[6] = new Vector3f(block.x+1,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[7] = new Vector3f(block.x+1,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            meshTexCoords.add(new Vector3f(0,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(0,0, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,0, block.getBlockTextureId()));
            
            // Right Side Plane
            vertices[8] = new Vector3f(block.x,block.y+1,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[9] = new Vector3f(block.x,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[10] = new Vector3f(block.x+1,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            meshTexCoords.add(new Vector3f(0,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(0,0, block.getBlockTextureId()));
            
            // Left Side Plane
            vertices[11] = new Vector3f(block.x,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[12] = new Vector3f(block.x,block.y+1,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[13] = new Vector3f(block.x+1,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            meshTexCoords.add(new Vector3f(0,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(0,0, block.getBlockTextureId()));

            // Back Plane
            vertices[14] = new Vector3f(block.x,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[15] = new Vector3f(block.x,block.y+1,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[16] = new Vector3f(block.x,block.y+1,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[17] = new Vector3f(block.x,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            meshTexCoords.add(new Vector3f(0,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(0,0, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,0, block.getBlockTextureId()));            

            indexes = new int[] {
                3,1,0, 0,2,3, //Bottom Plane
                4,5,6, 7,6,5, //Top sloped Plane
                8,9,10, //Right Side Plane
                11,12,13, // Left Side Plane
                14,15,16, 14,17,15 //Back Plane
            };
            
            point1 = new Vector3f(vertices[2]).subtract(vertices[0]);
            point2 = new Vector3f(vertices[1]).subtract(vertices[0]);
            normalA = point1.cross(point2).normalize();
            for (int i = 0; i < vertices.length; i++) {
                meshNormals.add(normalA);
            }
            
            break;
        case SLOPE_WEST:        
            vertices = new Vector3f[18];

            // Bottom Plane
            vertices[0] = new Vector3f(block.x,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[1] = new Vector3f(block.x,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[2] = new Vector3f(block.x+1,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[3] = new Vector3f(block.x+1,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            meshTexCoords.add(new Vector3f(0,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(0,0, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,0, block.getBlockTextureId()));
            
            // Top Sloped Plane
            vertices[4] = new Vector3f(block.x+1,block.y+1,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[5] = new Vector3f(block.x+1,block.y+1,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[6] = new Vector3f(block.x,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[7] = new Vector3f(block.x,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            meshTexCoords.add(new Vector3f(0,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(0,0, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,0, block.getBlockTextureId()));
            
            // Right Side Plane
            vertices[8] = new Vector3f(block.x,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[9] = new Vector3f(block.x+1,block.y+1,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[10] = new Vector3f(block.x+1,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            meshTexCoords.add(new Vector3f(0,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(0,0, block.getBlockTextureId()));

            // Left Side Plane
            vertices[11] = new Vector3f(block.x+1,block.y+1,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[12] = new Vector3f(block.x,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[13] = new Vector3f(block.x+1,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            meshTexCoords.add(new Vector3f(0,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(0,0, block.getBlockTextureId()));

            // Back Plane
            vertices[14] = new Vector3f(block.x+1,block.y+1,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[15] = new Vector3f(block.x+1,block.y+1,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[16] = new Vector3f(block.x+1,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
            vertices[17] = new Vector3f(block.x+1,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
            meshTexCoords.add(new Vector3f(0,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,1, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(0,0, block.getBlockTextureId()));
            meshTexCoords.add(new Vector3f(1,0, block.getBlockTextureId()));

            indexes = new int[] {
                3,1,0, 0,2,3, //Bottom Plane
                4,5,6, 7,6,5, //Top sloped Plane
                8,9,10, //Right Side Plane
                11,12,13, // Left Side Plane
                14,15,16, 17,16,15 //Back Plane
            };
            
            point1 = new Vector3f(vertices[2]).subtract(vertices[0]);
            point2 = new Vector3f(vertices[1]).subtract(vertices[0]);
            normalA = point1.cross(point2).normalize();
            for (int i = 0; i < vertices.length; i++) {
                meshNormals.add(normalA);
            }
            break;
        }
        
        for (int i : indexes) {
            this.meshIndexes.add(meshVerts.size() + i);
        }
        if (vertices != null) {
            meshVerts.addAll(Arrays.asList(vertices));
        }
        
        
                                
//        vertices[0] = new Vector3f(block.x,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
//        vertices[1] = new Vector3f(block.x+1,block.y,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
//        vertices[2] = new Vector3f(block.x,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
//        vertices[3] = new Vector3f(block.x+1,block.y,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
//
//        //Top Plane
//        vertices[4] = new Vector3f(block.x,block.y+1,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
//        vertices[5] = new Vector3f(block.x+1,block.y+1,block.z+1).multLocal(Block.BLOCK_RENDER_SIZE);
//        vertices[6] = new Vector3f(block.x,block.y+1,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
//        vertices[7] = new Vector3f(block.x+1,block.y+1,block.z).multLocal(Block.BLOCK_RENDER_SIZE);
        
        return;
    }
    
    private Vector3f[] getSlopeTextureCoords(Block block) {
        final Vector3f [] texCoords = new Vector3f[8];
        
        texCoords[2] = new Vector3f(0,0, block.getBlockTextureId());
        texCoords[3] = new Vector3f(1,0, block.getBlockTextureId());
        texCoords[0] = new Vector3f(0,1, block.getBlockTextureId());
        texCoords[1] = new Vector3f(1,1, block.getBlockTextureId());
        
        texCoords[6] = new Vector3f(0,0, block.getBlockTextureId());
        texCoords[7] = new Vector3f(1,0, block.getBlockTextureId());
        texCoords[4] = new Vector3f(0,1, block.getBlockTextureId());
        texCoords[5] = new Vector3f(1,1, block.getBlockTextureId());
        
        return texCoords;
    }
    
    public void addSlopesToMesh() {
        
        
        for (Block block : chunk.blocks.values()) {
            if (block.shape.equals(BlockShape.BOX)) {
                continue;
            }
                        
            this.processSlopeShape(block);
          //  Log.debug("BlockType:"+block.type+" slope:"+block.shape);
           // break;
        }
        
       
        
        

        
//        for (Block b : chunk.blocks.values()) {
//            if (b.shape.equals(BlockShape.SLOPE_WEST)) {
//                
//            }
//        } 
    }
    
    public void addMeshToScene() {
        Mesh mesh = new Mesh();

        Vector3f[] vertices = new Vector3f[this.meshVerts.size()];
        for (int i = 0; i < this.meshVerts.size(); i++) {
            vertices[i] = this.meshVerts.get(i);
        }
        
        Vector3f[] texCoords = new Vector3f[this.meshTexCoords.size()];
        for (int i = 0; i < this.meshTexCoords.size(); i++) {
            texCoords[i] = this.meshTexCoords.get(i);
        }
        
        Vector3f[] normals = new Vector3f[this.meshNormals.size()];
        for (int i = 0; i < this.meshNormals.size(); i++) {
            normals[i] = this.meshNormals.get(i);
        }
        
        int[] indexes = new int[this.meshIndexes.size()];
        for (int i = 0; i < this.meshIndexes.size(); i++) {
            indexes[i] = this.meshIndexes.get(i);
        }
        
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 3, BufferUtils.createFloatBuffer(texCoords));
        mesh.setBuffer(VertexBuffer.Type.Index,    3, indexes);
        mesh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
        mesh.updateBound();
        
        AssetManager assetManager = ((Boxel)Boxel.getInstance()).getAssetManager();
        
        chunk.geo = new Geometry("ChunkMesh", mesh);
        Material mat = new Material(assetManager, "Shaders/LightArray.j3md"); 
        mat.setTexture("DiffuseArray", TextureManager.terrianTextures);
        
    //    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"); 
      //  mat.setColor("Color", ColorRGBA.Red);
        
        mat.getAdditionalRenderState().setWireframe(false);
       // mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
       // chunk.geo.setCullHint(Spatial.CullHint.Never);
        materials.add(mat);
        chunk.geo.setMaterial(mat);
        chunk.geo.setLocalTranslation(chunk.x*CHUNK_SIZE, 0, chunk.z*CHUNK_SIZE);
        
        Boxel.mainScene.attachChild(chunk.geo);
    }
     
    /**
     *
     */
    void greedy() {

        /*
         * These are just working variables for the algorithm – almost all taken
         * directly from Mikola Lysenko’s javascript implementation.
         */
        int i, j, k, l, w, h, u, v, n;
        VoxelFace.VoxelSide side = VoxelFace.VoxelSide.SOUTH;
        
        final int[] x = new int []{0,0,0};
        final int[] sizes = new int[]{0,0,0};
        final int[] q = new int []{0,0,0};
        final int[] du = new int[]{0,0,0};
        final int[] dv = new int[]{0,0,0};         

        sizes[0] = CHUNK_SIZE;
        sizes[1] = YMAX;
        sizes[2] = CHUNK_SIZE;
        
        /*
         * We create a mask – this will contain the groups of matching voxel faces
         * as we proceed through the chunk in 6 directions – once for each face.
         */

        /*
         * These are just working variables to hold two faces during comparison.
         */
        VoxelFace voxelFace, voxelFace1;

        /**
         * We start with the lesser-spotted boolean for-loop (also known as the old flippy floppy).
         *
         * The variable backFace will be TRUE on the first iteration and FALSE on the second – this allows
         * us to track which direction the indices should run during creation of the quad.
         *
         * This loop runs twice, and the inner loop 3 times – totally 6 iterations – one for each
         * voxel face.
         */
        for (boolean backFace = true, b = false; b != backFace; backFace = backFace && b, b = !b) { 

            /*
             * We sweep over the 3 dimensions – most of what follows is well described by Mikola Lysenko
             * in his post – and is ported from his Javascript implementation.  Where this implementation
             * diverges, I’ve added commentary.
             */
            for(int d = 0; d < 3; d++) {

                u = (d + 1) % 3;
                v = (d + 2) % 3;
                
                final VoxelFace[] mask = new VoxelFace [sizes[u] * sizes[v]];

                x[0] = 0;
                x[1] = 0;
                x[2] = 0;

                q[0] = 0;
                q[1] = 0;
                q[2] = 0;
                q[d] = 1;

                /*
                 * Here we’re keeping track of the side that we’re meshing.
                 */
                if (d == 0)      { side = backFace ? VoxelFace.VoxelSide.WEST   : VoxelFace.VoxelSide.EAST;  }
                else if (d == 1) { side = backFace ? VoxelFace.VoxelSide.BOTTOM : VoxelFace.VoxelSide.TOP;   }
                else if (d == 2) { side = backFace ? VoxelFace.VoxelSide.SOUTH  : VoxelFace.VoxelSide.NORTH; }                

                /*
                 * We move through the dimension from front to back
                 */
                for(x[d] = -1; x[d] < sizes[d];) {

                    /*
                     * ——————————————————————-
                     *   We compute the mask
                     * ——————————————————————-
                     */
                    n = 0;

                    for(x[v] = 0; x[v] < sizes[v]; x[v]++) {

                        for(x[u] = 0; x[u] < sizes[u]; x[u]++) {

                            /*
                             * Here we retrieve two voxel faces for comparison.
                             */
                            voxelFace  = (x[d] >= 0 )             ? chunk.getVoxelFace(x[0], x[1], x[2], side)                      : null;
                            voxelFace1 = (x[d] < sizes[d] - 1) ?    chunk.getVoxelFace(x[0] + q[0], x[1] + q[1], x[2] + q[2], side) : null;

                            /*
                             * Note that we’re using the equals function in the voxel face class here, which lets the faces
                             * be compared based on any number of attributes.
                             *
                             * Also, we choose the face to add to the mask depending on whether we’re moving through on a backface or not.
                             */
                            mask[n++] = ((voxelFace != null && voxelFace1 != null && voxelFace.equals(voxelFace1)))
                                        ? null
                                        : backFace ? voxelFace1 : voxelFace;
                        }
                    }

                    x[d]++;

                    /*
                     * Now we generate the mesh for the mask
                     */
                    n = 0;

                    for(j = 0; j < sizes[v]; j++) {

                        for(i = 0; i < sizes[u];) {

                            if(mask[n] != null) {

                                /*
                                 * We compute the width
                                 */
                                for(w = 1; i + w < sizes[u] && mask[n + w] != null && mask[n + w].equals(mask[n]); w++) {}

                                /*
                                 * Then we compute height
                                 */
                                boolean done = false;

                                for(h = 1; j + h < sizes[v]; h++) {

                                    for(k = 0; k < w; k++) {

                                        if(mask[n + k + h * sizes[u]] == null || !mask[n + k + h * sizes[u]].equals(mask[n])) { done = true; break; }
                                    }

                                    if(done) { break; }
                                }

                                /*
                                 * Here we check the “transparent” attribute in the VoxelFace class to ensure that we don’t mesh
                                 * any culled faces.
                                 */
                                if (!mask[n].transparent && mask[n].block.shape.equals(BlockShape.BOX)) {
                                    /*
                                     * Add quad
                                     */
                                    x[u] = i;
                                    x[v] = j;

                                    du[0] = 0;
                                    du[1] = 0;
                                    du[2] = 0;
                                    du[u] = w;

                                    dv[0] = 0;
                                    dv[1] = 0;
                                    dv[2] = 0;
                                    dv[v] = h;

                                    /*
                                     * And here we call the quad function in order to render a merged quad in the scene.
                                     *
                                     * We pass mask[n] to the function, which is an instance of the VoxelFace class containing
                                     * all the attributes of the face – which allows for variables to be passed to shaders – for
                                     * example lighting values used to create ambient occlusion.
                                     */
                                    
                                
                                        quad(new Vector3f(x[0],                 x[1],                   x[2]),
                                             new Vector3f(x[0] + du[0],         x[1] + du[1],           x[2] + du[2]),
                                             new Vector3f(x[0] + du[0] + dv[0], x[1] + du[1] + dv[1],   x[2] + du[2] + dv[2]),
                                             new Vector3f(x[0] + dv[0],         x[1] + dv[1],           x[2] + dv[2]),
                                             w,
                                             h,
                                             mask[n],
                                             backFace,
                                             false);
                                    
                                
                                }

                                /*
                                 * We zero out the mask
                                 */
                                for(l = 0; l < h; ++l) {

                                    for(k = 0; k < w; ++k) { mask[n + k + l * sizes[u]] = null; }
                                }

                                /*
                                 * And then finally increment the counters and continue
                                 */
                                i += w;
                                n += w;

                            } else {

                              i++;
                              n++;
                            }
                        }
                    }
                }
            }
        }
    }

    void process() {
        greedy();
        addSlopesToMesh();
    }
    
}
