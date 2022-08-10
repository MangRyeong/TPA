package skh6075.tpa.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import skh6075.tpa.TPA;

public class TPADeniedCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }

        Player player = (Player) sender;
        if(!TPA.getInstance().isExistsResponseData(player.getUniqueId())){
            player.sendMessage(TPA.prefix + "당신은 받은 요청이 없습니다.");
            return false;
        }

        String name = TPA.getInstance().getTargetByResponser(player.getUniqueId());
        if(name == null){
            player.sendMessage(TPA.prefix + "당신은 받은 요청이 없습니다.");
            return false;
        }

        Player requester = player.getServer().getPlayer(name);
        if(requester != null){
            player.sendMessage(TPA.prefix + "TPA 요청을 거절했습니다.");
            requester.sendMessage(TPA.prefix + "§f" + player.getName() + "님§7께서 TPA 요청을 거절했습니다.");
            TPA.getInstance().removeTPAData(requester, player);
        }
        return true;
    }
}
