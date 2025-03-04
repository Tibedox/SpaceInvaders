package ru.samsung.spaceinvaders;

public class Player {
    String name = "Noname";
    int kills;
    int score;

    public void clone(Player p) {
        name = p.name;
        kills = p.kills;
        score = p.score;
    }
}
