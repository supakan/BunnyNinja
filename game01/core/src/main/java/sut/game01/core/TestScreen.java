package sut.game01.core;

import static playn.core.PlayN.*;


import playn.core.Image;
import playn.core.ImageLayer;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import playn.core.*;

public class TestScreen extends Screen {
    private final ImageLayer bg;
    private final ImageLayer back;
    private final ScreenStack ss ;

    public TestScreen(final ScreenStack ss){
        this.ss = ss;

        Image  bgImage =  assets().getImage("images/bg.png");
         this.bg = graphics().createImageLayer(bgImage);

        Image  backImage =  assets().getImage("images/back.png");
        this.back = graphics().createImageLayer(backImage);

        back.setTranslation(292,212);
        back.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event){
                ss.remove(ss.top());
            }

        });
    }

    @Override
    public void wasShown() {
        super.wasShown();
        this.layer.add(bg);
        this.layer.add(back);


    }
}
