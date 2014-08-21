/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.avrgaming.boxel.world;


import com.avrgaming.boxel.world.Block.BlockSide;
import com.avrgaming.boxel.world.VoxelFace.VoxelSide;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Ryan
 */
public class Chunk {
    
    public static final int CHUNK_SIZE = 16;
    public static final int YMAX = 128;
    public static final int BLOCKS_IN_CHUNK = CHUNK_SIZE*YMAX*CHUNK_SIZE;
    public static final int WATER_LEVEL = 10;
    
    /* private simple geometry information */
    public static final int VERTS_IN_CUBE = 8;
    public static final int TRIANGLES_IN_FACE = 2;
    public static final int VERTS_IN_TRIANGLE = 3;
    public static final int FACES_IN_CUBE = 6;
    public static final int INDEXES_SIZE =  BLOCKS_IN_CHUNK*
                                     FACES_IN_CUBE*
                                     TRIANGLES_IN_FACE*
                                     VERTS_IN_TRIANGLE;
    public int x;
    public int z;
    
    public boolean loaded = false;
    public boolean visible = false;
    MeshHelper meshHelper;
    Geometry geo;
    Geometry waterGeo;
    

    HeightMap map;
        
    
    public static ArrayList<Material> materials = new ArrayList<Material>();
    public static BlockCoord bcoord = new BlockCoord(0,0,0);

    
    public HashMap<BlockCoord, Block> blocks = new HashMap<BlockCoord, Block>();
    
    public Chunk(int cx, int cz) {
        this.x = cx;
        this.z = cz;
    }
    
    public TaskChunkLoad loadTask = null;
    public void loadAsync() {
        if (loadTask == null) {
            loadTask = new TaskChunkLoad(this);
            loadTask.start();
        }
    }
    
    public boolean isLoading() {
        if (loadTask == null) {
            return false;
        }
    
        return loadTask.isFinished();
    }
    
    public void load() {       
       // blocks = new Block[CHUNK_SIZE][YMAX][CHUNK_SIZE];
//        for (int y = 0; y < YMAX; y++) {
//            for (int x = 0; x < CHUNK_SIZE; x++) {
//                for (int z = 0; z < CHUNK_SIZE; z++) {
//                    blocks[x][y][z] = new Block(x + (this.x*16), y, z + (this.z*16));
//                }
//            }
//        }
        
        generate();
        
        meshHelper = new MeshHelper(this);
        meshHelper.process();
    }

//    private boolean checkForSlope(BlockCoord bcoord, BlockSide side) {
//        boolean doSlope = true;
//        boolean sideSloped = false;
//        BlockCoord next;
//        Block nextBlock;
//        
//        for (BlockSide s : BlockSide.values()) {
//            if (s.equals(BlockSide.TOP) || s.equals(BlockSide.BOTTOM)) {
//                continue;
//            }
//            
//            
//            next = BlockCoord.getAdjacentBlockCoord(bcoord, s);
//            nextBlock = blocks.get(next);
//            
//            if (nextBlock == null || nextBlock.isAir()) {
//                if (side != s) {
//                    doSlope = false;
//                }
//                
//                sideSloped = true;
//            }
//        }
//        
//        return doSlope && sideSloped;
//    }
    
    public void generate() {
        map = new HeightMap(CHUNK_SIZE*4);
        
        map.simplexF = 0.4f;
        map.simplexF2 = 0.4f;

        map.perturbF = 0.0f;
        map.perturbD = 0.0f;
        map.erodeLayers = 20;
        map.erodeF = 0.05f;
        map.smoothenLayers = 5;
        
        map.generate(x,z);
  
        for (int y = 0; y < YMAX; y++) {
            for (int x = 0; x < CHUNK_SIZE; x += 1) {
                for (int z = 0; z < CHUNK_SIZE; z += 1) {
                    
                    float[] points = new float[4];
                    points[0] = (map.heights[x][z]);
                    points[1] = (map.heights[x+1][z]);
                    points[2] = (map.heights[x][z+1]);
                    points[3] = (map.heights[x+1][z+1]);
                    
                    int height = (int)points[0];
                    if (y <= height || y == 0) {
                        int absWater = Math.abs(y - WATER_LEVEL);

                        BlockType type;
                        if (y < 3) {
                            type = BlockType.getType("Bedrock");
                        } else if (absWater <= 5) {
                            type = BlockType.getType("Sand");
                        } else if (absWater <= 7) {
                           type = BlockType.getType("dirt");
                        } else if (absWater <= 15) {
                            type = BlockType.getType("dirt");
                        } else {
                            type = BlockType.getType("stone");
                        }

    
                        
                        Block b = new Block(x,y,z, points, type);
                        b.shape = Block.BlockShape.BOX;
                        blocks.put(new BlockCoord(x,y,z),b);
                    }
                }
            }
        }

//        /* 
//         * Determine which blocks should be slope blocks
//         * and set them as such here.
//         */
//        BlockCoord bcoord = new BlockCoord(0,0,0);
//        for (Block b : blocks.values()) {
//            BlockCoord next;
//            Block nextBlock;
//            bcoord.x = b.x; bcoord.y = b.y; bcoord.z = b.z;
//            
//            next = BlockCoord.getAdjacentBlockCoord(bcoord, Block.BlockSide.TOP);
//            nextBlock = blocks.get(next);
//            
//            if (nextBlock != null && !nextBlock.isAir()) {
//                b.shape = Block.BlockShape.BOX;
//                continue;
//            }
//            
//            if (checkForSlope(bcoord, BlockSide.NORTH)) {
//                b.shape = Block.BlockShape.SLOPE_NORTH;
//                continue;
//            }
//            
//            if (checkForSlope(bcoord, BlockSide.SOUTH)) {
//                b.shape = Block.BlockShape.SLOPE_SOUTH;
//                continue;
//            }
//                        
//            if (checkForSlope(bcoord, BlockSide.EAST)) {
//                b.shape = Block.BlockShape.SLOPE_EAST;
//                continue;
//            }
//            
//            if (checkForSlope(bcoord, BlockSide.WEST)) {
//                b.shape = Block.BlockShape.SLOPE_WEST;
//                continue;
//            }
//            
//            b.shape = Block.BlockShape.BOX;
//        }
        
        
 
    }
    
    public void unload() {
        if (loaded) {
            geo.removeFromParent();
            System.out.println("Unloading "+this.x+","+this.z);
        }
        //blocks = null;
    }
   


    public boolean isBlockLocationActive(int x, int y, int z) {
        bcoord.x = x; bcoord.y = y; bcoord.z = z;
        Block block = this.blocks.get(bcoord);
        
        if (block == null) {
            return false;
        }
        
        if (block.type.equals(BlockType.getType("Air"))) {
            return false;
        } else {
            return true;
        }
    }
    
  
    
    /**
     * This function returns an instance of VoxelFace containing the attributes for
     * one side of a voxel.  In this simple demo we just return a value from the
     * sample data array.  However, in an actual voxel engine, this function would
     * check if the voxel face should be culled, and set per-face and per-vertex
     * values as well as voxel values in the returned instance.
     *
     * @param x
     * @param y
     * @param z
     * @param face
     * @return
     */
    VoxelFace getVoxelFace(final int x, final int y, final int z, final VoxelSide side) {

        Block block = blocks.get(new BlockCoord(x,y,z));
        if (block == null) {
            block = new Block(x,y,z, new float[4], "Air");
        }
        
        VoxelFace face = new VoxelFace(x,y,z, block, side);

        /*
         * Determine where the half triangles will be 
         * and make them.
         */
        /* XXX TODO cull inner faces. */
        face.transparent = block.isAir();                
        return face;
    }
    
 
    
    /*
     * Iterate through our mesh verts and attempt to build
     * triangles out of the edges. Ideally we should be able to
     * do this while building the quads... actually we need to do
     * this BEFORE we build the quads.. hermm..
     * 
     * To start, there will be at least 4 types of triangles.
     * - slope down NORTH
     * - slope down SOUTH
     * - slope down EAST
     * - slope down WEST
     *       
     */
    public void buildSlopes() {
        
    }
    

    
    
    /*
     * Cleans up after loading/messing around with mesh generation.
     */
    public void finalizeLoad() {
        meshHelper.cleanup();
        meshHelper = null;
        loaded = true;
    }
    
    @Override
    public String toString() {
        return "CHUNK("+x+","+z+")";
    }
    
}
