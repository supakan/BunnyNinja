package sut.game01.core;

import static playn.core.PlayN.*;


import com.sun.org.apache.xpath.internal.operations.Bool;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.*;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.util.Clock;
import pythagoras.f.IRectangle;
import sut.game01.core.character.*;
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
    private final ImageLayer bg;
    private final ImageLayer re;
    private final ImageLayer pause;
    private final ImageLayer home;
    private final ImageLayer skull;
    private final ImageLayer time;
    private final ImageLayer itempic;
    private final ScreenStack ss;
    private final Image bgImage;
    private final Image pauseImage;
    private final Image homeImage;
    private final Image skullImage;
    private final Image timeImage;
    private final Image itemImage;
    private Bunny z;

    private int d = 0 ;
    private int t = 0;
    private int it = 0;
    private int im =0;
    private int tcount = 0;
    private int fcount = 0;
    private int flcount = 0;
    private int swcount = 0;
    private int sawcount = 0;
    private int eyecount = 0;
    private int mfcount = 0;
    private int boxcount =0;
    private int itcount =0;
    private int ccount =0;
    private Map<String, Body> flag;
    private Map<String, Body> floor;
    private Map<String, Body> trap;
    private Map<String, Box> boxm;
    private Map<String, Saw> sawm;
    private Map<String, Body> wall;
    private Map<String, Eye> eyem;
    private Map<String,Mfloor> mfloorm;
    private Map<String,Switch> swm;
    private Map<Switch,Mfloor> swom;
    private Map<String,Item> itemm;
    private ArrayList<Mfloor> move;
    private ArrayList<Item> items;
    private float rx;
    private float ry;
    private ArrayList<ImageLayer> set;
    private int s = 2;
    private Eye eye;
    private Saw saw;
    private Item item;
    private RopeJointDef rdef;
    private Mfloor mfloor;
    private Box box;
    private Switch sw;
    private boolean res = false;
    private boolean over =false;
    private boolean moveb =false;
    private int timecount = 0;
    private boolean pauses = false;
    private boolean itemget = false;
    private Map<String, Joint> chain;
    private  Joint  j2;



    /* private RopeJointDef rdef;
    private int i= 0;
    private Joint j2;
    private Body body1;
    private BodyDef o1;
    private BodyDef o2;
    private Body o11;
    private Body o22;
    private int ccount = 0;
    private Zealot z;
    private Map<String, Bunny> bunny;

    private Map<String, Body> bchain;
    private Map<String, ImageLayer> pchain;*/



    public TestScreen(final ScreenStack ss) {
        this.ss = ss;
       this.finish = new sut.game01.core.Finish(ss);
        bgImage = assets().getImage("images/night.png");
        this.bg = graphics().createImageLayer(bgImage);

        Image reImage = assets().getImage("images/Return.png");
        this.re = graphics().createImageLayer(reImage);

        pauseImage = assets().getImage("images/pause.png");
        this.pause = graphics().createImageLayer(pauseImage);

        homeImage = assets().getImage("images/Homes.png");
        this.home = graphics().createImageLayer(homeImage);

        skullImage = assets().getImage("images/skulls.png");
        this.skull = graphics().createImageLayer(skullImage);
        skull.setTranslation(85f,5f);

        timeImage = assets().getImage("images/times.png");
        this.time = graphics().createImageLayer(timeImage);
        time.setTranslation(35f,5f);

        itemImage = assets().getImage("images/items.png");
        this.itempic = graphics().createImageLayer(itemImage);
        itempic.setTranslation(132f,5f);

        home.setTranslation(376,0);
        home.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseDown(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
                ss.remove(ss.top());
            }
        });

        pause.setTranslation(300,0);
        pause.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseDown(Mouse.ButtonEvent event) {
             //   finish.score(t,d,(TestScreen)ss.top());
             //  ss.push(finish);
                pauses =!pauses;
            }
        });
        re.setTranslation(338,0);
        re.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseDown(Mouse.ButtonEvent event) {
                d++;
                res = true;
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
        floor   = new HashMap<String, Body>();
        trap    = new HashMap<String, Body>();
        flag    = new HashMap<String, Body>();
        wall    = new HashMap<String, Body>();
        boxm     = new HashMap<String, Box>();
        sawm     = new HashMap<String, Saw>();
        eyem    = new HashMap<String, Eye>();
        mfloorm =new HashMap<String, Mfloor>();
        swm     = new HashMap<String, Switch>();
        swom    = new HashMap<Switch, Mfloor>();
        itemm     = new HashMap<String, Item>();
        move  = new ArrayList<Mfloor>();
        items = new ArrayList<Item>();
        set     = new ArrayList<ImageLayer>();
        chain   = new HashMap<String, Joint>();

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

        z = new Bunny(world, 0,0);
        world.setContactListener(new ContactListener() {
                                     @Override
                                     public void beginContact(Contact contact) {
                                         Body a = contact.getFixtureA().getBody();
                                         Body b = contact.getFixtureB().getBody();


                                         if (contact.getFixtureA().getBody() == z.body()) {
                                             for (Body b1 : trap.values()) {
                                                 if (b1 == b && !res) {
                                                     d++;
                                                     res = true;
                                                 }
                                             }
                                             for (Body b1 : flag.values()) {
                                                 if (b1 == b) {
                                                     b1.setActive(false);
                                                     over = true;
                                                  //   finish.score(t, d, (TestScreen) ss.top());
                                                   //  ss.push(finish);
                                                 }
                                             }
                                             for (Body b1 : floor.values())
                                                 if (b1 == b)
                                                     z.floor(true);

                                             for (Eye e : eyem.values()) {
                                                 if (e.body() == b) {
                                                     d++;
                                                     res = true;
                                                 }

                                             }
                                             for(Switch sw:swm.values()){
                                                 if(sw.body() == b){
                                                     sw.change();
                                                     move.add(swom.get(sw));
                                                     moveb= true;
                                                  //   swom.get(sw).change();
                                                 }
                                             }

                                              for (Mfloor b1 : mfloorm.values())
                                                 if (b1.body() == b)
                                                     z.floor(true);

                                             for (Box b1 : boxm.values())
                                                 if (b1.body() == b)
                                                     z.floor(true);

                                             for(Item b1 :itemm.values()){
                                                 if(b1.body() == b){
                                                     it++;
                                                     itemget=true;
                                                     items.add(b1);
                                                 }
                                             }

                                         } else if (contact.getFixtureB().getBody() == z.body()) {
                                             for (Body a1 : trap.values()) {
                                                 if (a1 == a && !res) {
                                                     d++;
                                                     res = true;
                                                     // delete.add(z.body());
                                                     // i = 1;
                                                 }
                                             }
                                             for (Body a1 : flag.values()) {
                                                 if (a1 == a) {
                                                     a1.setActive(false);
                                                     over = true;
                                                  //   finish.score(t, d, (TestScreen) ss.top());
                                                  //   ss.push(finish);
                                                 }
                                             }
                                             for (Body a1 : floor.values())
                                                 if (a1 == a)
                                                     z.floor(true);

                                             for (Eye e : eyem.values()) {
                                                 if (e.body() == a) {
                                                     d++;
                                                     res = true;
                                                 }
                                             }
                                             for(Switch sw:swm.values()){
                                                 if(sw.body() == a){
                                                     sw.change();
                                                     move.add(swom.get(sw));
                                                     moveb= true;
                                                 //    swom.get(sw).change();
                                                 }
                                             }
                                             for (Mfloor a1 : mfloorm.values())
                                                 if (a1.body() == a)
                                                     z.floor(true);
                                         }

                                         for (Box a1 : boxm.values())
                                             if (a1.body() == a)
                                                 z.floor(true);

                                         for(Item a1 :itemm.values()){
                                             if(a1.body() == a){
                                                 it++;
                                                 itemget=true;
                                                 items.add(a1);
                                             }
                                         }
                                     }

            @Override
            public void endContact(Contact contact) {


             /*   Body a = contact.getFixtureA().getBody();
                Body b = contact.getFixtureB().getBody();

                if (contact.getFixtureA().getBody() == z.body()) {

                    for(Body b1:wall.values()){
                        if(b1 == b)
                            z.slide(false);

                    }
                }

                else if (contact.getFixtureB().getBody() == z.body()) {
                    for(Body a1:wall.values()){
                        if(a1 == a)
                            z.slide(false);
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
        layer.add(home);
        layer.add(skull);
        layer.add(time);
        layer.add(itempic);
        stage(s);
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
            debugDraw.setFlags(DebugDraw.e_jointBit );//|
                    //DebugDraw.e_shapeBit |
                   // DebugDraw.e_aabbBit);
            debugDraw.setCamera(0, 0, 1f / TestScreen.M_PER_PIXEL);
            world.setDebugDraw(debugDraw);
        }
        while (set.size() > 0) {
            graphics().rootLayer().add(set.get(0));
            set.remove(0);}
    }

    @Override
    public void update(int delta) {
        if(!pauses) {
        super.update(delta);
        z.update(delta);

        for(Saw s:sawm.values())
        s.update(delta);

        for(Eye e:eyem.values())
            e.update(delta);

        for(Mfloor m:mfloorm.values()){
            m.update(delta);
        }

        for(Box b:boxm.values()){
            b.update(delta);
        }

        for(Switch sw:swm.values()){
            sw.update(delta);
        }

        world.step(0.033f,10,10);

                    //while (delete.size() > 0) {
                    if (res) {
                        z.body().setTransform(new Vec2(M_PER_PIXEL * rx, M_PER_PIXEL * ry), 0);
                        z.body().setLinearVelocity(new Vec2(0,0));
                        for (Box b : boxm.values()) {
                            b.reset();
                        }
                        for (Mfloor f : mfloorm.values())
                            f.reset();
                        for (Switch s : swm.values())
                            s.reset();
                        res = false;
                        t = 0;
                        timecount = 0;
                        it =0;
                        for(Item i :itemm.values()){
                            i.create(world);
                        }
                        //   world.destroyBody(delete.get(0));
                        // delete.remove(0);
                        // i=0;
                    }
                    if (moveb) {
                        move.get(0).change();
                        move.clear();
                        moveb = false;
                    }
                    if (over) {
                        finish.score(t, d,it,im, (TestScreen) ss.top());
                        again();
                        ss.push(finish);
                    }
                    if(itemget){
                        items.get(0).destroy(world);
                        items.clear();
                        itemget = false;
                    }
                }
    }

    @Override
    public void paint(Clock clock) {
        if (!pauses) {
            super.paint(clock);
            timecount++;

            if (showDebugDraw) {
                debugDraw.getCanvas().clear();
                world.drawDebugData();
                debugDraw.getCanvas().setFillColor(Color.rgb(255, 0, 0));
                debugDraw.getCanvas().drawText(": " + t, 50f, 15f);
                debugDraw.getCanvas().drawText(": " + d, 100f, 15f);
                debugDraw.getCanvas().drawText(": " + it+"/"+im, 140f, 15f);
            }
            z.paint(clock);
            layer.add(z.layer());

            for (Saw s : sawm.values()) {
                s.paint(clock);
                layer.add(s.layer());
            }
            for (Eye e : eyem.values()) {
                e.paint(clock);
                layer.add(e.layer());
            }
            for (Mfloor m : mfloorm.values()) {
                m.paint(clock);
                layer.add(m.layer());
            }

            for (Box b : boxm.values()) {
                b.paint(clock);
                layer.add(b.layer());
            }
            for (Switch sw : swm.values()) {
                sw.paint(clock);
                layer.add(sw.layer());
            }
            for(Item i:itemm.values()) {
                if (i.show()) {
                    i.paint(clock);
                    layer.add(i.layer().setVisible(true));
                } else {
                    i.paint(clock);
                    layer.add(i.layer().setVisible(false));
                }
            }

            }
            if (timecount % 60 == 0)
                t++;
        }


    @Override
    public void wasHidden() {
        super.wasHidden();
      again();

    }

    public void stage(int s){
        this.s=s;
        if(s == 1) {
            floor(100f, 460f,0);
            floor(300f, 460f,0);
            blockleft(347f, 360f,0);
            trap(420f, 460f, 0);
            trap(460f, 460f, 0);
            floors(520f, 460f,0);
            blockright(537f, 357f,0);
            trap(570f, 460f, 0);
            trap(610f, 460f, 0);
            trap(650f, 460f, 0);
            ffloor(570f, 330f,0);
            ffloor(640f, 260f,0);
            floor(400f, 230f,0);
            flag(315f, 195f);
            ffloor(200f, 230f, 0);
            item(200f,215f);
            rx = 20f;
            ry = 400f;
            z.body().setTransform(new Vec2(M_PER_PIXEL*rx,M_PER_PIXEL*ry),0);
        }
        else if(s == 2){
            floors(40f,460f,0);
            for(int i=0;i<16;i++){
                trap(90f+(40f*i),460f,0);
            }
            mfloor(100f,370f,14f,0,50f,0,true,true,1f);
            //floor(600f,360f,0);
            ffloor(520f,310f,0);
            ffloor(570f,310f,0);
            ffloor(620f,310f,0);
            ffloor(670f, 310f, 0);//-30
            box(550f, 285f);
            floor(320f, 240f, 0);
            mfloor(165f, 240f, 0f, 3.5f, 0f, 3f, false, true, 3f);
            floor(30f,100f,0);
            floor(600f, 100f, 0);
            flag(625, 60f);
            eye(240f, 190f, 400 * M_PER_PIXEL);
            mfloor(455f, 425f, 0f, 6f, 0f, 8f, false, false, 8f);
            sw(30f, 60f);
            ffloor(630f,300f,0);
            ffloor(630f,400f,0);
            item(630f,385f);
            floor(-50f, 220f, 0);
            item(10f,190f);
            trap(70f,220f,1);
            rx =20f;
            ry =410f;
            z.body().setTransform(new Vec2(M_PER_PIXEL*rx,M_PER_PIXEL*ry),0);
        }
   /*     else if(s ==3){
            floor(50f,200f,0);
            floors(200f,100f,0);
            box(50f,150f);
            joint();
        }*/



    }


    public void blockleft(float x,float y,int chain){
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
        floor.put("f"+fcount,body);
        fcount++;
    /*    if(chain == 1){
         o1=bodyDef;
         o11=body;
        }
       else if(chain == 2){
            o2=bodyDef;
            o22=body;
        }*/

    }
    public void blockright(float x,float y,int chain){
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
        floor.put("f"+fcount,body);
        fcount++;
       /*    if(chain == 1){
         o1=bodyDef;
         o11=body;
        }
       else if(chain == 2){
            o2=bodyDef;
            o22=body;
        }*/
    }
    public void floor(float x,float y,int chain){
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
        floor.put("f"+fcount,body);
        fcount++;
    }
    public void floors(float x,float y,int chain){
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
        floor.put("f"+fcount,body);
        fcount++;
    }
    public void ffloor(float x,float y,int chain){
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
        fixtureDef.friction = 10f;
        body.createFixture(fixtureDef);
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
        flag.put("f"+flcount,body);
        flcount++;
    }

    public void joint(){
        rdef = new RopeJointDef();
        rdef.type = JointType.ROPE;
        rdef.bodyA =floor.get("f"+(fcount-1));
        rdef.bodyB = boxm.get("Box"+(boxcount-1)).body();
        rdef.collideConnected = false;
        rdef.maxLength = 5.75f;
        rdef.localAnchorA.set(0,-.125f);
        rdef.localAnchorB.set(0,-0.725f);

        //world.createJoint(rdef);
        //    RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
         //   revoluteJointDef.bodyA = floor.get("f"+(fcount-1));//provided by testbed
          //  revoluteJointDef.bodyB = boxm.get("Box"+(boxcount-1)).body();
          //  revoluteJointDef.localAnchorA.set(0,0);//world coords, because m_groundBody is at (0,0)
          //  revoluteJointDef.localAnchorB.set(0, -130 * M_PER_PIXEL);
          //  revoluteJointDef.enableMotor = true;
          //  revoluteJointDef.maxMotorTorque = 2000f;
          //  revoluteJointDef.motorSpeed =3600f;
        j2 =world.createJoint(rdef);
       // blockj(a1,b1,ccount);
        chain.put(String.valueOf(ccount),j2);
        ccount++;
    }


    public void eye(float x,float y,float rangex){
        eye = new Eye(x,y,world,rangex);
        eyem.put("Eye"+eyecount,eye);
        eyecount++;
    }
    public void saw(float x,float y){
        saw = new Saw(x,y,world);
        sawm.put("Saw"+sawcount,saw);
        sawcount++;
    }

    public void box(float x,float y){
        box = new Box(x,y,world);
        boxm.put("Box"+boxcount,box);
        boxcount++;
    }

    public void mfloor(float x,float y,float rangex,float rangey,float forcex,float forcey,boolean movex,boolean move,float dy){
        mfloor = new Mfloor(x,y,world,rangex,rangey,forcex,forcey,movex,move,dy);
        mfloorm.put("Mfloor"+mfcount,mfloor);
        mfcount++;
    }

    public void sw(float x,float y){
        sw = new Switch(x,y,world);
        swm.put("sw"+swcount,sw);
        swom.put(sw,mfloor);
        swcount++;
    }

    public void item(float x,float y){
        item = new Item(x,y,world);
        itemm.put("item"+itcount,item);
        itcount++;
        im++;
    }

    public void again(){
        d=0;
        t=0;
        z.floor(true);
        z.cheat(false);
        z.body().setLinearVelocity(new Vec2(0f,0f));
        for (Body b1 : trap.values()) {
            world.destroyBody(b1);
        }

    for (Body b1 : flag.values()) {
        world.destroyBody(b1);
    }
    for (Body b1 : floor.values()) {
        world.destroyBody(b1);
    }
    for(Box b:boxm.values()){
        b.destroy(world);
    }
    for(Mfloor m:mfloorm.values()){
        m.destroy(world);
    }
    for(Eye e:eyem.values()){
        e.destroy(world);
    }
    for(Saw s:sawm.values()){
        s.destroy(world);
    }
        for(Switch sw:swm.values()){
            sw.destroy(world);
        }
        for(Item i:itemm.values()){
            i.destroy1(world);
        }

    mfloorm.clear();
    boxm.clear();
    eyem.clear();
    sawm.clear();
    trap.clear();
    flag.clear();
    floor.clear();
    swm.clear();
    swom.clear();
    itemm.clear();
        if(layer != null);
    layer.removeAll();
    itcount = 0;
    fcount = 0;
    flcount = 0;
    swcount = 0;
    sawcount = 0;
    eyecount = 0;
    mfcount = 0;
    boxcount =0;
    timecount = 0;
    it =0;
    im =0;
        if(over = true)
            over = false;
    }
    public void next(){
        if(s < 2){
        s++;
        again();
        }

        else{
            s=0;
            ss.remove(ss.top());
            ss.remove(ss.top());
        }


    }

    public void choose(int s) {
        this.s = s;
    }


   /*
    public void blockj(BodyDef a,BodyDef b,int ccount){

        float a1 = Vec2.dot(a.position,new Vec2(0,1));
        float b1 = Vec2.dot(b.position,new Vec2(0,1));
        float c =(a1+b1)/2;
        float c1 = Vec2.dot(a.position,new Vec2(1,0));

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.gravityScale=0f;
        bodyDef.position = new Vec2(c1,c);
        Body body = world.createBody(bodyDef);
        body.setFixedRotation(true);
      //  PolygonShape shape = new PolygonShape();

       // Image block = assets().getImage("images/FBlock.png");
       // ImageLayer blocks = graphics().createImageLayer(block);
//        shape.setAsBox(10 * M_PER_PIXEL/2, 85*M_PER_PIXEL/2);//size
        ChainShape shape = new ChainShape();
        shape.createChain(new Vec2[]{new Vec2(100f*M_PER_PIXEL,100f*M_PER_PIXEL),new Vec2(100f*M_PER_PIXEL,200f*M_PER_PIXEL)},1);
       FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f; // density much weight much
        body.createFixture(fixtureDef);


  //      blocks.setTranslation((c1/M_PER_PIXEL)-25f,(c/M_PER_PIXEL)-5f);
       // pchain.add(blocks);
        bchain.put(String.valueOf(ccount),body);

         public void wall(float x,float y,int chain){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position = new Vec2(x*M_PER_PIXEL,y*M_PER_PIXEL);
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();//|_|
        shape.setAsBox(40 * M_PER_PIXEL/2, 600*M_PER_PIXEL/2);//size
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f; // density much weight much
        fixtureDef.friction = 100f;
        body.createFixture(fixtureDef);
      //  Image block = assets().getImage("images/Floor.png");
      //  ImageLayer blocks = graphics().createImageLayer(block);
     //   blocks.setTranslation(x-100f,y-20f);
     //   set.add(blocks);
        wall.put("w"+fcount,body);
        fcount++;
    }
    }*/


}

