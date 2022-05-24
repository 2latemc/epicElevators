package at.late.elevators.listeners;

import at.late.elevators.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.block.data.type.DaylightDetector;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

import static at.late.elevators.Main.plotSquaredFound;
import static at.late.elevators.commands.ElevatorCommand.playSoundFail;
import static at.late.elevators.commands.ElevatorCommand.playSoundSucces;
import static at.late.elevators.utils.FileManager.getPrefix;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;
import static at.late.elevators.utils.FileManager.storage;
public class OnToggle implements Listener {
    @EventHandler
    public void onToggle(PlayerInteractEvent event) {
        boolean usePlotSquared = false;
        if(plotSquaredFound || storage.getBoolean("ForcePlotSquared")) {
            usePlotSquared = true;
        }
        if(usePlotSquared) {
            Player player = event.getPlayer();
            if (event.getAction().equals(RIGHT_CLICK_BLOCK)) {
                if (event.isCancelled()) {
                    return;
                }
                Block block = event.getClickedBlock();
                if (block.getType().equals(Material.DAYLIGHT_DETECTOR)) {
                    TileState stateTarget = (TileState) block.getState();
                    PersistentDataContainer containerTarget = stateTarget.getPersistentDataContainer();
                    NamespacedKey keyTarget = new NamespacedKey(Main.getPlugin(Main.class), "elevator");
                    if (containerTarget.has(keyTarget, PersistentDataType.STRING)) {
                        DaylightDetector day = (DaylightDetector) block.getBlockData();
                        if (day.isInverted()) {
                            playSoundSucces(player);
                            player.sendMessage(getPrefix() + Objects.requireNonNull(storage.getString("ElevatorPublic")).replaceAll("&", "§"));
                        } else {
                            playSoundFail(player);
                            player.sendMessage(getPrefix() + Objects.requireNonNull(storage.getString("ElevatorPrivate")).replaceAll("&", "§"));
                        }
                    }
                }
            }
        }
    }
}
