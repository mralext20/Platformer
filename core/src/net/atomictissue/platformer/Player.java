package net.atomictissue.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player extends Actor {
    private Body body;
    private World world;
    private int width;
    private int height;
    private boolean onGround;
    private ShapeRenderer renderer = null;

    final float MAX_VELOCITY = 8.0f;
    final float GROUND_ACCELERATION = 0.3f;
    final float AIR_ACCELERATION = 0.1f;
    final float GROUND_FRICTION = 0.9f;
    final float AIR_FRICTION = 0.95f;

    public Body getBody() {
        return body;
    }

    public Player(int x, int y, World world, ShapeRenderer renderer) {
        this.width = 1;
        this.height = 2;
        this.world = world;
        this.renderer = renderer;

        spawn(x, y);
    }

    public void spawn(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);

        PolygonShape box = new PolygonShape();
        box.setAsBox(width, height);

        FixtureDef playerFixtureDef = new FixtureDef();
        playerFixtureDef.shape = box;
        playerFixtureDef.density = 0.25f;
        playerFixtureDef.friction = 0.0f;

        body.createFixture(playerFixtureDef).setUserData("player");

        box.setAsBox(0.99f, 0.1f, new Vector2(0, -2.1f), 0);

        FixtureDef footFixtureDef = new FixtureDef();
        footFixtureDef.isSensor = true;
        footFixtureDef.shape = box;

        body.createFixture(footFixtureDef).setUserData("foot");

        box.dispose();
    }

    public void kill() {
        world.destroyBody(body);
    }

    @Override
    public void act(float delta) {
        float inputXForce = 0;

        Vector2 pos = body.getPosition();

        // apply left impulse
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            inputXForce = (MAX_VELOCITY + body.getLinearVelocity().x);

            if(onGround)
                inputXForce *= GROUND_ACCELERATION;
            else
                inputXForce *= AIR_ACCELERATION;

            body.applyLinearImpulse(-inputXForce, 0.0f, pos.x, pos.y, true);
        }

        // apply right impulse
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            inputXForce = (MAX_VELOCITY - body.getLinearVelocity().x);

            if(onGround)
                inputXForce *= GROUND_ACCELERATION;
            else
                inputXForce *= AIR_ACCELERATION;

            body.applyLinearImpulse(inputXForce, 0.0f, pos.x, pos.y, true);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W) && onGround)
            body.applyLinearImpulse(0.0f, 20.0f, pos.x, pos.y, true);
        else if(!Gdx.input.isKeyPressed(Input.Keys.W) && !onGround && body.getLinearVelocity().y > 10)
            body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y - 1);

        if(inputXForce == 0) {
            if(onGround)
                body.setLinearVelocity(body.getLinearVelocity().x * GROUND_FRICTION, body.getLinearVelocity().y);
            else
                body.setLinearVelocity(body.getLinearVelocity().x * AIR_FRICTION, body.getLinearVelocity().y);
        }

        if(pos.y < -25) {
            kill();
            spawn(-20, 4);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.valueOf("5F7CAFFF"));
        renderer.rect(body.getPosition().x - (width), body.getPosition().y - (height), width * 2, height * 2);
        renderer.end();
        batch.begin();
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
