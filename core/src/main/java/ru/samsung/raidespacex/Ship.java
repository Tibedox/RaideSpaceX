package ru.samsung.raidespacex;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

public class Ship extends SpaceObject{
    public int phase, nPhases = 12;
    private long timeLastPhase, timePhaseInterval = 40;

    public Ship(float x, float y) {
        super(x, y);
        width = 200;
        height = 200;
    }

    @Override
    public void move() {
        super.move();
        changePhase();
    }

    public void changePhase() {
        if(TimeUtils.millis() > timeLastPhase+timePhaseInterval) {
            if (++phase == 12) phase = 0;
            timeLastPhase = TimeUtils.millis();
        }
    }

    public void touch(float tx, float ty){
        vx = (tx-x) / 50;
        vy = (ty-y) / 50;
    }

    public void touch(Vector3 t){
        vx = (t.x-x) / 50;
        vy = (t.y-y) / 50;
    }
}
