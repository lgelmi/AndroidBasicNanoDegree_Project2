/*  Image sources:
    *   For the most part from common creative, sadly I forgot to write down the others (e.g. backgrounds)
    Sound sources:
    *   https://www.zapsplat.com/music/sword-swing-swoosh-fast-4/
    *   https://freesound.org/people/CTCollab/sounds/223630/
    *   https://freesound.org/people/Replix/sounds/173126/
 */

package com.example.android.fightkeeper;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

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
            healButton = findViewById(R.id.LeftHeal);
        }
    }

    private class rightViews extends sideViews {

        rightViews() {
            super();
            healthBar = findViewById(R.id.RightHealtBar);
            healthText = findViewById(R.id.RightHealthText);
            image = findViewById(R.id.RightPG);
            name = findViewById(R.id.RightPgName);
            damageText = findViewById(R.id.RightAttackValue);
            defenceText = findViewById(R.id.RightDefenceValue);
            focusText = findViewById(R.id.RightFocusValue);
            healText = findViewById(R.id.RightHealValue);
            healButton = findViewById(R.id.RightHeal);
        }
    }

    sideViews leftSide;
    sideViews rightSide;
    Fighter leftFighter;
    Fighter rightFighter;
    FrameLayout leftActions;
    FrameLayout rightActions;
    Context context;
    MediaPlayer attackSound;
    MediaPlayer defenceSound;
    MediaPlayer deathSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        leftSide = new leftViews();
        rightSide = new rightViews();
        leftActions = findViewById(R.id.LeftActions);
        rightActions = findViewById(R.id.RightActions);
        context = getApplicationContext();
        attackSound = MediaPlayer.create(context, R.raw.attack);
        attackSound.setAudioStreamType(AudioManager.STREAM_MUSIC);
        defenceSound = MediaPlayer.create(context, R.raw.defence);
        defenceSound.setAudioStreamType(AudioManager.STREAM_MUSIC);
        deathSound = MediaPlayer.create(context, R.raw.death);
        deathSound.setAudioStreamType(AudioManager.STREAM_MUSIC);


        newGame();
    }

    public void newGameView(View v) {
        /* View access to the newGame method.
        * */
        newGame();
    }

    public void attackAction(View v) {
        attackSound.start();

        String tag = v.getTag().toString();
        int damage;
        switch (tag) {
            case "left":
            case "Left":
            case "LEFT":
                damage = leftFighter.attack();
                if (rightFighter.getCurrentDefence() > 0) {
                    // Immediately apply damage
                    rightFighter.sufferDamage(damage);
                }
                newTurn(true);
                break;
            case "right":
            case "Right":
            case "RIGHT":
                damage = rightFighter.attack();
                if (leftFighter.getCurrentDefence() > 0) {
                    // Immediately apply damage
                    leftFighter.sufferDamage(damage);
                }
                newTurn(false);
                break;
        }
    }

    public void defenceAction(View v) {
        defenceSound.start();
        String tag = v.getTag().toString();
        switch (tag) {
            case "left":
            case "Left":
            case "LEFT":
                leftFighter.defence();
                newTurn(true);
                break;
            case "right":
            case "Right":
            case "RIGHT":
                rightFighter.defence();
                newTurn(false);
                break;
        }
    }

    public void focusAction(View v) {
        String tag = v.getTag().toString();
        switch (tag) {
            case "left":
            case "Left":
            case "LEFT":
                leftFighter.focus();
                newTurn(true);
                break;
            case "right":
            case "Right":
            case "RIGHT":
                rightFighter.focus();
                newTurn(false);
                break;
        }
    }

    public void healAction(View v) {
        String tag = v.getTag().toString();
        switch (tag) {
            case "left":
            case "Left":
            case "LEFT":
                leftFighter.heal();
                newTurn(true);
                break;
            case "right":
            case "Right":
            case "RIGHT":
                rightFighter.heal();
                newTurn(false);
                break;
        }

    }

    // Adapted from https://stackoverflow.com/a/28509431/8995069
    public static void setViewAndChildrenEnabled(View view, boolean enabled, int depth) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup && depth > 0) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled, depth - 1);
            }
        }
    }

    public Fighter randomFighter(sideViews views) {
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

    public int randomBackground() {
        /* Return a random background source id */
        int[] ids = {R.drawable.background_desert, R.drawable.background_reign};
        int rnd = new Random().nextInt(ids.length);
        return ids[rnd];
    }

    public void newGame() {
        /* Starts a new game.
        *
        * Creates two new fighter and instantiate their classes as well as resetting any other
        * interface property.
        * */
        // Sets the background
        ImageView background = findViewById(R.id.BackgroundImage);
        background.setImageResource(randomBackground());
        // Sets the characters
        this.leftFighter = randomFighter(leftSide);
        this.rightFighter = randomFighter(rightSide);
        // Set up turns
        boolean leftEnabled = new Random().nextBoolean();
        setViewAndChildrenEnabled(leftActions, leftEnabled, 2);
        setViewAndChildrenEnabled(rightActions, !leftEnabled, 2);
    }

    public void newTurn(boolean isLeftTurn) {
        /* Perform some standard operation to setup the turn mechanics */
        if (isLeftTurn) {
            leftFighter.sufferDamage(rightFighter.getCurrentDamage());
            rightFighter.newTurn();
            // Enables the right side
            setViewAndChildrenEnabled(leftActions, false, 2);
            setViewAndChildrenEnabled(rightActions, true, 2);
        } else {
            rightFighter.sufferDamage(leftFighter.getCurrentDamage());
            leftFighter.newTurn();
            // Enables the left side
            setViewAndChildrenEnabled(leftActions, true, 2);
            setViewAndChildrenEnabled(rightActions, false, 2);
        }
        if (leftFighter.isDead() || rightFighter.isDead()) {
            // Stops game
            deathSound.start();
            setViewAndChildrenEnabled(leftActions, false, 2);
            setViewAndChildrenEnabled(rightActions, false, 2);
        }
    }

}
