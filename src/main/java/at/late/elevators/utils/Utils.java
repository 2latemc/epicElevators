package at.late.elevators.utils;

import at.late.elevators.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

import static at.late.elevators.utils.FileManager.storage;
public class Utils {
    public static String floors(Location location) {
    int maxFloors = 1;
    int currentFloor = 1;
    int max = 256;
    int min = 0;
    boolean useNew = storage.getBoolean("UseNewHeightLimits");
    if(useNew) {
        max = 319;
        min = -64;
    }
        for (int y = location.getBlockY() - 1; y >= min; y--) {
        Location target;
        if ((target = new Location(location.getWorld(), location.getX(), y, location.getZ(), location.getYaw(), location.getPitch())).getBlock().getType() == Material.DAYLIGHT_DETECTOR) {
            Block targetBlock = target.getBlock();
            TileState stateTarget = (TileState) targetBlock.getState();
            PersistentDataContainer containerTarget = stateTarget.getPersistentDataContainer();
            NamespacedKey keyTarget = new NamespacedKey(Main.getPlugin(Main.class), "elevator");
            if (containerTarget.has(keyTarget, PersistentDataType.STRING)) {
                maxFloors++;
            }
        }
    }
    currentFloor = currentFloor + maxFloors;
        for (int y = location.getBlockY() + 1; y <= max; y++) {
        Location target;
        if ((target = new Location(location.getWorld(), location.getX(), y, location.getZ(), location.getYaw(), location.getPitch())).getBlock().getType() == Material.DAYLIGHT_DETECTOR) {
            Block targetBlock = target.getBlock();
            TileState stateTarget = (TileState) targetBlock.getState();
            PersistentDataContainer containerTarget = stateTarget.getPersistentDataContainer();
            NamespacedKey keyTarget = new NamespacedKey(Main.getPlugin(Main.class), "elevator");
            if (containerTarget.has(keyTarget, PersistentDataType.STRING)) {
                maxFloors++;
            }
        }
    }
        if(currentFloor == 0) {
        return Objects.requireNonNull(storage.getString("GroundFloor")).replaceAll("&", "§");
    }
        else {
            String format = storage.getString("TeleportLore").replaceAll("&", "§");
            format = format.replaceAll("%current_floor%", String.valueOf(currentFloor));
            format = format.replaceAll("%max_floors%", String.valueOf(maxFloors));
        return format;
    }
}
    public static String floorsDown(Location location) {    int max = 256;
        int min = 0;
        boolean useNew = storage.getBoolean("UseNewHeightLimits");
        if(useNew) {
            max = 319;
            min = -64;
        }

        int maxFloors2 = 1;
        int currentFloor2 = -1;
        for (int y = location.getBlockY() - 1; y >= min; y--) {
            Location target;
            if ((target = new Location(location.getWorld(), location.getX(), y, location.getZ(), location.getYaw(), location.getPitch())).getBlock().getType() == Material.DAYLIGHT_DETECTOR) {
                Block targetBlock = target.getBlock();
                TileState stateTarget = (TileState) targetBlock.getState();
                PersistentDataContainer containerTarget = stateTarget.getPersistentDataContainer();
                NamespacedKey keyTarget = new NamespacedKey(Main.getPlugin(Main.class), "elevator");
                if (containerTarget.has(keyTarget, PersistentDataType.STRING)) {
                    maxFloors2++;
                }
            }
        }
        currentFloor2 = currentFloor2 + maxFloors2;
        for (int y = location.getBlockY() + 1; y <= max; y++) {
            Location target;
            if ((target = new Location(location.getWorld(), location.getX(), y, location.getZ(), location.getYaw(), location.getPitch())).getBlock().getType() == Material.DAYLIGHT_DETECTOR) {
                Block targetBlock = target.getBlock();
                TileState stateTarget = (TileState) targetBlock.getState();
                PersistentDataContainer containerTarget = stateTarget.getPersistentDataContainer();
                NamespacedKey keyTarget = new NamespacedKey(Main.getPlugin(Main.class), "elevator");
                if (containerTarget.has(keyTarget, PersistentDataType.STRING)) {
                    maxFloors2++;
                }
            }
        }


        if(currentFloor2 == 1) {
            return Objects.requireNonNull(storage.getString("GroundFloor")).replaceAll("&", "§");
        }
        else {
            String format = storage.getString("TeleportLore").replaceAll("&", "§");
            format = format.replaceAll("%current_floor%", String.valueOf(currentFloor2));
            format = format.replaceAll("%max_floors%", String.valueOf(maxFloors2));
            return format;
        }
    }
}
