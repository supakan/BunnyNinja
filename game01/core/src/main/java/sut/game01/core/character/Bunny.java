package sut.game01.core.character;

import playn.core.*;
import playn.core.util.Callback;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;

import java.awt.event.MouseAdapter;

/**
 * Created by Administrator on 30/3/2559.
 */
public class Bunny {
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false ;
    private int action = 0;

    public enum State {
        RIDLE,LIDLE, LRUN, RRUN,RATK,LATK,LJUM,RJUM,LSLD,RSLD
    };


    private State state = State.RIDLE; // Start State

    private int e = 0 ; //Time Control
    private int offset = 0 ; //Set Of Picture

    public Bunny(final float x,final float y){
        sprite = SpriteLoader.getSprite("images/bunny.json");
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
                if (event.key() == Key.RIGHT && state != State.LJUM && state != State.RJUM) {
                    action = 4;
                    state = State.RRUN;
                    spriteIndex = -1;
                    e=0;
                }
                else  if (event.key() == Key.LEFT && state != State.LJUM && state != State.RJUM){
                    action = 3;
                    state = State.LRUN;
                    spriteIndex = -1;
                    e=0;
                }
               else if(event.key() == Key.Z && state != State.LJUM && state != State.RJUM){
                    if(state == State.LIDLE || state == State.LRUN || state == State.LATK){
                    action = 5;
                    state = State.LATK;
                    }
                    else if(state == State.RIDLE || state == State.RRUN || state == State.RATK){
                        action = 6;
                        state = State.RATK;
                    }
                    spriteIndex = -1;
                    e=0;
                }
                else if(event.key() == Key.SPACE && state != State.LJUM && state != State.RJUM){
                    if(state == State.LIDLE || state == State.LRUN || state == State.LATK){
                        action = 7;
                        state = State.LJUM;
                    }
                    else if(state == State.RIDLE || state == State.RRUN || state == State.RATK){
                        action = 8;
                        state = State.RJUM;
                    }
                    spriteIndex = -1;
                    e=0;
                }
            }

            @Override
            public void onKeyUp(Keyboard.Event event) {

                if (event.key() == Key.LEFT){
                    action = 0;
                    state = State.LIDLE;
                    spriteIndex = -1;
                    e=0;
                }

               else if (event.key() == Key.RIGHT){
                    action = 1;
                    state = State.RIDLE;
                    spriteIndex = -1;
                    e=0;
                }
                else if(event.key() == Key.Z){
                    if(state == State.LATK){
                    action = 0;
                    state = State.LIDLE;
                    }
                    else if(state == State.RATK){
                        action = 1;
                        state = State.RIDLE;
                    }
                    spriteIndex = -1;
                    e = 0;
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

        if(action == 3){
            state = State.LRUN;
        }
        else if(action == 4){
            state = State.RRUN;
        }
        else if(action == 0){
            state = State.RIDLE;
        }
        else if(action == 1){
            state = State.LIDLE;
        }
        else if(action == 5){
            state = State.LATK;
        }
        else if(action == 6){
            state = State.RATK;
        }


        if(e>150){

            switch (state){
                case LIDLE: offset = 0;
                    break;
                case RIDLE: offset = 4;
                    break;
                case LRUN: offset = 8 ;
                    if(spriteIndex == 11){
                        state =State.LIDLE;
                    }
                    break;
                case RRUN: offset = 12 ;
                    if(spriteIndex == 14){
                        state =State.RIDLE;
                        //action = 0;
                    }
                    break;
                case LATK: offset = 16 ;
                    if(spriteIndex == 18){
                        state = State.LIDLE;
                    }
                    break;
                case RATK: offset = 20 ;
                    if(spriteIndex == 22){
                        state = State.RIDLE;
                    }
                    break;
                case LJUM: offset = 24 ;
                    if(spriteIndex == 27){
                        state = State.LIDLE;
                    }
                    break;
                case RJUM: offset = 28 ;
                    if(spriteIndex == 31){
                        state = State.RIDLE;
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
