package sut.game01.core.character;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.*;
import playn.core.util.Callback;
import sut.game01.core.TestScreen;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;

import java.awt.event.MouseAdapter;

/**
 * Created by Administrator on 30/3/2559.
 */
public class FFloor {
  private Sprite sprite;
  private int spriteIndex = 0;
  private boolean hasLoaded = false ;
  private int action = 0;
  private int e = 0 ; //Time Control
  private int offset = 0 ; //Set Of Picture
  private Body body;

  public FFloor(final World world, final float x, final float y){
    sprite = SpriteLoader.getSprite("images/Block.json");
    sprite.addCallback(new Callback<Sprite>() {
      @Override
      public void onSuccess(Sprite result) {
        sprite.setSprite(spriteIndex);
        sprite.layer().setOrigin(sprite.width()/2f,sprite.height()/2f);
        sprite.layer().setTranslation(x, y);
        body = initPhysicsBody(world,
                TestScreen.M_PER_PIXEL*x,
                TestScreen.M_PER_PIXEL*y);
        hasLoaded = true;
      }
      @Override
      public void onFailure(Throwable cause) {
        PlayN.log().error("Error loading image",cause);
      }
    });
  }

  public Layer layer(){
    return sprite.layer();
  }

  private Body initPhysicsBody(World world, float x, float y){
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.STATIC;
    bodyDef.position = new Vec2(0,0);
    Body body = world.createBody(bodyDef);
    body.setFixedRotation(false);
    PolygonShape shape = new PolygonShape();
    shape.setAsBox(58* TestScreen.M_PER_PIXEL/2-0.2f,
            sprite.layer().height()*TestScreen.M_PER_PIXEL/2-0.1f);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
//    fixtureDef.density = 1f;
  //  fixtureDef.friction = 1f;
    fixtureDef.restitution = 0.1f;
    body.createFixture(fixtureDef);

    body.setLinearDamping(0.2f);
    body.setTransform(new Vec2(x,y),0f);

    return body;
  }
  public Body body(){
    return this.body;
  }

}

