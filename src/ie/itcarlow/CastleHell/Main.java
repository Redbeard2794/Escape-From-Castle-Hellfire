package ie.itcarlow.CastleHell;


import ie.itcarlow.CastleHell.SharedPreferencesManager;
import ie.itcarlow.CastleHell.Main;

import java.io.IOException;
import java.util.Vector;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.SAXUtils;
import org.andengine.util.debug.Debug;
import org.andengine.util.level.IEntityLoader;
import org.andengine.util.level.LevelLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Attributes;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Main extends BaseGameActivity implements IUpdateHandler, MessageHandler
{
	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// for levels
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
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DOOR = "door";


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
	
	//options text
	private ITextureRegion toggleDebugDrawTextureRegion;
	private Sprite toggleDebugDrawSprite;
	private Text debugDrawText;
	private String ddText = "Debug draw: ";
	
	private ITextureRegion backButtonTextureRegion;
	private Sprite backButtonSprite;
	

	private PhysicsWorld physicsWorld;
	private Player p;
	private Menu menu;
	// ProximityTrap t;

	// Platform plat;

	private final Context c = this;
	private Vector<Platform> listOfPlatforms = new Vector<Platform>();
	private Vector<ProximityTrap> listOfProximityTraps = new Vector<ProximityTrap>();
	private BitmapTextureAtlas platText;
	public ITextureRegion platform1_region;
	Camera camera;
	SmoothCamera mSmoothCamera;
	private float prevX = 0;
	private Vector<ProximityTrap> fallingTraps = new Vector<ProximityTrap>();
	private final byte SPLASH = 0;
	private final byte MENU = 1;
	private final byte GAME = 2;
	private final byte MULTIPLAYER = 4;
	byte END = 3;
	private final byte OPTIONS = 5;
	public int gameState = SPLASH;
	
	Sound deathScream;
	private Font f;
	private Text t;
	private String myText = "Time elapsed: ";
	private Text deathText;
	private String deaths = "Deaths: ";
	
	
	
	int time;
	int textLength = 50; 
	int realTime = 0;
	int deathCounter = 0;
	
	int currentLevel = 1;
	private Vector<Door> listOfDoors = new Vector<Door>();
	
	Music backgroundMusic;
	
	private WebSocket mWebSocketClient;
	
	private final String TAG = "WebSocketActivity";  
	
	int prevSentX = 0, prevSentY = 0; 
	
	private SharedPreferencesManager mManager;
	boolean dDraw;
	DebugRenderer debug;
	
	//mManager.getHighScore
	
	@Override
	public EngineOptions onCreateEngineOptions()  
	{
		mSmoothCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT,
				200, 0, 1.0f);

		//return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR,
		//		new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
		//		mSmoothCamera);
		
		EngineOptions engine = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				mSmoothCamera);
		engine.getAudioOptions().setNeedsSound(true);
		engine.getAudioOptions().setNeedsMusic(true);
		return engine;
		
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception
	{
		

			//deathScream = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "assets/sounds/wilhelmScream.wav");
		try
		{
			deathScream = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sounds/wilhelmScream.ogg");
		}
		catch (IOException e)
		{
			Debug.e("Cant find sound file");
		}
		
		try
		{
			backgroundMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "sounds/Drums of the Deep.ogg");
			this.backgroundMusic.setLooping(true);
			backgroundMusic.play();

		}
		catch (IOException e)
		{
			Debug.e("Cant find sound file");
		}
		//deathScream = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "assets/sounds/wilhelmScream.wav");
		loadGfx();
		
		 mWebSocketClient = new WebSocket(this);	
		 mEngine.registerUpdateHandler(new FPSLogger());
		 mManager =  SharedPreferencesManager.getInstance(this);
		 dDraw = mManager.getDebugDraw();  
		pOnCreateResourcesCallback.onCreateResourcesFinished();

	}

	private void loadGfx()
	{	

		f = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT,Typeface.BOLD_ITALIC), 27);
		f.load();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		// Background stuff
		BitmapTextureAtlas backgroundTexture = new BitmapTextureAtlas(
				getTextureManager(), 3500, 480);
		backgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(backgroundTexture, this,
						"level1Background.png", 0, 0);
		backgroundTexture.load();
		// End Background Stuff

		// Splash Screen Stuff
		platText = new BitmapTextureAtlas(getTextureManager(), 120, 60);
		platform1_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(platText, this, "plat2.png", 0, 0);
		platText.load();

		BitmapTextureAtlas splashTexture = new BitmapTextureAtlas(
				getTextureManager(), 517, 198);
		splashTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(splashTexture, this, "SplashScreen.png", 0, 0);
		splashTexture.load();

		BitmapTextureAtlas tapToPlayTexture = new BitmapTextureAtlas(
				getTextureManager(), 159, 30);
		tapToPlayTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(tapToPlayTexture, this, "tapToPlay.png", 0, 0);
		tapToPlayTexture.load();
		// End Splash Screen Stuff

		// Game Stuff
		BitmapTextureAtlas arrowTexture = new BitmapTextureAtlas(
				getTextureManager(), 178, 84);
		ArrowTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(arrowTexture, this, "SourceArrowTQ.png", 0, 0);
		arrowTexture.load();

		BitmapTextureAtlas arrowLeftTexture = new BitmapTextureAtlas(
				getTextureManager(), 178, 84);
		leftArrowTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(arrowLeftTexture, this,
						"SourceArrowTQLeft.png", 0, 0);
		arrowLeftTexture.load();

		BitmapTextureAtlas jumpButtonTexture = new BitmapTextureAtlas(
				getTextureManager(), 397, 86);
		jumpButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(jumpButtonTexture, this, "JumpButton.png", 0,
						0);
		jumpButtonTexture.load();
		
		
		BitmapTextureAtlas toggleDebugDrawTexture = new BitmapTextureAtlas(
				getTextureManager(),397,86);
		toggleDebugDrawTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(toggleDebugDrawTexture, this, "debugDrawButton.png",0,0);
		toggleDebugDrawTexture.load();
		
		//private ITextureRegion backButtonTextureRegion;
		BitmapTextureAtlas backButtonTexture = new BitmapTextureAtlas(
				getTextureManager(),397,86);
		backButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(backButtonTexture, this, "backButton.png",0,0);
		backButtonTexture.load();
		
		//private Sprite backButtonSprite;

		// t = new ProximityTrap(200, 50, this, getTextureManager());
		p = new Player(this, getTextureManager());
		menu = new Menu(this, getTextureManager());
		// End Game Stuff

	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception
	{

		this.mScene = new Scene();

		// register this activity as a scene touch listener
		// this.mScene.setOnSceneTouchListener(this);

		physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, 17), false);
		this.mScene.registerUpdateHandler(physicsWorld);

		
		loadLevel();
		
		pOnCreateSceneCallback.onCreateSceneFinished(this.mScene);
	}
	
	
	public void loadLevel()
	{
		// built in levelloader class
				final LevelLoader levelLoader = new LevelLoader();
				levelLoader.setAssetBasePath("level/");

				levelLoader.registerEntityLoader(LevelConstants.TAG_LEVEL,
						new IEntityLoader()
						{
							@Override
							public IEntity onLoadEntity(final String pEntityName,
									final Attributes pAttributes)
							{
								final int width = SAXUtils.getIntAttributeOrThrow(
										pAttributes,
										LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
								final int height = SAXUtils.getIntAttributeOrThrow(
										pAttributes,
										LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
								Main.this.runOnUiThread(new Runnable()
								{
									@Override
									public void run()
									{
										Toast.makeText(Main.this,
												"Welcome to Castle Hellfire",
												Toast.LENGTH_LONG).show();
										// Toast.makeText(Main.this,
										// "Loaded level with width=" + width +
										// " and height=" + height + ".",
										// Toast.LENGTH_LONG).show();
									}
								});
								return Main.this.mScene;
							}
						});

				levelLoader.registerEntityLoader(TAG_ENTITY, new IEntityLoader()
				{

					@Override
					public IEntity onLoadEntity(final String pEntityName,
							final Attributes pAttributes)
					{
						final int x = SAXUtils.getIntAttributeOrThrow(pAttributes,
								TAG_ENTITY_ATTRIBUTE_X);
						final int y = SAXUtils.getIntAttributeOrThrow(pAttributes,
								TAG_ENTITY_ATTRIBUTE_Y);
						final int width = SAXUtils.getIntAttributeOrThrow(pAttributes,
								TAG_ENTITY_ATTRIBUTE_WIDTH);
						final int height = SAXUtils.getIntAttributeOrThrow(pAttributes,
								TAG_ENTITY_ATTRIBUTE_HEIGHT);
						final String type = SAXUtils.getAttributeOrThrow(pAttributes,
								TAG_ENTITY_ATTRIBUTE_TYPE);

						final VertexBufferObjectManager vertexBufferObjectManager = Main.this
								.getVertexBufferObjectManager();

						final Platform spr;
						final Sprite pla;
						final ProximityTrap trap;
						final Door door;

						if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM))
						{
							spr = new Platform(c, getTextureManager(), x, y, width,
									height);
							listOfPlatforms.add(spr);
							return spr.getSprite();
						} else if (type
								.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PROXIMITYTRAP))
						{
							trap = new ProximityTrap(x, y, c, getTextureManager());
							listOfProximityTraps.add(trap);
							return trap.getSprite();
						}
						else if(type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DOOR))
						{
							door = new Door(x,y,c,getTextureManager());
							listOfDoors.add(door);
							return door.getDoorSprite();
						}
						else
						{
							return null;// move this into the if where we create the
										// platform.
						}
					}
				});
				if(currentLevel == 1)
				{
					try { 
						levelLoader.loadLevelFromAsset(this.getAssets(), "level1.lvl");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else if(currentLevel == 2) 
				{
					//p.getBody().setTransform(250 / 30, 250 /30,0);
					//p.getBody().setTransform(-500, 250 /30,0);
					CleanUpLevel();
					
					p.Populate(this.mEngine, mScene, physicsWorld, currentLevel);
					p.getHorseBody().setTransform(250 / 30, 250 /30,0); 
					mSmoothCamera.setChaseEntity(p.getPlayerHorseSprite());
					mSmoothCamera.setMaxVelocityX(1200);
					//p.getHorseBody().setTransform(30 / 30, 30 /30,0); 
					try {
						levelLoader.loadLevelFromAsset(this.getAssets(), "level2.lvl");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				populatePlatforms();
				populateProximityTraps();
				populateDoors();
	}

	public void populatePlatforms()
	{
		int size = listOfPlatforms.size();
		for (int i = 0; i < size; i++)
		{
			listOfPlatforms.get(i).Populate(this.mEngine, mScene, physicsWorld);
			if(currentLevel !=2)
				listOfPlatforms.get(i).getSprite().setVisible(false);
		}
	}

	public void populateProximityTraps()
	{
		int size = listOfProximityTraps.size();
		for (int i = 0; i < size; i++)
		{
			listOfProximityTraps.get(i).Populate(this.mEngine, mScene,physicsWorld);
		}
	}
	
	public void populateDoors()
	{
		int size = listOfDoors.size();
		for(int i= 0;i<size;i++)
		{
			listOfDoors.get(i).Populate(this.mEngine, mScene);
		}
	}
	
	//this method will clear the lists of different objects from the level to allow
	//us to go ahead and load another one
	public void CleanUpLevel()
	{
		//physicsWorld.clearPhysicsConnectors();
		//p.getBody().setTransform(-500, 250 /30,0);
		//p.getHorseBody().setTransform(250/30, 250 /30,0); 
		physicsWorld.destroyBody(p.getBody());
		int size = listOfPlatforms.size();
		for (int i = 0; i < size; i++)
		{
			physicsWorld.destroyBody(listOfPlatforms.get(i).getBody());
			mScene.detachChild(listOfPlatforms.get(i).getSprite());

		}
		listOfPlatforms.clear();
		
		int size1 = listOfProximityTraps.size();
		for (int i = 0; i < size1; i++)
		{
			physicsWorld.destroyBody(listOfProximityTraps.get(i).getBody());
			mScene.detachChild(listOfProximityTraps.get(i).getSprite());
		}
		listOfProximityTraps.clear();
		
		int size2 = listOfDoors.size();
		for (int i = 0; i < size2; i++)
		{
			mScene.detachChild(listOfDoors.get(i).getDoorSprite());
		}
		listOfDoors.clear();
		
		//mSmoothCamera.setChaseEntity(p.getPlayerHorseSprite());
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception
	{
		// TODO Auto-generated method stub
		// if(gameState == SPLASH)
		// {
		t = new Text(20,20,f,myText,textLength,new TextOptions(HorizontalAlign.CENTER),this.getVertexBufferObjectManager());
		t.setColor(1.0f, 0.0f, 0.0f);
		t.setText( myText + realTime);
		
		t.setZIndex(14);
		mScene.attachChild(t);
		deathText = new Text(20,80,f,deaths,textLength,new TextOptions(HorizontalAlign.CENTER),this.getVertexBufferObjectManager());
		deathText.setColor(1.0f, 0.0f, 0.0f);
		deathText.setText( myText + realTime);
		
		deathText.setZIndex(14);
		mScene.attachChild(deathText);
		splashSprite = new Sprite(CAMERA_WIDTH / 7, CAMERA_HEIGHT / 3,
				splashTextureRegion,
				this.mEngine.getVertexBufferObjectManager());
		splashSprite.setZIndex(11);

		mScene.attachChild(splashSprite);

		tapToPlaySprite = new Sprite(CAMERA_WIDTH / 2.5f, CAMERA_HEIGHT - 100,
				tapToPlayTextureRegion,
				this.mEngine.getVertexBufferObjectManager())
		{
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY)
			{
				int myEventAction = pSceneTouchEvent.getAction();

				switch (myEventAction)
				{
					case MotionEvent.ACTION_DOWN:
					{
						break;
					}
					case MotionEvent.ACTION_MOVE:
					{
						break;
					}
					case MotionEvent.ACTION_UP:
					{
						gameState = MENU;
						tapToPlaySprite.setVisible(false);
						splashSprite.setVisible(false);
						break;
					}
				}
				return true;
			}
		};
		tapToPlaySprite.setZIndex(11);
		mScene.attachChild(tapToPlaySprite);
		this.mScene.registerTouchArea(tapToPlaySprite);
		// }
		// else if (gameState == GAME)
		// {
		backgroundSprite = new Sprite(-300, 0, backgroundTextureRegion,
				this.mEngine.getVertexBufferObjectManager());
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
					case MotionEvent.ACTION_DOWN:
					{
						prevX = p.getPlayerX();
						p.setMoveRight(true);
						mSmoothCamera.setChaseEntity(p.getCurrentSprite());
						break;
					}
					case MotionEvent.ACTION_MOVE:
					{

						break;
					}
					case MotionEvent.ACTION_UP:
					{
						p.setMoveRight(false);
						p.setFaceRight(true);
						p.setFaceLeft(false);
						mSmoothCamera.setChaseEntity(p.getCurrentSprite());
						break;
					}
				}
				return true;
			}
		};
		mScene.attachChild(rightArrowSprite);
		this.mScene.registerTouchArea(rightArrowSprite);
		// this.mScene.setTouchAreaBindingOnActionDownEnabled(true);

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
					case MotionEvent.ACTION_DOWN:
					{
						prevX = p.getPlayerX();
						p.setMoveLeft(true);
						mSmoothCamera.setChaseEntity(p.getCurrentSprite());
						break;
					}
					case MotionEvent.ACTION_MOVE:
					{
						break;
					}
					case MotionEvent.ACTION_UP:
					{
						p.setMoveLeft(false);
						p.setFaceRight(false);
						p.setFaceLeft(true);
						mSmoothCamera.setChaseEntity(p.getCurrentSprite());
						break;
					}
				}
				return true;
			}
		};
		mScene.attachChild(leftArrowSprite);
		this.mScene.registerTouchArea(leftArrowSprite);

		jumpButtonSprite = new Sprite(210, 400, jumpButtonTextureRegion,
				this.mEngine.getVertexBufferObjectManager())
		{
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY)
			{
				int myEventAction = pSceneTouchEvent.getAction();

				switch (myEventAction)
				{
					case MotionEvent.ACTION_DOWN:
					{
						if(p.getIsJumping() == false)
						{
							p.Jump(currentLevel);
							p.setIsJumping(true);
						}
						
						break;
					}
					case MotionEvent.ACTION_MOVE:
					{

						break;
					}
					case MotionEvent.ACTION_UP:
					{
						p.setMoveLeft(false);
						p.setMoveRight(false);
						break;
					}
				
				}
				return true;
			}
		};
		mScene.attachChild(jumpButtonSprite);
		this.mScene.registerTouchArea(jumpButtonSprite);
		
		debugDrawText = new Text(20,20,f,myText,textLength,new TextOptions(HorizontalAlign.CENTER),this.getVertexBufferObjectManager());
		debugDrawText.setColor(1.0f, 0.0f, 0.0f);
		debugDrawText.setText( ddText + dDraw);
		
		mScene.attachChild(debugDrawText);
		
		toggleDebugDrawSprite = new Sprite(50, 50, toggleDebugDrawTextureRegion,
				this.mEngine.getVertexBufferObjectManager())
		{
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY)
			{
				int myEventAction = pSceneTouchEvent.getAction();

				switch (myEventAction)
				{

					case MotionEvent.ACTION_UP:
					{
						if(gameState == OPTIONS)
						{
						dDraw = !dDraw;
						debugDrawText.setText( ddText + dDraw);
						mManager.saveDebugDraw(dDraw);
						}
						//private Text debugDrawText;
						//private String ddText = "Debug draw: ";

						break;
					}
				
				}
				return true;
			}
		};
		mScene.attachChild(toggleDebugDrawSprite);
		this.mScene.registerTouchArea(toggleDebugDrawSprite);
		
		backButtonSprite = new Sprite(350, 50, backButtonTextureRegion,
				this.mEngine.getVertexBufferObjectManager())
		{
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY)
			{
				int myEventAction = pSceneTouchEvent.getAction();

				switch (myEventAction)
				{
					case MotionEvent.ACTION_DOWN:
					{
						//gameState = MENU;
						
						break;
					}
					case MotionEvent.ACTION_UP:
					{
						//if(gameState!=OPTIONS)
						//if(gameState == OPTIONS)
						if(gameState == OPTIONS)
						{
							menu.setOptions(false);
							gameState = MENU; 
							toggleDebugDrawSprite.setVisible(false);
							backButtonSprite.setVisible(false);
						}
						//Main.this.reset();
						//gameState = GAME;
						break;
					}
				
				}
				return true;
			}
		};
		mScene.attachChild(backButtonSprite);
		this.mScene.registerTouchArea(backButtonSprite);
		
		this.mEngine.registerUpdateHandler(this);
		physicsWorld.setContactListener(contactListener);
		// t.Populate(this.mEngine, mScene);
		p.Populate(this.mEngine, mScene, physicsWorld, currentLevel);
		menu.Populate(this.mEngine, mScene);
		
		debug = new DebugRenderer(physicsWorld, getVertexBufferObjectManager());
		pScene.attachChild(debug); 
		debug.setVisible(dDraw);



		pOnPopulateSceneCallback.onPopulateSceneFinished();

	}

	@Override
	public void onUpdate(float pSecondsElapsed)
	{
		// TODO Auto-generated method stub

		setSpritesForGameState();
		if (gameState == GAME)
		{
			toggleDebugDrawSprite.setVisible(false);
			// p.Move();
			// mSmoothCamera.setCenter(p.getPlayerX(), p.getPlayerY());
			if (p.getPlayerX() > prevX)
			{
				rightArrowSprite.setX(p.getPlayerX() + 200);
				leftArrowSprite.setX(p.getPlayerX() - 330);
				jumpButtonSprite.setX(p.getPlayerX() - 130);
				t.setX(p.getPlayerX()-280);
				deathText.setX(p.getPlayerX()-280);
				prevX = p.getPlayerX();
			}

			else if (p.getPlayerX() < prevX)
			{
				rightArrowSprite.setX(p.getPlayerX() + 200);
				leftArrowSprite.setX(p.getPlayerX() - 330);
				jumpButtonSprite.setX(p.getPlayerX() - 130);
				t.setX(p.getPlayerX()-280);
				deathText.setX(p.getPlayerX()-280);
				prevX = p.getPlayerX();
			}
			for(int i=0;i<listOfProximityTraps.size();i++)
			{
				if(p.getCurrentSprite().getX() > listOfProximityTraps.get(i).getSprite().getX() -80 && listOfProximityTraps.get(i).getHasFallen() == false)
				{
					listOfProximityTraps.get(i).Trigger();
					fallingTraps.add(listOfProximityTraps.get(i));
				}
			}
			
			splashSprite.setVisible(false);
			tapToPlaySprite.setVisible(false);
			// p.getCurrentSprite().setVisible(true);
			backgroundSprite.setVisible(true);
			rightArrowSprite.setVisible(true);
			leftArrowSprite.setVisible(true);
			jumpButtonSprite.setVisible(true);
			rightArrowSprite.setZIndex(12);
			p.getCurrentSprite().setVisible(true);
			
			if(p.getDead() == true)
			{
				//p.getCurrentSprite().setX(250);
				//p.getCurrentSprite().setY(250);
				if(currentLevel == 1)
					p.getBody().setTransform(250 / 30, 250 /30,0);
				else if(currentLevel == 2)
					p.getHorseBody().setTransform(250 / 30, 250 /30,0); 
				deathScream.play();
				p.setDead(false);
				deathCounter++;
				for(int i=0;i<fallingTraps.size();i++)
				{
					if(fallingTraps.get(i).getHasFallen() == true)
					{
						fallingTraps.get(i).getBody().setTransform(fallingTraps.get(i).getStartX()/30, fallingTraps.get(i).getStartY()/30, 0);
						fallingTraps.get(i).setHasFallen(false);
						fallingTraps.get(i).Reset();
						fallingTraps.remove(i);
					}
				}
			}
			time++;
			if(time>=60)
			{
				realTime++;
				time = 0;
			}
			t.setText( myText + realTime);
			deathText.setText(deaths+deathCounter);
			
			if(p.getCurrentSprite().collidesWith(listOfDoors.get(0).getDoorSprite()))
			{
				if(currentLevel < 2)
				{
					currentLevel+=1;
					//p.getBody().setTransform(250 / 30, 250 /30,0);
					//p.getHorseBody().setTransform(250 / 30, 250 /30,0);

						loadLevel();
					mScene.detachChild(rightArrowSprite);
					mScene.detachChild(leftArrowSprite);
					mScene.unregisterTouchArea(leftArrowSprite);
					mScene.unregisterTouchArea(rightArrowSprite);
				}

				//p.setPlayerX(250);
				//p.setPlayerY(250);
			}
			
			if(currentLevel == 2)
			{
				p.moveOnHorse();
				if (p.getPlayerX() > prevX)
				{
					rightArrowSprite.setX(p.getPlayerX() + 200);
					leftArrowSprite.setX(p.getPlayerX() - 330);
					jumpButtonSprite.setX(p.getPlayerX() - 130);
					t.setX(p.getPlayerX()-280);
					deathText.setX(p.getPlayerX()-280);
					prevX = p.getPlayerX();
				}

				else if (p.getPlayerX() < prevX)
				{
					rightArrowSprite.setX(p.getPlayerX() + 200);
					leftArrowSprite.setX(p.getPlayerX() - 330);
					jumpButtonSprite.setX(p.getPlayerX() - 130);
					t.setX(p.getPlayerX()-280);
					deathText.setX(p.getPlayerX()-280);
					prevX = p.getPlayerX();
				}
			}
			
/*			for(int i=0;i<fallingTraps.size();i++)
			{
				if(fallingTraps.get(i).getHasFallen() == true)
				{
					fallingTraps.get(i).getBody().setTransform(fallingTraps.get(i).getSprite().getX()/30, fallingTraps.get(i).getStartY()/30, 0);
					fallingTraps.get(i).setHasFallen(false);
					fallingTraps.get(i).Reset();
					fallingTraps.remove(i);
				}
			}*/
			
		}
		if (gameState == SPLASH)
		{
			splashSprite.setVisible(true);
			tapToPlaySprite.setVisible(true);
			// p.getCurrentSprite().setVisible(false);
			backgroundSprite.setVisible(true);
			rightArrowSprite.setVisible(false);
			leftArrowSprite.setVisible(false);
			jumpButtonSprite.setVisible(false);
			p.getCurrentSprite().setVisible(false);
			rightArrowSprite.setZIndex(4);
			t.setVisible(false);
			deathText.setVisible(false);
			toggleDebugDrawSprite.setVisible(false);
			debugDrawText.setVisible(false);
			backButtonSprite.setVisible(false);
		} 
		else if (gameState == MENU)
		{
			// playButtonSprite.setVisible(true);
			// optionsButtonSprite.setVisible(true);
			// quitButtonSprite.setVisible(true);
			menu.update();
			t.setVisible(false);
			deathText.setVisible(false);
			backgroundSprite.setVisible(true);
			if (menu.getStartGame() == true)
			{
				gameState = GAME;
				t.setVisible(true);
				deathText.setVisible(true);
				int size = listOfPlatforms.size();
				for (int i = 0; i < size; i++)
				{
					listOfPlatforms.get(i).getSprite().setVisible(true);
				}
				toggleDebugDrawSprite.setVisible(false);
				debugDrawText.setVisible(false);
				backButtonSprite.setVisible(false);
			}
			
			else if(menu.isMultiplayer() == true)
			{
				//p.getBody().setTransform(250/30, 0, 0);
				gameState = MULTIPLAYER;
				t.setVisible(true);
				deathText.setVisible(true);
				int size = listOfPlatforms.size();
				for (int i = 0; i < size; i++)
				{
					listOfPlatforms.get(i).getSprite().setVisible(true);
				}
				toggleDebugDrawSprite.setVisible(false);
				backButtonSprite.setVisible(false);
				mWebSocketClient.sendMessage();
				debugDrawText.setVisible(true);
			}
			
			else if(menu.isOptions() == true)
			{
				gameState = OPTIONS;
				toggleDebugDrawSprite.setVisible(true);
				debug.setVisible(dDraw);
				debugDrawText.setVisible(true);
				backButtonSprite.setVisible(true);
			}
			
		}
		
		else if(gameState == OPTIONS)
		{
			debug.setVisible(dDraw);
			debugDrawText.setVisible(true);
		}
		
		else if(gameState == MULTIPLAYER)
		{
			toggleDebugDrawSprite.setVisible(false);
			// p.Move();
			// mSmoothCamera.setCenter(p.getPlayerX(), p.getPlayerY());
			if (p.getPlayerX() > prevX)
			{
				rightArrowSprite.setX(p.getPlayerX() + 200);
				leftArrowSprite.setX(p.getPlayerX() - 330);
				jumpButtonSprite.setX(p.getPlayerX() - 130);
				t.setX(p.getPlayerX()-280);
				deathText.setX(p.getPlayerX()-280);
				prevX = p.getPlayerX();
			}

			else if (p.getPlayerX() < prevX)
			{
				rightArrowSprite.setX(p.getPlayerX() + 200);
				leftArrowSprite.setX(p.getPlayerX() - 330);
				jumpButtonSprite.setX(p.getPlayerX() - 130);
				t.setX(p.getPlayerX()-280);
				deathText.setX(p.getPlayerX()-280);
				prevX = p.getPlayerX();
			}
			/*for(int i=0;i<listOfProximityTraps.size();i++)
			{
				if(p.getCurrentSprite().getX() > listOfProximityTraps.get(i).getSprite().getX() -80 && listOfProximityTraps.get(i).getHasFallen() == false)
				{
					listOfProximityTraps.get(i).Trigger();
					fallingTraps.add(listOfProximityTraps.get(i));
				}
			}*/
			
			splashSprite.setVisible(false);
			tapToPlaySprite.setVisible(false);
			// p.getCurrentSprite().setVisible(true);
			backgroundSprite.setVisible(true);
			rightArrowSprite.setVisible(true);
			leftArrowSprite.setVisible(true);
			jumpButtonSprite.setVisible(true);
			rightArrowSprite.setZIndex(12);
			p.getCurrentSprite().setVisible(true);
			  
			if(p.getDead() == true)
			{
				if(currentLevel == 1){
					p.getBody().setTransform(250 / 30, 250 /30,0);
				}
				else if(currentLevel == 2)
					p.getHorseBody().setTransform(250 / 30, 250 /30,0); 
				deathScream.play();
				p.setDead(false);
				deathCounter++;
				for(int i=0;i<fallingTraps.size();i++) 
				{
					if(fallingTraps.get(i).getHasFallen() == true)
					{
						fallingTraps.get(i).getBody().setTransform(fallingTraps.get(i).getStartX()/30, fallingTraps.get(i).getStartY()/30, 0);
						fallingTraps.get(i).setHasFallen(false);
						fallingTraps.get(i).Reset();
						fallingTraps.remove(i);
					}
				}  
				updateDeathCounter(deathCounter);
			}
			time++;
			if(time>=60)
			{
				realTime++;
				time = 0;
			}
			t.setText( myText + realTime);
			deathText.setText(deaths+deathCounter);
			
			if(p.getCurrentSprite().collidesWith(listOfDoors.get(0).getDoorSprite()))
			{
				updateLevelState(true);
				if(currentLevel < 2)
				{
					currentLevel+=1;
					//p.getBody().setTransform(250 / 30, 250 /30,0);
					//p.getHorseBody().setTransform(250 / 30, 250 /30,0);

						loadLevel();
					mScene.detachChild(rightArrowSprite);
					mScene.detachChild(leftArrowSprite);
					mScene.unregisterTouchArea(leftArrowSprite);
					mScene.unregisterTouchArea(rightArrowSprite);
				}

				//p.setPlayerX(250);
				//p.setPlayerY(250);
			}
			
			if(currentLevel == 2)
			{
				p.moveOnHorse();
				if (p.getPlayerX() > prevX)
				{
					rightArrowSprite.setX(p.getPlayerX() + 200);
					leftArrowSprite.setX(p.getPlayerX() - 330);
					jumpButtonSprite.setX(p.getPlayerX() - 130);
					t.setX(p.getPlayerX()-280);
					deathText.setX(p.getPlayerX()-280);
					prevX = p.getPlayerX();
				}

				else if (p.getPlayerX() < prevX)
				{
					rightArrowSprite.setX(p.getPlayerX() + 200);
					leftArrowSprite.setX(p.getPlayerX() - 330);
					jumpButtonSprite.setX(p.getPlayerX() - 130);
					t.setX(p.getPlayerX()-280);
					deathText.setX(p.getPlayerX()-280);
					prevX = p.getPlayerX();
				}
			}
			
			//send the players position if it has changed
			if((int)p.getPlayerX() != prevSentX || (int)p.getPlayerY() != prevSentY)
			{
				prevSentX = (int)p.getPlayerX();
				prevSentY = (int)p.getPlayerY();
				updateState(false);
			}
			
			debugDrawText.setVisible(false);
		}
		
		else if (gameState == GAME)
		{

		}

	}

	
	ContactListener contactListener = new ContactListener()
	{
		@Override
	    public void beginContact(Contact contact)
	    {
			final Fixture x1 = contact.getFixtureA();
	        final Fixture x2 = contact.getFixtureB(); 
	        final Object userdata1  = x1.getBody().getUserData();
	        final Object userdata2 = x2.getBody().getUserData();
	        
	        
	        if (userdata1 != null && userdata2 != null)
	        {       
	        	if(currentLevel == 1){
	        		if (userdata1.equals("player") && userdata2.equals("platform") ||
	        				userdata1.equals("platform") && userdata2.equals("player"))
	        		{                                               
	        			p.setIsJumping(false);
	        		}
	        	}
	        	else if(currentLevel == 2)
	        	{
	        		if (userdata1.equals("horse") && userdata2.equals("platform") ||
	        				userdata1.equals("platform") && userdata2.equals("horse"))
	        		{                                               
	        			p.setIsJumping(false);
	        		}
	        	}

	        }
	        
	    }

		@Override
		public void endContact(Contact contact)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse)
		{
			
			final Fixture x1 = contact.getFixtureA();
	        final Fixture x2 = contact.getFixtureB(); 
	        final Object userdata1  = x1.getBody().getUserData();
	        final Object userdata2 = x2.getBody().getUserData();
	        
            if (userdata1.equals("player") && userdata2.equals("proximityTrap") ||
            		userdata1.equals("proximityTrap") && userdata2.equals("player"))
            {
            	//p.setDead(true);
            	for(int i=0;i< fallingTraps.size();i++)
            	{

            		//fallingTraps.get(i).getBody().setTransform(fallingTraps.get(i).getSprite().getX()/30, fallingTraps.get(i).getStartY()/30, 0);
            		//fallingTraps.get(i).Reset();
            		if(fallingTraps.get(i).getHasFallen() == false)
            		{
            			p.setDead(true);
            			fallingTraps.get(i).setHasFallen(true);
            		}
            		//fallingTraps.remove(i);
            		//fallingTraps.remove(fallingTraps.get(i));
            	}
            }
            else if (userdata1.equals("platform") && userdata2.equals("proximityTrap") || 
            		userdata1.equals("proximityTrap") && userdata2.equals("platform"))
            {
            	for(int i=0;i<fallingTraps.size();i++)
            	{
            		//fallingTraps.get(i).Reset();
            		fallingTraps.get(i).setHasFallen(true);
            	}
            }
		}
		
	};
	void setSpritesForGameState()
	{

	}

	@Override
	public void reset()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void handleMessage(String message) {
		try {
			JSONObject json = new JSONObject(message);
    		String type = json.getString("type");
    		String data = json.getString("data");

 		   	
 		   	//issue is here. this never happens
 		   	if ( type.equalsIgnoreCase("updateState"))  
 		   	{
 		   		//hPos = data;
 		   		updateState(true);
 		   	}  
 		   	
 		   	//deal with html messages here
 		   	if(type.equalsIgnoreCase("updateTrap"))
 		   	{
 		   		
 		   	}
 		   	if(type.equalsIgnoreCase("trapHit"))
 		   	{
 		   		
 		   	}

 

		} catch (JSONException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void updateState(boolean remoteMessage)
	{
		if(remoteMessage == true)
		{
			Log.d(TAG,"true");
			//Log.d(TAG, hPos);
			//Log.d(TAG, "Y: "+tY); 
			//waiting.setText(hPos); 
			//print the info on the screen
		}
		else if(remoteMessage == false){
			mWebSocketClient.sendMessage((int)p.getPlayerX(), (int)p.getPlayerY()); 
		}
	}
	public void updateLevelState(boolean update)
	{
		mWebSocketClient.sendMessageDoor(update);
	}
	
	public void updateDeathCounter(int dt)
	{
		mWebSocketClient.sendDeathCount(dt);
	}
}
