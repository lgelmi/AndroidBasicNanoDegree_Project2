package com.example.android.fightkeeper;

import android.content.Context;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
}

abstract class Fighter {

    // General
    private sideViews views;

    public Context context;

    abstract String name();

    // Graphical
    abstract int height();

    abstract int imageId();

    // Stats
    abstract int maxHealthPoints();

    private int currentHealth;

    abstract int minDamage();

    public int getMinDamage() {
        return minDamage() * (focusAmount + 1);
    }

    abstract int maxDamage();

    public int getMaxDamage() {
        return maxDamage() * (focusAmount + 1);
    }

    abstract int minDefence();

    public int getMinDefence() {
        return minDefence() * (focusAmount + 1);
    }

    abstract int maxDefence();

    public int getMaxDefence() {
        return maxDefence() * (focusAmount + 1);
    }

    abstract double focusBonus();

    protected int focusAmount = 0;

    abstract int minHeal();

    public int getMinHeal() {
        return minHeal() * (focusAmount + 1);
    }

    abstract int maxHeal();

    public int getMaxHeal() {
        return maxHeal() * (focusAmount + 1);
    }

    abstract int healChargeTurns();

    int healTurnsLeft;

    public Fighter(Context context, sideViews views) {
        // Initialize the Fighter dynamic stats
        this.context = context;
        this.views = views;
        currentHealth = maxHealthPoints();
        healTurnsLeft = 0;
        focusAmount = 0;
        this.views.image.setImageResource(imageId());
        this.views.image.getLayoutParams().height = (int) context.getResources().getDisplayMetrics().density * height();
        this.views.name.setText(name());
        setCurrentHealth(maxHealthPoints());
        updateVisibleStats();
    }

    public void setCurrentHealth(int newHealth) {
        if (newHealth > this.maxHealthPoints())
            newHealth = 0;
        else if (newHealth < 0)
            newHealth = 0;
        currentHealth = newHealth;
        views.healthText.setText(context.getResources().getString(R.string.Health, currentHealth, maxHealthPoints()));
        views.healthBar.setProgress(currentHealth * 100 / maxHealthPoints());
    }

    public void updateVisibleStats() {
        views.damageText.setText(context.getResources().getString(R.string.Damage, getMinDamage(), getMaxDamage()));
        views.defenceText.setText(context.getResources().getString(R.string.DefenceAmount, getMinDefence(), getMaxDefence()));
        views.focusText.setText(context.getResources().getString(R.string.FocusMultiplier, focusBonus()));
        if (healTurnsLeft == 0)
            views.healText.setText(context.getResources().getString(R.string.HealAmount, getMinHeal(), getMaxHeal()));
        else
            views.healText.setText(context.getResources().getString(R.string.HealTurns, healTurnsLeft));
    }


    public boolean isDead() {
        return currentHealth == 0;
    }

}

class Goblin extends Fighter {


    public Goblin(Context context, sideViews views) {
        super(context, views);
    }

    @Override
    String name() {
        return "Goblin";
    }

    @Override
    int height() {
        return 90;
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
        return 2;
    }

    @Override
    int maxHeal() {
        return 4;
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
        return 90;
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
        return 3;
    }

    @Override
    int maxHeal() {
        return 6;
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
        return 4;
    }

    @Override
    int maxHeal() {
        return 8;
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
        return 170;
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
        return 5;
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
        return 2;
    }

    @Override
    int maxHeal() {
        return 4;
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
        return 220;
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
        return 3;
    }

    @Override
    int minHeal() {
        return 1;
    }

    @Override
    int maxHeal() {
        return 6;
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
        return 200;
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
        return 4;
    }

    @Override
    int maxHeal() {
        return 8;
    }

    @Override
    int healChargeTurns() {
        return 5;
    }
}