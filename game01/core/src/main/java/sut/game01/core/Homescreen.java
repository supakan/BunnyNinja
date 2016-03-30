package sut.game01.core;

import static playn.core.PlayN.*;


import playn.core.Image;
import playn.core.ImageLayer;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import playn.core.*;

public class Homescreen extends Screen {
    private final ImageLayer bg;
    private final ImageLayer start;
    private final ImageLayer exit;
    private final ScreenStack ss ;
   // private TestScreen testScreen;
    private StageSelect stage;

    public Homescreen(final ScreenStack ss){
        this.ss = ss;
      //  this.testScreen = new TestScreen(ss);
        this.stage = new StageSelect(ss);
        Image  bgImage =  assets().getImage("images/bg.png");
        this.bg = graphics().createImageLayer(bgImage);

        Image  startImage =  assets().getImage("images/start2.png" );
        this.start = graphics().createImageLayer(startImage);

        Image  exitImage =  assets().getImage("images/exit1.png" );
        this.exit = graphics().createImageLayer(exitImage);

        exit.setTranslation((640/2)-(256/2),(480/2)-(128/2)+60);
        start.setTranslation((640/2)-(256/2),(480/2)-128-128/4+60);
        start.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event){
                ss.push(stage);
            }

        });

        exit.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event){
                System.exit(0);
            }

        });
    }

    @Override
    public void wasShown() {
        super.wasShown();
        this.layer.add(bg);
        this.layer.add(start);
        this.layer.add(exit);


    }
}
