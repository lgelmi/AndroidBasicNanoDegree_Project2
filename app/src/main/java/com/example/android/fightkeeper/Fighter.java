package com.example.android.fightkeeper;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

/**
 * Fighter definitions.
 * <p>
 * The abstract class describing the fighter behaviour is defined as well as other fighter specific treat.
 */


class sideViews {
    ProgressBar healthBar;
    TextView healthText;
    ImageView image;
    TextView name;
    TextView damageText;
    TextView defenceText;
    TextView focusText;
    TextView healText;
    LinearLayout healButton;
}

abstract class Fighter {

    // General
    private sideViews views;

    private Context context;

    abstract String name();

    // Graphical
    abstract int height();

    abstract int imageId();

    // Stats
    abstract int maxHealthPoints();

    private int currentHealth;

    abstract int minDamage();

    private int getMinDamage() {
        return (int) (minDamage() * Math.pow(focusBonus(), focusAmount));
    }

    abstract int maxDamage();

    private int getMaxDamage() {
        return (int) (maxDamage() * Math.pow(focusBonus(), focusAmount));
    }

    private int currentDamage;

    public int getCurrentDamage() {
        return currentDamage;
    }

    private void setCurrentDamage(int damage) {
        /* When setting the damage, also display it */
        currentDamage = damage;
        views.damageText.setText(Html.fromHtml(context.getResources().getString(R.string.ActualDamage, damage)));
    }

    abstract int minDefence();

    private int getMinDefence() {
        return (int) (minDefence() * Math.pow(focusBonus(), focusAmount));
    }

    abstract int maxDefence();

    private int getMaxDefence() {
        return (int) (maxDefence() * Math.pow(focusBonus(), focusAmount));
    }

    private int currentDefence;

    int getCurrentDefence() {
        return currentDefence;
    }

    private void setCurrentDefence(int defence) {
        /* When setting the damage, also display it */
        currentDefence = defence;
        views.defenceText.setText(Html.fromHtml(context.getResources().getString(R.string.ActualDefence, defence)));
    }

    abstract double focusBonus();

    private int focusAmount = 0;

    abstract int minHeal();

    private int getMinHeal() {
        return (int) (minHeal() * Math.pow(focusBonus(), focusAmount));
    }

    abstract int maxHeal();

    private int getMaxHeal() {
        return (int) (maxHeal() * Math.pow(focusBonus(), focusAmount));
    }

    abstract int healChargeTurns();

    private int healTurnsLeft;

    Fighter(Context context, sideViews views) {
        // Initialize the Fighter dynamic stats
        this.context = context;
        this.views = views;
        this.views.image.setImageResource(imageId());
        this.views.image.getLayoutParams().height = (int) context.getResources().getDisplayMetrics().density * height();
        this.views.name.setText(name());
        currentHealth = maxHealthPoints();
        healTurnsLeft = 0;
        focusAmount = 0;
        setCurrentHealth(maxHealthPoints());
        newTurn();
    }

    public void newTurn() {
        currentDamage = 0;
        currentDefence = 0;
        if (healTurnsLeft > 0)
            healTurnsLeft--;
        updateVisibleStats();
    }

    private void setCurrentHealth(int newHealth) {
        if (newHealth > maxHealthPoints())
            newHealth = maxHealthPoints();
        else if (newHealth < 0)
            newHealth = 0;
        currentHealth = newHealth;
        views.healthText.setText(context.getResources().getString(R.string.Health, currentHealth, maxHealthPoints()));
        views.healthBar.setProgress(currentHealth * 100 / maxHealthPoints());
    }

    // Adapted from https://stackoverflow.com/a/28509431/8995069 I'm lazy so I want create a "common" method file and just duplicate it
    private static void setViewAndChildrenEnabled(View view, boolean enabled, int depth) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup && depth > 0) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled, depth - 1);
            }
        }
    }

    private void updateVisibleStats() {
        views.damageText.setText(context.getResources().getString(R.string.Damage, getMinDamage(), getMaxDamage()));
        views.defenceText.setText(context.getResources().getString(R.string.DefenceAmount, getMinDefence(), getMaxDefence()));
        views.focusText.setText(context.getResources().getString(R.string.FocusMultiplier, focusBonus()));
        if (healTurnsLeft == 0) {
            setViewAndChildrenEnabled(views.healButton, true, 2);
            views.healText.setText(context.getResources().getString(R.string.HealAmount, getMinHeal(), getMaxHeal()));
        } else {
            setViewAndChildrenEnabled(views.healButton, false, 2);
            views.healText.setText(Html.fromHtml(context.getResources().getString(R.string.HealTurns, healTurnsLeft)));
        }
    }

    void sufferDamage(int damage) {
        /* Handles the damage */
        if (damage - currentDefence < 0)
            damage = 0;
        else
            damage -= currentDefence;
        setCurrentHealth(currentHealth - damage);
    }

    int attack() {
        /* Generates a random attack value and stores it in the current attack. Consume focus.
           Also returns it.
         */
        Random rand = new Random();
        int damage = rand.nextInt(getMaxDamage() - getMinDamage() + 1) + getMinDamage();
        focusAmount = 0;
        updateVisibleStats();
        setCurrentDamage(damage);
        return damage;
    }

    int defence() {
        /* Generates a random defence value and stores it in currentDefence. Consume focus.
           Also returns it.
         */
        Random rand = new Random();
        int defence = rand.nextInt(getMaxDefence() - getMinDefence() + 1) + getMinDefence();
        focusAmount = 0;
        updateVisibleStats();
        setCurrentDefence(defence);
        return defence;
    }

    int focus() {
        /* Adds one to the focus charges.
        *  Returns the current value */
        focusAmount++;
        updateVisibleStats();
        return focusAmount;
    }

    int heal() {
        /* Generates a random heal value and update the health points. Consume focus.
           Starts the countdown.
           Also returns it.
         */
        Random rand = new Random();
        int heal = rand.nextInt(getMaxHeal() - getMinHeal() + 1) + getMinHeal();
        focusAmount = 0;
        healTurnsLeft = healChargeTurns();
        updateVisibleStats();
        setCurrentHealth(currentHealth + heal);
        return heal;
    }

    boolean isDead() {
        return currentHealth == 0;
    }

}

class Goblin extends Fighter {


    Goblin(Context context, sideViews views) {
        super(context, views);
    }

    @Override
    String name() {
        return "Goblin";
    }

    @Override
    int height() {
        return 80;
    }

    @Override
    int imageId() {
        return R.drawable.pg_goblin;
    }

    @Override
    int maxHealthPoints() {
        return 40;
    }

    @Override
    int minDamage() {
        return 3;
    }

    @Override
    int maxDamage() {
        return 10;
    }

    @Override
    int minDefence() {
        return 1;
    }

    @Override
    int maxDefence() {
        return 5;
    }

    @Override
    double focusBonus() {
        return 1.5;
    }

    @Override
    int minHeal() {
        return 4;
    }

    @Override
    int maxHeal() {
        return 8;
    }

    @Override
    int healChargeTurns() {
        return 2;
    }
}

class RedGoblin extends Fighter {


    RedGoblin(Context context, sideViews views) {
        super(context, views);
    }

    @Override
    String name() {
        return "Goblin Chief";
    }

    @Override
    int height() {
        return 70;
    }

    @Override
    int imageId() {
        return R.drawable.pg_red_goblin;
    }

    @Override
    int maxHealthPoints() {
        return 35;
    }

    @Override
    int minDamage() {
        return 2;
    }

    @Override
    int maxDamage() {
        return 8;
    }

    @Override
    int minDefence() {
        return 3;
    }

    @Override
    int maxDefence() {
        return 7;
    }

    @Override
    double focusBonus() {
        return 2;
    }

    @Override
    int minHeal() {
        return 4;
    }

    @Override
    int maxHeal() {
        return 8;
    }

    @Override
    int healChargeTurns() {
        return 3;
    }
}

class Knight extends Fighter {


    Knight(Context context, sideViews views) {
        super(context, views);
    }

    @Override
    String name() {
        return "Knight";
    }

    @Override
    int height() {
        return 120;
    }

    @Override
    int imageId() {
        return R.drawable.pg_knight;
    }

    @Override
    int maxHealthPoints() {
        return 45;
    }

    @Override
    int minDamage() {
        return 2;
    }

    @Override
    int maxDamage() {
        return 5;
    }

    @Override
    int minDefence() {
        return 4;
    }

    @Override
    int maxDefence() {
        return 10;
    }

    @Override
    double focusBonus() {
        return 2;
    }

    @Override
    int minHeal() {
        return 3;
    }

    @Override
    int maxHeal() {
        return 16;
    }

    @Override
    int healChargeTurns() {
        return 4;
    }
}

class AtrociousKiller extends Fighter {

    AtrociousKiller(Context context, sideViews views) {
        super(context, views);
    }

    @Override
    String name() {
        return "Atrocious Killer";
    }

    @Override
    int height() {
        return 150;
    }

    @Override
    int imageId() {
        return R.drawable.pg_atrocious_killer;
    }

    @Override
    int maxHealthPoints() {
        return 100;
    }

    @Override
    int minDamage() {
        return 1;
    }

    @Override
    int maxDamage() {
        return 1;
    }

    @Override
    int minDefence() {
        return 5;
    }

    @Override
    int maxDefence() {
        return 15;
    }

    @Override
    double focusBonus() {
        return 5;
    }

    @Override
    int minHeal() {
        return 3;
    }

    @Override
    int maxHeal() {
        return 10;
    }

    @Override
    int healChargeTurns() {
        return 5;
    }
}

class DarkCreature extends Fighter {

    DarkCreature(Context context, sideViews views) {
        super(context, views);
    }

    @Override
    String name() {
        return "Dark Creature";
    }

    @Override
    int height() {
        return 100;
    }

    @Override
    int imageId() {
        return R.drawable.pg_dark_creature;
    }

    @Override
    int maxHealthPoints() {
        return 25;
    }

    @Override
    int minDamage() {
        return 6;
    }

    @Override
    int maxDamage() {
        return 12;
    }

    @Override
    int minDefence() {
        return 4;
    }

    @Override
    int maxDefence() {
        return 8;
    }

    @Override
    double focusBonus() {
        return 2;
    }

    @Override
    int minHeal() {
        return 5;
    }

    @Override
    int maxHeal() {
        return 15;
    }

    @Override
    int healChargeTurns() {
        return 2;
    }
}

class Jotun extends Fighter {

    Jotun(Context context, sideViews views) {
        super(context, views);
    }

    @Override
    String name() {
        return "Jotun";
    }

    @Override
    int height() {
        return 200;
    }

    @Override
    int imageId() {
        return R.drawable.pg_jotun;
    }

    @Override
    int maxHealthPoints() {
        return 70;
    }

    @Override
    int minDamage() {
        return 2;
    }

    @Override
    int maxDamage() {
        return 5;
    }

    @Override
    int minDefence() {
        return 6;
    }

    @Override
    int maxDefence() {
        return 10;
    }

    @Override
    double focusBonus() {
        return 2.5;
    }

    @Override
    int minHeal() {
        return 1;
    }

    @Override
    int maxHeal() {
        return 8;
    }

    @Override
    int healChargeTurns() {
        return 3;
    }
}

class Kraken extends Fighter {

    Kraken(Context context, sideViews views) {
        super(context, views);
    }

    @Override
    String name() {
        return "Kraken";
    }

    @Override
    int height() {
        return 190;
    }

    @Override
    int imageId() {
        return R.drawable.pg_kraken;
    }

    @Override
    int maxHealthPoints() {
        return 70;
    }

    @Override
    int minDamage() {
        return 4;
    }

    @Override
    int maxDamage() {
        return 10;
    }

    @Override
    int minDefence() {
        return 2;
    }

    @Override
    int maxDefence() {
        return 4;
    }

    @Override
    double focusBonus() {
        return 1.5;
    }

    @Override
    int minHeal() {
        return 6;
    }

    @Override
    int maxHeal() {
        return 12;
    }

    @Override
    int healChargeTurns() {
        return 5;
    }
}