package ru.evolationstudios.customcommands.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import ru.evolationstudios.customcommands.Config;

public final class CommandExplode implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Entity))
        {
            sender.sendMessage(Config.Message.Failed);
            return true;
        }

        Entity e = (Entity)sender;

        if (args.length < 4) return false;

        double x, y, z;
        float power;
        boolean burnblocks = false;

        try { x = Double.parseDouble(args[0]); }
        catch (NumberFormatException ex) { sender.sendMessage(genConvErrMsg(args[0], "double")); return true; }

        try { y = Double.parseDouble(args[1]); }
        catch (NumberFormatException ex) { sender.sendMessage(genConvErrMsg(args[1], "double")); return true; }

        try { z = Double.parseDouble(args[2]); }
        catch (NumberFormatException ex) { sender.sendMessage(genConvErrMsg(args[2], "double")); return true; }

        try { power = Float.parseFloat(args[3]); }
        catch (NumberFormatException ex) { sender.sendMessage(genConvErrMsg(args[3], "float")); return true; }

        if (args.length >= 5) burnblocks = Boolean.parseBoolean(args[4]);

        e.getWorld().createExplosion(x, y, z, power, burnblocks);
        sender.sendMessage(Config.Message.Success);

        return true;
    }

    private String genConvErrMsg(String value, String type)
    {
        return Config.Message.CantParseArgumentValue.replaceAll("%VALUE%", value).replaceAll("%TYPE%", type);
    }
}
