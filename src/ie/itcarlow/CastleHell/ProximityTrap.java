package ie.itcarlow.CastleHell;

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

import android.content.Context;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class ProximityTrap
{
	private Sprite mSprite;
	private ITextureRegion trapTextureRegion;
	private final float posX;
	private final float posY;
	private float startY;
	private float startX;

	private Body body;
	
	boolean falling;
	boolean hasFallen;

	public ProximityTrap(float xPos, float yPos, Context c, TextureManager t)
	{
		loadGFX(c, t);
		posX = xPos;
		posY = yPos;
		startY = posY;
		startX = posX;
		falling = false;
		hasFallen = false;
	}

	public float getStartX() {
		return startX;
	}

	public boolean getHasFallen() {
		return hasFallen;
	}

	public void setHasFallen(boolean hasFallen) {
		this.hasFallen = hasFallen;
	}

	public void setFalling(boolean falling) {
		this.falling = falling;
	}

	private void loadGFX(Context c, TextureManager t)
	{
		BitmapTextureAtlas trapTexture = new BitmapTextureAtlas(t, 53, 51);
		trapTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(trapTexture, c, "SpikeFallTrap1.png", 0, 0);
		trapTexture.load();
	}

	public float getStartY() {
		return startY;
	}

	public boolean isFalling() {
		return falling;
	}

	public void Populate(Engine c, Scene s, PhysicsWorld p)
	{
		mSprite = new Sprite(posX, posY, trapTextureRegion,
				c.getVertexBufferObjectManager());
		mSprite.setZIndex(10);
		s.attachChild(mSprite);

		FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.1f, 0.5f);
		body = PhysicsFactory.createBoxBody(p, mSprite,
				BodyDef.BodyType.StaticBody, FIXTURE_DEF);
		body.setUserData("proximityTrap");
		p.registerPhysicsConnector(new PhysicsConnector(mSprite, body, true,
				true)
		{
			@Override
			public void onUpdate(float pSecondsElapsed)
			{
				super.onUpdate(pSecondsElapsed);
			}
		});
	}

	public void Trigger()
	{
		body.setType(BodyDef.BodyType.DynamicBody);
		falling = true;
	}
	public void Reset()
	{
		falling = false;
		body.setType(BodyDef.BodyType.StaticBody);
		//body.setTransform(posX/30, startY/30, 0);
		//body.applyLinearImpulse(new Vector2(5,0), body.getPosition());
	}

	public Body getBody() {
		return body;
	}

	public Sprite getSprite()
	{
		return mSprite;
	}
}
