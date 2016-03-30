package sut.game01.core;

import static playn.core.PlayN.*;


import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.util.Callback;
import sut.game01.core.character.Zealot;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import playn.core.*;

public class StageSelect extends Screen {
    private final ImageLayer bg;
    private final ImageLayer stage;
    private final ScreenStack ss;
    private final Image bgImage;
    private final ImageLayer s1Im;
    private final ImageLayer s2Im;
    private final ImageLayer s3Im;
    private final Image s1;
    private final Image s2;
    private final Image s3;
    private TestScreen testScreen;


    public StageSelect(final ScreenStack ss) {
        this.ss = ss;
        this.testScreen = new TestScreen(ss);
        bgImage = assets().getImage("images/bg.png");
        this.bg = graphics().createImageLayer(bgImage);

        s1 = assets().getImage("images/s1.png");
        this.s1Im = graphics().createImageLayer(s1);

        s2 = assets().getImage("images/s2.png");
        this.s2Im = graphics().createImageLayer(s2);

        s3 = assets().getImage("images/s3.png");
        this.s3Im = graphics().createImageLayer(s3);

        Image stageImage = assets().getImage("images/Stage.png");
        this.stage = graphics().createImageLayer(stageImage);
        stage.setTranslation(35, 0);
        s1Im.setTranslation(105,100);
        s2Im.setTranslation(105+85+10,100);
        s3Im.setTranslation(105+85+10+95,100);

        s1Im.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event){
                ss.push(testScreen);
            }

        });

        s2Im.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event){
                ss.push(testScreen);
            }

        });

        s3Im.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event){
                ss.push(testScreen);
            }

        });
    }

    @Override
    public void wasShown() {
        super.wasShown();
        this.layer.add(bg);
        this.layer.add(stage);
        this.layer.add(s1Im);
        this.layer.add(s2Im);
        this.layer.add(s3Im);


    }
}
