package ie.itcarlow.CastleHell;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.content.Context;


public class Door {
	private ITextureRegion doorTextureRegion;
	private Sprite doorSprite;
	public Sprite getDoorSprite() {
		return doorSprite;
	}
	public void setDoorSprite(Sprite doorSprite) {
		this.doorSprite = doorSprite;
	}

	float posX, posY;

	
	public Door(float x, float y, Context c, TextureManager t)
	{
		posX = x;
		posY = y;
		loadGFX(c, t);
	}
	private void loadGFX(Context c, TextureManager t)
	{
		BitmapTextureAtlas doorTexture = new BitmapTextureAtlas(t, 94, 111);
		doorTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(doorTexture,c.getAssets(),"Door1.png",0,0);
		doorTexture.load();
	}
	public void Populate(Engine c, Scene s)
	{
		doorSprite = new Sprite(posX, posY, doorTextureRegion,
				c.getVertexBufferObjectManager());
		doorSprite.setZIndex(8);
		doorSprite.setVisible(true);
		s.attachChild(doorSprite);
	}
	
	public void update()//Sprite playerSprite)
	{
		/*if(doorSprite.contains(playerSprite.getX(), playerSprite.getY()))
		{
			//do something here....
		}*/
	}
}
