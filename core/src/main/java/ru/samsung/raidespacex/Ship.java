package ru.samsung.raidespacex;

import static ru.samsung.raidespacex.Main.SCR_HEIGHT;
import static ru.samsung.raidespacex.Main.SCR_WIDTH;

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
        outOfScreen();
    }

    public void changePhase() {
        if(TimeUtils.millis() > timeLastPhase+timePhaseInterval) {
            if (++phase == 12) phase = 0;
            timeLastPhase = TimeUtils.millis();
        }
    }

    private void outOfScreen(){
        if(x<width/2) {
            vx = 0;
            x = width/2;
        }
        if(x>SCR_WIDTH-width/2) {
            vx = 0;
            x = SCR_WIDTH-width/2;
        }
        if(y<height/2) {
            vy = 0;
            y = height/2;
        }
        if(y>SCR_HEIGHT-height/2) {
            vy = 0;
            y = SCR_HEIGHT-height/2;
        }
    }

    public void touch(float tx, float ty){
        vx = (tx-x) / 20;
        vy = (ty-y) / 20;
    }

    public void touch(Vector3 t){
        vx = (t.x-x) / 20;
        vy = (t.y-y) / 20;
    }

    public void stop(){
        vx = 0;
        vy = 0;
    }
}
