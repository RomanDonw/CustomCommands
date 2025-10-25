package ru.evolationstudios.customcommands.commands.TeleportRequests;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.evolationstudios.customcommands.Config;
import ru.evolationstudios.customcommands.TeleportAccepts;

public final class CommandTPA implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length < 1) return false;

        if (!(sender instanceof Player)) return true;
        Player src_player = (Player)sender;

        Player dest_player = Bukkit.getServer().getPlayer(args[0]);
        if (dest_player == null)
        {
            sender.sendMessage(Config.Message.PlayerNotFound);
            return true;
        }

        if (!TeleportAccepts.ExistsRequestFromPlayer(dest_player, src_player))
        {
            TeleportAccepts.AddRequest(src_player, dest_player);
            //sender.sendMessage("Sent request to player \"" + args[0] + "\".");
            sender.sendMessage(Config.Message.SentRequestToPlayer.replaceAll("%PLAYER_NAME%", args[0]));
            //dest_player.sendMessage("Player \"" + src_player.getName() + "\" sent teleport request to you.\nUse /tpaccept to accept the request or /tpdecline to cancel the request.");
            dest_player.sendMessage(Config.Message.PlayerSentRequestToYou.replaceAll("%PLAYER_NAME%", src_player.getName()));
        }
        else
        {
            //sender.sendMessage("You already sent request to player \"" + args[0] + "\".");
            sender.sendMessage(Config.Message.YouAlreadySentRequest.replaceAll("%PLAYER_NAME%", args[0]));
        }

        return true;
    }
}
