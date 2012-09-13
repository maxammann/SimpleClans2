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
 *     Created: 05.09.12 01:00
 */

package com.p000ison.dev.simpleclans2.commands.general;

import com.p000ison.dev.simpleclans2.Language;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.util.ChatBlock;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import com.p000ison.dev.simpleclans2.util.comparators.KDRComparator;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Represents a AlliancesCommand
 */
public class AlliancesCommand extends GenericConsoleCommand {


    public AlliancesCommand(SimpleClans plugin)
    {
        super("Alliances", plugin);
        setArgumentRange(0, 1);
        setUsages(MessageFormat.format(Language.getTranslation("usage.alliances"), plugin.getSettingsManager().getClanCommand()));
        setIdentifiers(Language.getTranslation("alliances.command"));
        setPermission("simpleclans.anyone.alliances");
    }

    @Override
    public String getMenu()
    {
        return MessageFormat.format(Language.getTranslation("menu.alliances"), plugin.getSettingsManager().getClanCommand());
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
        ChatBlock.saySingle(sender, plugin.getSettingsManager().getServerName() + subColor + " " + Language.getTranslation("alliances"));
        ChatBlock.sendBlank(sender);

        chatBlock.setAlignment("l", "l");
        chatBlock.addRow(headColor + Language.getTranslation("clan"), Language.getTranslation("allies"));

        for (Clan clan : clans) {
            if (!clan.isVerified()) {
                completeSize--;
                continue;
            }

            Set<Clan> allies = clan.getAllies();

            String alliesString;

            if (allies == null || allies.isEmpty()) {
                alliesString = subColor + "None";
            } else {
                alliesString = GeneralHelper.clansToString(clan.getAllies(), ChatColor.DARK_GRAY + " + ");
            }

            chatBlock.addRow("  " + ChatColor.AQUA + clan.getTag(), alliesString);
        }

        int[] boundings = getBoundings(completeSize, page);


        chatBlock.sendBlock(sender, boundings[0], boundings[1]);

        ChatBlock.sendBlank(sender);
    }
}
