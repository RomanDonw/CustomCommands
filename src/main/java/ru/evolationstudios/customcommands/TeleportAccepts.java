package ru.evolationstudios.customcommands;

import java.util.HashMap;
import java.util.Map;
import java.time.Instant;
import java.util.Stack;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.entity.Player;

public final class TeleportAccepts
{
    //public static long TimeInSecondsToAccept = 30; // move it to config-file.

    private final static Map<Player, Stack<TeleportAcceptInfo>> tpAccepts = new HashMap<>();

    public static void AddRequest(Player src, Player dest)
    {
        TeleportAcceptInfo tpainfo = new TeleportAcceptInfo();

        tpainfo.endTime = Instant.now().plusSeconds(Config.TeleportRequests.TimeInSecondsToAccept);
        tpainfo.sourcePlayer = src;

        if (!tpAccepts.containsKey(dest)) tpAccepts.put(dest, new Stack<>());
        tpAccepts.get(dest).push(tpainfo);
    }

    public static boolean PlayerAvailable(Player player)
    {
        if (!tpAccepts.containsKey(player)) return false;
        if (!player.isOnline())
        {
            tpAccepts.remove(player);
            return false;
        }
        return true;
    }

    public static boolean IsRequestActual(@Nonnull TeleportAcceptInfo tpainfo)
    {
        return !(!tpainfo.sourcePlayer.isOnline() || tpainfo.endTime.isBefore(Instant.now()));
    }

    public static boolean AvailableLastRequest(Player player)
    {
        if (!PlayerAvailable(player)) return false;

        boolean result = IsRequestActual(tpAccepts.get(player).peek());
        if (!result) unsafe_RemoveLastRequest(player);
        return result;
    }

    public static boolean AvailableRequestByIndex(Player player, int index)
    {
        if (!PlayerAvailable(player)) return false;

        Stack<TeleportAcceptInfo> tpainfos = tpAccepts.get(player);

        if (index >= tpainfos.size() || index < 0) return false;

        boolean result = IsRequestActual(tpainfos.get(index));
        if (!result) unsafe_RemoveRequestByIndex(player, index);
        return result;
    }

    @Nullable
    public static Stack<TeleportAcceptInfo> GetRequests(Player player)
    {
        if (!PlayerAvailable(player)) return null;
        return tpAccepts.get(player);
    }

    @Nullable
    public static TeleportAcceptInfo GetLastRequest(Player player)
    {
        if (!AvailableLastRequest(player)) return null;
        return tpAccepts.get(player).peek();
    }

    @Nullable
    public static TeleportAcceptInfo GetRequestByIndex(Player player, int index)
    {
        if (!AvailableRequestByIndex(player, index)) return null;
        return tpAccepts.get(player).get(index);
    }

    public static boolean AcceptLastRequest(Player player)
    {
        if (!AvailableLastRequest(player)) return false;

        unsafe_Accept(player, tpAccepts.get(player).peek());
        unsafe_RemoveLastRequest(player);

        return true;
    }

    public static boolean AcceptRequestByIndex(Player player, int index)
    {
        if (!AvailableRequestByIndex(player, index)) return false;

        unsafe_Accept(player, tpAccepts.get(player).get(index));
        unsafe_RemoveRequestByIndex(player, index);

        return true;
    }

    public static boolean AcceptAllRequests(Player player)
    {
        if (!PlayerAvailable(player)) return false;

        Stack<TeleportAcceptInfo> tpainfos = tpAccepts.get(player);
        boolean result = false;

        for (int i = 0; i < tpainfos.size(); i++)
        {
            TeleportAcceptInfo tpainfo = tpainfos.get(i);
            if (IsRequestActual(tpainfo))
            {
                unsafe_Accept(player, tpainfo);
                result = true;
            }
            unsafe_RemoveRequestByIndex(player, i);
        }
        return result;
    }

    public static boolean RemovePlayer(Player player)
    {
        if (!PlayerAvailable(player)) return false;

        tpAccepts.remove(player);

        return true;
    }

    public static boolean RemoveLastRequest(Player player)
    {
        if (!AvailableLastRequest(player)) return false;

        unsafe_RemoveLastRequest(player);

        return true;
    }

    public static boolean RemoveRequestByIndex(Player player, int index)
    {
        if (!AvailableRequestByIndex(player, index)) return false;

        unsafe_RemoveRequestByIndex(player, index);

        return true;
    }

    public static int SearchRequest(Player player, Player src_player)
    {
        if (!PlayerAvailable(player)) return -1;

        Stack<TeleportAcceptInfo> tpainfos = tpAccepts.get(player);

        for (int i = 0; i < tpainfos.size(); i++)
        {
            TeleportAcceptInfo tpainfo = tpainfos.get(i);
            if (!IsRequestActual(tpainfo))
            {
                unsafe_RemoveRequestByIndex(player, i);
                continue;
            }

            if (tpainfo.sourcePlayer.equals(src_player)) return i;
        }

        return -1;
    }

    public static boolean ExistsRequestFromPlayer(Player player, Player src_player)
    {
        return SearchRequest(player, src_player) >= 0;
    }

    // Unsafe-functions for internal usage.

    private static void unsafe_Accept(Player player, @Nonnull TeleportAcceptInfo tpainfo)
    {
        tpainfo.sourcePlayer.teleport(player);
    }

    private static void unsafe_RemoveRequestByIndex(Player player, int index)
    {
        Stack<TeleportAcceptInfo> tpainfos = tpAccepts.get(player);
        tpainfos.remove(index);
        if (tpainfos.empty()) tpAccepts.remove(player);
    }

    private static void unsafe_RemoveLastRequest(Player player)
    {
        Stack<TeleportAcceptInfo> tpainfos = tpAccepts.get(player);
        tpainfos.pop();
        if (tpainfos.empty()) tpAccepts.remove(player);
    }
}
