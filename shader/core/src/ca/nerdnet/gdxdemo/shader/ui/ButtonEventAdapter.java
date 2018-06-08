package ca.nerdnet.gdxdemo.shader.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import ca.nerdnet.brucie.core.BrucieEvent;
import ca.nerdnet.brucie.core.BrucieListener;

/**
 * Created by colin on 7/25/17.
 */

public class ButtonEventAdapter extends ChangeListener {
    private final BrucieListener myListener;
    private final String myTag;

    public ButtonEventAdapter(BrucieListener listener, String tag) {
        myListener = listener;
        myTag = tag;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        BrucieEvent ev = new BrucieEvent();
        ev.tag = myTag;
        myListener.onEvent(ev);

    }
}
