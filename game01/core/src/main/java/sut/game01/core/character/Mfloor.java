package sut.game01.core.character;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.util.Clock;
import sut.game01.core.TestScreen;
import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

/**
 * Created by Gun on 2016/05/18.
 */
public class Mfloor {

    private BodyDef bodyDef;
    private Body body;
    private Image block;
    private ImageLayer blocks;
    private FixtureDef fixtureDef;
    private float starty;
    private float startx;
    private float forecy1;
    private float forcex1;
    private float rangey0;
    private float rangex0;
    private boolean movex;
    private float x;
    private float y;
    private float bx = 0;
    private float by = 0;
    private boolean move ;
    private float dy;
    private boolean stop = false;
    private boolean fmove;

    public Mfloor(float x, float y, World world,float rangex0,float rangey0,float forcex1,float forecy1,boolean movex,boolean move,float dy){
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(x*TestScreen.M_PER_PIXEL,y*TestScreen.M_PER_PIXEL);
        bodyDef.linearDamping = 0.0f;
        bodyDef.angularDamping = 0.0f;
        body = world.createBody(bodyDef);
        body.setGravityScale(0);
        body.setFixedRotation(true);
        PolygonShape shape = new PolygonShape();//|_|
        block = assets().getImage("images/FBlock.png");
        blocks = graphics().createImageLayer(block);
        shape.setAsBox(50 * TestScreen.M_PER_PIXEL/2, 10*TestScreen.M_PER_PIXEL/2);//size
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f; // density much weight much
        fixtureDef.friction =3f;
        body.createFixture(fixtureDef);
        starty =body.getPosition().y;
        startx =body.getPosition().x;
        this.rangex0 = rangex0;
        this.rangey0 = rangey0;
        this.forcex1 = forcex1;
        this.forecy1 = forecy1;
        this.dy =dy;
        if(this.rangex0 == 0)
            this.rangex0 = startx;
        if(this.rangey0 == 0)
            this.rangey0 = starty;
        if(this.dy ==0)
            dy = 1;
        this.movex =movex;
        this.move =move;
        if(!this.move){
            body.setType(BodyType.STATIC);
            bx=startx;
            by=starty;
            stop=true;
        }
        this.fmove = move;
    }

    public void update(int delta) {
        if (move) {
            if (movex) {
                //not care y
                if (body.getPosition().x > rangex0)
                    x = -forcex1;
                else if (body.getPosition().x <= startx)
                    x = forcex1;

                if (body.getPosition().y > starty) {
                    body.applyForce(new Vec2(x, -60f), body.getPosition());
                } else if (body.getPosition().y < starty) {
                    body.applyForce(new Vec2(x, starty - body.getPosition().y), body.getPosition());
                } else {
                    body.applyForce(new Vec2(x, 0), body.getPosition());
                }
                body.setLinearVelocity(new Vec2(0f, 0f));
            } else {
                if (body.getPosition().y < rangey0)
                    y = forecy1 / dy;
                else if (body.getPosition().y >= starty)
                    y = -forecy1;

                if (body.getPosition().x > startx) {
                    body.applyForce(new Vec2(((startx - body.getPosition().x) * 800), 0), body.getPosition());
                } else if (body.getPosition().x < startx) {
                    body.applyForce(new Vec2((startx - body.getPosition().x) * 800, 0), body.getPosition());
                } else {
                    body.applyForce(new Vec2(0, y), body.getPosition());
                }
                body.setLinearVelocity(new Vec2(0f, y));
            }
        }

    }
    public void paint(Clock clock){

       // body.applyForce(new Vec2(100f,0f),body.getPosition());
      // body.setTransform(new Vec2(body.getPosition().x+0.5f*TestScreen.M_PER_PIXEL,y1),0f);
        blocks.setTranslation(
                (body.getPosition().x/TestScreen.M_PER_PIXEL)-25f,
                (body.getPosition().y/TestScreen.M_PER_PIXEL)-5f);
    }
    public Layer layer(){
        return this.blocks;
    }
    public Body body(){
        return body;
    }
    public void destroy(World world){
        world.destroyBody(body);
    }
    public void change(){
        move = !move;
        bx=body.getPosition().x;
        by=body.getPosition().y;
        if(move){
            body.setActive(false);
            body.setType(BodyType.DYNAMIC);
            body.setActive(true);
        System.out.println("Move");
        }
        else if(!move){
            body.setActive(false);
            body.setType(BodyType.STATIC);
            body.setActive(true);
            System.out.println("Stop");
        }
    }
    public void reset(){
        if(stop){
            body().setTransform(new Vec2(bx,by),0);
            body.setType(BodyType.STATIC);
            move = fmove;
        }
    }
}
