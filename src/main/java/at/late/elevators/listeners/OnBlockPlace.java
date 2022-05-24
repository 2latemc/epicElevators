package at.late.elevators.listeners;

import at.late.elevators.Main;
import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static at.late.elevators.Main.plotSquaredFound;
import static at.late.elevators.commands.ElevatorCommand.playSoundFail;
import static at.late.elevators.commands.ElevatorCommand.playSoundLevelup;
import static at.late.elevators.utils.FileManager.getPrefix;
import static at.late.elevators.utils.FileManager.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OnBlockPlace implements Listener {
    boolean usePlotSquared = false;
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(event.isCancelled()) {
            return;
        }
        if(plotSquaredFound || storage.getBoolean("ForcePlotSquared")) {
            usePlotSquared = true;
        }
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if(Objects.requireNonNull(event.getItemInHand().getItemMeta()).getLocalizedName().equals("elevator")) {
            List<String> list = storage.getStringList("AllowedWorlds");
            if(!list.contains(player.getLocation().getWorld().getName())) {
                event.setCancelled(true);
                String msg = Objects.requireNonNull(storage.getString("NotInWorld")).replaceAll("&", "§");
                msg = msg.replaceAll("%world%", player.getLocation().getWorld().getName());
                player.sendMessage(getPrefix() + msg);
                playSoundFail(player);
                return;
            }
            if(usePlotSquared) {
                PlotAPI api = new PlotAPI();
                PlotPlayer plotPlayer = api.wrapPlayer(player.getUniqueId());
                Plot plot = plotPlayer.getCurrentPlot();
                if (plot == null) {
                    player.sendMessage(getPrefix() + Objects.requireNonNull(storage.getString("NotOnPlot")).replaceAll("&", "§"));
                    playSoundFail(player);
                    event.setCancelled(true);
                    return;
                }
                if (!(plot.getMembers().contains(player.getUniqueId()) || plot.getOwners().contains(player.getUniqueId()) || plot.getTrusted().contains(player.getUniqueId()) || player.hasPermission("late.team"))) {
                    return;
                }
            }
            TileState state = (TileState) block.getState();
            PersistentDataContainer container = state.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "elevator");
            container.set(key, PersistentDataType.STRING, "elevator");
            state.update();
            player.sendMessage(getPrefix() + Objects.requireNonNull(storage.getString("ElevatorPlaced")).replaceAll("&", "§"));
            playSoundLevelup(player);
        }
    }
}
