package ca.nerdnet.brucie.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * As with SplashScreen, it is best to keep out of this and accept that it works. :)
 *
 */

public class LoadingScreen extends Scene {
    private static final String TAG="LOADINGSCREEN";

    private Texture loadingImg;

    private OrthographicCamera myCam;
    private SpriteBatch myBatch;
    private float myX, myY;

    @Override
    public void show() {
        Gdx.app.log(TAG,"Show loading screen");
        AssetManager am = myGame.getAssetManager();
        loadingImg = am.get("brucie/loading.png",Texture.class);

        myX = 400f-loadingImg.getWidth()/2f;
        myY = 240f-loadingImg.getHeight()/2f;

        myCam = new OrthographicCamera();
        myCam.setToOrtho(false,800,480);
        myCam.update();
        myBatch = new SpriteBatch();
        myBatch.setProjectionMatrix(myCam.combined);
    }

    public boolean isDone() { return true; }
    public boolean isImmortal() {return true;}

    @Override
    public void preload() {
        return;
    }
    @Override
    public void dispose() {
        Gdx.app.log(TAG,"Disposing Scene");
        myBatch.dispose();
        super.dispose();
    }

    @Override
    public void render(float delta) {

        // Render "loading" graphic
        Gdx.gl.glClearColor(0.2f,0.2f,0.2f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        myBatch.begin();
        myBatch.draw(loadingImg,myX,myY);
        myBatch.end();

    }

    @Override
    public void resize(int width, int height) {
        myCam.update();
        myBatch.setProjectionMatrix(myCam.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

}
