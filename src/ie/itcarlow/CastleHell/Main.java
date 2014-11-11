package ie.itcarlow.CastleHell;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

public class Main extends BaseGameActivity implements IUpdateHandler {
	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;
	
	private Scene mScene;
	
	private BitmapTextureAtlas playerAnimatedSprite;
	private ITiledTextureRegion playerTiledTextureRegion;
	AnimatedSprite playerSprite;
	
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
        playerAnimatedSprite = new BitmapTextureAtlas(getTextureManager(), 685, 72);
        playerTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(playerAnimatedSprite, this.getAssets(), "PlayerWalkRight.png", 0, 0, 11, 1);
        playerAnimatedSprite.load();
        
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
		   playerSprite.animate(200);
		   mScene.attachChild(playerSprite);
		   
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
