package com.reed.factionsstyleclaims.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;

public class FSCSavedData extends SavedData {
    public static final String FSC_DATA = "fsc_data";
    private final Map<String, Integer> playerPower = new HashMap<>();
    private final Map<String, Integer> playerMaxPower = new HashMap<>();
    private final Map<String, Integer> teamPower = new HashMap<>();
    private final Map<String, Integer> teamMaxPower = new HashMap<>();
    private final static String POWER_KEY_PREFIX = "POWER_";
    private final static String MAX_POWER_KEY_PREFIX = "MAX_POWER_";
    private final static String TEAM_POWER_KEY_PREFIX = "TEAM_POWER_";
    private final static String TEAM_MAX_POWER_KEY_PREFIX = "TEAM_MAX_POWER_";

    public static FSCSavedData create() {
        FSCSavedData fscSavedData = new FSCSavedData();
        return fscSavedData;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        for(Map.Entry<String, Integer> entry : playerPower.entrySet()) {
            tag.putInt(getPowerKey(entry.getKey()), entry.getValue());
        }
        for(Map.Entry<String, Integer> entry : playerMaxPower.entrySet()) {
            tag.putInt(getMaxPowerKey(entry.getKey()), entry.getValue());
        }
        for(Map.Entry<String, Integer> entry : teamPower.entrySet()) {
            tag.putInt(getTeamPowerKey(entry.getKey()), entry.getValue());
        }
        for(Map.Entry<String, Integer> entry : teamMaxPower.entrySet()) {
            tag.putInt(getTeamMaxPowerKey(entry.getKey()), entry.getValue());
        }
        return tag;
    }

    public static FSCSavedData load(CompoundTag tag) {
        FSCSavedData fscSavedData = create();
        for (String key : tag.getAllKeys()) {
            if (key.startsWith(POWER_KEY_PREFIX)) {
                fscSavedData.playerPower.put(
                        key.replace(POWER_KEY_PREFIX, ""),
                        Integer.valueOf(tag.getInt(key))
                );
            }
            if (key.startsWith(MAX_POWER_KEY_PREFIX)) {
                fscSavedData.playerMaxPower.put(
                        key.replace(MAX_POWER_KEY_PREFIX, ""),
                        Integer.valueOf(tag.getInt(key))
                );
            }
            if (key.startsWith(TEAM_POWER_KEY_PREFIX)) {
                fscSavedData.teamPower.put(
                        key.replace(TEAM_POWER_KEY_PREFIX, ""),
                        Integer.valueOf(tag.getInt(key))
                );
            }
            if (key.startsWith(TEAM_MAX_POWER_KEY_PREFIX)) {
                fscSavedData.teamMaxPower.put(
                        key.replace(TEAM_MAX_POWER_KEY_PREFIX, ""),
                        Integer.valueOf(tag.getInt(key))
                );
            }
        }
        return fscSavedData;
    }
    public String getPowerKey(String playerName) {
        return POWER_KEY_PREFIX + playerName;
    }
    public String getMaxPowerKey(String playerName) {
        return MAX_POWER_KEY_PREFIX + playerName;
    }
    public String getTeamPowerKey(String teamName) {
        return TEAM_POWER_KEY_PREFIX + teamName;
    }
    public String getTeamMaxPowerKey(String teamName) {
        return TEAM_MAX_POWER_KEY_PREFIX + teamName;
    }
    public Map<String, Integer> getPowerMap() {
        return playerPower;
    }
    public Map<String, Integer> getMaxPowerMap() {
        return playerMaxPower;
    }
    public Map<String, Integer> getTeamPowerMap() {
        return teamPower;
    }
    public Map<String, Integer> getTeamMaxPowerMap() {
        return teamMaxPower;
    }

}
