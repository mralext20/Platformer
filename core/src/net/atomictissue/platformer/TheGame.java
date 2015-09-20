package net.atomictissue.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TheGame extends ApplicationAdapter {
    private World world;
    private static Player player;

    Stage stage;

    private OrthographicCamera camera;
    private ShapeRenderer renderer;
    private Box2DDebugRenderer debugRenderer;

    public static Player getPlayer() {
        return player;
    }

    @Override
    public void create() {
        world = new World(new Vector2(0, -35), true);

        world.setContactListener(new MyContactListener());

        // initialize camera
        camera = new OrthographicCamera(50, 50);

        // initialize renderer
        renderer = new ShapeRenderer();
        renderer.setProjectionMatrix(camera.combined);

        // initialize debug renderer
        debugRenderer = new Box2DDebugRenderer();

        // initialize stage
        Viewport viewport = new FitViewport(50, 50);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);

        // create ground
        Ground ground1 = new Ground(-20, -20, 5, 2, world, renderer);
        Ground ground2 = new Ground(11, -20, 14, 2, world, renderer);
        stage.addActor(ground1);
        stage.addActor(ground2);

        // create player
        player = new Player(-20, 4, world, renderer);
        stage.addActor(player);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //debugRenderer.render(world, camera.combined);
        stage.act();
        stage.draw();
        world.step(1 / 60f, 6, 2);
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
    }
}
