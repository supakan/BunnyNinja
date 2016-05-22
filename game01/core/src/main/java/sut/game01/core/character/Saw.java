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
public class Saw {

    private BodyDef bodyDef;
    private Body body;
    private Image block;
    private ImageLayer blocks;
    private FixtureDef fixtureDef;


    public Saw(float x, float y, World world){
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(x*TestScreen.M_PER_PIXEL,y*TestScreen.M_PER_PIXEL);
        body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();//|_|
        block = assets().getImage("images/Saw.png");
        blocks = graphics().createImageLayer(block);
        shape.setAsBox(40 * TestScreen.M_PER_PIXEL/2, 21*TestScreen.M_PER_PIXEL/2);//size
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f; // density much weight much
        fixtureDef.friction=5f;
        body.createFixture(fixtureDef);
    }
    public void paint(Clock clock){
        blocks.setTranslation(
                (body.getPosition().x/TestScreen.M_PER_PIXEL)-20f,
                (body.getPosition().y/TestScreen.M_PER_PIXEL)-11.5f);
    }
    public Layer layer(){
        return this.blocks;
    }
    public Body body(){
        return body;
    }

    public void update(int delta){

    }
    public void destroy(World world){
        world.destroyBody(body);
    }

}
