package ru.evolationstudios.customcommands.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.evolationstudios.customcommands.Config;

public final class CommandPlayerDataLoad implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length < 1) return false;

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null)
        {
            sender.sendMessage(Config.Message.PlayerNotFound);
            return true;
        }

        target.loadData();

        sender.sendMessage(Config.Message.LoadedPlayerData);

        return true;
    }
}
