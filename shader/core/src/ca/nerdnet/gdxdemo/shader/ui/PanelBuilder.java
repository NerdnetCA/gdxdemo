package ca.nerdnet.gdxdemo.shader.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import ca.nerdnet.brucie.core.BrucieGame;
import ca.nerdnet.brucie.core.BrucieListener;

/**
 * Created by colin on 7/25/17.
 */

public class PanelBuilder {

    public static final int ORIENTATION_VERTICAL = 1;
    public static final int ORIENTATION_HORIZONTAL = 2;

    private final ButtonBuilder myButtonBuilder;
    private final BrucieGame myGame;
    private final Skin mySkin;
    private final BrucieListener myListener;

    private int myOrientation;

    private float buttonMargin = 16f;
    private String myTitle;
    private float myWidth;
    private int myEdge = Panel.ATTACH_NONE;

    private Array<ButtonSpec> buttonSpecs;
    private int myAlignment = Align.top;
    private String background;
    private float framePad = 32f;

    public PanelBuilder(BrucieGame game, Skin skin, BrucieListener listener) {
        myGame = game;
        mySkin = skin;
        myListener = listener;
        buttonSpecs = new Array<ButtonSpec>();
        myOrientation = ORIENTATION_VERTICAL;
        myButtonBuilder = new ButtonBuilder(myGame,mySkin,myListener);
    }

    public Panel build() {
        Panel rv = null;

        rv = new Panel(mySkin);
        rv.pad(buttonMargin);
        rv.setFillParent(true);
        rv.align(myAlignment);

        Table p = rv;
        if(background != null) {
            p = new Table(mySkin);
            p.pad(framePad);
            p.setBackground(background);
            rv.add(p);
        }

        if(myTitle != null) {
            Label l = new Label(myTitle,mySkin);
            p.add(l);
            p.row();
        }

        for( ButtonSpec buttonSpec : buttonSpecs) {
            myButtonBuilder.reset();
            if(buttonSpec.name != null) {
                myButtonBuilder.setText(buttonSpec.name);
            } else if(buttonSpec.img != null) {
                myButtonBuilder.setIcon(buttonSpec.img);
            }
            if(buttonSpec.action != null) {
                myButtonBuilder.setActionName(buttonSpec.action);
            }

            if(myOrientation == ORIENTATION_VERTICAL) {
                if(myWidth > 0.0f && buttonSpec.name != null)
                    p.add(myButtonBuilder.build()).width(myWidth);
                else
                    p.add(myButtonBuilder.build());
                p.row().padTop(buttonMargin);
            } else {
                p.add(myButtonBuilder.build());
            }
        }

        rv.setAttachedEdge(myEdge);

        return rv;
    }

    public PanelBuilder setAttachedEdge(int edge) {
        myEdge = edge;
        return this;
    }

    public PanelBuilder setTitle(String title) {
        myTitle = title;
        return this;
    }

    public PanelBuilder setButtonStyle(String buttonStyle) {
        myButtonBuilder.setButtonStyle(buttonStyle);
        return this;
    }
    public PanelBuilder setLabelStyle(String labelStyle) {
        myButtonBuilder.setLabelStyle(labelStyle);
        return this;
    }

    public PanelBuilder setButtonMargin(float margin) {
        buttonMargin = margin;
        return this;
    }

    public PanelBuilder setFramePad(float pad) {
        framePad = pad;
        return this;
    }

    public PanelBuilder setWidth(float width) {
        myWidth = width;
        return this;
    }

    public PanelBuilder setOrientation(int orientation) {
        myOrientation = orientation;
        return this;
    }

    public void setAlignment(int align) {
        myAlignment = align;
    }

    public void setBackground(String drawableId) {
        background = drawableId;
    }

    public PanelBuilder addLabel(String text) {
        ButtonSpec spec = new ButtonSpec();
        spec.name = text;
        buttonSpecs.add(spec);
        return this;
    }

    public PanelBuilder addButton(TextureRegion t, String action) {
        ButtonSpec spec = new ButtonSpec();
        spec.img = new Image(t);
        spec.action = action;
        buttonSpecs.add(spec);
        return this;
    }

    public PanelBuilder addButton(String text, String action) {
        ButtonSpec spec = new ButtonSpec();
        spec.name = text;
        spec.action = action;
        buttonSpecs.add(spec);
        return this;
    }

    private class ButtonSpec {
        String name;
        String action;
        Image img;
    }
}
