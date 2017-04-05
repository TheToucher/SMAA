package smaa;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import java.util.*;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
   
    
    @Override
    public void simpleInitApp() {
        
                     
        flyCam.setDragToRotate(true);        
        flyCam.setMoveSpeed(100);
        
        setPauseOnLostFocus(false);
        
        
        
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        float posScale = 40f;
        for (int ii=0; ii<40; ii++) {
            Random r = new Random((long)ii * 897l);
            
            Geometry geom = new Geometry("Box",  new Box(1, 1, 1));
            geom.move(FastMath.nextRandomFloat()*posScale, FastMath.nextRandomFloat()*posScale, FastMath.nextRandomFloat()*posScale);
            geom.move(-posScale/2f,-posScale/2f,-posScale/2f);
            geom.rotate(r.nextFloat()*10f,r.nextFloat()*10f, r.nextFloat()*10f);
            mat = mat.clone();
            mat.setColor("Color", ColorRGBA.randomColor());
            geom.setMaterial(mat);
            
            rootNode.attachChild(geom);
  
        }
        
        
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        
        SMAAFiilter tttf = new SMAAFiilter();
        fpp.addFilter(tttf);

        viewPort.addProcessor(fpp);

    }


}
