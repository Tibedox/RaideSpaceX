package ru.samsung.raidespacex;

import static ru.samsung.raidespacex.Main.*;

import com.badlogic.gdx.math.MathUtils;

public class Fragment extends SpaceObject{
    int numImg;
    public Fragment(float x, float y, int type, int numImgFragmets) {
        super(x, y);
        this.type = type;
        numImg = MathUtils.random(0, numImgFragmets-1);
        width = MathUtils.random(30f, 50f);
        height = MathUtils.random(30f, 50f);
        vx = MathUtils.random(-10f, 10f);
        vy = MathUtils.random(-10f, 10f);
    }

    public boolean outOfScreen(){
        return y>SCR_HEIGHT+height/2 || y<-height/2 || x>SCR_WIDTH+width/2 || x<-width/2;
    }
}
