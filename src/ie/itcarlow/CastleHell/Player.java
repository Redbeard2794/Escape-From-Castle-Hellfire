package ie.itcarlow.CastleHell;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import android.content.Context;

public class Player
{

	// if true player is facing to the right, if false player is facing towards
	// the left
	boolean right;
	private float playerX, playerY;
	private float centreX, centreY;

	private BitmapTextureAtlas playerTexture;
	private ITiledTextureRegion playerTiledTextureRegion;
	AnimatedSprite playerSprite;

	public Player(Context c, TextureManager t)
	{
		loadGFX(c, t);
		playerX = 250;
		playerY = 250;
		// super(pX,pY,pTiledTextureRegion,pVertextBufferObjectManager);
		// centreX = this.getX() + this.getWidth()/2;
		// centreY = this.getY() +this.getHeight()/2;

		// playerX = this.getX();
		// playerY = this.getY();;
		// right = true;
	}

	public float getPlayerX()
	{
		return playerX;
	}

	public void setPlayerX(float pX)
	{
		this.playerX = pX;
	}

	public float getPlayerY()
	{
		return playerY;
	}

	public void setPlayerY(float pY)
	{
		this.playerY = pY;
	}

	private void loadGFX(Context c, TextureManager t)
	{
		playerTexture = new BitmapTextureAtlas(t, 542, 73);
		playerTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(playerTexture, c.getAssets(),
						"PlayerRightFixed.png", 0, 0, 11, 1);
		playerTexture.load();
	}

	public void Populate(Engine c, Scene s)
	{
		playerSprite = new AnimatedSprite(250, 250, playerTiledTextureRegion,
				c.getVertexBufferObjectManager());
		playerSprite.animate(250);
		s.attachChild(playerSprite);

	}

	private void update()
	{
		// playerX+=1;
		// this.setX(this.getX()+1);
	}

}
