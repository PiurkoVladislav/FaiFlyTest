package com.vse_vrut.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.vse_vrut.game.detalis.Assets;
import com.vse_vrut.game.detalis.Constants;

/**
 * Created by Влад on 16.12.2017.
 */
public class GameplayWB extends ScreenAdapter {

    public static final String TAG = GameplayWB.class.getName();

    private Level level;
    private SpriteBatch banch;
    private ExtendViewport viewport;
    private ShapeRenderer render;

    @Override
    public void show() {
        AssetManager am = new AssetManager();
        Assets.instance.init();
        level = new Level();
        banch = new SpriteBatch();
        viewport = new ExtendViewport(Constants.WORLD_SIZE,Constants.WORLD_SIZE);

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        Assets.instance.dispose();
    }

    @Override
    public void render(float delta) {
        level.update(delta);
        viewport.apply();
        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        banch.setProjectionMatrix(viewport.getCamera().combined);
        level.render(banch);

    }

}
