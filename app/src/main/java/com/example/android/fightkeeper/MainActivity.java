package com.example.android.fightkeeper;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private class leftViews extends sideViews {

        leftViews() {
            super();
            healthBar = findViewById(R.id.LeftHealtBar);
            healthText = findViewById(R.id.LeftHealthText);
            image = findViewById(R.id.LeftPG);
            name = findViewById(R.id.LeftPgName);
            damageText = findViewById(R.id.LeftAttackValue);
            defenceText = findViewById(R.id.LeftDefenceValue);
            focusText = findViewById(R.id.LeftFocusValue);
            healText = findViewById(R.id.LeftHealValue);
        }
    }

    private class rightViews extends sideViews {

        rightViews() {
            super();
            this.healthBar = findViewById(R.id.RightHealtBar);
            this.healthText = findViewById(R.id.RightHealthText);
            this.image = findViewById(R.id.RightPG);
            this.name = findViewById(R.id.RightPgName);
            this.damageText = findViewById(R.id.RightAttackValue);
            this.defenceText = findViewById(R.id.RightDefenceValue);
            this.focusText = findViewById(R.id.RightFocusValue);
            this.healText = findViewById(R.id.RightHealValue);
        }
    }

    sideViews leftSide;
    sideViews rightSide;
    Fighter leftFighter;
    Fighter rightFighter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.leftSide = new leftViews();
        this.rightSide = new rightViews();
        newGame();
    }

    public void newGameView(View v) {
        /* View access to the newGame method.
        * */
        newGame();
    }


    public Fighter randomFighter(Context context, sideViews views) {
        // I wanted to do this with the class itself and then build the instance outside.
        // Apparently this is not possible due to the class structure... A pity.
        // TODO Find a workaround to improve this!

//        List<Fighter> availableFighters =
//                new ArrayList<>(Arrays.asList(Goblin.class, RedGoblin.class,
//                        Knight.class, AtrociousKiller.class, DarkCreature.class,
//                        Jotun.class, Kraken.class));
        List<Integer> weights = new ArrayList<>(Arrays.asList(20, 15, 20, 1, 10, 5, 5));
        int total = 0;
        for (Integer weight : weights)
            total += weight;
        Random random = new Random();
        int value = random.nextInt(total);
        int PGIndex = 0;
        while (value > 0) {
            if (value > weights.get(PGIndex)) {
                value -= weights.get(PGIndex);
                PGIndex++;
            } else
                break;
        }
        switch (PGIndex) {
            case 1:
                return new RedGoblin(context, views);
            case 2:
                return new Knight(context, views);
            case 3:
                return new AtrociousKiller(context, views);
            case 4:
                return new DarkCreature(context, views);
            case 5:
                return new Jotun(context, views);
            case 6:
                return new Kraken(context, views);
            default:
                return new Goblin(context, views);

        }
    }

    public void newGame() {
        /* Starts a new game.
        *
        * Creates two new fighter and instantiate their classes as well as resetting any other
        * interface property.
        * */
        Context context = getApplicationContext();
        this.leftFighter = randomFighter(context, leftSide);
        this.rightFighter = randomFighter(context, rightSide);
    }


}
