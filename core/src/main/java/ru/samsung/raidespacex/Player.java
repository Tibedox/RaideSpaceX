package ru.samsung.raidespacex;

import java.util.Arrays;

public class Player {
    String name = "Noname";
    int score;
    int[] killedType = new int[4];
    int kills;

    public void clear(){
        score = 0;
        Arrays.fill(killedType, 0);
        kills = 0;
    }

    public Player() {
    }

    public Player(Player p) {
        name = p.name;
        score = p.score;
        for (int i = 0; i < killedType.length; i++) {
            killedType[i] = p.killedType[i];
        }
        kills = p.kills;
    }
}
