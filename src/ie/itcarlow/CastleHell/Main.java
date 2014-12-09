package ie.itcarlow.CastleHell;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.Toast;
import com.badlogic.gdx.math.Vector2;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
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

import java.util.Vector;

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
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PATTERNTRAP = "patternTrap";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_INSTAKILLTRAP = "instakillTrap";
	
	
	private Scene mScene;

	private ITextureRegion ArrowTextureRegion;
	private Sprite rightArrowSprite;

	private ITextureRegion leftArrowTextureRegion;
	private Sprite leftArrowSprite;

	private ITextureRegion jumpButtonTextureRegion;
	private Sprite jumpButtonSprite;

	private ITextureRegion backgroundTextureRegion;
	private Sprite backgroundSprite;

	private ITextureRegion splashTextureRegion;
	private Sprite splashSprite;

	private ITextureRegion tapToPlayTextureRegion;
	private Sprite tapToPlaySprite;

	private PhysicsWorld physicsWorld;
	private Player p;
	private Menu menu;
	//ProximityTrap t;
	
	//Platform plat;
	
	private final Context c = this;
	private Vector<Platform> listOfPlatforms = new Vector<Platform>();
	private Vector<ProximityTrap> listOfProximityTraps = new Vector<ProximityTrap>();
	private BitmapTextureAtlas platText;
	public ITextureRegion platform1_region;
	Camera camera;
	SmoothCamera mSmoothCamera;
	private float prevX = 250;
	
	private final byte SPLASH = 0;
	private final byte MENU = 1;
	private final byte GAME = 2;
	byte END = 3;
	public int gameState = SPLASH;
	
	@Override
	public EngineOptions onCreateEngineOptions()
	{
		mSmoothCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, 100, 0, 1.0f);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mSmoothCamera);
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
		//Background stuff
		BitmapTextureAtlas backgroundTexture = new BitmapTextureAtlas(getTextureManager(), 3500, 480);
		backgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backgroundTexture,
				this, "level1Background.png",0,0);
		backgroundTexture.load();
		//End Background Stuff
		
		//Splash Screen Stuff
		platText = new BitmapTextureAtlas(getTextureManager(),120,60);
		platform1_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(platText, this, "plat2.png",0,0);
		platText.load();

		BitmapTextureAtlas splashTexture = new BitmapTextureAtlas(getTextureManager(), 517, 198);
		splashTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTexture,this,"SplashScreen.png",0,0);
		splashTexture.load();

		BitmapTextureAtlas tapToPlayTexture = new BitmapTextureAtlas(getTextureManager(), 159, 30);
		tapToPlayTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tapToPlayTexture,this,"tapToPlay.png",0,0);
		tapToPlayTexture.load();
		//End Splash Screen Stuff
		
		//Game Stuff
		BitmapTextureAtlas arrowTexture = new BitmapTextureAtlas(getTextureManager(), 178, 84);
		ArrowTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(arrowTexture, this, "SourceArrowTQ.png", 0, 0);
		arrowTexture.load();

		BitmapTextureAtlas arrowLeftTexture = new BitmapTextureAtlas(getTextureManager(), 178, 84);
		leftArrowTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(arrowLeftTexture, this,
						"SourceArrowTQLeft.png", 0, 0);
		arrowLeftTexture.load();

		BitmapTextureAtlas jumpButtonTexture = new BitmapTextureAtlas(getTextureManager(), 397, 86);
		jumpButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(jumpButtonTexture, this, "JumpButton.png", 0,
						0);
		jumpButtonTexture.load();

		//t = new ProximityTrap(200, 50, this, getTextureManager());
		p = new Player(this, getTextureManager());
		menu = new Menu(this,getTextureManager());
		//End Game Stuff
		
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception
	{

		this.mScene = new Scene();
		
		// register this activity as a scene touch listener
		// this.mScene.setOnSceneTouchListener(this);

		physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false);
		//this.mScene.registerUpdateHandler(physicsWorld);

		//built in levelloader class
		final LevelLoader levelLoader = new LevelLoader();
		levelLoader.setAssetBasePath("level/");
		
		levelLoader.registerEntityLoader(LevelConstants.TAG_LEVEL, new IEntityLoader() {
			@Override
			public IEntity onLoadEntity(final String pEntityName, final Attributes pAttributes) {
			final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
			final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
			Main.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(Main.this, "Welcome to Castle Hellfire", Toast.LENGTH_LONG).show();
			//Toast.makeText(Main.this, "Loaded level with width=" + width + " and height=" + height + ".", Toast.LENGTH_LONG).show();
			}
			});
			return Main.this.mScene;
			}
			});
		
		
		
		levelLoader.registerEntityLoader(TAG_ENTITY, new IEntityLoader() {
			
			@Override
			public IEntity onLoadEntity(final String pEntityName, final Attributes pAttributes) {
			final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
			final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
			final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_WIDTH);
			final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_HEIGHT);
			final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);
			
			final VertexBufferObjectManager vertexBufferObjectManager = Main.this.getVertexBufferObjectManager();
			
			final Platform spr;
			final Sprite pla;
			final ProximityTrap trap;

			if(type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM)) {
				spr = new Platform(c,getTextureManager(),x, y, width, height);
				listOfPlatforms.add(spr);
				return spr.getSprite();
			}
			else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PROXIMITYTRAP)) {
				trap = new ProximityTrap(x,y,c,getTextureManager());
				listOfProximityTraps.add(trap);
				return trap.getSprite();
			}

			else {
				return null;//move this into the if where we create the platform. 
			}
			}
			});
		
		levelLoader.loadLevelFromAsset(this.getAssets(), "level1.lvl");
		populatePlatforms();
		populateProximityTraps();
		pOnCreateSceneCallback.onCreateSceneFinished(this.mScene);
	}
	public void populatePlatforms()
	{
		int size = listOfPlatforms.size();
		for(int i=0;i<size;i++)
		{
			listOfPlatforms.get(i).Populate(this.mEngine, mScene,physicsWorld);
			listOfPlatforms.get(i).getSprite().setVisible(false);
		}
	}
	public void populateProximityTraps()
	{
		int size = listOfProximityTraps.size();
		for(int i=0;i<size;i++)
		{
			listOfProximityTraps.get(i).Populate(this.mEngine, mScene);
		}
	}
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception
	{
		// TODO Auto-generated method stub
		//if(gameState == SPLASH)
		//{

			splashSprite = new Sprite(CAMERA_WIDTH/7,CAMERA_HEIGHT/3,splashTextureRegion,this.mEngine.getVertexBufferObjectManager());
			splashSprite.setZIndex(11);
			
			mScene.attachChild(splashSprite);
			
			tapToPlaySprite = new Sprite(CAMERA_WIDTH/2.5f,CAMERA_HEIGHT-100,tapToPlayTextureRegion,this.mEngine.getVertexBufferObjectManager())
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
			        	   gameState = MENU;
			       			tapToPlaySprite.setVisible(false);
			       			splashSprite.setVisible(false);
			                break;}
			        }
					return true;
				}
			};
			tapToPlaySprite.setZIndex(11);
			mScene.attachChild(tapToPlaySprite);
			this.mScene.registerTouchArea(tapToPlaySprite);
		//}
		//else if (gameState == GAME)
		//{
		backgroundSprite = new Sprite(-300,0,backgroundTextureRegion,this.mEngine.getVertexBufferObjectManager());
		backgroundSprite.setZIndex(7);
		mScene.attachChild(backgroundSprite);
		mScene.sortChildren();
		
		
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
		        	  prevX = p.getPlayerX();
		        	  p.setMoveRight(true);
		        	  mSmoothCamera.setChaseEntity(p.getCurrentSprite());
		        	   break;}
		          case MotionEvent.ACTION_MOVE: {

		            	break;}
		           case MotionEvent.ACTION_UP:{
		        	   p.setMoveRight(false);
			        	  p.setFaceRight(true);
			        	  p.setFaceLeft(false);
			        	  mSmoothCamera.setChaseEntity(p.getCurrentSprite());
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
		        	  prevX = p.getPlayerX();
		        	  p.setMoveLeft(true);
		        	  mSmoothCamera.setChaseEntity(p.getCurrentSprite());
		        	   break;}
		          case MotionEvent.ACTION_MOVE: {

		            	break;}
		           case MotionEvent.ACTION_UP:{
		        	   	  p.setMoveLeft(false);
			        	  p.setFaceRight(false);
			        	  p.setFaceLeft(true);
			        	  mSmoothCamera.setChaseEntity(p.getCurrentSprite());
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
		this.mEngine.registerUpdateHandler(physicsWorld);

		//t.Populate(this.mEngine, mScene);
		p.Populate(this.mEngine, mScene,physicsWorld);
		menu.Populate(this.mEngine, mScene);

		pOnPopulateSceneCallback.onPopulateSceneFinished();

	}

	@Override
	public void onUpdate(float pSecondsElapsed)
	{
		// TODO Auto-generated method stub

		setSpritesForGameState();
		if(gameState == GAME)
		{
			//p.Move();
			//mSmoothCamera.setCenter(p.getPlayerX(), p.getPlayerY());
			if (p.getPlayerX()>prevX)
			{
				rightArrowSprite.setX(p.getPlayerX()+200);
				leftArrowSprite.setX(p.getPlayerX()-330);
				//rightArrowSprite.setX(p.getPlayerX()+280);
				jumpButtonSprite.setX(p.getPlayerX()-130);
				prevX = p.getPlayerX();
			}
		
			else if (p.getPlayerX()<prevX)
			{
				rightArrowSprite.setX(p.getPlayerX()+200);
				leftArrowSprite.setX(p.getPlayerX()-330);
				//rightArrowSprite.setX(p.getPlayerX()+280);
				jumpButtonSprite.setX(p.getPlayerX()-130);
				prevX = p.getPlayerX();
			}
		}
		if (gameState == SPLASH)
		{
			splashSprite.setVisible(true);
			tapToPlaySprite.setVisible(true);
			//p.getCurrentSprite().setVisible(false);
			backgroundSprite.setVisible(true);
			rightArrowSprite.setVisible(false);
			leftArrowSprite.setVisible(false);
			jumpButtonSprite.setVisible(false);
			p.getCurrentSprite().setVisible(false);
			rightArrowSprite.setZIndex(4);
		}
		else if (gameState == MENU)
		{
			//playButtonSprite.setVisible(true);
			//optionsButtonSprite.setVisible(true);
			//quitButtonSprite.setVisible(true);
			menu.update();
			
			backgroundSprite.setVisible(true);
			if(menu.getStartGame() == true)
			{
				gameState = GAME;
				int size = listOfPlatforms.size();
       			for(int i=0;i<size;i++)
       			{
       				listOfPlatforms.get(i).getSprite().setVisible(true);
       			}
			}
		}
		else if (gameState == GAME)
		{
			splashSprite.setVisible(false);
			tapToPlaySprite.setVisible(false);
			//p.getCurrentSprite().setVisible(true);
			backgroundSprite.setVisible(true);
			rightArrowSprite.setVisible(true);
			leftArrowSprite.setVisible(true);
			jumpButtonSprite.setVisible(true);
			rightArrowSprite.setZIndex(12);
			p.getCurrentSprite().setVisible(true);
		}
		
	}
	void setSpritesForGameState()
	{

	}
	@Override
	public void reset()
	{
		// TODO Auto-generated method stub

	}
}
