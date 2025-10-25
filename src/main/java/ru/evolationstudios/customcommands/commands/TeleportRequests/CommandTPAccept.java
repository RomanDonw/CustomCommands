package ru.evolationstudios.customcommands.commands.TeleportRequests;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.evolationstudios.customcommands.Config;
import ru.evolationstudios.customcommands.TeleportAccepts;

public final class CommandTPAccept implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player)) return true;
        Player player = (Player)sender;

        if (args.length >= 1)
        {
            Player src_player = Bukkit.getServer().getPlayer(args[0]);
            if (src_player == null)
            {
                sender.sendMessage(Config.Message.PlayerNotFound);
                return true;
            }

            int index = TeleportAccepts.SearchRequest(player, src_player);
            if (index < 0)
            {
                sender.sendMessage(Config.Message.RequestFromThisPlayerNotFound);
                return true;
            }

            TeleportAccepts.AcceptRequestByIndex(player, index);
            //sender.sendMessage("Accepted teleport request from player \"" + src_player.getName() + "\".");
            sender.sendMessage(Config.Message.AcceptedRequestFromPlayer.replaceAll("%PLAYER_NAME%", src_player.getName()));
        }
        else
        {
            if (!TeleportAccepts.AvailableLastRequest(player))
            {
                sender.sendMessage(Config.Message.RequestAreNotAvailable);
                return true;
            }
            TeleportAccepts.AcceptLastRequest(player);
            sender.sendMessage(Config.Message.AcceptedLastRequest);
        }

        return true;
    }
}
