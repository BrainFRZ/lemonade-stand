package lemonadestand;

import java.util.LinkedList;

public class Business extends LinkedList<Stand> {
    public static final double STAND_PRICE    = 150.00;     //Cost of a new stand
    public static final double STARTING_MONEY = 100.00;     //Money you start with in each stand

    private double money;

    public Business() {
        money = STARTING_MONEY + STAND_PRICE;   //Adds what it'll cost for the first stand
    }

    public double getMoney() {
        return money;
    }

    public boolean spend(double expense) {
        boolean spent = false;

        if (expense < 0) {
            throw new IllegalArgumentException("Negative expenses aren't allowed:" + expense);
        } else if (money >= expense) {
            money -= expense;
            spent = true;
        }

        return spent;
    }

    public void addProfit(double profit) {
        if (profit < 0) {
            throw new IllegalArgumentException("Negative profits aren't allowed:" + profit);
        } else {
            money += profit;
        }
    }

    @Override
    public boolean add(Stand newStand) {
        boolean added = false;

        if (money >= STAND_PRICE) {
            money -= STAND_PRICE;
            super.add(newStand);
            added = true;
        }

        return added;
    }

    @Override
    public Object clone() {
        return super.clone();
    }
}
