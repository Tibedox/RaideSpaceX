package ru.samsung.raidespacex;

import static ru.samsung.raidespacex.Main.*;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class Enemy extends SpaceObject{
    public int hp;
    public int price;
    public int phase, nPhases = 12;
    private long timeLastPhase, timePhaseInterval = 40;

    public Enemy() {
        type = MathUtils.random(0, 3);
        enemySettings(type);
        x = MathUtils.random(width/2, SCR_WIDTH-width/2);
        y = MathUtils.random(SCR_HEIGHT+height, SCR_HEIGHT*2);
    }

    @Override
    public void move() {
        super.move();
        changePhase();
    }

    public boolean outOfScreen(){
        return y<-height/2;
    }

    public void changePhase() {
        if(TimeUtils.millis() > timeLastPhase+timePhaseInterval) {
            if (++phase == nPhases) phase = 0;
            timeLastPhase = TimeUtils.millis();
        }
    }

    private void enemySettings(int type){
        switch (type){
            case 0:
                hp = 2;
                price = 2;
                width = height = 200;
                vy = MathUtils.random(-7f, -5f);
                break;
            case 1:
                hp = 6;
                price = 5;
                width = height = 280;
                vy = MathUtils.random(-4f, -3f);
                break;
            case 2:
                hp = 3;
                price = 3;
                width = height = 220;
                vy = MathUtils.random(-6f, -4f);
                break;
            case 3:
                hp = 1;
                price = 1;
                width = height = 150;
                vy = MathUtils.random(-9f, -7f);
                break;
        }
    }
}
