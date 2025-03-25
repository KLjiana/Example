package com.kaleblangley.examples.api;

public interface IEnchantmentParameter {
    void setMinLevel(int minLevel);

    void setMaxLevel(int maxLevel);

    void setMinCost(int minCost);

    void setMaxCost(int maxCost);

    void setTreasureOnly(boolean treasureOnly);

    void setCurse(boolean curse);

    void setTradeable(boolean tradeable);

    void setDiscoverable(boolean discoverable);

    void setAllowedOnBooks(boolean allowedOnBooks);
}
