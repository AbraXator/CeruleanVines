package net.abraxator.moresnifferflowers.nutrition;

public enum NutritionType {
    SWEET("sweet"),
    SALTY("salty"),
    SOUR("sour"),
    NEUTRAL("neutral"),
    SPICY("spicy");
    
    public final String name;
    
    NutritionType(String name) {
        this.name = name;
    }
}
