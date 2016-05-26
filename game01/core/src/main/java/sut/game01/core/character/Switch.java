package sut.game01.core.character;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.*;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.TestScreen;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;

import java.awt.event.MouseAdapter;

/**
 * Created by Administrator on 30/3/2559.
 */
public class Switch {
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false ;
    private int action = 0;
    private Body body;




    private Body initPhysicsBody(World world, float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position = new Vec2(0,0);
        Body body = world.createBody(bodyDef);
        body.setFixedRotation(true);
        body.setGravityScale(0);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(58* TestScreen.M_PER_PIXEL/2-0.2f,
                sprite.layer().height()*TestScreen.M_PER_PIXEL/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        body.createFixture(fixtureDef);
        body.setTransform(new Vec2(x,y),0f);
        return body;
    }

    public Switch(final float x, final float y,final World world){
        sprite = SpriteLoader.getSprite("images/sw.json");
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

    public void update(int delta){
        if(!hasLoaded) return;
        sprite.setSprite(spriteIndex);

    }
    public void paint(Clock clock){
        if(!hasLoaded) return;
        sprite.layer().setTranslation(
                body.getPosition().x/TestScreen.M_PER_PIXEL,
                body.getPosition().y/TestScreen.M_PER_PIXEL);
        //  sprite.layer().setRotation(body.getAngle());
    }
    public void destroy(World world){
        if(world != null)
            world.destroyBody(body);
    }
    public Body body(){
        return body;
    }
    public void change(){
        spriteIndex = (spriteIndex +1)%2;
    }
    public void reset(){
        spriteIndex = 0;
    }
}
