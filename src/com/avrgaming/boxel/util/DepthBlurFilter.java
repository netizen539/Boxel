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
package com.avrgaming.boxel.util;
 
import com.jme3.asset.AssetManager;
import com.jme3.post.Filter;
import com.jme3.material.Material;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
 
 
/**
*  A post-processing filter that performs a depth range
*  blur using a scaled convolution filter.
*
*  @version   $Revision: 779 $
*  @author    Paul Speed
*/
public class DepthBlurFilter extends Filter
{
//    private float focusDistance = 10.0f;
//    private float focusRange = 1.0f;
//    private float blurScale = 0.6f;
    
    private float focusDistance = 50.0f;
    private float focusRange = 10.0f;
    private float blurScale = 0.6f;
    
    // These values are set internally based on the
    // viewport size.
    private float xScale;
    private float yScale;

    public DepthBlurFilter()
    {
        super( "Depth Blur" );
    }

    /**
    *  Sets the distance at which objects are purely in focus.
    */
    public void setFocusDistance( float f )
    {
        this.focusDistance = f;
    }

    public float getFocusDistance()
    {
        return focusDistance;
    }

    /**
    *  Sets the range to either side of focusDistance where the
    *  objects go gradually out of focus.  Less than focusDistance – focusRange
    *  and greater than focusDistance + focusRange, objects are maximally "blurred".
    */
    public void setFocusRange( float f )
    {
        this.focusRange = f;
    }

    public float getFocusRange()
    {
        return focusRange;
    }

    /**
    *  Sets the blur amount by scaling the convolution filter up or
    *  down.  A value of 1 (the default) performs a sparse 5×5 evenly
    *  distribubted convolution at pixel level accuracy.  Higher values skip
    *  more pixels, and so on until you are no longer blurring the image
    *  but simply hashing it.
    *
    *  The sparse convolution is as follows:
    *%MINIFYHTMLc3d0cd9fab65de6875a381fd3f83e1b338%*
    *  Where ‘x’ is the texel being modified.  Setting blur scale higher
    *  than 1 spaces the samples out.
    */
    public void setBlurScale( float f )
    {
        this.blurScale = f;
    }

    public float getBlurScale()
    {
    return blurScale;
    }

    @Override
    public boolean isRequiresDepthTexture()
    {
    return true;
    }

    @Override
    public Material getMaterial()
    {
        material.setFloat( "FocusDistance", focusDistance );
        material.setFloat( "FocusRange", focusRange );
        material.setFloat( "XScale", blurScale * xScale );
        material.setFloat( "YScale", blurScale * yScale );
        return material;
    }

//    @Override
//    public void preRender( RenderManager renderManager, ViewPort viewPort )
//    {
//    }

    @Override
    public void initFilter( AssetManager assets, RenderManager renderManager,
    ViewPort vp, int w, int h )
    {
        material = new Material( assets, "Shaders/DepthBlur.j3md" );
        xScale = 1.0f / w;
        yScale = 1.0f / h;
    }

    @Override
    public void cleanUpFilter( Renderer r )
    {
    }
}