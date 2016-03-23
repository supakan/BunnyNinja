package sut.game01.core;


import react.UnitSlot;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import playn.core.Font;

import static playn.core.PlayN.graphics;


public class Homescreen extends UIScreen {
    private Root root ;
    private ScreenStack ss;
    public static final Font TITLE_FONT =
            graphics().createFont("Helvetica", Font.Style.PLAIN, 24);
    private TestScreen testScreen;
    public Homescreen(ScreenStack ss){
        this.ss=ss;
        this.testScreen = new TestScreen(ss);
    }
 @Override
    public void wasShown(){
     super.wasShown();
     root = iface.createRoot(
         AxisLayout.vertical().gap(15),
                 SimpleStyles.newSheet(), this.layer);

        root.addStyles(Style.BACKGROUND
                .is(Background.bordered(0xFFCCCCCC, 0xFF99CCFF, 5)
                        .inset(5, 10)));
      root.setSize(width(),height());

      root.add(new Label("Test na ja")
              .addStyles(Style.FONT.is(Homescreen.TITLE_FONT)));
     root.add(new Button("Start").onClick((new UnitSlot() {
         @Override
         public void onEmit() {
             ss.push(testScreen);
         }
     })));
     }

 }

