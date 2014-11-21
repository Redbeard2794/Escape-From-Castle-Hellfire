package ie.itcarlow.CastleHell;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;

public class Player{

	//if true player is facing to the right, if false player is facing towards the left
	boolean right;
	private float playerX, playerY;
	private float centreX, centreY;
	
	private BitmapTextureAtlas playerTexture;
	private ITiledTextureRegion playerTiledTextureRegion;
	AnimatedSprite playerSprite;
	
	public Player(Context c,TextureManager t)
	{     
		loadGFX(c,t);
		//super(pX,pY,pTiledTextureRegion,pVertextBufferObjectManager);
		//centreX = this.getX() + this.getWidth()/2;
		//centreY = this.getY() +this.getHeight()/2;
		
		//playerX = this.getX();
		//playerY = this.getY();;
		//right = true;
	}
	private void loadGFX(Context c,TextureManager t)
	{
		playerTexture = new BitmapTextureAtlas(t,542,73);
		playerTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(playerTexture, c,
				"PlayerRightFixed.png", 0, 0, 11, 1);
		playerTexture.load();
	}
	public void Populate(Engine c,Scene s)
	{
		playerSprite = new AnimatedSprite(53,51,playerTiledTextureRegion,c.getVertexBufferObjectManager());
		s.attachChild(playerSprite);
		
	}
	private void update()
	{
		//playerX+=1;
		//this.setX(this.getX()+1);
	}
	
}
