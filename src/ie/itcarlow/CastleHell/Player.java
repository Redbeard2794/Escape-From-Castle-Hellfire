package ie.itcarlow.CastleHell;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Player extends AnimatedSprite{

	//if true player is facing to the right, if false player is facing towards the left
	boolean right;
	private float playerX, playerY;
	private float centreX, centreY;
	
	
	public Player(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertextBufferObjectManager)
	{     
		super(pX,pY,pTiledTextureRegion,pVertextBufferObjectManager);
		centreX = this.getX() + this.getWidth()/2;
		centreY = this.getY() +this.getHeight()/2;
		
		playerX = this.getX();
		playerY = this.getY();;
		right = true;
	}

	private void update()
	{
		//playerX+=1;
		this.setX(this.getX()+1);
	}
	
}
