package sut.game01.core;

import static playn.core.PlayN.*;



import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.character.Bunny;
import sut.game01.core.character.Zealot;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import playn.core.*;

import java.util.HashMap;
import java.util.Map;

public class TestScreen extends Screen {
    //floor is static(Never Move don't care physic draw)
    //dynamic body (Can Move Clash under physic law)
    private final ImageLayer bg;
    private final ImageLayer back;
    private final ScreenStack ss;
    private final Image bgImage;
    //  private Zealot z;
    private Bunny z;

    public static float M_PER_PIXEL = 1 / 26.666667f;
    private static int width = 24;
    private static int height = 18;
    private World world;
    private DebugDrawBox2D debugDraw;
    private boolean showDebugDraw = true;
    private Map<String, Bunny> bunny;
    private int i=1;
    private GroupLayer layer1 ;
    private int c =0 ;

    public TestScreen(final ScreenStack ss) {
        this.ss = ss;

        bgImage = assets().getImage("images/bg.png");
        this.bg = graphics().createImageLayer(bgImage);

        Image backImage = assets().getImage("images/back.png");
        this.back = graphics().createImageLayer(backImage);

        back.setTranslation(292, 212);
        back.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
            }

        });
        Vec2 gravity = new Vec2(0.0f,10.0f); // Vec2(x(Horizental),y(Vertical))
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);
        this.bunny = new HashMap<String, Bunny>();
      /*  mouse().setListener(new Mouse.Adapter(){ // click create object
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {

                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyType.DYNAMIC;
                bodyDef.position = new Vec2(event.x()*M_PER_PIXEL,event.y()*M_PER_PIXEL);
                Body body = world.createBody(bodyDef);

                // PolygonShape shape = new PolygonShape();//|_|
                // shape.setAsBox(1,1);//size
                CircleShape shape = new CircleShape();
                shape.setRadius(0.4f);//size
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;
                fixtureDef.density = 0.4f; // density much weight much
                fixtureDef.friction = 0.1f;
                fixtureDef.restitution = 2f;//crash 0 no crash
                body.createFixture(fixtureDef);
                body.setLinearDamping(1f); // spring
                //body.setTransform(new Vec2(50,40),0);// picture warp
                //body.applyForce(new Vec2(-1000f,-500f),body.getPosition());
            }
        });*/

        mouse().setListener(new Mouse.Adapter(){ // Homework
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                Bunny z = new Bunny(world,event.x(),event.y());
                bunny.put("bunny_"+i,z);
                i++;
            }
        });


        Body ground = world.createBody(new BodyDef());
        EdgeShape groundshape = new EdgeShape();
        groundshape.set(new Vec2(0,0),new Vec2(0,height));
        ground.createFixture(groundshape, 0.0f);

        EdgeShape groundshape1 = new EdgeShape();
        groundshape1.set(new Vec2(0,height),new Vec2(width,height));
        ground.createFixture(groundshape1,0.0f);

        EdgeShape groundshape2 = new EdgeShape();
        groundshape2.set(new Vec2(width,0),new Vec2(width,height));
        ground.createFixture(groundshape2,0.0f);

        EdgeShape groundshape3 = new EdgeShape();
        groundshape3.set(new Vec2(0,0),new Vec2(width,0));
        ground.createFixture(groundshape3,0.0f);
    }



    @Override
    public void wasShown() {
        super.wasShown();
        layer1 = this.layer;
        layer1.add(bg);
        // z = new Zealot(560f,400f);
          // this.layer.add(z.layer()
        c=1;
        if(showDebugDraw){
            CanvasImage image = graphics().createImage(
                    (int)(width/TestScreen.M_PER_PIXEL),
                    (int)(height/TestScreen.M_PER_PIXEL));
            layer1.add(graphics().createImageLayer(image));
            debugDraw = new DebugDrawBox2D();
            debugDraw.setCanvas(image);
            debugDraw.setFlipY(false);
            debugDraw.setStrokeAlpha(150);
            debugDraw.setFillAlpha(75);
            debugDraw.setStrokeWidth(2.0f);
            debugDraw.setFlags(DebugDraw.e_shapeBit |
                    DebugDraw.e_jointBit |
                    DebugDraw.e_aabbBit);
            debugDraw.setCamera(0,0,1f/TestScreen.M_PER_PIXEL);
            world.setDebugDraw(debugDraw);
        }


    }

    @Override
    public void update(int delta) {
        super.update(delta);
        for(Bunny z:this.bunny.values())
        z.update(delta);
        world.step(0.033f,10,10);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        if(showDebugDraw){
            debugDraw.getCanvas().clear();
            world.drawDebugData();
        }
        for(Bunny z:this.bunny.values()){
            z.paint(clock);
            layer1.add(z.layer());
        }
    }
}
