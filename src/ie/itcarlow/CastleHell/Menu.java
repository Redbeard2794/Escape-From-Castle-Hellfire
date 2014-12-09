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
	}
	public void Populate(Engine c, Scene s)
	{
		playButtonSprite = new Sprite(720/2.5f,480/4,playButtonTextureRegion,c.getVertexBufferObjectManager()){
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
		        	   //gameState = GAME;
		        	   //move this stuff to menu button for playing the game
					   //int size = listOfPlatforms.size();
		       			//for(int i=0;i<size;i++)
		       			//{
		       			//	listOfPlatforms.get(i).getSprite().setVisible(true);
		       			//}
		       			playButtonSprite.setVisible(false);
		       			optionsButtonSprite.setVisible(false);
		       			quitButtonSprite.setVisible(false);
		       			//playButtonSprite.setX(-1000);
		       			//optionsButtonSprite.setX(-1000);
		       			//quitButtonSprite.setX(-1000);
		       			//tapToPlaySprite.setVisible(false);
		       			//splashSprite.setVisible(false);
		                break;}
		        }
				return true;
			}
		};
		playButtonSprite.setZIndex(8);
		s.attachChild(playButtonSprite);
		//playButtonSprite.setVisible(false);
		s.registerTouchArea(playButtonSprite);
		
		optionsButtonSprite = new Sprite(720/2.5f,480/2,optionsButtonTextureRegion,c.getVertexBufferObjectManager())
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
		
		quitButtonSprite = new Sprite(720/2.5f,480-120,quitButtonTextureRegion,c.getVertexBufferObjectManager())
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
		quitButtonSprite.setZIndex(8);
		s.attachChild(quitButtonSprite);
		//quitButtonSprite.setVisible(false);
		s.registerTouchArea(quitButtonSprite);
		
		playButtonSprite.setVisible(false);
		optionsButtonSprite.setVisible(false);
		quitButtonSprite.setVisible(false);
	}
	public void update()
	{
		if (startGame == true)
		{
			playButtonSprite.setVisible(false);
			optionsButtonSprite.setVisible(false);
			quitButtonSprite.setVisible(false);
		}
		else
		{
			playButtonSprite.setVisible(true);
			optionsButtonSprite.setVisible(true);
			quitButtonSprite.setVisible(true);
		}
	}
	public boolean getStartGame(){return startGame;}
}
