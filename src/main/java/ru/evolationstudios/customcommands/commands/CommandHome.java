package ru.evolationstudios.customcommands.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import ru.evolationstudios.customcommands.Config;

public final class CommandHome implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player)) return true;

        Player p = (Player)sender;

        Location l = p.getBedSpawnLocation();
        if (l != null)
        {
            p.teleport(l.add(0.5, 0, 0.5));
            sender.sendMessage(Config.Message.TeleportedToHome);
        }

        return true;
    }
}
