package ie.itcarlow.CastleHell;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.content.Context;
import android.view.MotionEvent;

public class Menu {
	private ITextureRegion playButtonTextureRegion;
	private Sprite playButtonSprite;

	private ITextureRegion optionsButtonTextureRegion;
	private Sprite optionsButtonSprite;

	private ITextureRegion quitButtonTextureRegion;
	private Sprite quitButtonSprite;
	
	private ITextureRegion multiplayerTextureRegion;
	private Sprite multiplayerSprite;
	
	boolean startGame = false;
	
	public Menu(Context c, TextureManager t)
	{
		loadGFX(c, t);
	}
	private void loadGFX(Context c, TextureManager t)
	{
		BitmapTextureAtlas playButtonTexture = new BitmapTextureAtlas(t, 199, 38);
		playButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(playButtonTexture,c.getAssets(),"menuButtons/playButton.png",0,0);
		playButtonTexture.load();

		BitmapTextureAtlas optionsButtonTexture = new BitmapTextureAtlas(t, 199, 38);
		optionsButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsButtonTexture,c.getAssets(),"menuButtons/optionsButton.png",0,0);
		optionsButtonTexture.load();

		BitmapTextureAtlas quitButtonTexture = new BitmapTextureAtlas(t, 199, 38);
		quitButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(quitButtonTexture,c.getAssets(),"menuButtons/quitButton.png",0,0);
		quitButtonTexture.load();
		
		BitmapTextureAtlas multiplayerButtonTexture = new BitmapTextureAtlas(t,199,38);
		multiplayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(multiplayerButtonTexture,c.getAssets(),
				"menuButtons/multiplayerButton.png",0,0);
		multiplayerButtonTexture.load();
	}
	public void Populate(Engine c, Scene s)
	{
		playButtonSprite = new Sprite(720/2.5f,480/6,playButtonTextureRegion,c.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY)
			{
				int myEventAction = pSceneTouchEvent.getAction();

		        switch (myEventAction) 
		        {
		          case MotionEvent.ACTION_DOWN:{
		        	   break;}
		          case MotionEvent.ACTION_MOVE: {
		            	break;}
		           case MotionEvent.ACTION_UP:{
		        	   startGame = true;

		       			playButtonSprite.setVisible(false);
		       			optionsButtonSprite.setVisible(false);
		       			quitButtonSprite.setVisible(false);
		       			multiplayerSprite.setVisible(false);

		                break;}
		        }
				return true;
			}
		};
		playButtonSprite.setZIndex(8);
		s.attachChild(playButtonSprite);

		s.registerTouchArea(playButtonSprite);
		
		optionsButtonSprite = new Sprite(720/2.5f,480/3,optionsButtonTextureRegion,c.getVertexBufferObjectManager())
		{
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY)
			{
				int myEventAction = pSceneTouchEvent.getAction();

		        switch (myEventAction) 
		        {
		          case MotionEvent.ACTION_DOWN:{
		        	   break;}
		          case MotionEvent.ACTION_MOVE: {
		            	break;}
		           case MotionEvent.ACTION_UP:{
		                break;}
		        }
				return true;
			}
		};
		optionsButtonSprite.setZIndex(8);
		s.attachChild(optionsButtonSprite);
		//optionsButtonSprite.setVisible(false);
		s.registerTouchArea(optionsButtonSprite);
		
		quitButtonSprite = new Sprite(720/2.5f,320,quitButtonTextureRegion,c.getVertexBufferObjectManager())
		{
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY)
			{
				int myEventAction = pSceneTouchEvent.getAction();

		        switch (myEventAction) 
		        {
		          case MotionEvent.ACTION_DOWN:{
		        	   break;}
		          case MotionEvent.ACTION_MOVE: {
		            	break;}
		           case MotionEvent.ACTION_UP:{
		        	   System.exit(0);
		                break;}
		        }
				return true;
			}
		};
		quitButtonSprite.setZIndex(8);
		s.attachChild(quitButtonSprite);
		//quitButtonSprite.setVisible(false);
		s.registerTouchArea(quitButtonSprite);
		
		multiplayerSprite = new Sprite(720/2.5f,480/2,multiplayerTextureRegion,c.getVertexBufferObjectManager())
		{
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY)
			{
				int myEventAction = pSceneTouchEvent.getAction();

		        switch (myEventAction) 
		        {
		          case MotionEvent.ACTION_DOWN:{
		        	   break;}
		          case MotionEvent.ACTION_MOVE: {
		            	break;}
		           case MotionEvent.ACTION_UP:{
		        	   //System.exit(0);
		                break;}
		        }
				return true;
			}
		};
		multiplayerSprite.setZIndex(8);
		s.attachChild(multiplayerSprite);
		s.registerTouchArea(multiplayerSprite);
		
		playButtonSprite.setVisible(false);
		optionsButtonSprite.setVisible(false);
		multiplayerSprite.setVisible(false);
		quitButtonSprite.setVisible(false);
	}
	public void update()
	{
		if (startGame == true)
		{
			playButtonSprite.setVisible(false);
			optionsButtonSprite.setVisible(false);
			quitButtonSprite.setVisible(false);
			multiplayerSprite.setVisible(false);
		}
		else
		{
			playButtonSprite.setVisible(true);
			optionsButtonSprite.setVisible(true);
			quitButtonSprite.setVisible(true);
			multiplayerSprite.setVisible(true);
		}
	}
	public boolean getStartGame(){return startGame;}
}
