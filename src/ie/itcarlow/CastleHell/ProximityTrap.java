package ie.itcarlow.CastleHell;

import android.content.Context;
import com.badlogic.gdx.physics.box2d.Body;
import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

public class ProximityTrap
{
	private Sprite mSprite;
	private ITextureRegion trapTextureRegion;
	private final float posX;
	private final float posY;

	private Body body;
	public ProximityTrap(float xPos, float yPos, Context c, TextureManager t)
	{
		loadGFX(c, t);
		posX = xPos;
		posY = yPos;
	}

	private void loadGFX(Context c, TextureManager t)
	{
		BitmapTextureAtlas trapTexture = new BitmapTextureAtlas(t, 53, 51);
		trapTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(trapTexture, c, "SpikeFallTrap1.png", 0, 0);
		trapTexture.load();
	}

	public void Populate(Engine c, Scene s)
	{
		mSprite = new Sprite(posX, posY, trapTextureRegion,
				c.getVertexBufferObjectManager());
		mSprite.setZIndex(10);
		s.attachChild(mSprite);
	}

	public void Trigger()
	{

	}
	
	public Sprite getSprite(){return mSprite;}
}
