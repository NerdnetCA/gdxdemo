package ca.nerdnet.brucie.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The westower splash/bootstrap scene.
 *
 * This is not really documented - best to keep your paws out of here.
 *
 */

public class SplashScreen extends Scene {
    private static final String TAG = "SPLASH";

    private static final float bgR = 0f;
    private static final float bgG = 129.0f/256.0f;
    private static final float bgB = 91f/256f;


    // Const
    public static final float DISPLAY_TIME = 1.2f;  // minimum display time.
    public static final float F_WIDTH = 800f;  // This version of Splash works in 800x480
    public static final float F_HEIGHT = 480f; // virtual resolution.

    // Private fields
    private Texture mySplash;
    private OrthographicCamera myCamera;
    private SpriteBatch myBatch;
    private float sx, sy;

    private float accTime;
    private boolean completed;

    @Override
    public void preload() {
        loadAsset("brucie/splash.png",Texture.class);
    }
    @Override
    public void dispose() {
        myBatch.dispose();
        super.dispose();
    }


    public boolean isDone() {
        return completed;
    }

    @Override
    public void show() {

        myCamera = new OrthographicCamera();
        myCamera.setToOrtho(false,F_WIDTH,F_HEIGHT);
        myBatch = new SpriteBatch();

        //mySplash = manager.get(BrucieConfig.asset_splash_img,Texture.class);
        mySplash = myGame.assetManager.get("brucie/splash.png",Texture.class);

        sx = F_WIDTH/2f - mySplash.getWidth()/2f;
        sy = F_HEIGHT/2f - mySplash.getHeight()/2f;
        accTime = 0f;
    }

    @Override
    public void render(float delta) {
        accTime += delta;

        if(accTime > DISPLAY_TIME) {
            if(!completed) {
                completed = true;
            }
        }

        Gdx.gl20.glClearColor(bgR,bgG,bgB,1f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        myBatch.begin();
        if(accTime < 1f) {
            myBatch.setColor(1,1,1,1f * accTime);
        } else {
            myBatch.setColor(1,1,1,1);
        }
        myBatch.draw(mySplash,sx,sy);
        myBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        if(myCamera!=null)myCamera.update();
        myBatch.setProjectionMatrix(myCamera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

}
