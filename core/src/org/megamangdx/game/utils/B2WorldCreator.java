package org.megamangdx.game.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import org.megamangdx.game.MegamanGame;
import org.megamangdx.game.screens.PlayScreen;

public class B2WorldCreator {

    private World world;
    private TiledMap map;

    public B2WorldCreator(PlayScreen playScreen) {
        this.world = playScreen.getWorld();
        this.map = playScreen.getMap();

        //create body and fixture variables
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //create ground bodies/fixtures
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MegamanGame.PPM,
                    (rect.getY() + rect.getHeight() / 2) / MegamanGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / MegamanGame.PPM, rect.getHeight() / 2 / MegamanGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MegamanGame.PPM,
                    (rect.getY() + rect.getHeight() / 2) / MegamanGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / MegamanGame.PPM, rect.getHeight() / 2 / MegamanGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MegamanGame.PPM,
                    (rect.getY() + rect.getHeight() / 2) / MegamanGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / MegamanGame.PPM, rect.getHeight() / 2 / MegamanGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

    }
}