package ru.evolationstudios.customcommands.commands.TeleportRequests;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.evolationstudios.customcommands.Config;
import ru.evolationstudios.customcommands.TeleportAccepts;

public final class CommandTPAcceptAll implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player)) return true;
        Player player = (Player)sender;

        if (TeleportAccepts.PlayerAvailable(player) && TeleportAccepts.AcceptAllRequests(player))
        {
            sender.sendMessage(Config.Message.AcceptedAllRequests);
        }
        else
        {
            sender.sendMessage(Config.Message.YouHaveNotAnythingRequestsToYou);
        }

        return true;
    }
}
