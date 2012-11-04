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

package com.p000ison.dev.simpleclans2.commands.general;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import com.p000ison.dev.simpleclans2.util.chat.Align;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.util.comparators.KDRComparator;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author phaed
 */
public class LeaderboardCommand extends GenericConsoleCommand {


    public LeaderboardCommand(SimpleClans plugin)
    {
        super("Leaderboard", plugin);
        this.plugin = plugin;
        setArgumentRange(0, 1);
        setUsages(MessageFormat.format(Language.getTranslation("usage.leaderboard"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("leaderboard.command"));
        setPermission("simpleclans.anyone.leaderboard");
    }

    @Override
    public String getMenu()
    {
        return MessageFormat.format(Language.getTranslation("menu.leaderboard"), plugin.getSettingsManager().getClanCommand());
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {

        ChatColor headColor = plugin.getSettingsManager().getHeaderPageColor();
        ChatColor subColor = plugin.getSettingsManager().getSubPageColor();
        NumberFormat formatter = new DecimalFormat("#.#");


        List<ClanPlayer> clanPlayers = new ArrayList<ClanPlayer>(plugin.getClanPlayerManager().getClanPlayers());

        int page = 0;
        int completeSize = clanPlayers.size();

        if (args.length == 1) {
            try {
                page = Integer.parseInt(args[0]) - 1;
            } catch (NumberFormatException e) {
                ChatBlock.sendMessage(sender, Language.getTranslation("number.format"));
                return;
            }
        }

        Collections.sort(clanPlayers, new KDRComparator());

        ChatBlock chatBlock = new ChatBlock();

        ChatBlock.sendBlank(sender);
        ChatBlock.sendSingle(sender, plugin.getSettingsManager().getServerName() + subColor + " " + Language.getTranslation("leaderboard.command"));
        ChatBlock.sendBlank(sender);
        ChatBlock.sendMessage(sender, headColor + MessageFormat.format(Language.getTranslation("total.clan.players.0"), subColor.toString() + clanPlayers.size()));
        ChatBlock.sendBlank(sender);

        chatBlock.setAlignment(Align.CENTER, Align.LEFT, Align.CENTER, Align.CENTER, Align.CENTER);
        chatBlock.addRow("  " + headColor + Language.getTranslation("rank"), Language.getTranslation("player"), Language.getTranslation("kdr"), Language.getTranslation("clan"), Language.getTranslation("seen"));

        int rank = 1;

        int[] boundings = getBoundings(completeSize, page);

        int end = boundings[1];

        for (int i = boundings[0]; i < end; i++) {
            ClanPlayer clanPlayer = clanPlayers.get(i);

            if (clanPlayer.isBanned()) {
                end++;
                continue;
            }

            String name = (clanPlayer.isLeader() ? plugin.getSettingsManager().getLeaderColor() : ((clanPlayer.isTrusted() ? plugin.getSettingsManager().getTrustedColor() : plugin.getSettingsManager().getUntrustedColor()))) + clanPlayer.getName();
            String lastSeen = (GeneralHelper.isOnline(clanPlayer.toPlayer()) ? ChatColor.GREEN + Language.getTranslation("online") : ChatColor.WHITE + clanPlayer.getLastSeen());

            String clanTag = ChatColor.WHITE + Language.getTranslation("free.agent");

            if (clanPlayer.getClan() != null) {
                clanTag = clanPlayer.getClan().getTag();
            }

            chatBlock.addRow("  " + rank, name, ChatColor.YELLOW.toString() + formatter.format(clanPlayer.getKDR()), clanTag, lastSeen);
            rank++;
        }


        chatBlock.sendBlock(sender);

        ChatBlock.sendBlank(sender);
    }
}
