package lemonadestand;

public enum Weather {
    RAINY, SUNNY, CLOUDY;

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
