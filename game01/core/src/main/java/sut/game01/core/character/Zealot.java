package sut.game01.core.character;

import playn.core.*;
import playn.core.util.Callback;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;

import java.awt.event.MouseAdapter;

/**
 * Created by Administrator on 30/3/2559.
 */
public class Zealot {
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false ;
    private int action = 0;

    public enum State {
        IDLE, RUN, ATTK
    };


    private State state = State.IDLE; // Start State

    private int e = 0 ; //Time Control
    private int offset = 0 ; //Set Of Picture

    public Zealot(final float x,final float y){
        sprite = SpriteLoader.getSprite("images/zealot.json");
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width()/2f,sprite.height()/2f);
                sprite.layer().setTranslation(x, y+ 13f);
                hasLoaded = true;
            }
            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image",cause);
            }
        });

        PlayN.keyboard().setListener(new Keyboard.Adapter() {
            @Override
            public void onKeyDown(Keyboard.Event event) {
                if (event.key() == Key.Z) {
                    action = 2;
                    state = State.ATTK;
                    spriteIndex = -1;
                    e=0;
                }
                else  if (event.key() == Key.LEFT){
                    action = 1;
                    state = State.RUN;
                    spriteIndex = -1;
                    e=0;
                }
            }

            @Override
            public void onKeyUp(Keyboard.Event event) {

                          if (event.key() == Key.LEFT){
                            action = 0;
                            state = State.IDLE;
                            spriteIndex = -1;
                            e=0;
                        }
                    }

                });
            }





    public Layer layer(){
        return sprite.layer();
    }

    public void update(int delta){
        if(!hasLoaded) return;
        e = e +delta;

        if(action == 1){
            state = State.RUN;


        }
        else if(action == 2){
            state = State.ATTK;


        }
        else if(action == 0){
            state = State.IDLE;
        }
    /*    if(e > 150){
            spriteIndex = offset + ((spriteIndex + 1)%4);
            sprite.setSprite(spriteIndex);
            e=0;
        }*/

        if(e>150){

            switch (state){
                case IDLE: offset = 0;
                    break;
                case RUN: offset = 4 ;
                  if(spriteIndex == 7){
                        state =State.IDLE;
                    }
                    break;
                case ATTK: offset = 8 ;
                    if(spriteIndex == 10){
                        state =State.IDLE;
                        action = 0;
                    }
                    break;
            }
            spriteIndex = offset + ((spriteIndex + 1)%4);
            sprite.setSprite(spriteIndex);
            e=0;
            System.out.println(action);
        }

    }
}
