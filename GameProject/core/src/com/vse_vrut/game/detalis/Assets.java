package com.vse_vrut.game.detalis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.vse_vrut.game.entitys.Car;
import com.vse_vrut.game.entitys.Gate;

/**
 * Created by Влад on 16.12.2017.
 */
public class Assets implements Disposable, AssetErrorListener {

    public static final String TAG = Assets.class.getName();

    public static final Assets instance = new Assets();

    public CarAssets carAssets;
    public GateAssets gateAssets;

    private AssetManager assetManager;

    private Assets() {
    }

    public void init(AssetManager assetManager){
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);
        assetManager.load(Constants.TEXTURE_ATLAS, TextureAtlas.class);
        assetManager.finishLoading();

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS);

        carAssets = new CarAssets(atlas);
        gateAssets = new GateAssets(atlas);
    }
    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset: " + asset.fileName, throwable);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    public class CarAssets{

        public TextureAtlas.AtlasRegion car;

        public CarAssets(TextureAtlas atlas){
            car = atlas.findRegion(Constants.CAR_SPRITE);
        }

    }

    public class GateAssets{
        public TextureAtlas.AtlasRegion gateOpen;
        public TextureAtlas.AtlasRegion gateClose;

        public final Animation gateOpeningAnimation;

        public GateAssets(TextureAtlas atlas){
            gateOpen = atlas.findRegion(Constants.GATE_OPEN);
            gateClose = atlas.findRegion(Constants.GATE_CLOSE);

            Array<TextureAtlas.AtlasRegion> gateOpeningFrame = new Array<TextureAtlas.AtlasRegion>();

            gateOpeningFrame.add(atlas.findRegion(Constants.GATE_OPEN));
            gateOpeningFrame.add(atlas.findRegion(Constants.GATE_CLOSE));

            gateOpeningAnimation = new Animation(Constants.OPENING_LOOP_DURATION,gateOpeningFrame, Animation.PlayMode.LOOP);

        }
    }
}
