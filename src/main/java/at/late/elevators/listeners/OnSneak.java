package at.late.elevators.listeners;

import at.late.elevators.Main;
import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.block.data.type.DaylightDetector;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

import static at.late.elevators.commands.ElevatorCommand.playSoundSucces;
import static at.late.elevators.utils.FileManager.getPrefix;
import static at.late.elevators.utils.FileManager.storage;
import static at.late.elevators.Main.plotSquaredFound;
import static at.late.elevators.commands.ElevatorCommand.playSoundFail;
import static at.late.elevators.utils.Utils.floorsDown;

public class OnSneak implements Listener {
    boolean usePlotSquared = false;
    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        int max = 256;
        int min = 0;
        boolean useNew = storage.getBoolean("UseNewHeightLimits");
        if(useNew) {
            max = 319;
            min = -64;
        }
        if(event.isSneaking()) {
            if(plotSquaredFound || storage.getBoolean("ForcePlotSquared")) {
                usePlotSquared = true;
            }
            Player player = event.getPlayer();
            if (player.getLocation().getBlock().getType() == Material.DAYLIGHT_DETECTOR) {
                Block block = player.getLocation().getBlock();
                TileState state = (TileState) block.getState();
                Location locationBlock = block.getLocation();
                Location locationPlayer = player.getLocation();
                PersistentDataContainer container = state.getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "elevator");
                if (!container.has(key, PersistentDataType.STRING)) {
                    return;
                }
                if (container.has(key, PersistentDataType.STRING)) {
                    DaylightDetector dayBlock = (DaylightDetector) block.getBlockData();
                    if(dayBlock.isInverted() && usePlotSquared) {
                            PlotAPI api = new PlotAPI();
                            PlotPlayer plotPlayer = api.wrapPlayer(player.getUniqueId());
                            Plot plot = plotPlayer.getCurrentPlot();
                            if (!(plot.getMembers().contains(player.getUniqueId()) || plot.getOwners().contains(player.getUniqueId()) || plot.getTrusted().contains(player.getUniqueId()) || player.hasPermission("late.team"))) {
                                player.sendMessage(Objects.requireNonNull(storage.getString("ElevatorPrivate")).replaceAll("&", "§"));
                                playSoundFail(player);
                                return;
                            }
                    }
                    Boolean isPrivate = false;
                    for (int y = locationBlock.getBlockY() - 1; y >= min; y--) {
                        Location target;
                        if ((target = new Location(locationPlayer.getWorld(), locationPlayer.getX(), y, locationPlayer.getZ(), locationPlayer.getYaw(), locationPlayer.getPitch())).getBlock().getType() == Material.DAYLIGHT_DETECTOR) {
                            Block targetBlock = target.getBlock();
                            TileState stateTarget = (TileState) targetBlock.getState();
                            PersistentDataContainer containerTarget = stateTarget.getPersistentDataContainer();
                            NamespacedKey keyTarget = new NamespacedKey(Main.getPlugin(Main.class), "elevator");
                            if (containerTarget.has(keyTarget, PersistentDataType.STRING)) {
                                DaylightDetector day = (DaylightDetector) targetBlock.getBlockData();
                                if(day.isInverted() && usePlotSquared) {
                                        PlotAPI api = new PlotAPI();
                                        PlotPlayer plotPlayer = api.wrapPlayer(player.getUniqueId());
                                        Plot plot = plotPlayer.getCurrentPlot();
                                        isPrivate = true;
                                        if (plot.getMembers().contains(player.getUniqueId()) || plot.getOwners().contains(player.getUniqueId()) || plot.getTrusted().contains(player.getUniqueId()) || player.hasPermission("late.team")) {
                                            player.teleport(target.add(0.0D, 1.0D, 0.0D));
                                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(floorsDown(locationPlayer)));
                                            player.sendMessage(getPrefix() + storage.getString("ElevatorTeleported").replaceAll("&", "§"));
                                            playSoundSucces(player);
                                            return;
                                        }
                                }
                                else {
                                    player.teleport(target.add(0.0D, 1.0D, 0.0D));
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(floorsDown(locationPlayer)));
                                    player.sendMessage(getPrefix()+ storage.getString("ElevatorTeleported").replaceAll("&", "§"));
                                    playSoundSucces(player);
                                    return;
                                }
                            }
                        }
                    }
                    if(isPrivate) {
                        player.sendMessage(getPrefix() + storage.getString("AllElevatorsPrivate").replaceAll("&", "§"));
                        playSoundFail(player);
                    } else  {
                        player.sendMessage(getPrefix() + storage.getString("NoElevatorsFound").replaceAll("&", "§"));
                        playSoundFail(player);
                    }
                }
            }
        }
    }
}
