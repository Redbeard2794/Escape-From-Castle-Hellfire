package ie.itcarlow.CastleHell;

import org.andengine.audio.sound.Sound;
import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Player
{
	//https://github.com/nicolasgramlich/AndEngineExamples/blob/GLES2/src/org/andengine/examples/LevelLoaderExample.java
	//https://github.com/nicolasgramlich/AndEngineExamples/blob/GLES2/assets/level/example.lvl
	
	// if true player is facing to the right, if false player is facing towards
	// the left
	private boolean moveRight;
	private boolean moveLeft;
	private boolean faceRight;
	private boolean faceLeft;
	private boolean isJumping;
	private float playerX, playerY;
	private float centreX, centreY;

	private ITiledTextureRegion playerTiledTextureRegion;
	private AnimatedSprite playerSprite;

	private ITiledTextureRegion playerLeftTiledTextureRegion;
	private AnimatedSprite playerLeftSprite;

	private ITiledTextureRegion playerRightIdleTiledTextureRegion;
	private AnimatedSprite playerRightIdleSprite;

	private ITiledTextureRegion playerLeftIdleTiledTextureRegion;
	private AnimatedSprite playerLeftIdleSprite;
	
	private AnimatedSprite currentSprite;
	boolean dead;

	public Player(Context c, TextureManager t)
	{
		loadGFX(c, t);
		playerX = 250;
		playerY = 250;
		moveRight = false;
		moveLeft = false;
		faceRight = true;
		faceLeft = false;
		isJumping = false;
		dead = false;
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
	public boolean getDead(){return dead;}
	public void setDead(boolean b){dead = b;}

	public boolean getIsJumping()
	{
		return isJumping;
	}

	public void setIsJumping(boolean jump)
	{
		this.isJumping = jump;
	}

	private Body body;

	private void loadGFX(Context c, TextureManager t)
	{
		BitmapTextureAtlas playerTexture = new BitmapTextureAtlas(t, 405, 80);
		playerTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(playerTexture, c.getAssets(),
						"playerRightSheet.png", 0, 0, 9, 1);
		playerTexture.load();

		BitmapTextureAtlas playerLeftTexture = new BitmapTextureAtlas(t, 405, 80);
		playerLeftTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(playerLeftTexture, c.getAssets(),
						"playerLeftSheet.png", 0, 0, 9, 1);
		playerLeftTexture.load();

		BitmapTextureAtlas playerRightIdleTexture = new BitmapTextureAtlas(t, 672, 75);
		playerRightIdleTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(playerRightIdleTexture, c.getAssets(),
						"playerIdleRightSheet.png", 0, 0, 16, 1);
		playerRightIdleTexture.load();

		BitmapTextureAtlas playerLeftIdleTexture = new BitmapTextureAtlas(t, 672, 75);
		playerLeftIdleTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(playerLeftIdleTexture, c.getAssets(),
						"playerIdleLeftSheet.png", 0, 0, 16, 1);
		playerLeftIdleTexture.load();
	}

	public void Populate(Engine c, Scene s,PhysicsWorld p)
	{
		playerSprite = new AnimatedSprite(playerX, playerY, playerTiledTextureRegion,
				c.getVertexBufferObjectManager());
		playerSprite.animate(250);
		s.attachChild(playerSprite);
		
		playerLeftSprite = new AnimatedSprite(playerX,playerY,playerLeftTiledTextureRegion,
				c.getVertexBufferObjectManager());
		playerLeftSprite.animate(250);
		s.attachChild(playerLeftSprite);
		
		playerSprite.setVisible(true);
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

		FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0,0.1f,0.5f);
		body = PhysicsFactory.createBoxBody(p,playerSprite, BodyDef.BodyType.DynamicBody,FIXTURE_DEF);
		body.setUserData("player");

		p.registerPhysicsConnector(new PhysicsConnector(playerSprite,body,true,true)
		{
			@Override
		public  void onUpdate(float pSecondsElapsed)
			{
				super.onUpdate(pSecondsElapsed);
				Move();
			}
		});
		p.registerPhysicsConnector(new PhysicsConnector(playerLeftSprite,body,true,true)
		{
			@Override
		public  void onUpdate(float pSecondsElapsed)
			{
				super.onUpdate(pSecondsElapsed);
				Move();
			}
		});
		p.registerPhysicsConnector(new PhysicsConnector(playerRightIdleSprite,body,true,true)
		{
			@Override
		public  void onUpdate(float pSecondsElapsed)
			{
				super.onUpdate(pSecondsElapsed);
				Move();
			}
		});
		p.registerPhysicsConnector(new PhysicsConnector(playerLeftIdleSprite,body,true,true)
		{
			@Override
		public  void onUpdate(float pSecondsElapsed)
			{
				super.onUpdate(pSecondsElapsed);
				Move();
			}
		});
	}
	public void Jump()
	{
		Vector2 curSpeed = body.getLinearVelocity();
		body.setLinearVelocity(curSpeed.x, curSpeed.y + 100);
	}

	public void Move()
	{
		if(moveRight)
		{
			body.setLinearVelocity(2, body.getLinearVelocity().y);
			
			playerSprite.setVisible(true);
			playerLeftSprite.setVisible(false);
			playerRightIdleSprite.setVisible(false);
			playerLeftIdleSprite.setVisible(false);
			currentSprite = playerSprite;
		}
		else
		{
			playerSprite.setVisible(false);
		}
		if(moveLeft)
		{
			body.setLinearVelocity(-2, body.getLinearVelocity().y);
			
			playerSprite.setVisible(false);
			playerLeftSprite.setVisible(true);
			playerRightIdleSprite.setVisible(false);
			playerLeftIdleSprite.setVisible(false);
			currentSprite = playerLeftSprite;
		}
		else
		{
			playerLeftSprite.setVisible(false);
		}
		if(faceRight && !moveRight && !moveLeft)
		{
			playerRightIdleSprite.setVisible(true);
			playerLeftIdleSprite.setVisible(false);
			currentSprite = playerRightIdleSprite;
		}
		else if(faceLeft && !moveRight && !moveLeft)
		{
			playerLeftIdleSprite.setVisible(true);
			playerRightIdleSprite.setVisible(false);
			currentSprite = playerLeftIdleSprite;
		}
		playerX = currentSprite.getX();
		playerY = currentSprite.getY();
		
		if(currentSprite.getY() > 480)
		{
			dead = true;
		}
	}

	
}
