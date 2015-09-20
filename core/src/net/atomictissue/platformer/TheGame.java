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
    private Player player;

    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;

    private ShapeRenderer renderer;


    @Override
	public void create () {
		world = new World(new Vector2(0, -35), true);

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

        // create player
        player = new Player();
        player.spawn(-20, 4, world);

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

        final float MAX_VELOCITY = 8.0f;
        final float GROUND_ACCELERATION = 0.3f;
        final float AIR_ACCELERATION = 0.1f;
        final float GROUND_FRICTION = 0.9f;
        final float AIR_FRICTION = 0.95f;
        float inputXForce = 0;

        boolean onGround = footContacts > 0;

        Vector2 pos = player.body.getPosition();

        // apply left impulse
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            inputXForce = (MAX_VELOCITY + player.body.getLinearVelocity().x);

            if(onGround)
                inputXForce *= GROUND_ACCELERATION;
            else
                inputXForce *= AIR_ACCELERATION;

            player.body.applyLinearImpulse(-inputXForce, 0.0f, pos.x, pos.y, true);
        }

        // apply right impulse
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            inputXForce = (MAX_VELOCITY - player.body.getLinearVelocity().x);

            if(onGround)
                inputXForce *= GROUND_ACCELERATION;
            else
                inputXForce *= AIR_ACCELERATION;

            player.body.applyLinearImpulse(inputXForce, 0.0f, pos.x, pos.y, true);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W) && onGround)
            player.body.applyLinearImpulse(0.0f, 20.0f, pos.x, pos.y, true);
        else if(!Gdx.input.isKeyPressed(Input.Keys.W) && !onGround && player.body.getLinearVelocity().y > 10)
                player.body.setLinearVelocity(player.body.getLinearVelocity().x, player.body.getLinearVelocity().y - 1);

        if(inputXForce == 0) {
            if(onGround)
                player.body.setLinearVelocity(player.body.getLinearVelocity().x * GROUND_FRICTION, player.body.getLinearVelocity().y);
            else
                player.body.setLinearVelocity(player.body.getLinearVelocity().x * AIR_FRICTION, player.body.getLinearVelocity().y);
        }

        if(pos.y < -25) {
            player.kill(world);
            player.spawn(-20, 4, world);
        }

        world.step(1 / 60f, 6, 2);

    }
}
