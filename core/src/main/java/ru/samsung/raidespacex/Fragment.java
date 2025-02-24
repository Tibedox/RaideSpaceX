package ru.samsung.raidespacex;

import static ru.samsung.raidespacex.Main.*;

import com.badlogic.gdx.math.MathUtils;

public class Fragment extends SpaceObject{
    public int numImg;
    public float rotation;
    private float vRotation;

    public Fragment(float x, float y, int type, int numImgFragmets) {
        super(x, y);
        this.type = type;
        numImg = MathUtils.random(0, numImgFragmets-1);
        width = MathUtils.random(30f, 50f);
        height = MathUtils.random(30f, 50f);
        float a = MathUtils.random(0f, 360f);
        float v = MathUtils.random(2f, 10f);
        vx = v*MathUtils.sin(a);
        vy = v*MathUtils.cos(a);
        vRotation = MathUtils.random(-5f, 5f);
    }

    @Override
    public void move() {
        super.move();
        rotation += vRotation;
    }

    public boolean outOfScreen(){
        return y>SCR_HEIGHT+height/2 || y<-height/2 || x>SCR_WIDTH+width/2 || x<-width/2;
    }
}
