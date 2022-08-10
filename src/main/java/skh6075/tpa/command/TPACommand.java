package skh6075.tpa.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import skh6075.tpa.TPA;

public class TPACommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }

        Player player = (Player) sender;
        if(TPA.getInstance().isExistsRequestData(player.getUniqueId()) && !TPA.getInstance().canRequestTPA(player.getUniqueId())){
            String target = TPA.getInstance().getTargetByRequester(player.getUniqueId());
            player.sendMessage(TPA.prefix + "당신은 이미 §f" + target + "님§7에게 TPA를 요청했습니다.");
            return false;
        }

        if(args.length < 1){
            player.sendMessage(TPA.prefix + "/tpa <이름>");
            return false;
        }

        String name = args[0];
        Player target = player.getServer().getPlayer(name);
        if(target == null || TPA.getInstance().isExistsResponseData(target.getUniqueId())){
            player.sendMessage(TPA.prefix + "해당 플레이를 찾을 수 없거나 이미 다른 플레이어에게 TPA 신청을 받았습니다.");
            return false;
        }

        TPA.getInstance().toTPARequest(player, target);
        player.sendMessage(TPA.prefix + "§f" + target.getName() + "님§7에게 TPA 신청을 보냈습니다. (1분 뒤에 다시 가능)");
        target.sendMessage(TPA.prefix + "§f" + player.getName() + "님§7으로 부터 TPA 신청을 받았습니다.");
        target.sendMessage(TPA.prefix + "TPA 신청 수락은 §a/tpa수락§f, 거절은 §c/tpa거절§7 로 가능합니다.");
        return true;
    }
}
