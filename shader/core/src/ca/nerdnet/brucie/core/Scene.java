package ca.nerdnet.brucie.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

/**
 * Superclass for the scenes of the game.
 *
 *
 */


public abstract class Scene implements Screen, WrangledObject {
    private static final String TAG = "SCENE";

    // Scene class helps track asset loading.
    private Array<String> assetList;

    protected BrucieGame myGame;

    /**
     * From WrangledObject.
     * @param game
     * @param param
     */
    public void configure(BrucieGame game, String param) {
        myGame = game;
        assetList = new Array<String>(false,16);
    }

    /** Scenes are generally allowed to run until isDone returns false.
     * Therefore, you must override this if you ever want the scene to end.
     * @return
     */
    public boolean isDone() {
        return true;
    }

    /** preload is called to allow the scene to queue up asset loads.
     *
     */
    public abstract void preload();

    /** using loadAsset rather than the libgdx AssetManager directly,
     * allows the Brucie Scene class to do some management for you.
     *
     * @param name
     * @param assetType
     */
    public void loadAsset(String name, Class assetType) {
        myGame.assetManager.load(name, assetType);
        assetList.add(name);
    }

    /** loadAsset, but with parameter.
     *
     * @param name
     * @param assetType
     * @param param
     */
    public void loadAsset(String name, Class assetType, AssetLoaderParameters param) {
        myGame.assetManager.load(name, assetType, param);
        assetList.add(name);
    }

    /**
     * dispose really REALLY needs to be called for Scenes, in order to mark
     * assets for unloading. The hide method will do this.
     */
    @Override
    public void dispose() {
        Gdx.app.log(TAG, "Disposing Scene " + getClass().getName());
        Iterator<String> iter = assetList.iterator();
        while(iter.hasNext()) {
            myGame.assetManager.unload(iter.next());
        }
    }

    /** hide will call dispose. See dispose. If you override this, make sure to call super.
     *
     */
    @Override
    public void hide() {
        dispose();
    }
}
