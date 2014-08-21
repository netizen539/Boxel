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

import com.avrgaming.boxel.util.SimplexNoise;

public class HeightMap {
    
    public int size;
    public float[][] heights;
    
    public float simplexF = 0.2f;
    public float simplexF2 = 0.2f;
    public float perturbF = 2.0f;
    public float perturbD = 2.0f;
    public int erodeLayers = 10;
    public float erodeF = 16.0f;
    public int smoothenLayers = 1;
    public float amplitude = 100.0f;
    
    public HeightMap(int size) {
        this.size = size;
        this.heights = new float[size][size];
    }
    
    public void generate(int chunkX, int chunkZ) {
        this.addSimplexNoise(simplexF, simplexF2, chunkX*16, chunkZ*16);
        this.perturb(perturbF, perturbD);
        for (int i = 0; i < erodeLayers; i++) {
            this.erode(erodeF);
        }
        
        for (int i = 0; i < smoothenLayers; i++) {
            this.smoothen();
        }
        
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                this.heights[x][z] *= amplitude;
            }
        }
    }
    
    public void addSimplexNoise(float f1, float f2, int startX, int startZ) {
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                heights[x][z] += SimplexNoise.noise(f1*(x+startX) / (float)size, f2*(z+startZ) / (float)size, 0);
            }
        }
    }
    
    public void perturb(float f, float d) {
        int u,v;
        float[][] temp = new float[size][size];
        
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                u = x + (int)(SimplexNoise.noise(f*x/(float)size, f*z/(float)size, 0)*d);
                v = z + (int)(SimplexNoise.noise(f*x/(float)size, f*z/(float)size, 1)*d);

                if (u < 0) u = 0;
                if (u >= size) u = size - 1;
                if (v < 0) v = 0;
                if (v >= size) v = size - 1;
                temp[x][z] = heights[u][v];
            }
        }
        heights = temp;
    }
    
    public void erode(float smoothness) {
        for (int x = 1; x < size - 1 ; x++) {
            for (int z = 1; z < size -1; z++ ) {
                float d_max = 0.0f;
                int[] match = { 0, 0 };
                
                for (int u = -1; u <= 1; u++) {
                    for (int v = -1; v <= 1; v++) {
                        if (Math.abs(u) + Math.abs(v) > 0) {
                            float d_i = heights[x][z] - heights[x+u][z+v];
                            if (d_i > d_max) {
                                d_max = d_i;
                                match[0] = u; match[1] = v;
                            }
                        }
                    }
                }
                
                if (0 < d_max && d_max <= (smoothness / (float)size)) {
                    float d_h = 0.5f * d_max;
                    heights[x][z] -= d_h;
                    heights[x + match[0]][z + match[1]] += d_h;
                }
            }
        }
    }
    
    public void smoothen() {
        for (int x = 1; x < size - 1; ++x) {
            for (int z = 1; z < size -1; ++z) {
                float total = 0.0f;
                for (int u = -1; u <= 1; u++) {
                    for (int v = -1; v <= 1; v++) {
                        total += heights[x+u][z+v];
                    }
                }
                
                heights[x][z] = total / 9.0f;
            }
        }
    }
    
}
