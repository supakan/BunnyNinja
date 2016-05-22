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
public class Eye {
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false ;
    private int action = 0;
    private Body body;
    private float x1 =0f;
    private float y1 =0f;
    private float rangex;
    private float starty;
    private float startx;
    public enum State {
        LWALK,RWALK
    };


    private State state = State.RWALK; // Start State
    private int e = 0 ; //Time Control
    private int offset = 0 ; //Set Of Picture

    private Body initPhysicsBody(World world, float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
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
        //  fixtureDef.restitution = 0.1f;
        body.createFixture(fixtureDef);

        //  body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x,y),0f);
        startx =body.getPosition().x;
        starty =body.getPosition().y;
        return body;
    }

    public Eye(final float x, final float y,final World world,float rangex){
        this.rangex =rangex;
        sprite = SpriteLoader.getSprite("images/Eyes.json");
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
        e = e +delta;

        if(body.getPosition().x > rangex+1f)
            body.setTransform(new Vec2(rangex,starty),0);

        else if(body.getPosition().x < startx-1f)
            body.setTransform(new Vec2(startx,starty),0);

        else if(body.getPosition().x >= rangex ){
            x1=-50f;
            state =State.LWALK;
        }
        else if(body.getPosition().x <= startx ){
            x1 =50f;
            state =State.RWALK;
        }

        if(body.getPosition().y != starty){
            body.setTransform(new Vec2(body.getPosition().x,starty),0);
        }

        if(e>150){
            switch (state){
                case LWALK: offset = 0;
                    break;
                case RWALK: offset = 4;
                    break;

            }
            spriteIndex = offset + ((spriteIndex + 1)%4);
            sprite.setSprite(spriteIndex);
            e=0;
            body.applyForce(new Vec2(x1,y1),body.getPosition());
            body.setLinearVelocity(new Vec2(0f, 0f));
        }

    }
    public void paint(Clock clock){
        if(!hasLoaded) return;
        sprite.layer().setTranslation(
                body.getPosition().x/TestScreen.M_PER_PIXEL,
                body.getPosition().y/TestScreen.M_PER_PIXEL);
        //  sprite.layer().setRotation(body.getAngle());
    }
    public void destroy(World world){
        world.destroyBody(body);
    }
    public Body body(){
        return body;
    }
}
