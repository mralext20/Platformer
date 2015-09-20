package net.atomictissue.platformer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Ground extends Actor {
    private Body body;
    private World world;
    private int width;
    private int height;
    private ShapeRenderer renderer;

    public Body getBody() {
        return body;
    }

    public Ground(int x, int y, int width, int height, World world, ShapeRenderer renderer) {
        this.width = width;
        this.height = height;
        this.renderer = renderer;

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        PolygonShape box = new PolygonShape();
        box.setAsBox(width, height);

        Fixture fixture = body.createFixture(box, 0.0f);
        fixture.setUserData("ground");

        box.dispose();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.valueOf("655128FF"));
        renderer.rect(body.getPosition().x - (width), body.getPosition().y - (height), width * 2, height * 2);
        renderer.end();
        batch.begin();
    }
}
