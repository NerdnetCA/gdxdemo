package ca.nerdnet.brucie.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Wrangler for singleton classes.
 */

public class SingletonWrangler<T> extends Wrangler<T> {
    private static final String TAG = "SINGLETONWRANGLER";
    protected ObjectMap<String, T> objectMap;

    public SingletonWrangler(BrucieGame game) {
        super(game);
        objectMap = new ObjectMap<String, T>();
    }

    public void registerWrangleable(String name, T inst) {
        objectMap.put(name, inst);
    }

    public T getWrangledInstance(String name,String param) {
        T inst = objectMap.get(name);

        if(inst != null) return inst;

        String classname = classnameMap.get(name);

        if(classname != null) {
            try {
                Gdx.app.log(TAG,"Wrangling class '"+classname+"' as "+name);
                inst = (T) Class.forName(classname).getConstructor().newInstance();
                if (inst instanceof WrangledObject) {
                    ((WrangledObject) inst).configure(myGame,param);
                }
                objectMap.put(name,inst);
                return inst;
            } catch (Exception e ) {
                // Couldn't instantiate
                Gdx.app.log(TAG,"Unable to instantiate " + classname);
            }
        } else {
            Gdx.app.log(TAG,"No class found for " + name);
        }
        return null;
    }
}
