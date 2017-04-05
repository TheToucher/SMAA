/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smaa;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 *
 * @author James
 */
public class SMAAFiilter extends Filter {

    private final static Logger logger = Logger.getLogger(SMAAFiilter.class.getName());
    private RenderManager renderManager;
    private ViewPort viewPort;

    public SMAAFiilter() {
        super("SMAA Filter");
    }

   

    
   
       
    private AssetManager assetManager;

    private Filter.Pass SMAA_EdgeDetectionPass;
    private Material SMAA_EdgeDetectionMat;
    private Filter.Pass SMAA_BlendingWeightCalPass;
    private Material SMAA_BlendingWeightCalMat;
    private Filter.Pass SMAA_NeighborhoodBlendingPass;
    private Material SMAA_NeighborhoodBlendingMat;

   
    
    
     @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int screenWidth, int screenHeight) {
       
        
        this.renderManager = renderManager;
        this.viewPort = vp;

        this.assetManager = manager;

        postRenderPasses = new ArrayList<Filter.Pass>();

        SMAA_EdgeDetectionMat = new Material(manager, "/MatDefs/Post/TTTR/SMAA/SMAA_EdgeDetection.j3md");
        SMAA_EdgeDetectionPass = new Filter.Pass() {

            @Override
            public boolean requiresSceneAsTexture() {
                return true;
            }
            
            @Override
           public boolean requiresDepthAsTexture() {
                return true;
            }

        };
        SMAA_EdgeDetectionPass.init(renderManager.getRenderer(), screenWidth, screenHeight, Image.Format.RGBA32F, Image.Format.Depth, 1, SMAA_EdgeDetectionMat);
        SMAA_EdgeDetectionPass.getRenderedTexture().setMagFilter(Texture.MagFilter.Bilinear);
        SMAA_EdgeDetectionPass.getRenderedTexture().setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
        postRenderPasses.add(SMAA_EdgeDetectionPass);

        
        SMAA_BlendingWeightCalMat = new Material(manager, "/MatDefs/Post/TTTR/SMAA/SMAA_BlendingWeightCalc.j3md");
        Texture areaTex = assetManager.loadTexture(new TextureKey("Textures/AreaTexDX10.dds", false));
        areaTex.setMagFilter(Texture.MagFilter.Bilinear);
        areaTex.setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
        SMAA_BlendingWeightCalMat.setTexture("AreaTexture", areaTex);
        Texture searchTex = assetManager.loadTexture(new TextureKey("Textures/SearchTex.dds", false));
        searchTex.setMagFilter(Texture.MagFilter.Bilinear);
        searchTex.setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
        SMAA_BlendingWeightCalMat.setTexture("SearchTexture", searchTex);
        SMAA_BlendingWeightCalPass = new Filter.Pass() {

            @Override
            public boolean requiresSceneAsTexture() {
                return false;
            }
            
            @Override
           public boolean requiresDepthAsTexture() {
                return false;
            }
           
            @Override
            public void beforeRender() {
                SMAA_BlendingWeightCalMat.setTexture("EdgesTexture", SMAA_EdgeDetectionPass.getRenderedTexture());

            }
           

        };
        SMAA_BlendingWeightCalPass.init(renderManager.getRenderer(), screenWidth, screenHeight, Image.Format.RGBA8, Image.Format.Depth, 1, SMAA_BlendingWeightCalMat);
        SMAA_BlendingWeightCalPass.getRenderedTexture().setMagFilter(Texture.MagFilter.Bilinear);
        SMAA_BlendingWeightCalPass.getRenderedTexture().setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
        postRenderPasses.add(SMAA_BlendingWeightCalPass);
        
        
        SMAA_NeighborhoodBlendingMat = new Material(manager, "/MatDefs/Post/TTTR/SMAA/SMAA_NeighborhoodBlending.j3md");
        SMAA_NeighborhoodBlendingPass = new Filter.Pass() {

            @Override
            public boolean requiresSceneAsTexture() {
                return true;
            }
            
            @Override
           public boolean requiresDepthAsTexture() {
                return false;
            }
           
            @Override
            public void beforeRender() {
                SMAA_NeighborhoodBlendingMat.setTexture("WeightsTexture", SMAA_BlendingWeightCalPass.getRenderedTexture());
            }
           

        };
        SMAA_NeighborhoodBlendingPass.init(renderManager.getRenderer(), screenWidth, screenHeight, Image.Format.RGBA8, Image.Format.Depth, 1, SMAA_NeighborhoodBlendingMat);
        postRenderPasses.add(SMAA_NeighborhoodBlendingPass);
        SMAA_NeighborhoodBlendingPass.getRenderedTexture().setMagFilter(Texture.MagFilter.Bilinear);
        SMAA_NeighborhoodBlendingPass.getRenderedTexture().setMinFilter(Texture.MinFilter.BilinearNoMipMaps);

       
        
        //final material
        material = new Material(manager, "Common/MatDefs/Post/Overlay.j3md");
        material.setTexture("Texture", SMAA_NeighborhoodBlendingPass.getRenderedTexture());
        material.setColor("Color", ColorRGBA.White);
                
        
    }

    
    @Override
    protected Image.Format getDefaultPassTextureFormat() {
        return Image.Format.RGBA32F;
    }
    

           
    @Override
    protected boolean isRequiresBilinear() {
        return true;
    }
    
   
    /**
     * Override this method and return false if your Filter does not need the scene texture
     * @return
     */
    @Override
    protected boolean isRequiresSceneTexture() {
        return true;
    }


    @Override
    protected Material getMaterial() {
        return material;
    }

}

