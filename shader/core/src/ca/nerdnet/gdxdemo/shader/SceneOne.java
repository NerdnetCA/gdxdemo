package ca.nerdnet.gdxdemo.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ca.nerdnet.brucie.core.*;
import ca.nerdnet.gdxdemo.shader.ui.ButtonEventAdapter;
import ca.nerdnet.gdxdemo.shader.ui.Panel;
import ca.nerdnet.gdxdemo.shader.ui.PanelBuilder;

public class SceneOne extends Scene implements BrucieListener {
    private static final String TAG = "SCENEONE";
    private ShaderProgram shaderProgram;
    private float fTime;

    private Stage myStage;
    private Skin mySkin;
    private boolean done=false;
    private SpriteBatch myBatch;
    private Texture myBg;
    private Sprite mySprite;
    private Slider ampSlider;
    private Slider speedSlider;
    private FrameBuffer myFB;
    private SpriteBatch bgBatch;

    @Override
    public void preload() {
        loadAsset("ui/ctulublu_ui.json",Skin.class);
        loadAsset("demo_one.png",Texture.class);
    }

    @Override
    public void dispose() {
        myStage.dispose();
        myBatch.dispose();
        bgBatch.dispose();
        super.dispose();
    }

    @Override
    public void show() {
        // This is the camera that we will orient the scene and
        // viewport against.
        OrthographicCamera cam = new OrthographicCamera();
        cam.setToOrtho(false,
                BrucieConfig.getVWidth(),
                BrucieConfig.getVHeight()
        );
        cam.update();

        // Create auto-resizing viewport and initialize the libgdx 'Stage'
        FitViewport vp = new FitViewport(
                BrucieConfig.getVWidth(),
                BrucieConfig.getVHeight(), cam
        );
        myStage = new Stage();
        Gdx.input.setInputProcessor(myStage);
        myStage.setViewport(vp);

        // Get the background image and stuff it into a Sprite instance
        myBg = myGame.getAssetManager().get("demo_one.png",Texture.class);
        mySprite = new Sprite(myBg);
        mySprite.setPosition(0,0);

        // Get ui skin
        mySkin = myGame.getAssetManager().get("ui/ctulublu_ui.json",Skin.class);

        // Create a frame buffer - in this case, we do this to make applying
        // the opengl shader a little bit cleaner.
        myFB = new FrameBuffer(Pixmap.Format.RGBA8888,
                BrucieConfig.getVWidth(),
                BrucieConfig.getVHeight(),
                false);

        // Read in the vertex and fragment shader programs
        String vertexShader = Gdx.files.internal("glsl/vertone.glsl").readString();
        String fragmentShader = Gdx.files.internal("glsl/fragone.fglsl").readString();
        // Compile shader program from vertex and fragment shaders
        shaderProgram = new ShaderProgram( vertexShader, fragmentShader);
        // Sadly, we really do need to set this, because pedantic mode
        // is absolutely ridiculous.
        shaderProgram.pedantic = false;


        // The messy part : build the UI
        Table t = new Table(mySkin);
        t.pad(20);
        t.setFillParent(true);
        t.align(Align.bottomRight);

        Label l = new Label("Control\nampl : speed",mySkin,"small");
        t.add(l).colspan(2);

        t.row().pad(15f);
        ampSlider = new Slider(0f,0.05f,0.002f,true, mySkin);
        speedSlider = new Slider(5f,20f,1f,true, mySkin);
        t.add(ampSlider);
        t.add(speedSlider);
        t.row().pad(15f);
        Button backButton = new Button(mySkin, "default");
        backButton.add(new Label("Back", mySkin));
        backButton.addListener(new ButtonEventAdapter(this,"back"));
        t.add(backButton).colspan(2);
        t.layout();

        // Pop the UI panel into the Stage
        myStage.addActor(t);

        // Initialize the spritebatch which will be used to draw the background
        bgBatch = new SpriteBatch();
        bgBatch.setProjectionMatrix(cam.combined);

        // Initialize the spritebatch which will be used to render the framebuffer.
        // We need to make a new camera for this, changing the yDown parameter,
        // or else the framebuffer renders upside down.
        myBatch = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(true,
                BrucieConfig.getVWidth(),
                BrucieConfig.getVHeight()
        );
        myBatch.setProjectionMatrix(cam.combined);

    }

    @Override
    public void render(float delta) {
        // Update time, taking speed slider into account
        fTime += delta * speedSlider.getValue();

        // Start rendering on the framebuffer.
        myFB.begin();
        Gdx.gl20.glClearColor(0.5f,0.5f,0.5f,1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Draw background
        bgBatch.begin();
        bgBatch.draw(mySprite,mySprite.getX(), mySprite.getY(), mySprite.getWidth(),
                mySprite.getHeight());
        bgBatch.end();
        myFB.end();

        // Now the framebuffer is drawn, and we now render it to the display.
        // This may seem like a superfluous extra step in this case, but
        // framebuffers are extremely useful in more complex effects.
        myBatch.begin();
        myBatch.setShader(shaderProgram);
        shaderProgram.setUniformf("time", fTime);
        shaderProgram.setUniformf("amp", ampSlider.getValue());
        myBatch.draw(myFB.getColorBufferTexture(),0,0);
        myBatch.end();

        // Finally, draw the UI on top.
        myStage.act(delta);
        myStage.draw();
    }

    @Override
    public void resize(int screenWidth, int screenHeight) {
        myStage.getViewport().update(screenWidth,screenHeight,false);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public boolean isDone() { return done; }

    @Override
    public boolean onEvent(BrucieEvent e) {
        String action = e.tag;
        Gdx.app.log(TAG,"CLICK :"+action);
        if("back".equals(action)) {
            myGame.queueScene("BEGIN");
            done=true;
            return true;
        }
        return false;
    }
}
