package ca.nerdnet.brucie.core;

/**
 * Created by colin on 7/25/17.
 */

/** If a class implements WrangledObject, a Wrangler will call the configure
 * method after the class constructor.
 */
public interface WrangledObject {
    public void configure(BrucieGame game, String param);
}
