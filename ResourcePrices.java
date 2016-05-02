package lemonadestand;

class ResourcePrices {
    private static final double[] LEMONS_PRICES   = { 1.00, 0.50, 0.33 };
    private static final double[] SUGAR_PRICES    = { 0.50, 0.25 };
    private static final double[] ICE_PRICES      = { 0.10, 0.05 };
    private static final double WATER_PRICE       =   0.05;
    private static final double CUPS_PRICE        =   0.02;
    private static final double SIGN_PRICE        =   0.25;

    public double lemons;
    public double sugar;
    public double ice;
    public double water;
    public double cups;
    public double signs;
    public double costPerCup;

    protected ResourcePrices() {
        lemons = LEMONS_PRICES[Game.random.nextInt(LEMONS_PRICES.length)];
        sugar  = SUGAR_PRICES[Game.random.nextInt(SUGAR_PRICES.length)];
        ice    = ICE_PRICES[Game.random.nextInt(ICE_PRICES.length)];
        water  = WATER_PRICE;
        cups   = CUPS_PRICE;
        signs  = SIGN_PRICE;

        costPerCup = lemons + sugar + ice + water + cups;
    }
}
