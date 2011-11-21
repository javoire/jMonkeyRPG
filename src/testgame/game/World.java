/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testgame.game;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import java.util.ArrayList;
import java.util.List;
import jme3tools.converters.ImageToAwt;

/**
 *
 * @author jo_da12
 */
public class World extends AbstractAppState {
    
    private TerrainQuad terrain;
    private AssetManager assetManager;
    private Node rootNode;
    private Application app;
    Material mat_terrain;
    
    public World (Application app, Node rootNode) {
        this.rootNode = rootNode;
        this.app = app;
        this.assetManager = this.app.getAssetManager();
    }

    public void initTerrain() {
        
        /** 1. Create terrain material and load four textures into it. */
        mat_terrain = new Material(assetManager, 
                "Common/MatDefs/Terrain/Terrain.j3md");

        /** 1.1) Add ALPHA map (for red-blue-green coded splat textures) */
        mat_terrain.setTexture("Alpha", assetManager.loadTexture(
                "Textures/Terrain/splat/alphamap.png"));

        /** 1.2) Add GRASS texture into the red layer (Tex1). */
        Texture grass = assetManager.loadTexture(
                "Textures/Terrain/splat/grass.jpg");
        grass.setWrap(WrapMode.Repeat);
        mat_terrain.setTexture("Tex1", grass);
        mat_terrain.setFloat("Tex1Scale", 64f);

        /** 1.3) Add DIRT texture into the green layer (Tex2) */
        Texture dirt = assetManager.loadTexture(
                "Textures/Terrain/splat/dirt.jpg");
        dirt.setWrap(WrapMode.Repeat);
        mat_terrain.setTexture("Tex2", dirt);
        mat_terrain.setFloat("Tex2Scale", 32f);

        /** 1.4) Add ROAD texture into the blue layer (Tex3) */
        Texture rock = assetManager.loadTexture(
                "Textures/Terrain/splat/road.jpg");
        rock.setWrap(WrapMode.Repeat);
        mat_terrain.setTexture("Tex3", rock);
        mat_terrain.setFloat("Tex3Scale", 128f);

        /** 2. Create the height map */
        AbstractHeightMap heightmap = null;
        Texture heightMapImage = assetManager.loadTexture(
                "Textures/Terrain/splat/mountains512.png");
        heightmap = new ImageBasedHeightMap(
        ImageToAwt.convert(heightMapImage.getImage(), false, true, 0));

        heightmap.load();

        /** 3. We have prepared material and heightmap. 
         * Now we create the actual terrain:
         * 3.1) Create a TerrainQuad and name it "my terrain".
         * 3.2) A good value for terrain tiles is 64x64 -- so we supply 64+1=65.
         * 3.3) We prepared a heightmap of size 512x512 -- so we supply 512+1=513.
         * 3.4) As LOD step scale we supply Vector3f(1,1,1).
         * 3.5) We supply the prepared heightmap itself.
         */
        int patchSize = 65;
        terrain = new TerrainQuad("my terrain", patchSize, 513, heightmap.getHeightMap());

        /** 4. We give the terrain its material, position & scale it, and attach it. */
        terrain.setMaterial(mat_terrain);
        terrain.setLocalTranslation(0, -100, 0);
        terrain.setLocalScale(2f, 1f, 2f);
        rootNode.attachChild(terrain);

        /** 5. The LOD (level of detail) depends on were the camera is: */
        List<Camera> cameras = new ArrayList<Camera>();
        cameras.add(app.getCamera());
        TerrainLodControl control = new TerrainLodControl(terrain, cameras);
        terrain.addControl(control); 
 
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
}


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package testgame.game;
//
//import com.jme3.app.Application;
//import com.jme3.app.state.AbstractAppState;
//import com.jme3.asset.AssetManager;
//import com.jme3.material.Material;
//import com.jme3.renderer.Camera;
//import com.jme3.scene.Node;
//import com.jme3.terrain.geomipmap.TerrainLodControl;
//import com.jme3.terrain.geomipmap.TerrainQuad;
//import com.jme3.terrain.heightmap.AbstractHeightMap;
//import com.jme3.terrain.heightmap.ImageBasedHeightMap;
//import com.jme3.texture.Texture;
//import com.jme3.texture.Texture.WrapMode;
//import java.util.ArrayList;
//import java.util.List;
//import jme3tools.converters.ImageToAwt;
//
///**
// *
// * @author jo_da12
// */
//public class World extends AbstractAppState {
//      
//
//    public World (Application app, Node rootNode) {
//        this.rootNode = rootNode;
//        this.app = app;
//        assetManager = app.getAssetManager();
//    }
//
//    public static void main(String[] args) {
//        
//    }
//    
//    public void initTerrain() {
//        
//        /** 1. Create terrain material and load four textures into it. */
//        mat_terrain = new Material(assetManager, 
//                "Common/MatDefs/Terrain/Terrain.j3md");
//
//        /** 1.1) Add ALPHA map (for red-blue-green coded splat textures) */
//        mat_terrain.setTexture("Alpha", assetManager.loadTexture(
//                "Textures/Terrain/splat/alphamap.png"));
//
//        /** 1.2) Add GRASS texture into the red layer (Tex1). */
//        Texture grass = assetManager.loadTexture(
//                "Textures/Terrain/splat/grass.jpg");
//        grass.setWrap(WrapMode.Repeat);
//        mat_terrain.setTexture("Tex1", grass);
//        mat_terrain.setFloat("Tex1Scale", 64f);
//
//        /** 1.3) Add DIRT texture into the green layer (Tex2) */
//        Texture dirt = assetManager.loadTexture(
//                "Textures/Terrain/splat/dirt.jpg");
//        dirt.setWrap(WrapMode.Repeat);
//        mat_terrain.setTexture("Tex2", dirt);
//        mat_terrain.setFloat("Tex2Scale", 32f);
//
//        /** 1.4) Add ROAD texture into the blue layer (Tex3) */
//        Texture rock = assetManager.loadTexture(
//                "Textures/Terrain/splat/road.jpg");
//        rock.setWrap(WrapMode.Repeat);
//        mat_terrain.setTexture("Tex3", rock);
//        mat_terrain.setFloat("Tex3Scale", 128f);
//
//        /** 2. Create the height map */
//        AbstractHeightMap heightmap = null;
//        Texture heightMapImage = assetManager.loadTexture(
//                "Textures/Terrain/splat/mountains512.png");
//        heightmap = new ImageBasedHeightMap(
//        ImageToAwt.convert(heightMapImage.getImage(), false, true, 0));
//
//        heightmap.load();
//
//        /** 3. We have prepared material and heightmap. 
//         * Now we create the actual terrain:
//         * 3.1) Create a TerrainQuad and name it "my terrain".
//         * 3.2) A good value for terrain tiles is 64x64 -- so we supply 64+1=65.
//         * 3.3) We prepared a heightmap of size 512x512 -- so we supply 512+1=513.
//         * 3.4) As LOD step scale we supply Vector3f(1,1,1).
//         * 3.5) We supply the prepared heightmap itself.
//         */
//        int patchSize = 65;
//        terrain = new TerrainQuad("my terrain", patchSize, 513, heightmap.getHeightMap());
//
//        /** 4. We give the terrain its material, position & scale it, and attach it. */
//        terrain.setMaterial(mat_terrain);
//        terrain.setLocalTranslation(0, -100, 0);
//        terrain.setLocalScale(2f, 1f, 2f);
//        rootNode.attachChild(terrain);
//
//        /** 5. The LOD (level of detail) depends on were the camera is: */
//        List<Camera> cameras = new ArrayList<Camera>();
//        cameras.add(app.getCamera());
//        TerrainLodControl control = new TerrainLodControl(terrain, cameras);
//        terrain.addControl(control); 
// 
//    }
//    
//}
