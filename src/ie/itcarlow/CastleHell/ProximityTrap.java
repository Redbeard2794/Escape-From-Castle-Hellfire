package ie.itcarlow.CastleHell;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.content.Context;

public class ProximityTrap
{
	private Sprite mSprite;
	private BitmapTextureAtlas trapTexture;
	private ITextureRegion trapTextureRegion;
	
	public ProximityTrap(Context c,TextureManager t)
	{
		loadGFX(c,t);
	}
	
	private void loadGFX(Context c,TextureManager t)
	{
		trapTexture = new BitmapTextureAtlas(t,53,51);
		trapTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(trapTexture, c, "SpikeFallTrap1.png",0,0);
		trapTexture.load();
	}
	
	public void Populate(Engine c,Scene s)
	{
		mSprite = new Sprite(53,51,trapTextureRegion,c.getVertexBufferObjectManager());
		s.attachChild(mSprite);
		
	}
	public void Trigger()
	{
		
	}
}
