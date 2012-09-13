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
 *     Created: 02.09.12 18:33
 */


package com.p000ison.dev.simpleclans2.commands.general;

import com.p000ison.dev.simpleclans2.Language;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.util.ChatBlock;
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
 * Represents a ListCommand
 */
public class ListCommand extends GenericConsoleCommand {

    NumberFormat formatter = new DecimalFormat("#.#");

    public ListCommand(SimpleClans plugin)
    {
        super("List", plugin);
        setArgumentRange(0, 1);
        setUsages(MessageFormat.format(Language.getTranslation("usage.list"), "clan"));
        setIdentifiers(Language.getTranslation("list.command"));
        setPermission("simpleclans.anyone.list");
    }

    @Override
    public String getMenu()
    {
        return MessageFormat.format(Language.getTranslation("menu.list"), "clan");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args)
    {
        ChatColor headColor = plugin.getSettingsManager().getHeadingPageColor();
        ChatColor subColor = plugin.getSettingsManager().getSubPageColor();


        List<Clan> clans = new ArrayList<Clan>(plugin.getClanManager().getClans());

        if (clans.isEmpty()) {
            sender.sendMessage(ChatColor.RED + Language.getTranslation("no.clans.have.been.created"));
            return;
        }

        int page = 0;
        int completeSize = clans.size();

        if (args.length == 1) {
            try {
                page = Integer.parseInt(args[0]) - 1;
            } catch (NumberFormatException e) {
                sender.sendMessage(Language.getTranslation("number.format"));
                return;
            }
        }

        Collections.sort(clans, new KDRComparator());

        ChatBlock chatBlock = new ChatBlock();

        ChatBlock.sendBlank(sender);
        ChatBlock.saySingle(sender, plugin.getSettingsManager().getServerName() + subColor + " " + Language.getTranslation("clans.lower"));
        ChatBlock.sendBlank(sender);
        ChatBlock.sendMessage(sender, headColor + Language.getTranslation("total.clans") + " " + subColor + clans.size());
        ChatBlock.sendBlank(sender);

        chatBlock.setAlignment("c", "l", "c", "c");

        chatBlock.addRow(Language.getTranslation("rank"), Language.getTranslation("name"), Language.getTranslation("kdr"), Language.getTranslation("members"));

        int rank = 1;

        for (Clan clan : clans) {
            if (!plugin.getSettingsManager().isShowUnverifiedClansOnList()) {
                if (!clan.isVerified()) {
                    completeSize--;
                    continue;
                }
            }

            String tag = clan.getTag();
            String name = clan.getName();
            String fullName = tag + " " + name;
            String size = ChatColor.WHITE.toString() + clan.getSize();
            String kdr = clan.isVerified() ? ChatColor.YELLOW + "" + formatter.format(clan.getKDR()) : "";

            chatBlock.addRow("  " + rank, fullName, kdr, size);
            rank++;
        }

        int[] boundings = getBoundings(completeSize, page);

        chatBlock.sendBlock(sender, boundings[0], boundings[1]);

        ChatBlock.sendBlank(sender);
    }
}
