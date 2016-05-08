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
public class Bunny {
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false ;
    private int action = 0;
    private Body body;
    public enum State {
        RIDLE,LIDLE, LRUN, RRUN,RATK,LATK,LJUM,RJUM,LSLD,RSLD
    };


    private State state = State.LRUN; // Start State
    private int e = 0 ; //Time Control
    private int offset = 0 ; //Set Of Picture

    private Body initPhysicsBody(World world, float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0,0);
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(58* TestScreen.M_PER_PIXEL/2,
                sprite.layer().height()*TestScreen.M_PER_PIXEL/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 1f;
        body.createFixture(fixtureDef);

        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x,y),0f);

        return body;
    }

    public Bunny(final World world,final float x,final float y){
        sprite = SpriteLoader.getSprite("images/bunny.json");
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

        PlayN.keyboard().setListener(new Keyboard.Adapter() {
            @Override
            public void onKeyDown(Keyboard.Event event) {
                if (event.key() == Key.RIGHT && state != State.LJUM && state != State.RJUM) {
                    action = 4;
                    state = State.RRUN;
                    spriteIndex = -1;
                    e=0;
                }
                else  if (event.key() == Key.LEFT && state != State.LJUM && state != State.RJUM){
                    action = 3;
                    state = State.LRUN;
                    spriteIndex = -1;
                    e=0;
                }
               else if(event.key() == Key.Z && state != State.LJUM && state != State.RJUM){
                    if(state == State.LIDLE || state == State.LRUN || state == State.LATK){
                    action = 5;
                    state = State.LATK;
                    }
                    else if(state == State.RIDLE || state == State.RRUN || state == State.RATK){
                        action = 6;
                        state = State.RATK;
                    }
                    spriteIndex = -1;
                    e=0;
                }
                else if(event.key() == Key.SPACE && state != State.LJUM && state != State.RJUM){
                    if(state == State.LIDLE || state == State.LRUN || state == State.LATK){
                        action = 7;
                        state = State.LJUM;
                    }
                    else if(state == State.RIDLE || state == State.RRUN || state == State.RATK){
                        action = 8;
                        state = State.RJUM;
                    }
                    spriteIndex = -1;
                    e=0;
                }
            }

            @Override
            public void onKeyUp(Keyboard.Event event) {

                if (event.key() == Key.LEFT) {
                    action = 1;
                    state = State.LIDLE;
                    spriteIndex = -1;
                    e = 0;
                } else if (event.key() == Key.RIGHT) {
                    action = 0;
                    state = State.RIDLE;
                    spriteIndex = -1;
                    e = 0;
                } else if (event.key() == Key.Z) {
                    if (state == State.LATK) {
                        action = 1;
                        state = State.LIDLE;
                    } else if (state == State.RATK) {
                        action = 0;
                        state = State.RIDLE;
                    }
                    spriteIndex = -1;
                    e = 0;
                }
            }
        });
    }





    public Layer layer(){
        return sprite.layer();
    }

    public void update(int delta){
        if(!hasLoaded) return;
        e = e +delta;

       if(action == 3){
            state = State.LRUN;
        }
        else if(action == 4){
            state = State.RRUN;
        }
        else if(action == 0){
            state = State.RIDLE;
        }
        else if(action == 1){
            state = State.LIDLE;
        }
        else if(action == 5){
            state = State.LATK;
        }
        else if(action == 6){
            state = State.RATK;
        }


       if(e>150){

            switch (state){
                case LIDLE: offset = 12;
                    break;
                case RIDLE: offset = 0;
                    break;
                case LRUN: offset = 8 ;
                    if(spriteIndex == 11){
                        state =State.LIDLE;
                    }
                    break;
                case RRUN: offset = 20 ;
                    if(spriteIndex == 23){
                        state =State.RIDLE;
                        //action = 0;
                    }
                    break;
                case LATK: offset = 28 ;
                    if(spriteIndex == 31){
                        state = State.LIDLE;
                    }
                    break;
                case RATK: offset = 24 ;
                    if(spriteIndex == 27){
                        state = State.RIDLE;
                    }
                    break;
                case LJUM: offset = 4 ;
                    if(spriteIndex == 7){
                        state = State.LIDLE;
                    }
                    break;
                case RJUM: offset = 16 ;
                    if(spriteIndex == 19){
                        state = State.RIDLE;
                    }
                    break;
            }
       /* if(e>150){ //test run

            switch (state){
                case LRUN: offset = 8;
                    if(spriteIndex == 11){
                        state =State.RRUN;
                    }
                    break;
                case RRUN: offset = 20;
                    if(spriteIndex == 23){
                        state =State.LRUN;
                    }
                    break;

            }*/
            spriteIndex = offset + ((spriteIndex + 1)%4);
            sprite.setSprite(spriteIndex);
            e=0;
            //System.out.println(action);
        }

    }
    public void paint(Clock clock){
        if(!hasLoaded) return;
        sprite.layer().setTranslation(
                body.getPosition().x/TestScreen.M_PER_PIXEL+12f,
                body.getPosition().y/TestScreen.M_PER_PIXEL);
    }

}
