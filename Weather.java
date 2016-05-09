package lemonadestand;

/**
 * This enum holds the different weather forecasts available.
 *
 * @author Terry Weiss
 */
public enum Weather {
    RAINY, SUNNY, CLOUDY;

    /**
     * Generates a string representation describing the forecast.
     *
     * @return String representation of weather
     */
    /*
     * BEGIN To String
     *     Init string to "crazy"
     *     IF (it's rainy) THEN
     *         Set string to "rainy"
     *     ELSE IF (it's sunny)
     *         Set string to "hot and sunny"
     *     ELSE IF (it's cloudy) THEN
     *         Set string to "cloudy and cool"
     *     END IF
     *     Return string
     * END To String
     */
    @Override
    public String toString() {
        String str = "crazy";

        if (this == RAINY) {
            str = "rainy";
        } else if (this == SUNNY) {
            str = "hot and sunny";
        } else if (this == CLOUDY) {
            str = "cloudy and cool";
        }

        return str;
    }
}
