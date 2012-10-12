/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SimpleClans2 is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SimpleClans2.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Last modified: 10.10.12 21:57
 */

package com.p000ison.dev.simpleclans2.commands.clan;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;

/**
 * Represents a ProfileCommand
 */
public class ProfileCommand extends GenericPlayerCommand {

    private final static NumberFormat formatter = new DecimalFormat("#.#");

    public ProfileCommand(SimpleClans plugin)
    {
        super("Profile", plugin);
        setArgumentRange(0, 0);
        setUsages(MessageFormat.format(Language.getTranslation("usage.profile"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("profile.command"));
        setPermission("simpleclans.member.profile");
    }

    @Override
    public String getMenu(ClanPlayer cp)
    {
        if (cp != null) {
            if (cp.getClan().isVerified()) {
                return MessageFormat.format(Language.getTranslation("menu.profile.own"), plugin.getSettingsManager().getClanCommand());
            }
        }
        return null;
    }

    @Override
    public void execute(Player player, String command, String[] args)
    {

        ClanPlayer cp = plugin.getClanPlayerManager().getClanPlayer(player);

        if (cp == null) {
            player.sendMessage(ChatColor.RED + Language.getTranslation("not.a.member.of.any.clan"));
        } else {
            if (cp.getClan().isVerified()) {
                Clan clan = cp.getClan();
                if (clan.isVerified()) {
                    showClanProfile(player, clan, plugin);
                } else {
                    player.sendMessage(ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
                }
            } else {
                player.sendMessage(ChatColor.RED + Language.getTranslation("clan.is.not.verified"));
            }
        }
    }

    public static void showClanProfile(CommandSender sender, Clan clan, SimpleClans plugin)
    {
        ChatColor subColor = plugin.getSettingsManager().getSubPageColor();
        ChatColor headColor = plugin.getSettingsManager().getHeadingPageColor();

        ChatBlock.sendBlank(sender);
        ChatBlock.sendSingle(sender, plugin.getSettingsManager().getClanColor() + clan.getName() + subColor + " " + Language.getTranslation("profile"));
        ChatBlock.sendBlank(sender);

        String name = clan.getName();
        String leaders = plugin.getSettingsManager().getLeaderColor() + GeneralHelper.clansPlayersToString(clan.getLeaders(), ",");
        String onlineCount = ChatColor.WHITE.toString() + GeneralHelper.stripOfflinePlayers(clan.getAllMembers()).size();
        String membersOnline = onlineCount + subColor + "/" + ChatColor.WHITE + clan.getSize();
        String inactive = ChatColor.WHITE.toString() + clan.getInactiveDays() + subColor + "/" + ChatColor.WHITE.toString() + (clan.isVerified() ? plugin.getSettingsManager().getPurgeInactiveClansDays() : plugin.getSettingsManager().getPurgeUnverifiedClansDays()) + " " + Language.getTranslation("days");
        String founded = ChatColor.WHITE + clan.getFounded();

        String rawAllies = GeneralHelper.clansToString(clan.getAllies(), ",");
        String allies = ChatColor.WHITE + (rawAllies == null ? Language.getTranslation("none") : rawAllies);

        String rawRivals = GeneralHelper.clansToString(clan.getRivals(), ",");
        String rivals = ChatColor.WHITE + (rawRivals == null ? Language.getTranslation("none") : rawRivals);
        String kdr = ChatColor.YELLOW + formatter.format(clan.getKDR());

        int[] kills = clan.getTotalKills();

        String deaths = ChatColor.WHITE.toString() + kills[0];
        String rival = ChatColor.WHITE.toString() + kills[1];
        String civ = ChatColor.WHITE.toString() + kills[2];
        String neutral = ChatColor.WHITE.toString() + kills[3];

        String status = ChatColor.WHITE + (clan.isVerified() ? plugin.getSettingsManager().getTrustedColor() + Language.getTranslation("verified") : plugin.getSettingsManager().getUntrustedColor() + Language.getTranslation("unverified"));

        sender.sendMessage("  " + subColor + MessageFormat.format(Language.getTranslation("name.0"), name));
        sender.sendMessage("  " + subColor + MessageFormat.format(Language.getTranslation("status.0"), status));
        sender.sendMessage("  " + subColor + MessageFormat.format(Language.getTranslation("leaders.0"), leaders));
        sender.sendMessage("  " + subColor + MessageFormat.format(Language.getTranslation("members.online.0"), membersOnline));
        sender.sendMessage("  " + subColor + MessageFormat.format(Language.getTranslation("kdr.0"), kdr));
        sender.sendMessage("  " + subColor + Language.getTranslation("kill.totals") + " " + headColor + "[" + Language.getTranslation("rival") + ":" + rival + " " + headColor + Language.getTranslation("neutral") + ":" + neutral + " " + headColor + Language.getTranslation("civilian") + ":" + civ + headColor + "]");
        sender.sendMessage("  " + subColor + MessageFormat.format(Language.getTranslation("deaths.0"), deaths));
        sender.sendMessage("  " + subColor + MessageFormat.format(Language.getTranslation("allies.0"), allies));
        sender.sendMessage("  " + subColor + MessageFormat.format(Language.getTranslation("rivals.0"), rivals));
        sender.sendMessage("  " + subColor + MessageFormat.format(Language.getTranslation("founded.0"), founded));
        sender.sendMessage("  " + subColor + MessageFormat.format(Language.getTranslation("inactive.0"), inactive));

        ChatBlock.sendBlank(sender);
    }
}
