package sut.game01.core;

import static playn.core.PlayN.*;



import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.util.Clock;
import sut.game01.core.character.Bunny;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import playn.core.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestScreen extends Screen {
    private sut.game01.core.Finish finish;
    public static float M_PER_PIXEL = 1 / 26.666667f;
    private static int width = 24;
    private static int height = 18;
    private World world;
    private DebugDrawBox2D debugDraw;
    private boolean showDebugDraw = true;
    //floor is static(Never Move don't care physic draw)
    //dynamic body (Can Move Clash under physic law)
    private final ImageLayer bg;
    private final ImageLayer re;
    private final ImageLayer pause;
    private final ScreenStack ss;
    private final Image bgImage;
    private final Image pauseImage;
    //  private Zealot z;
    private Bunny z;
    private int i= 0;
    private int d = 0 ;
    private int t = 0;
    private int tcount = 0;
    private int fcount = 0;
    private int flcount = 0;
    // private Map<String, Bunny> bunny;
    private Map<String, Body> flag;
    private Map<String, Body> floor;
    private Map<String, Body> trap;
    private ArrayList<Body> delete;
    private float rx;
    private float ry;
    private ArrayList<ImageLayer> set;
    private int s = 1;


    public TestScreen(final ScreenStack ss) {
        this.ss = ss;
       this.finish = new sut.game01.core.Finish(ss);
        bgImage = assets().getImage("images/night.png");
        this.bg = graphics().createImageLayer(bgImage);

        Image reImage = assets().getImage("images/Return.png");
        this.re = graphics().createImageLayer(reImage);

        pauseImage = assets().getImage("images/pause.png");
        this.pause = graphics().createImageLayer(pauseImage);




        pause.setTranslation(300,0);
        pause.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseDown(Mouse.ButtonEvent event) {
                finish.score(t,d,(TestScreen)ss.top());
               ss.push(finish);

            }
        });
        re.setTranslation(338,0);
        re.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseDown(Mouse.ButtonEvent event) {
                d++;
                delete.add(z.body());
                i = 1;
            }
        });
      /*  back.setTranslation(292, 212);
        back.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
            }

        });*/
        Vec2 gravity = new Vec2(0.0f,10.0f); // Vec2(x(Horizental),y(Vertical))
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);
        //this.bunny = new HashMap<String, Bunny>();
       // map = new HashMap<String, Body>();
        floor = new HashMap<String, Body>();
        trap = new HashMap<String, Body>();
        flag = new HashMap<String, Body>();
        delete = new ArrayList<Body>();
        set =new ArrayList<ImageLayer>();


      /*  mouse().setListener(new Mouse.Adapter(){ // Homework
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                Bunny z = new Bunny(world,event.x(),event.y());
                bunny.put("bunny_"+i,z);
                i++;
            }
        });*/


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
      //  z = new Bunny(world,500f,200f);
        z = new Bunny(world, 0,0);
      //  z = new Bunny(world,500f,200f);
       // map.put("Bunny",z.body());
       // System.out.println(z.body());

        world.setContactListener(new ContactListener() {
                                     @Override
                                     public void beginContact(Contact contact) {
                                         Body a = contact.getFixtureA().getBody();
                                         Body b = contact.getFixtureB().getBody();
                                         if (contact.getFixtureA().getBody() == z.body()) {
                                             for (Body b1 : trap.values()) {
                                                 if (b1 == b && i == 0) {
                                                     d++;
                                                    // z.body().setActive(false);
                                                     delete.add(z.body());
                                                     i = 1;
                                                 }
                                             }
                                             for (Body b1 : flag.values()) {
                                                 if (b1 == b) {
                                                     b1.setActive(false);
                                                     if(z.body() != null) {
                                                        // z.body().setActive(false);
                                                         z.destroy(world);
                                                     }
                                                     finish.score(t, d, (TestScreen) ss.top());
                                                     ss.push(finish);
                                                 }
                                             }
                                             for (Body b1 : floor.values())
                                                 if (b1 == b)
                                                     z.floor(true);
                                         }

                                         else if (contact.getFixtureB().getBody() == z.body()) {
                                             for (Body a1 : trap.values()) {
                                                 if (a1 == a && i == 0) {
                                                     d++;
                                                    // z.body().setActive(false);
                                                     delete.add(z.body());
                                                     i = 1;
                                                 }
                                             }
                                             for (Body a1 : flag.values()) {
                                                 if (a1 == a){
                                                     a1.setActive(false);
                                                     if(z.body() != null) {
                                                        // z.body().setActive(false);
                                                         z.destroy(world);
                                                     }
                                                 finish.score(t,d,(TestScreen)ss.top());
                                                 ss.push(finish);
                                                 }
                                             }
                                             for (Body a1 : floor.values())
                                                 if (a1 == a)
                                                     z.floor(true);
                                         }
                                     }

             /*       for(Wall w:wall.values()){
                        if(w.body() == b) {
                            z.slide(1);
                            z.floor(0);
                            break;
                        }
                    }
                }
               else if(contact.getFixtureB().getBody() ==z.body()){
                    for(Floor f:floor.values()) {
                        if(f.body() == a) {
                            z.floor(1);
                         //   z.slide(0);
                          //  System.out.println("Floor");
                            break;
                        }
                    }

                    for(FFloor f:floorf.values()){
                        if(f.body() == a){
                            z.floor(1);
                       //     z.slide(0);
                        }
                    }
                   /* for(Wall w:wall.values()){
                        if(w.body() == a) {
                            z.slide(1);
                            z.floor(0);
                           // System.out.println("Wall");
                            break;
                        }
                    }*/


           /*     if(contact.getFixtureA().getBody() == f.body() && contact.getFixtureB().getBody() == z.body()) {
                    z.floor(1);
                    //  s = s + 10;
                    //   b.setActive(false);
                    // delete.add(b);
                }

            }*/

            @Override
            public void endContact(Contact contact) {
/*                Body a =contact.getFixtureA().getBody();
                Body b =contact.getFixtureB().getBody();

                if(contact.getFixtureA().getBody() ==z.body() || contact.m_fixtureB.getBody() == z.body()){
                  //  z.slide(0);
                }


                if(contact.getFixtureA().getBody() ==z.body()){
                    for(Floor f:floor.values()) {
                        if(f.body() == b){
                            z.floor(0);
                            break;
                        }
                    }
                    for(Wall w:wall.values()){
                        if(w.body() == b){
                            z.slide(0);
                            break;
                        }
                    }
                }
              else if(contact.getFixtureB().getBody() ==z.body()){
                    for(Floor f:floor.values()) {
                        if(f.body() == a) {
                            z.floor(0);
                            break;
                        }
                    }
                    for(Wall w:wall.values()){
                        if(w.body() == a){
                            z.slide(0);
                            break;
                        }
                    }
                }*/
            }

            @Override
            public void preSolve(Contact contact, Manifold manifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse) {

            }
        });

    /*   mouse().setListener(new Mouse.Adapter(){ // click create object
            @Override
           public void onMouseUp(Mouse.ButtonEvent event) {

                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyType.STATIC;
                bodyDef.position = new Vec2(event.x()*M_PER_PIXEL,event.y()*M_PER_PIXEL);
                Body body = world.createBody(bodyDef);
                //map.put("coin_"+c,body);
               // c++;
                 PolygonShape shape = new PolygonShape();//|_|
          //      Vec2[] polygon = new Vec2[3];
            //    polygon[0] = new Vec2(-2f,1f);
            //    polygon[1] = new Vec2(-2f, 3.1f);
             //   polygon[2] = new Vec2(0.5f, 3.1f);
             //   shape.set(polygon, polygon.length);
                Image  block = assets().getImage("images/FBlock.png");
                ImageLayer blocks = graphics().createImageLayer(block);
                blocks.height();
                 shape.setAsBox(50 * M_PER_PIXEL/2,
                         10*M_PER_PIXEL/2);//size
                //CircleShape shape = new CircleShape();
               // shape.setRadius(0.4f);//size
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;
                fixtureDef.density = 0.4f; // density much weight much
               // fixtureDef.friction = 0.1f;
             //   fixtureDef.restitution = 2f;//crash 0 no crash
                body.createFixture(fixtureDef);
              //  body.setLinearDamping(1f); // spring
             //   body.setTransform(new Vec2(50,40),0);// picture warp
             //   body.applyForce(new Vec2(-1000f,-500f),body.getPosition());
              //  for(Floor f:floor.values()){
              //   map.put(""+t,f.body());
               //  t++;
               // }
              //  for(Body b:map.values()){
             //       System.out.println(b);
             //   }
             //   System.out.println("------------------------------------------------");
                // System.out.println(z.body());

                blocks.setTranslation(event.x()-25f,event.y()-5f);
                layer.add(blocks);
            }
        });*/

    }



    @Override
    public void wasShown() {
        super.wasShown();
        layer.add(bg);
        layer.add(pause);
        layer.add(re);
        stage(s);

        // z = new Zealot(560f,400f);
          // this.layer.add(z.layer()
       // bunny.put("bunny_1",z);
        if(showDebugDraw){
            CanvasImage image = graphics().createImage(
                    (int)(width/TestScreen.M_PER_PIXEL),
                    (int)(height/TestScreen.M_PER_PIXEL));
            layer.add(graphics().createImageLayer(image));
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
        while (set.size() > 0) {
            graphics().rootLayer().add(set.get(0));
            set.remove(0);}
    }

    @Override
    public void update(int delta) {
        super.update(delta);
       // for(Bunny z:this.bunny.values())
        z.update(delta);
        world.step(0.033f,10,10);

           while (delete.size() > 0) {
               if(delete.get(0) != null){
               delete.get(0).setTransform(new Vec2(M_PER_PIXEL*rx,M_PER_PIXEL*ry),0);
             //   world.destroyBody(delete.get(0));
                delete.remove(0);
               i=0;
               }
             //  z = new Bunny(world,500f,200f);
            }
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
      //  layer.add(this.blocks);
        if(showDebugDraw){
            debugDraw.getCanvas().clear();
            world.drawDebugData();
            debugDraw.getCanvas().setFillColor(Color.rgb(255,0,0));
            debugDraw.getCanvas().drawText("Time: "+t,30f,15f);
            debugDraw.getCanvas().drawText("Die: "+d,100f,15f);
        }
       // for(Bunny z:this.bunny.values()){
            z.paint(clock);
            layer.add(z.layer());
       // }



    }

    @Override
    public void wasHidden() {
        super.wasHidden();
      /*  System.out.println("Was Hidden");
        if(z.body() != null) {
            z.destroy(world);
            System.out.println("Remove Z");
        }*/
        for (Body b1 : trap.values()) {
            world.destroyBody(b1);
        }
        for (Body b1 : flag.values()) {
            world.destroyBody(b1);
        }
        for (Body b1 : floor.values()) {
            world.destroyBody(b1);
        }
        layer.removeAll();
    }

    public void stage(int s){
        this.s=s;
        if(s == 1) {
            floor(100f, 460f);
            floor(300f, 460f);
            blockleft(347f, 360f);
            trap(420f, 460f, 0);
            trap(460f, 460f, 0);
            floors(520f, 460f);
            blockright(537f, 357f);
            trap(570f, 460f, 0);
            trap(610f, 460f, 0);
            trap(650f, 460f, 0);
            ffloor(570f, 330f);
            ffloor(640f, 260f);
            floor(400f, 230f);
            flag(315f, 190f);
            rx = 20f;
            ry = 400f;
            z.body().setTransform(new Vec2(M_PER_PIXEL*rx,M_PER_PIXEL*ry),0);
        }
    }


    public void blockleft(float x,float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position = new Vec2(x*M_PER_PIXEL,y*M_PER_PIXEL);
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();//|_|
        Vec2[] polygon = new Vec2[3];
        polygon[0] = new Vec2(2f,1f);
        polygon[1] = new Vec2(2f, 3.1f);
        polygon[2] = new Vec2(-0.5f, 3.1f);
        shape.set(polygon, polygon.length);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f; // density much weight much
        body.createFixture(fixtureDef);
        Image block = assets().getImage("images/Left.png");
        ImageLayer blocks = graphics().createImageLayer(block);
        blocks.setTranslation(x-17f,y+24f);
        set.add(blocks);
        //graphics().rootLayer().add(blocks);
        floor.put("f"+fcount,body);
        fcount++;
    }
    public void blockright(float x,float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position = new Vec2(x*M_PER_PIXEL,y*M_PER_PIXEL);
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();//|_|
        Vec2[] polygon = new Vec2[3];
        polygon[0] = new Vec2(-2f,1f);
        polygon[1] = new Vec2(-2f, 3.1f);
        polygon[2] = new Vec2(0.5f, 3.1f);
        shape.set(polygon, polygon.length);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f; // density much weight much
        body.createFixture(fixtureDef);
        Image block = assets().getImage("images/Right.png");
        ImageLayer blocks = graphics().createImageLayer(block);
        blocks.setTranslation(x-54f,y+24f);
        set.add(blocks);
        //graphics().rootLayer().add(blocks);
        floor.put("f"+fcount,body);
        fcount++;
    }
    public void floor(float x,float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position = new Vec2(x*M_PER_PIXEL,y*M_PER_PIXEL);
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();//|_|
        shape.setAsBox(200 * M_PER_PIXEL/2, 40*M_PER_PIXEL/2);//size
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f; // density much weight much
        body.createFixture(fixtureDef);
        Image block = assets().getImage("images/Floor.png");
        ImageLayer blocks = graphics().createImageLayer(block);
        blocks.setTranslation(x-100f,y-20f);
        set.add(blocks);
       // graphics().rootLayer().add(blocks);
        floor.put("f"+fcount,body);
        fcount++;
    }
    public void floors(float x,float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position = new Vec2(x*M_PER_PIXEL,y*M_PER_PIXEL);
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();//|_|
        Image block = assets().getImage("images/Floors.png");
        ImageLayer blocks = graphics().createImageLayer(block);
        shape.setAsBox(71 * M_PER_PIXEL/2, 40*M_PER_PIXEL/2);//size
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f; // density much weight much
        body.createFixture(fixtureDef);
        blocks.setTranslation(x-35.5f,y-20f);
        set.add(blocks);
        //graphics().rootLayer().add(blocks);
        floor.put("f"+fcount,body);
        fcount++;
    }
    public void ffloor(float x,float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position = new Vec2(x*M_PER_PIXEL,y*M_PER_PIXEL);
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();//|_|
        Image block = assets().getImage("images/FBlock.png");
        ImageLayer blocks = graphics().createImageLayer(block);
        shape.setAsBox(50 * M_PER_PIXEL/2, 10*M_PER_PIXEL/2);//size
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f; // density much weight much
        body.createFixture(fixtureDef);
        blocks.setTranslation(x-25f,y-5f);
        set.add(blocks);
        //graphics().rootLayer().add(blocks);
        floor.put("f"+fcount,body);
        fcount++;
    }
    public void trap(float x,float y,int pic){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position = new Vec2(x*M_PER_PIXEL,y*M_PER_PIXEL);
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();//|_|
        if(pic == 0) {
             Image   block = assets().getImage("images/trap.png");
            ImageLayer blocks = graphics().createImageLayer(block);
            blocks.setTranslation(x-20f,y-20f);
            set.add(blocks);
        }
        else if(pic == 1){
             Image    block = assets().getImage("images/trapl.png");
            ImageLayer blocks = graphics().createImageLayer(block);
            blocks.setTranslation(x-20f,y-20f);
            set.add(blocks);
        }
        else{
             Image   block = assets().getImage("images/trapr.png");
            ImageLayer blocks = graphics().createImageLayer(block);
            blocks.setTranslation(x-20f,y-20f);
            set.add(blocks);
        }
        shape.setAsBox(40 * M_PER_PIXEL/2, 40*M_PER_PIXEL/2);//size
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f; // density much weight much
        body.createFixture(fixtureDef);
        //graphics().rootLayer().add(blocks);
        trap.put("t"+tcount,body);
        tcount++;
    }
    public void flag (float x,float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position = new Vec2(x*M_PER_PIXEL,y*M_PER_PIXEL);
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();//|_|
        Image   block = assets().getImage("images/Flag.png");
        ImageLayer blocks = graphics().createImageLayer(block);
        blocks.setTranslation(x-16f,y-16f);
        set.add(blocks);
        shape.setAsBox(32 * M_PER_PIXEL/2, 32*M_PER_PIXEL/2);//size
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f; // density much weight
        body.createFixture(fixtureDef);
        //graphics().rootLayer().add(blocks);
        flag.put("f"+flcount,body);
        flcount++;
    }
    public void again(){
        d=0;
        t=0;
    }
    public void next(){
        s++;
        d=0;
        t=0;
    }

    public void choose(int s) {
        this.s = s;

    }

}

