package ca.nerdnet.brucie.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Iterator;

/**
 * Wranglers are a way of obtaining a platform or or game specific implementation
 * of an Interface.
 *
 * @param <T>
 */
public class Wrangler<T> {
    private static final String TAG="WRANGLER";
    protected ObjectMap<String, String> classnameMap;

    protected BrucieGame myGame;

    private String jsonFile = null;

    /** Constructor. Note: We need to give a reference to the BrucieGame here
     * because it is assumed that the implementations we will be managing
     * will need a handle to the main game object.
     * @param game
     */
    public Wrangler(BrucieGame game) {
        myGame = game;
        classnameMap = new ObjectMap<String, String>();
    }

    /** Set name of JSON file which provides a static map to
     * appropriate implementation classes.  See example JSON file.
     *
     * @param internalFilename libgdx internal file name
     */
    public void setJsonFile(String internalFilename) {
        jsonFile = internalFilename;
    }

    /** Initialize the Wrangler, and load up whatever needs loading.
     *
     */
    public void initializeWrangleables() {
        if(jsonFile != null) {
            Json json = new Json();
            FileHandle fh = Gdx.files.internal(jsonFile);
            Array<JsonValue> defs = json.fromJson(Array.class, fh);
            Iterator<JsonValue> iter = defs.iterator();
            while(iter.hasNext()) {
                JsonValue mapdef = iter.next();
                WrangleDef wdef = json.readValue(WrangleDef.class,mapdef);

                if(wdef.name != null)
                    classnameMap.put(wdef.name,wdef.classname);
            }
        }
    }

    /** Register a classname dynamically, as opposed to by JSON file.
     *
     * @param name Name to use as the key value
     * @param classname full classname of the implementation
     */
    public void registerWrangleable(String name, String classname) {
        classnameMap.put(name, classname);
    }

    /** Get all keys.
     *
     * @return
     */
    public Array<String> getKeys() {
        return classnameMap.keys().toArray();
    }

    /** Get classname for given key.
     *
     * @param key
     * @return
     */
    public String getClassname(String key) {
        return classnameMap.get(key);
    }

    /** Return a fully initialized instance of the class denoted by the key value.
     *
     * @param name key value
     * @param param parameter string with which to initialize. May be null.
     * @return
     */
    public T getWrangledInstance(String name,String param) {
        String classname = classnameMap.get(name);

        if(classname != null) {
            try {
                Gdx.app.log(TAG,"Wrangling class '"+classname+"' as "+name);
                T inst = (T) Class.forName(classname).getConstructor().newInstance();
                if (inst instanceof WrangledObject) {
                    ((WrangledObject) inst).configure(myGame,param);
                }
                return inst;
            } catch (Exception e ) {
                // Couldn't instantiate
                Gdx.app.log(TAG,"Unable to instantiate: " + classname);
            }
        } else {
            Gdx.app.log(TAG,"No class found for " + name);
        }
        return null;

    }
}
