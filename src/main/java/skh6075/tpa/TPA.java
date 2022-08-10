package skh6075.tpa;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import skh6075.tpa.command.TPAAcceptCommand;
import skh6075.tpa.command.TPACommand;
import skh6075.tpa.command.TPADeniedCommand;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class TPA extends JavaPlugin implements Listener {
    public final static HashMap<UUID, String> tpaRequester = new HashMap<>();
    public final static HashMap<UUID, String> tpaResponser = new HashMap<>();
    public final static HashMap<UUID, Long> tpaTimer = new HashMap<>();

    public final static String prefix = "§l§6 [!]§r§7 ";

    private static TPA instance;

    public static TPA getInstance(){
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("tpa")).setExecutor(new TPACommand());
        Objects.requireNonNull(getCommand("tpa수락")).setExecutor(new TPAAcceptCommand());
        Objects.requireNonNull(getCommand("tpa거절")).setExecutor(new TPADeniedCommand());

        getServer().getPluginManager().registerEvents(this, this);
    }

    public Boolean isExistsRequestData(UUID uuid){
        return tpaRequester.containsKey(uuid);
    }

    @Nullable
    public String getTargetByRequester(UUID uuid){
        return tpaRequester.getOrDefault(uuid, null);
    }

    public Boolean isExistsResponseData(UUID uuid){
        return tpaResponser.containsKey(uuid);
    }

    public String getTargetByResponser(UUID uuid){
        return tpaResponser.getOrDefault(uuid, null);
    }

    public Boolean canRequestTPA(UUID uuid){
        if(!tpaTimer.containsKey(uuid)){
            return true;
        }
        long diff = System.currentTimeMillis() - tpaTimer.get(uuid);
        return diff > 60000; //1min
    }

    public void toTPARequest(Player player, Player target){
        tpaRequester.put(player.getUniqueId(), target.getName());
        tpaResponser.put(target.getUniqueId(), player.getName());
        tpaTimer.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public void removeTPAData(Player requester, Player responser){
        tpaRequester.remove(requester.getUniqueId());
        tpaResponser.remove(responser.getUniqueId());
        tpaTimer.remove(requester.getUniqueId());
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(isExistsRequestData(player.getUniqueId())){
            Player target = getServer().getPlayer(tpaRequester.get(player.getUniqueId()));
            if(target != null){
                removeTPAData(player, target);
            }
        }else if(isExistsResponseData(player.getUniqueId())){
            Player requester = getServer().getPlayer(tpaResponser.get(player.getUniqueId()));
            if(requester != null){
                removeTPAData(requester, player);
            }
        }
    }
}
