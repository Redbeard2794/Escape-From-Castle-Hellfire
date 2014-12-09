package ie.itcarlow.CastleHell;

import android.content.Context;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

public class Platform {
	private final float platformX;
	private final float platformY;
	private ITextureRegion platformTextureRegion;
	private Sprite platformSprite;
	private final float w;
	private final float h;

	private Body body;

	public Platform(Context c, TextureManager t, int x, int y, int width, int height)
	{
		loadGFX(c, t);
		platformX = x;
		platformY = y;
		w=width;
		h=height;
	}
	
	private void loadGFX(Context c, TextureManager t)
	{
		BitmapTextureAtlas platformTexture = new BitmapTextureAtlas(t, 120, 60);
		platformTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(platformTexture,
				c.getAssets(), "plat2.png",0,0);
		platformTexture.load();
	}
	public void Populate(Engine c, Scene s,PhysicsWorld p)
	{
		platformSprite = new Sprite(platformX, platformY, w, h, platformTextureRegion,
				c.getVertexBufferObjectManager());
		platformSprite.setZIndex(10);
		s.attachChild(platformSprite);

		FixtureDef fixDef = PhysicsFactory.createFixtureDef(0,0.1f,0.5f);
		body = PhysicsFactory.createBoxBody(p,platformSprite, BodyDef.BodyType.StaticBody,fixDef);
		body.setUserData("platform");

		p.registerPhysicsConnector(new PhysicsConnector(platformSprite,body,false,false)
		{
			@Override
			public void onUpdate(float pSecondsElapsed)
			{
				super.onUpdate(pSecondsElapsed);
			}
		});
	}
	public void Update()
	{
		
	}
	public Sprite getSprite(){return platformSprite;}
	public void setSprite(Sprite sp){platformSprite = sp;}
}
