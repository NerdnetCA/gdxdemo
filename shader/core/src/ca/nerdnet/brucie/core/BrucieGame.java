package ca.nerdnet.brucie.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

/**
 * Main game handler.
 */

public class BrucieGame implements ApplicationListener {
    private static final int MODE_BOOT = 0;   // We start in boot mode
    private static final int MODE_START = 0x10; // First scene is loading
    private static final int MODE_NORMAL = 0x01; // Normal running mode, rendering a scene
    private static final int MODE_PRELOAD = 0x11;// Rendering a scene, plus preloading one.
    private static final int MODE_CUE = 0x21;    // Preloaded scene is ready, waiting for trigger
    private static final int MODE_LOADING = 0x12;// Current scene done, waiting for next to load.
    private static final String TAG = "BRUCIEGAME";

    protected BrucieConfig brucieConfig;

    private Scene nextScene, currentScene;

    protected Wrangler<Scene> sceneWrangler;
    protected SingletonWrangler<GameFeature> featureWrangler;
    protected AssetManager assetManager;

    private int myMode=0;
    private SplashScreen splash;

    public BrucieGame(BrucieConfig config) {
        brucieConfig = config;
    }

    @Override
    public void create() {
        assetManager = new AssetManager();

        // Initialization of the wranglers happens here because we want to wait
        // for libGdx to be fully set up first.
        sceneWrangler.initializeWrangleables();
        featureWrangler.initializeWrangleables();

        assetManager.load(brucieConfig.loading_img,Texture.class);

        splash = new SplashScreen();
        splash.configure(this,null);
        splash.preload();
        currentScene=splash;

        assetManager.finishLoading();

        splash.show();
        splash.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        queueScene("BEGIN");
        myMode = MODE_START;
    }

    /** Queue a scene to be displayed when the current is done.
     *
     * @param scenename
     */
    public void queueScene(String scenename) {
        if(nextScene != null) {
            // This is a problem..
            // Please do not queue a scene with one already queued!
            // throw new BrucieException(); ?
            Gdx.app.log(TAG,"Attempted to queue scene with scene already queued!");
            return;
        } else {
            Scene s = sceneWrangler.getWrangledInstance(scenename,null);
            nextScene = s;
            s.preload();
            myMode = MODE_PRELOAD;
        }
    }

    public Wrangler<GameFeature> getFeatureWrangler() {
        return featureWrangler;
    }
    public GameFeature getFeature(String name) {
        GameFeature f = featureWrangler.getWrangledInstance(name,null);
        return f;
    }

    public void registerFeature(String name, GameFeature f) {
        featureWrangler.registerWrangleable(name,f);
    }

    @Override
    public void resize(int width, int height) {
        currentScene.resize(width,height);
    }

    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        switch(myMode) {
            case MODE_START:
                // Bootstrap mode - display the splash.
                splash.render(delta);
                if(assetManager.update() && splash.isDone()) {
                    Gdx.app.log(TAG,"Switching to Normal Mode");
                    myMode=MODE_NORMAL;
                    currentScene = nextScene;
                    nextScene=null;
                    splash.hide();
                    splash=null;
                    currentScene.show();
                    resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                }
                break;
            case MODE_NORMAL:
                // Normal running mode.
                currentScene.render(delta);
                break;
            case MODE_PRELOAD:
                // Normal running with a queued scene that may still be loading assets.
                currentScene.render(delta);
                if(assetManager.update()) {
                    if(currentScene.isDone()) {
                        Gdx.app.log(TAG,"Switching to Loading Mode");
                        myMode=MODE_LOADING;
                        currentScene.hide();
                        currentScene = new LoadingScreen();
                        currentScene.configure(this,null);
                        currentScene.show();
                        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                    } else {
                        // When loading is done, switch to CUE
                        Gdx.app.log(TAG,"Switching to CUE Mode");
                        myMode=MODE_CUE;
                    }
                }
                break;
            case MODE_CUE:
                // Next scene is queued up and we are just waiting for the
                // current one to finish.
                currentScene.render(delta);
                if(currentScene.isDone()) {
                    Gdx.app.log(TAG,"CUE Triggered, switching to normal");
                    currentScene.hide();
                    currentScene=nextScene;
                    nextScene=null;
                    currentScene.show();
                    resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                    myMode=MODE_NORMAL;
                }
                break;
            case MODE_LOADING:
                // The current scene is done, but the next one is not done
                // loading assets - display loading screen.
                currentScene.render(delta);
                if(assetManager.update()) {
                    Gdx.app.log(TAG,"Loading done - Switching to Normal Mode");
                    currentScene.hide();
                    currentScene=nextScene;
                    nextScene=null;
                    currentScene.show();
                    resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                    myMode=MODE_NORMAL;
                }
                break;
        }
    }
    @Override
    public void dispose () {
        if (currentScene != null) currentScene.hide();
        // Do we need to do either of these?
        //if (assetManager != null) assetManager.dispose();
        //if (nextScene != null) nextScene.hide();
    }

    @Override
    public void pause () {
        if (currentScene != null) currentScene.pause();
    }

    @Override
    public void resume () {
        if (currentScene != null) currentScene.resume();
    }


    public AssetManager getAssetManager() {
        return assetManager;
    }
}
