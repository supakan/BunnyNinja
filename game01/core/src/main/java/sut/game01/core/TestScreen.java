package sut.game01.core;

import static playn.core.PlayN.*;


import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.util.Callback;
import sut.game01.core.character.Bunny;
import sut.game01.core.character.Zealot;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import playn.core.*;

public class TestScreen extends Screen {
    private final ImageLayer bg;
    private final ImageLayer back;
    private final ScreenStack ss ;
    private final Image  bgImage;
  //  private Zealot z;
    private Bunny z;

    public TestScreen(final ScreenStack ss){
        this.ss = ss;

         bgImage =  assets().getImage("images/bg.png");
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

       // z = new Zealot(560f,400f);
        z = new Bunny(560f,400f);
        this.layer.add(z.layer());

    }

    @Override
    public void update(int delta) {
        super.update(delta);
        z.update(delta);
    }
/*  @Override
    public void wasAdded() {
        super.wasAdded();
        bgImage.addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image result) {

            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });
        layer.add(bg);
    }*/
}
