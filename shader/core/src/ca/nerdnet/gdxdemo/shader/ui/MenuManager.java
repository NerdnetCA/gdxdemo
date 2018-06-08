package ca.nerdnet.gdxdemo.shader.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.Iterator;

/**
 * Created by colin on 11/15/17.
 */
public class MenuManager extends Actor {
    private static final String TAG = "MENUMANAGER";

    Array<MenuSpec> menuSpecs;

    public void dot() {
        menuSpecs = new Array<MenuSpec>();
        Json json = new Json();
        FileHandle fh = Gdx.files.internal("daleks/mainmenu.json");
        Array<JsonValue> defs = json.fromJson(Array.class, fh);
        Iterator<JsonValue> iter = defs.iterator();
        while(iter.hasNext()) {
            JsonValue def = iter.next();
            menuSpecs.add(json.readValue(MenuSpec.class, def));
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Group g;

    }

    public static class ItemSpec {
        String title;
        String action;
        String menu;
    }

    public static class MenuSpec {
        String id;
        Array<ItemSpec> items;
    }
}
