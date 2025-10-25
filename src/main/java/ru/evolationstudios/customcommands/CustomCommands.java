package ru.evolationstudios.customcommands;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.CommandExecutor;

import ru.evolationstudios.customcommands.commands.*;
import ru.evolationstudios.customcommands.commands.TeleportRequests.*;

public final class CustomCommands extends JavaPlugin
{
    public static boolean DEBUG = false;

    @Override
    public void onEnable() {
        Path pluginfolder = Paths.get(this.getDataFolder().getAbsolutePath());
        if (!Files.exists(pluginfolder))
        {
            try
            {
                Files.createDirectory(pluginfolder);
            }
            catch (IOException e)
            {
                throw new RuntimeException("Can't create plugin folder.");
            }
        }

        Path configpath = pluginfolder.resolve("config.json");
        if (Files.exists(configpath) && !Files.isDirectory(configpath))
        {
            try
            {
                FileReader r = new FileReader(configpath.toFile());

                ReadConfig(r);

                r.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException("Can't open/read config file.");
            }
        }
        else
        {
            try
            {
                FileWriter w = new FileWriter(configpath.toFile());

                w.write("""
                        {
                            "teleport-requests":
                            {
                                "time-in-seconds-to-accept": 30
                            },
                            "messages":
                            {
                                "teleported-to-home": "Teleported to bed respawn location.",
                                "teleported-to-back": "Teleported to last death location.",
                                "teleport-requests":
                                {
                                    "player-not-found": "Player not found.",
                                    "request-are-not-available": "Teleport request are not available.",
                                    "you-have-not-anything-requests-to-you": "You have not anything teleport requests to you.",
                                    "sent-request-to-player": "Sent request to player \\"%PLAYER_NAME%\\".",
                                    "player-sent-request-to-you": "Player \\"%PLAYER_NAME%\\" sent teleport request to you.\\nUse /tpaccept to accept the request or /tpdecline to decline the request.",
                                    "you-already-sent-request": "You already sent request to player \\"%PLAYER_NAME%\\".",
                                    "you-have-not-sent-request": "You haven't sent request to player \\"%PLAYER_NAME%\\".",
                                    "canceled-request-to-player": "Canceled teleport request to player \\"%PLAYER_NAME%\\".",
                                    "player-canceled-request-from-you": "Player \\"%PLAYER_NAME%\\" canceled teleport request to you.",
                                    "request-from-this-player-not-found": "Teleport request from this player not found.",
                                    "accepted-last-request": "Accepted last teleport request.",
                                    "accepted-request-from-player": "Accepted teleport request from player \\"%PLAYER_NAME%\\".",
                                    "request-not-found": "Teleport request from this player not found.",
                                    "declined-request-from-player": "Declined teleport request from player \\"%PLAYER_NAME%\\".",
                                    "declined-last-request": "Declined last teleport request.",
                                    "declined-all-requests": "Declined all teleport requests to you.",
                                    "accepted-all-requests": "Accepted all teleport requests to you."
                                }
                            }
                        }
                        """);

                w.close();

                FileReader r = new FileReader(configpath.toFile());
                ReadConfig(r);
                r.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException("Can't create config file.");
            }
        }

        RegisterCommand("home", new CommandHome());
        RegisterCommand("back", new CommandBack());
        RegisterCommand("suicide", new CommandSuicide());

        RegisterCommand("tpa", new CommandTPA());
        RegisterCommand("tpc", new CommandTPC());
        RegisterCommand("tpaccept", new CommandTPAccept());
        RegisterCommand("tpacceptall", new CommandTPAcceptAll());
        RegisterCommand("tpdecline", new CommandTPDecline());
        RegisterCommand("tpdeclineall", new CommandTPDeclineAll());
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
    }

    private void RegisterCommand(String name, CommandExecutor handler)
    {
        PluginCommand c = this.getCommand(name);
        if (c != null)
        {
            c.setExecutor(handler);
            c.setPermission("customcommands.command." + name);
        }
        else throw new RuntimeException("Can't find definition of command \"" + name + "\" in plugin.yml!"); //getLogger().warning("Definition of command \"" + name + "\" isn't exist.");
    }

    private void ReadConfig(FileReader configFile) throws RuntimeException
    {
        try
        {
            JsonReader jr = Json.createReader(configFile);
            JsonObject config = jr.readObject();

            JsonObject tpRequests = config.getJsonObject("teleport-requests");

            Config.TeleportRequests.TimeInSecondsToAccept = tpRequests.getInt("time-in-seconds-to-accept");

            JsonObject messages = config.getJsonObject("messages");

            Config.Message.TeleportedToBack = messages.getString("teleported-to-back");
            Config.Message.TeleportedToHome = messages.getString("teleported-to-home");

            JsonObject tpRequestsMessages = messages.getJsonObject("teleport-requests");

            Config.Message.AcceptedAllRequests = tpRequestsMessages.getString("accepted-all-requests");
            if (DEBUG) System.out.println("Read 1");
            Config.Message.AcceptedLastRequest = tpRequestsMessages.getString("accepted-last-request");
            if (DEBUG) System.out.println("Read 2");
            Config.Message.AcceptedRequestFromPlayer = tpRequestsMessages.getString("accepted-request-from-player");
            if (DEBUG) System.out.println("Read 3");
            Config.Message.CanceledRequestToPlayer = tpRequestsMessages.getString("canceled-request-to-player");
            if (DEBUG) System.out.println("Read 4");
            Config.Message.DeclinedAllRequests = tpRequestsMessages.getString("declined-all-requests");
            if (DEBUG) System.out.println("Read 5");
            Config.Message.DeclinedLastRequest = tpRequestsMessages.getString("declined-last-request");
            if (DEBUG) System.out.println("Read 6");
            Config.Message.DeclinedRequestFromPlayer = tpRequestsMessages.getString("declined-request-from-player");
            if (DEBUG) System.out.println("Read 7");
            Config.Message.PlayerCanceledRequestFromYou = tpRequestsMessages.getString("player-canceled-request-from-you");
            if (DEBUG) System.out.println("Read 8");
            Config.Message.PlayerNotFound = tpRequestsMessages.getString("player-not-found");
            if (DEBUG) System.out.println("Read 9");
            Config.Message.PlayerSentRequestToYou = tpRequestsMessages.getString("player-sent-request-to-you");
            if (DEBUG) System.out.println("Read 10");
            Config.Message.RequestAreNotAvailable = tpRequestsMessages.getString("request-are-not-available");
            if (DEBUG) System.out.println("Read 11");
            Config.Message.RequestFromThisPlayerNotFound = tpRequestsMessages.getString("request-from-this-player-not-found");
            if (DEBUG) System.out.println("Read 12");
            Config.Message.RequestNotFound = tpRequestsMessages.getString("request-not-found");
            if (DEBUG) System.out.println("Read 13");
            Config.Message.SentRequestToPlayer = tpRequestsMessages.getString("sent-request-to-player");
            if (DEBUG) System.out.println("Read 14");
            Config.Message.YouAlreadySentRequest = tpRequestsMessages.getString("you-already-sent-request");
            if (DEBUG) System.out.println("Read 15");
            Config.Message.YouHaveNotAnythingRequestsToYou = tpRequestsMessages.getString("you-have-not-anything-requests-to-you");
            if (DEBUG) System.out.println("Read 16");
            Config.Message.YouHaveNotSentRequest = tpRequestsMessages.getString("you-have-not-sent-request");
            if (DEBUG) System.out.println("Read 17");

            jr.close();
        }
        catch (JsonException e)
        {
            throw new RuntimeException("Can't read/parse config file.");
        }
    }
}

