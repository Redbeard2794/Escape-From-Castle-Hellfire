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
	//https://github.com/nicolasgramlich/AndEngineExamples/blob/GLES2/src/org/andengine/examples/LevelLoaderExample.java
	//https://github.com/nicolasgramlich/AndEngineExamples/blob/GLES2/assets/level/example.lvl
	
	// if true player is facing to the right, if false player is facing towards
	// the left
	boolean moveRight;
	boolean moveLeft;
	boolean faceRight;
	boolean faceLeft;
	private float playerX, playerY;
	private float centreX, centreY;

	private BitmapTextureAtlas playerTexture;
	private ITiledTextureRegion playerTiledTextureRegion;
	AnimatedSprite playerSprite;
	
	private BitmapTextureAtlas playerLeftTexture;
	private ITiledTextureRegion playerLeftTiledTextureRegion;
	AnimatedSprite playerLeftSprite;
	
	private BitmapTextureAtlas playerRightIdleTexture;
	private ITiledTextureRegion playerRightIdleTiledTextureRegion;
	AnimatedSprite playerRightIdleSprite;
	
	private BitmapTextureAtlas playerLeftIdleTexture;
	private ITiledTextureRegion playerLeftIdleTiledTextureRegion;
	AnimatedSprite playerLeftIdleSprite;
	
	AnimatedSprite currentSprite;

	public Player(Context c, TextureManager t)
	{
		loadGFX(c, t);
		playerX = 250;
		playerY = 250;
		moveRight = false;
		moveLeft = false;
		faceRight = true;
		faceLeft = false;
		// super(pX,pY,pTiledTextureRegion,pVertextBufferObjectManager);
		// centreX = this.getX() + this.getWidth()/2;
		// centreY = this.getY() +this.getHeight()/2;

		// playerX = this.getX();
		// playerY = this.getY();;
		// right = true;
	}

	public float getPlayerX(){return playerX;}
	public void setPlayerX(float pX){this.playerX = pX;}
	public float getPlayerY(){return playerY;}
	public void setPlayerY(float pY){this.playerY = pY;}
	public boolean getMoveRight(){return moveRight;}
	public void setMoveRight(boolean r){moveRight = r;}
	public boolean getMoveLeft(){return moveLeft;}
	public void setMoveLeft(boolean l){moveLeft = l;}
	public boolean getFaceRight(){return faceRight;}
	public void setFaceRight(boolean b){faceRight = b;}
	public boolean getFaceLeft(){return faceLeft;}
	public void setFaceLeft(boolean b){faceLeft = b;}
	public AnimatedSprite getCurrentSprite(){return currentSprite;}

	private void loadGFX(Context c, TextureManager t)
	{
		playerTexture = new BitmapTextureAtlas(t, 405, 80);
		playerTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(playerTexture, c.getAssets(),
						"playerRightSheet.png", 0, 0, 9, 1);
		playerTexture.load();
		
		playerLeftTexture = new BitmapTextureAtlas(t,405,80);
		playerLeftTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(playerLeftTexture, c.getAssets(),
						"playerLeftSheet.png", 0, 0, 9, 1);
		playerLeftTexture.load();
		
		playerRightIdleTexture = new BitmapTextureAtlas(t, 672, 75);
		playerRightIdleTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(playerRightIdleTexture, c.getAssets(),
						"playerIdleRightSheet.png", 0, 0, 16, 1);
		playerRightIdleTexture.load();
		
		playerLeftIdleTexture = new BitmapTextureAtlas(t, 672, 75);
		playerLeftIdleTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(playerLeftIdleTexture, c.getAssets(),
						"playerIdleLeftSheet.png", 0, 0, 16, 1);
		playerLeftIdleTexture.load();
	}

	public void Populate(Engine c, Scene s)
	{
		playerSprite = new AnimatedSprite(playerX, playerY, playerTiledTextureRegion,
				c.getVertexBufferObjectManager());
		playerSprite.animate(250);
		s.attachChild(playerSprite);
		
		playerLeftSprite = new AnimatedSprite(playerX,playerY,playerLeftTiledTextureRegion,
				c.getVertexBufferObjectManager());
		playerLeftSprite.animate(250);
		s.attachChild(playerLeftSprite);
		
		playerSprite.setVisible(false);
		playerLeftSprite.setVisible(false);
		
		playerRightIdleSprite = new AnimatedSprite(playerX, playerY, playerRightIdleTiledTextureRegion,
				c.getVertexBufferObjectManager());
		playerRightIdleSprite.animate(200);
		s.attachChild(playerRightIdleSprite);
		
		playerLeftIdleSprite = new AnimatedSprite(playerX, playerY, playerLeftIdleTiledTextureRegion,
				c.getVertexBufferObjectManager());
		playerLeftIdleSprite.animate(200);
		s.attachChild(playerLeftIdleSprite);
		
		playerRightIdleSprite.setVisible(false);
		playerLeftIdleSprite.setVisible(false);
		
		currentSprite = playerSprite;
	}

	public void Update()
	{
		playerSprite.setX(playerX);
		playerSprite.setY(playerY);
		playerLeftSprite.setX(playerX);
		playerLeftSprite.setY(playerY);
		playerRightIdleSprite.setX(playerX);
		playerRightIdleSprite.setY(playerY);
		playerLeftIdleSprite.setX(playerX);
		playerLeftIdleSprite.setY(playerY);
		//playerSprite.setVisible(false);
		// playerX+=1;
		// this.setX(this.getX()+1);
		if(moveRight==true)
		{
			playerSprite.setVisible(true);
			currentSprite = playerSprite;
			//playerLeftSprite.setVisible(false);
			playerRightIdleSprite.setVisible(false);
			playerLeftIdleSprite.setVisible(false);
			playerX+=1.5;
			currentSprite = playerSprite;
		}
		else
		{
			playerSprite.setVisible(false);
		}
		if(moveLeft==true)
		{
			//playerSprite.setVisible(false);
			playerLeftSprite.setVisible(true);
			playerRightIdleSprite.setVisible(false);
			playerLeftIdleSprite.setVisible(false);
			playerX-=1.5;
			currentSprite = playerLeftSprite;
		}
		else
		{
			playerLeftSprite.setVisible(false);
		}
		if(faceRight == true && moveRight == false && moveLeft == false)
		{
			playerRightIdleSprite.setVisible(true);
			playerLeftIdleSprite.setVisible(false);
			currentSprite = playerRightIdleSprite;
		}
		else if(faceLeft == true && moveRight == false && moveLeft == false)
		{
			playerLeftIdleSprite.setVisible(true);
			playerRightIdleSprite.setVisible(false);
			currentSprite = playerLeftIdleSprite;
		}
		//playerX+=1;
	}

}
