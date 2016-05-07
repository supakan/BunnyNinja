package sut.game01.core.character;

import playn.core.*;
import playn.core.util.Callback;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;

import java.awt.event.MouseAdapter;

/**
 * Created by Administrator on 30/3/2559.
 */
public class Enemy {
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false ;
    private int action = 0;

    public enum State {
        LWALK,RWALK
    };


    private State state = State.RWALK; // Start State
    private int e = 0 ; //Time Control
    private int offset = 0 ; //Set Of Picture

    public Enemy(final float x,final float y){
        sprite = SpriteLoader.getSprite("images/e.json");
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
    }

    public Layer layer(){
        return sprite.layer();
    }

    public void update(int delta){
        if(!hasLoaded) return;
        e = e +delta;


        if(e>150){

            switch (state){
                case LWALK: offset = 0;
                    if(spriteIndex == 3){
                        state =State.RWALK;
                    }
                    break;
                case RWALK: offset = 4;
                    if(spriteIndex == 7){
                        state =State.LWALK;
                    }
                    break;

            }
            spriteIndex = offset + ((spriteIndex + 1)%4);
            sprite.setSprite(spriteIndex);
            e=0;
        }

    }
}
