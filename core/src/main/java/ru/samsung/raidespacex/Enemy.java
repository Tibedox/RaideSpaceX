package ru.samsung.raidespacex;

import static ru.samsung.raidespacex.Main.*;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class Enemy extends SpaceObject{
    public int hp;
    public int phase, nPhases = 12;
    private long timeLastPhase, timePhaseInterval = 40;

    public Enemy() {
        width = height = 200;
        type = MathUtils.random(0, 3);
        hp = getHealthTypes(type);
        x = MathUtils.random(width/2, SCR_WIDTH-width/2);
        y = MathUtils.random(SCR_HEIGHT+height, SCR_HEIGHT*2);
        vy = MathUtils.random(-6f, -3f);
    }

    @Override
    public void move() {
        super.move();
        changePhase();
    }

    public void changePhase() {
        if(TimeUtils.millis() > timeLastPhase+timePhaseInterval) {
            if (++phase == nPhases) phase = 0;
            timeLastPhase = TimeUtils.millis();
        }
    }

    private int getHealthTypes(int type){
        switch (type){
            case 0: return 2;
            case 1: return 4;
            case 2: return 3;
            case 3: return 1;
        }
        return 0;
    }
}
