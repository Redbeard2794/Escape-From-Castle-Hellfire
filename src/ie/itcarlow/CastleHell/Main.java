package ie.itcarlow.CastleHell;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

public class Main extends BaseGameActivity implements IUpdateHandler {
	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;
	
	private Scene mScene;
	
	private BitmapTextureAtlas playerAnimatedSprite;
	private ITiledTextureRegion playerTiledTextureRegion;
	AnimatedSprite playerSprite;
	
	private BitmapTextureAtlas ArrowTexture;
	private ITextureRegion ArrowTextureRegion;
	Sprite rightArrowSprite;
	
	private BitmapTextureAtlas arrowLeftTexture;
	private ITextureRegion leftArrowTextureRegion;
	Sprite leftArrowSprite;
	
	private BitmapTextureAtlas jumpButtonTexture;
	private ITextureRegion jumpButtonTextureRegion;
	Sprite jumpButtonSprite;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}
    @Override
	public void onCreateResources(
       OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {

    	 loadGfx();
		 pOnCreateResourcesCallback.onCreateResourcesFinished();

    }

    private void loadGfx() {     
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");  
        
        playerAnimatedSprite = new BitmapTextureAtlas(getTextureManager(), 542, 73);
        playerTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(playerAnimatedSprite, this.getAssets(), "PlayerRightFixed.png", 0, 0, 11, 1);
        playerAnimatedSprite.load();
        
        ArrowTexture = new BitmapTextureAtlas(getTextureManager(), 178,84);//237,112
        ArrowTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(ArrowTexture, this, "SourceArrowTQ.png", 0, 0);
        ArrowTexture.load();
        
        arrowLeftTexture = new BitmapTextureAtlas(getTextureManager(),178,84);
        leftArrowTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(arrowLeftTexture, this, "SourceArrowTQLeft.png", 0, 0);
        arrowLeftTexture.load();
        
        jumpButtonTexture = new BitmapTextureAtlas(getTextureManager(),397,86);//397,86
        jumpButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(jumpButtonTexture, this, "JumpButton.png", 0, 0);
        jumpButtonTexture.load();
        
        
        
    }

    @Override
  	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
  			throws Exception {

  		this.mScene = new Scene();
  		this.mScene.setBackground(new Background(255, 0, 0));
  		//register this activity as a scene touch listener
  		//this.mScene.setOnSceneTouchListener(this);
  	    pOnCreateSceneCallback.onCreateSceneFinished(this.mScene);  		
  	}
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		// TODO Auto-generated method stub
		   
		   playerSprite = new AnimatedSprite(0,0,playerTiledTextureRegion, this.getVertexBufferObjectManager());
		   playerSprite.animate(1000);
		   mScene.attachChild(playerSprite);
		   
		   rightArrowSprite = new Sprite(530,390,ArrowTextureRegion,this.mEngine.getVertexBufferObjectManager());
		   mScene.attachChild(rightArrowSprite);
		   
		   leftArrowSprite = new Sprite(10,390,leftArrowTextureRegion,this.mEngine.getVertexBufferObjectManager());
		   mScene.attachChild(leftArrowSprite);
		   
		   jumpButtonSprite = new Sprite(210, 400, jumpButtonTextureRegion, this.mEngine.getVertexBufferObjectManager());
		   mScene.attachChild(jumpButtonSprite);
		   
		   pOnPopulateSceneCallback.onPopulateSceneFinished();
		
	}
	@Override
	public void onUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
}
