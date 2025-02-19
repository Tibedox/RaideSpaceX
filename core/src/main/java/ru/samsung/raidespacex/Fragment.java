package ru.samsung.raidespacex;

import static ru.samsung.raidespacex.Main.SCR_HEIGHT;
import static ru.samsung.raidespacex.Main.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;

public class Fragment extends SpaceObject{
    public Fragment(float x, float y) {
        super(x, y);
        width = MathUtils.random(30, 50);
        height = MathUtils.random(30, 50);
        vx = MathUtils.random(-10, 10);
        vy = MathUtils.random(-10, 10);
    }

    public boolean outOfScreen(){
        return y>SCR_HEIGHT+height/2 || y<-height/2 || x>SCR_WIDTH+width/2 || x<-width/2;
    }
}
