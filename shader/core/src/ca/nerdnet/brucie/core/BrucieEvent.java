package ca.nerdnet.brucie.core;

/**
 * Created by colin on 7/25/17.
 */

public class BrucieEvent {

    public static final int EVENT_GAME_RESET = 0x00;

    public static final int EVENT_ACTION_SELECT = 0x10;

    public int type;
    public String tag;

    private BrucieListener myListener;

    public void pendEvent(BrucieListener listener) { myListener = listener; }

    public boolean fireEvent() {
        if(myListener != null) {
            myListener.onEvent(this);
            return true;
        }
        return false;
    }
}
