package net.atomictissue.platformer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Player {
    public Body body;

    public Player() {
    }

    public void spawn(float x, float y, World world) {
        BodyDef playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(x, y);
        playerBodyDef.fixedRotation = true;

        body = world.createBody(playerBodyDef);

        PolygonShape playerBox = new PolygonShape();
        playerBox.setAsBox(0.5f, 1.0f);

        FixtureDef playerFixtureDef = new FixtureDef();
        playerFixtureDef.shape = playerBox;
        playerFixtureDef.density = 1.0f;
        playerFixtureDef.friction = 0.0f;

        Fixture playerFixture = body.createFixture(playerFixtureDef);
        playerFixture.setUserData("player");

        playerBox.dispose();

        PolygonShape footSensorBox = new PolygonShape();
        footSensorBox.setAsBox(0.49f, 0.1f, new Vector2(0, -1.1f), 0);

        FixtureDef footFixtureDef = new FixtureDef();
        footFixtureDef.isSensor = true;
        footFixtureDef.shape = footSensorBox;

        Fixture footFixture = body.createFixture(footFixtureDef);
        footFixture.setUserData("foot");
    }

    public void kill(World world) {
        world.destroyBody(body);
    }
}
