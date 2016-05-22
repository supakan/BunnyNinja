package sut.game01.core;

import static playn.core.PlayN.*;


import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.character.Zealot;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import playn.core.*;

public class Finish extends Screen {
    private final ImageLayer bg;
    private final ScreenStack ss;
    private final Image bgImage;
    private final ImageLayer show;
    private final Image showImage;
    private final ImageLayer next;
    private final Image nextImage;
    private final ImageLayer re;
    private final Image reImage;
    private final ImageLayer home;
    private final Image homeImage;
    private boolean showDebugDraw = true;
    private DebugDrawBox2D debugDraw;
    private static int width = 24;
    private static int height = 18;
    private int t;
    private int d;
    private World world;
    private TestScreen test;

    public Finish(final ScreenStack ss) {
        this.ss = ss;

        bgImage = assets().getImage("images/Black.png");
        this.bg = graphics().createImageLayer(bgImage);

        showImage =assets().getImage("images/show.png");
        this.show = graphics().createImageLayer(showImage);
        show.setTranslation(190f,50f);

        nextImage =assets().getImage("images/Next.png");
        this.next = graphics().createImageLayer(nextImage);
        next.setTranslation(260f,285f);

       /* next.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseDown(Mouse.ButtonEvent event) {
                test.next();
                ss.remove(ss.top());

            }
        });*/

        reImage =assets().getImage("images/ReturnB.png");
        this.re = graphics().createImageLayer(reImage);
        re.setTranslation(320f,285f);

        re.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseDown(Mouse.ButtonEvent event) {
                test.again();
                ss.remove(ss.top());

            }
        });
        homeImage =assets().getImage("images/Home.png");
        this.home = graphics().createImageLayer(homeImage);
        home.setTranslation(380f,285f);

        home.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseDown(Mouse.ButtonEvent event) {
                test.choose(0);
                ss.remove(ss.top());
                ss.remove(ss.top());
                ss.remove(ss.top());

            }
        });
        next.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseDown(Mouse.ButtonEvent event) {
                test.next();
                ss.remove(ss.top());

            }
        });

        Vec2 gravity = new Vec2(0.0f,10.0f);
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);

    }

    @Override
    public void wasShown() {
        super.wasShown();
        layer.add(bg);
        layer.add(show);
        layer.add(re);
        layer.add(next);
        layer.add(home);
        if(showDebugDraw){
            CanvasImage image = graphics().createImage(
                    (int)(width/TestScreen.M_PER_PIXEL),
                    (int)(height/TestScreen.M_PER_PIXEL));
            layer.add(graphics().createImageLayer(image));
            debugDraw = new DebugDrawBox2D();
            debugDraw.setCanvas(image);
            debugDraw.setFlipY(false);
            debugDraw.setStrokeAlpha(250);
            debugDraw.setFillAlpha(250);
            debugDraw.setStrokeWidth(2.0f);
            debugDraw.setFlags(DebugDraw.e_shapeBit |
                    DebugDraw.e_jointBit |
                    DebugDraw.e_aabbBit);
            debugDraw.setCamera(0,0,1f/TestScreen.M_PER_PIXEL);
            world.setDebugDraw(debugDraw);
        }
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        if(showDebugDraw){
            debugDraw.getCanvas().clear();
            world.drawDebugData();
            debugDraw.getCanvas().setFillColor(Color.rgb(255,0,0));
            debugDraw.getCanvas().drawText(""+t,370f,190f);
            debugDraw.getCanvas().drawText(""+d,370f,260f);
        }
    }
    public void score(int a,int b,TestScreen test){
        t=a;
        d=b;
        this.test =test;
    }
}
