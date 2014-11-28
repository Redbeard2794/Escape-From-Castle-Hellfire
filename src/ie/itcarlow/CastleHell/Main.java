package ie.itcarlow.CastleHell;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.SAXUtils;
import org.andengine.util.level.IEntityLoader;
import org.andengine.util.level.LevelLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.xml.sax.Attributes;

import android.content.Context;
import android.view.MotionEvent;

public class Main extends BaseGameActivity implements IUpdateHandler
{
	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	//for levels
	private static final String TAG_ENTITY = "entity";
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_ENTITY_ATTRIBUTE_WIDTH = "width";
	private static final String TAG_ENTITY_ATTRIBUTE_HEIGHT = "height";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";
	
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM = "platform";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PROXIMITYTRAP = "proximityTrap";
	
	
	private Scene mScene;

	private BitmapTextureAtlas ArrowTexture;
	private ITextureRegion ArrowTextureRegion;
	Sprite rightArrowSprite;

	private BitmapTextureAtlas arrowLeftTexture;
	private ITextureRegion leftArrowTextureRegion;
	Sprite leftArrowSprite;

	private BitmapTextureAtlas jumpButtonTexture;
	private ITextureRegion jumpButtonTextureRegion;
	Sprite jumpButtonSprite;
	
	private BitmapTextureAtlas backgroundTexture;
	private ITextureRegion backgroundTextureRegion;
	Sprite backgroundSprite;

	Player p;

	ProximityTrap t;
	
	//Platform plat;
	
	Context c = this;
	


	@Override
	public EngineOptions onCreateEngineOptions()
	{
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception
	{

		loadGfx();
		pOnCreateResourcesCallback.onCreateResourcesFinished();

	}

	private void loadGfx()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		// playerAnimatedSprite = new BitmapTextureAtlas(getTextureManager(),
		// 542,
		// 73);
		// playerTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
		// .createTiledFromAsset(playerAnimatedSprite, this.getAssets(),
		// "PlayerRightFixed.png", 0, 0, 11, 1);
		// playerAnimatedSprite.load();
		backgroundTexture = new BitmapTextureAtlas(getTextureManager(),720,480);
		backgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backgroundTexture,
				this, "Background1.png",0,0);
		backgroundTexture.load();
		
		ArrowTexture = new BitmapTextureAtlas(getTextureManager(), 178, 84);// 237,112
		ArrowTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(ArrowTexture, this, "SourceArrowTQ.png", 0, 0);
		ArrowTexture.load();

		arrowLeftTexture = new BitmapTextureAtlas(getTextureManager(), 178, 84);
		leftArrowTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(arrowLeftTexture, this,
						"SourceArrowTQLeft.png", 0, 0);
		arrowLeftTexture.load();

		jumpButtonTexture = new BitmapTextureAtlas(getTextureManager(), 397, 86);// 397,86
		jumpButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(jumpButtonTexture, this, "JumpButton.png", 0,
						0);
		jumpButtonTexture.load();

		t = new ProximityTrap(200, 50, this, getTextureManager());
		p = new Player(this, getTextureManager());
		//plat = new Platform(this,getTextureManager());
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception
	{

		this.mScene = new Scene();
		//this.mScene.setBackground(new Background(255, 0, 0));
		// register this activity as a scene touch listener
		// this.mScene.setOnSceneTouchListener(this);
		//final LevelLoader levelLoader = new LevelLoader();
		//levelLoader.setAssetBasePath("level/");
		
		//built in levelloader class
		final LevelLoader levelLoader = new LevelLoader();
		levelLoader.setAssetBasePath("level/");
		
		//levelLoader.registerEntityLoader(TAG_ENTITY, new IEntityLoader()
		//Need this version of registerEntityLoader.....I think
		//Also need to modify the platform constructor so it takes:
		//x, y, width and height
		//means i will have to modify the example code a bit to make it work
		//The example code just draws an animated sprite here. All it needs before hand is the texture region
		//so that will take a bit of fiddling
		
		levelLoader.registerEntityLoader(TAG_ENTITY, new IEntityLoader() {
			
			@Override
			public IEntity onLoadEntity(final String pEntityName, final Attributes pAttributes) {
			final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
			final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
			final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_WIDTH);
			final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_HEIGHT);
			final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);
//			final VertexBufferObjectManager vertexBufferObjectManager = LevelLoaderExample.this.getVertexBufferObjectManager();
//			final AnimatedSprite face;//replace with sprite
			
			
			
			final Platform spr;
			//Dont declare it here. Create an empty list/array of platform objects 
			//and add a new one here(so we can update it and call populate methods later)
			//list tutorial http://tutorials.jenkov.com/java-collections/list.html
			if(type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM)) {
				spr = new Platform(c,getTextureManager(),x, y, width, height);
			}
//			} else if(type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_CIRCLE)) {
//			face = new AnimatedSprite(x, y, width, height, LevelLoaderExample.this.mCircleFaceTextureRegion, vertexBufferObjectManager);
//			} else if(type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TRIANGLE)) {
//			face = new AnimatedSprite(x, y, width, height, LevelLoaderExample.this.mTriangleFaceTextureRegion, vertexBufferObjectManager);
//			} else if(type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_HEXAGON)) {
//			face = new AnimatedSprite(x, y, width, height, LevelLoaderExample.this.mHexagonFaceTextureRegion, vertexBufferObjectManager);
//			} 
			else {
			throw new IllegalArgumentException();
			}
//			face.animate(200);
			//spr.Populate(c, mScene);
			return spr.getSprite();//get the platform sprite(a get method) and return it
			}
			});
		
		
		pOnCreateSceneCallback.onCreateSceneFinished(this.mScene);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception
	{
		// TODO Auto-generated method stub


		backgroundSprite = new Sprite(0,0,backgroundTextureRegion,this.mEngine.getVertexBufferObjectManager());
		mScene.attachChild(backgroundSprite);
		rightArrowSprite = new Sprite(530, 390, ArrowTextureRegion,
				this.mEngine.getVertexBufferObjectManager())
		{
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY)
			{
				int myEventAction = pSceneTouchEvent.getAction();

		        switch (myEventAction) 
		        {
		          case MotionEvent.ACTION_DOWN:{
		        	  p.setMoveRight(true);

		        	   //mScene.setBackground(new Background(0, 255, 0));
		        	   break;}
		          case MotionEvent.ACTION_MOVE: {

		            	break;}
		           case MotionEvent.ACTION_UP:{
		        	   p.setMoveRight(false);
			        	  p.setFaceRight(true);
			        	  p.setFaceLeft(false);
		        	   //mScene.setBackground(new Background(255, 0, 0));
		                break;}
		        }
				return true;
			}
		};
		mScene.attachChild(rightArrowSprite);
		this.mScene.registerTouchArea(rightArrowSprite);
		//this.mScene.setTouchAreaBindingOnActionDownEnabled(true);
		
		leftArrowSprite = new Sprite(10, 390, leftArrowTextureRegion,
				this.mEngine.getVertexBufferObjectManager())
		{
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY)
			{
				int myEventAction = pSceneTouchEvent.getAction();

		        switch (myEventAction) 
		        {
		          case MotionEvent.ACTION_DOWN:{
		        	  p.setMoveLeft(true);

		        	   //mScene.setBackground(new Background(0, 0, 255));
		        	   break;}
		          case MotionEvent.ACTION_MOVE: {

		            	break;}
		           case MotionEvent.ACTION_UP:{
		        	   p.setMoveLeft(false);
			        	  p.setFaceRight(false);
			        	  p.setFaceLeft(true);
		        	   //mScene.setBackground(new Background(255, 0, 0));
		                break;}
		        }
				return true;
			}
		};
		mScene.attachChild(leftArrowSprite);
		this.mScene.registerTouchArea(leftArrowSprite);

		jumpButtonSprite = new Sprite(210, 400, jumpButtonTextureRegion,
				this.mEngine.getVertexBufferObjectManager());
		mScene.attachChild(jumpButtonSprite);

		this.mEngine.registerUpdateHandler(this);

		t.Populate(this.mEngine, mScene);
		p.Populate(this.mEngine, mScene);
		//plat.Populate(this.mEngine, mScene);

		pOnPopulateSceneCallback.onPopulateSceneFinished();

	}

	@Override
	public void onUpdate(float pSecondsElapsed)
	{
		// TODO Auto-generated method stub

		p.Update();

	}

	@Override
	public void reset()
	{
		// TODO Auto-generated method stub

	}
}
