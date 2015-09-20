package net.atomictissue.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class TheGame extends ApplicationAdapter {
    public static int footContacts = 0;

    private World world;
    private Body groundBody;
    private Body playerBody;

    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;

    private ShapeRenderer renderer;


    @Override
	public void create () {
		world = new World(new Vector2(0, -30), true);

        world.setContactListener(new MyContactListener());

        // create ground
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(-20.0f, -20.0f);

        groundBody = world.createBody(groundBodyDef);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(5.0f, 2.0f);

        Fixture groundFixture = groundBody.createFixture(groundBox, 0.0f);
        groundFixture.setUserData("ground");

        groundBox.setAsBox(18.5f, 2.0f, new Vector2(28.5f, 0.0f), 0.0f);

        groundFixture = groundBody.createFixture(groundBox, 0.0f);
        groundFixture.setUserData("ground");

        groundBox.dispose();

        // create box
        BodyDef playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(-20.0f, 4.0f);
        playerBodyDef.fixedRotation = true;

        playerBody = world.createBody(playerBodyDef);

        PolygonShape playerBox = new PolygonShape();
        playerBox.setAsBox(0.5f, 1.0f);

        FixtureDef playerFixtureDef = new FixtureDef();
        playerFixtureDef.shape = playerBox;
        playerFixtureDef.density = 1.0f;
        playerFixtureDef.friction = 0.0f;

        Fixture playerFixture = playerBody.createFixture(playerFixtureDef);
        playerFixture.setUserData("player");

        playerBox.dispose();

        PolygonShape footSensorBox = new PolygonShape();
        footSensorBox.setAsBox(0.3f, 0.5f, new Vector2(0, -1.5f), 0);

        FixtureDef footFixtureDef = new FixtureDef();
        footFixtureDef.isSensor = true;
        footFixtureDef.shape = footSensorBox;

        Fixture footFixture = playerBody.createFixture(footFixtureDef);
        footFixture.setUserData("foot");

        // initialize camera
        camera = new OrthographicCamera(50, 50);

        // initialize debug renderer
        debugRenderer = new Box2DDebugRenderer();
    }

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debugRenderer.render(world, camera.combined);

        final float MAX_VELOCITY = 10.0f;
        final float GROUND_ACCELERATION = 0.3f;
        final float AIR_ACCELERATION = 0.1f;
        final float GROUND_FRICTION = 0.9f;
        final float AIR_FRICTION = 0.99999f;

        Vector2 vel = playerBody.getLinearVelocity();
        Vector2 pos = playerBody.getPosition();
        float inputXForce = 0;

        boolean onGround = footContacts > 0;


        // apply left impulse, but only if max velocity is not reached yet
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            inputXForce = (MAX_VELOCITY + vel.x);

            if(onGround)
                inputXForce *= GROUND_ACCELERATION;
            else
                inputXForce *= AIR_ACCELERATION;

            playerBody.applyLinearImpulse(-inputXForce, 0.0f, pos.x, pos.y, true);
        }

        // apply right impulse, but only if max velocity is not reached yet
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            inputXForce = (MAX_VELOCITY - vel.x);

            if(onGround)
                inputXForce *= GROUND_ACCELERATION;
            else
                inputXForce *= AIR_ACCELERATION;

            playerBody.applyLinearImpulse(inputXForce, 0.0f, pos.x, pos.y, true);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W) && onGround) {
            playerBody.applyLinearImpulse(0.0f, 6.0f, pos.x, pos.y, true);
        }

        if(inputXForce == 0) {
            if(onGround)
                playerBody.setLinearVelocity(playerBody.getLinearVelocity().x * GROUND_FRICTION, playerBody.getLinearVelocity().y);
            else
                playerBody.setLinearVelocity(playerBody.getLinearVelocity().x * AIR_FRICTION, playerBody.getLinearVelocity().y);
        }

        world.step(1 / 60f, 6, 2);

    }
}
