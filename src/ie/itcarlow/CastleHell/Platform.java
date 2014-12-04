package ie.itcarlow.CastleHell;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.content.Context;

public class Platform {
	private float platformX, platformY;
	private BitmapTextureAtlas platformTexture;
	private ITextureRegion platformTextureRegion;
	Sprite platformSprite;
	float w;
	float h;
	
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
		platformTexture = new BitmapTextureAtlas(t,120,60);
		platformTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(platformTexture,
				c.getAssets(), "plat2.png",0,0);
		platformTexture.load();
	}
	public void Populate(Engine c, Scene s)
	{
		platformSprite = new Sprite(platformX, platformY, w, h, platformTextureRegion,
				c.getVertexBufferObjectManager());
		platformSprite.setZIndex(10);
		s.attachChild(platformSprite);
	}
	public void Update()
	{
		
	}
	public Sprite getSprite(){return platformSprite;}
	public void setSprite(Sprite sp){platformSprite = sp;}
}
