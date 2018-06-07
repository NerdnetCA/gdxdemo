package ca.nerdnet.gdxdemo.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ca.nerdnet.brucie.core.BrucieConfig;
import ca.nerdnet.brucie.core.Scene;

public class MainScene extends Scene {
    private Stage myStage;

    @Override
    public void preload() {

    }

    @Override
    public void show() {
        OrthographicCamera cam = new OrthographicCamera();
        cam.setToOrtho(false, BrucieConfig.getVWidth(), BrucieConfig.getVHeight());
        FitViewport vp = new FitViewport(BrucieConfig.getVWidth(),BrucieConfig.getVHeight(),cam);
        myStage = new Stage();
        Gdx.input.setInputProcessor(myStage);
        myStage.setViewport(vp);

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

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
}
