package ca.nerdnet.gdxdemo.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ca.nerdnet.brucie.core.BrucieConfig;
import ca.nerdnet.brucie.core.BrucieEvent;
import ca.nerdnet.brucie.core.BrucieListener;
import ca.nerdnet.brucie.core.Scene;

import ca.nerdnet.gdxdemo.shader.ui.*;

public class MainScene extends Scene implements BrucieListener {
    private static final String TAG = "MAINSCENE";
    private Stage myStage;
    private Skin mySkin;
    private boolean done=false;

    @Override
    public void preload() {
        loadAsset("ui/ctulublu_ui.json",Skin.class);
        loadAsset("demo_one.png",Texture.class);
    }

    @Override
    public void show() {
        OrthographicCamera cam = new OrthographicCamera();
        cam.setToOrtho(false, BrucieConfig.getVWidth(), BrucieConfig.getVHeight());
        FitViewport vp = new FitViewport(BrucieConfig.getVWidth(),BrucieConfig.getVHeight(),cam);
        myStage = new Stage();
        Gdx.input.setInputProcessor(myStage);
        myStage.setViewport(vp);

        // Store references to all of our assets.
        AssetManager am = myGame.getAssetManager();
        mySkin = am.get("ui/ctulublu_ui.json",Skin.class);

        // Build the menu
        PanelBuilder pb = new PanelBuilder(myGame, mySkin, this);

        pb.setTitle("Shader Demo 1").setFramePad(30f).setButtonMargin(60f);
        pb.setWidth(400f);

        pb.addButton("Ripple 1","one")
                .addButton("Ripple 2","two")
                .addButton("Value/Sat.", "three");

        Texture bgtex = myGame.getAssetManager().get("demo_one.png");
        Sprite bg = new Sprite(bgtex);
        Image bgim = new Image(bgtex);
        myStage.addActor(bgim);

        Panel p = pb.build();
        myStage.addActor(p);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0,0,0,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        myStage.act(delta);
        myStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        myStage.getViewport().update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    public void dispose() {
        if(myStage != null) myStage.dispose();
        super.dispose();
    }

    public boolean isDone() { return done; }
    
    @Override
    public boolean onEvent(BrucieEvent e) {
        String action = e.tag;
        Gdx.app.log(TAG,"CLICK :"+action);
        if("one".equals(action)) {
            myGame.queueScene("S-ONE");
            done=true;
            return true;
        } else if("two".equals(action)) {
            myGame.queueScene("S-TWO");
            done=true;
            return true;
        } else if("three".equals(action)) {
            myGame.queueScene("S-THREE");
            done=true;
            return true;
        }
        return false;
    }
}
