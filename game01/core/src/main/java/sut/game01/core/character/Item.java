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
public class Item {

    private BodyDef bodyDef;
    private Body body;
    private Image block;
    private ImageLayer blocks;
    private FixtureDef fixtureDef;
    private float startx;
    private float starty;
    private boolean show = true;


    public Item(float x, float y, World world){
        // startx =x*TestScreen.M_PER_PIXEL;
        // starty = y*TestScreen.M_PER_PIXEL;
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position = new Vec2(x*TestScreen.M_PER_PIXEL,y*TestScreen.M_PER_PIXEL);
        body = world.createBody(bodyDef);
        body.setFixedRotation(true);
        PolygonShape shape = new PolygonShape();//|_|
        block = assets().getImage("images/Item.png");
        blocks = graphics().createImageLayer(block);
        shape.setAsBox(10 * TestScreen.M_PER_PIXEL/2, 20*TestScreen.M_PER_PIXEL/2);//size
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.3f; // density much weight much
        fixtureDef.friction=1f;
        body.createFixture(fixtureDef);
        startx =body.getPosition().x;
        starty =body.getPosition().y;
    }

    public void update(int delta){
    }
    public void paint(Clock clock){
        blocks.setTranslation(
                (body.getPosition().x/TestScreen.M_PER_PIXEL)-5f,
                (body.getPosition().y/TestScreen.M_PER_PIXEL)-10f);
    }
    public Layer layer(){
        return this.blocks;
    }
    public Body body(){
        return body;
    }
    public void reset(){
        body.setTransform(new Vec2(startx,starty),0);
    }
    public void destroy(World world){
        body.setActive(false);
        show =false;
    }

    public void destroy1(World world){
        if(world != null)
        world.destroyBody(body);
    }

    public void create(World world){
       body.setActive(true);
        show = true;
    }
    public boolean show(){
        return  this.show;
    }

}
