package ca.nerdnet.gdxdemo.shader.ui;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Created by colin on 7/25/17.
 */

public class Panel extends Table {
    public static final int ATTACH_NONE = 0;
    public static final int ATTACH_TOP = 1;
    public static final int ATTACH_BOTTOM = 2;
    public static final int ATTACH_LEFT = 3;
    public static final int ATTACH_RIGHT = 4;

    public float hideTime = 0.6f;
    private float hideDepth = 400f;

    private boolean inited = false;
    private boolean busy = true;
    private boolean stateHidden = false;
    private float nominalX, nominalY;
    private float hiddenX, hiddenY;
    private int attachment;

    public Panel() {
        super();
    }

    public Panel(Skin skin) {
        super(skin);
    }

    public void activate() {
        if(!inited) {
            nominalX = getX();
            nominalY = getY();
            busy = false;
            refreshCoords();
            inited=true;
        }
    }

    public void setHidden() {
        setX(hiddenX);
        setY(hiddenY);
        stateHidden = true;
    }

    public boolean isHidden() { return stateHidden; }
    public boolean isBusy() { return busy; }

    public void setAttachedEdge(int attach) { attachment = attach; }

    public void setHideTime(float time) {
        hideTime = time;
    }
    public void setHideDepth(float depth) {
        hideDepth = depth;
    }

    public void transitHidden(boolean hide) {
        if(!busy) {
            if(stateHidden != hide) {
                if(hide) {
                    busy=true;
                    addAction(
                            Actions.sequence(
                                    Actions.moveTo(hiddenX, hiddenY, hideTime, Interpolation.exp5In),
                                    Actions.run(new Runnable() {
                                        @Override
                                        public void run() {
                                            busy=false;
                                        }
                                    })
                            )
                    );
                } else {
                    busy=true;
                    addAction(
                            Actions.sequence(
                                    Actions.moveTo(nominalX, nominalY, hideTime, Interpolation.circleOut),
                                    Actions.run(new Runnable() {
                                        @Override
                                        public void run() {
                                            busy=false;
                                        }
                                    })
                            )
                    );
                }
                stateHidden = hide;
            }
        }
    }

    // This method may be poorly named
    private void refreshCoords() {
        if (attachment == ATTACH_BOTTOM) {
            hiddenX = nominalX;
            hiddenY = nominalY - hideDepth;
        } else if (attachment == ATTACH_TOP) {
            hiddenX = nominalX;
            hiddenY = nominalY + hideDepth;
        } else if (attachment == ATTACH_LEFT) {
            hiddenX = nominalX - hideDepth;
            hiddenY = nominalY;
        } else if (attachment == ATTACH_RIGHT) {
            hiddenX = nominalX + hideDepth;
            hiddenY = nominalY;
        } else if (attachment == ATTACH_NONE) {

        }
    }
}
