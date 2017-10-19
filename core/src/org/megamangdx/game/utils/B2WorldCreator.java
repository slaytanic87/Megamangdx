package org.megamangdx.game.utils;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import org.megamangdx.game.MegamanGame;

/**
 * @author Lam
 */
public class B2WorldCreator {

    private World world;
    private TiledMap map;

    public B2WorldCreator(World world, TiledMap map) {
        this.world = world;
        this.map = map;

    }


    public void createBoundaryBoxWithBody(String name, Short categorieBit) {
        //create body and fixture variables
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        MapLayer mapLayer = map.getLayers().get(name);

        if (mapLayer != null) {
            MapObjects mapObjects = mapLayer.getObjects();
            for (MapObject object : mapObjects.getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / MegamanGame.PPM,
                        (rect.getY() + rect.getHeight() / 2) / MegamanGame.PPM);

                body = world.createBody(bdef);

                shape.setAsBox(rect.getWidth() / 2 / MegamanGame.PPM, rect.getHeight() / 2 / MegamanGame.PPM);
                fdef.shape = shape;
                if (categorieBit != null) {
                    fdef.filter.categoryBits = categorieBit;
                }
                body.createFixture(fdef);
            }
        }
    }

}
