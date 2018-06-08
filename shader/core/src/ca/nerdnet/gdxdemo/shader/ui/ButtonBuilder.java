package ca.nerdnet.gdxdemo.shader.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import ca.nerdnet.brucie.core.BrucieGame;
import ca.nerdnet.brucie.core.BrucieListener;

/**
 * Created by colin on 7/25/17.
 */

public class ButtonBuilder {
    private static final String TAG = "BUTTONBUILDER";

    private final BrucieGame myGame;
    private final Skin mySkin;
    private final BrucieListener myListener;

    private String myText;
    private Image myIcon;
    private String myActionName;
    private String myLabelStyle="default";
    private String myButtonStyle="default";

    public ButtonBuilder(BrucieGame game, Skin skin, BrucieListener listener) {
        myGame = game;
        mySkin = skin;
        myListener = listener;
    }

    public ButtonBuilder reset() {
        myText=null;
        myIcon=null;
        myActionName=null;
        return this;
    }

    public Actor build() {
        Actor rv = null;

        if(myText != null) {
            Gdx.app.log(TAG,"Button - " + myText);
            if(myActionName != null) {
                Button b = new Button(mySkin, myButtonStyle);
                b.add(new Label(myText, mySkin,myLabelStyle));
                b.addListener(new ButtonEventAdapter(myListener,myActionName));
                rv = b;
            } else {
                rv = new Label(myText,mySkin,myLabelStyle);
            }
        } else {
            Gdx.app.log(TAG,"Image Button");
            Button b = new Button(myIcon,mySkin, myButtonStyle);
            b.addListener(new ButtonEventAdapter(myListener,myActionName));
            rv = b;
        }

        return rv;
    }

    public ButtonBuilder setText(String text) {
        myText = text;
        return this;
    }

    public ButtonBuilder setIcon(Image icon) {
        myIcon = icon;
        return this;
    }

    public ButtonBuilder setActionName(String actionName) {
        myActionName = actionName;
        return this;
    }

    public ButtonBuilder setButtonStyle(String style) {
        myButtonStyle = style;
        return this;
    }
    public ButtonBuilder setLabelStyle(String style) {
        myLabelStyle = style;
        return this;
    }
}
