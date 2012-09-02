/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Created: 02.09.12 18:29
 */


package com.p000ison.dev.simpleclans2.commands.commands;

import com.p000ison.dev.simpleclans2.Language;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
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

    private SimpleClans plugin;
    NumberFormat formatter = new DecimalFormat("#.#");

    public ListCommand(SimpleClans plugin)
    {
        super("List");
        this.plugin = plugin;
        setArgumentRange(0, 0);
        setUsages(MessageFormat.format(Language.getTranslation("usage.list"), "clan"));
        setIdentifiers(Language.getTranslation("list.command"));
    }

    @Override
    public String getMenu(ClanPlayer cp, CommandSender sender)
    {
        if (sender.hasPermission("simpleclans.anyone.list")) {
            return MessageFormat.format(Language.getTranslation("menu.list"), "clan");
        }
        return null;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args)
    {
//        String headColor = plugin.getSettingsManager().getPageHeadingsColor();
//        String subColor = plugin.getSettingsManager().getPageSubTitleColor();


        if (sender.hasPermission("simpleclans.anyone.list")) {

            List<Clan> clans = new ArrayList<Clan>(plugin.getClanManager().getClans());


            if (clans.isEmpty()) {
                sender.sendMessage(ChatColor.RED + Language.getTranslation("no.clans.have.been.created"));
                return;
            }

            Collections.sort(clans, new KDRComparator());

            ChatBlock chatBlock = new ChatBlock();

            ChatBlock.sendBlank(sender);
//            ChatBlock.saySingle(sender, plugin.getSettingsManager().getServerName() + subColor + " " + plugin.getLang("clans.lower") + " " + headColor + Helper.generatePageSeparator(plugin.getSettingsManager().getPageSep()));
            ChatBlock.sendBlank(sender);
//            ChatBlock.sendMessage(sender, headColor + plugin.getLang("total.clans") + " " + subColor + clans.size());
            ChatBlock.sendBlank(sender);

            chatBlock.setAlignment("c", "l", "c", "c");

            chatBlock.addRow(Language.getTranslation("rank"), Language.getTranslation("name"), Language.getTranslation("kdr"), Language.getTranslation("members"));

            int rank = 1;

            for (Clan clan : clans) {
//                if (!plugin.getSettingsManager().isShowUnverifiedOnList()) {
//                    if (!clan.isVerified()) {
//                        continue;
//                    }
//                }

                String tag = clan.getTag();
                String name =  clan.getName();
                String fullname = tag + " " + name;
                String size = ChatColor.WHITE + "" + clan.getSize();
                String kdr = clan.isVerified() ? ChatColor.YELLOW + "" + formatter.format(clan.getTotalKDR()) : "";

                chatBlock.addRow("  " + rank, fullname, kdr, size);
                rank++;
            }


//            boolean more = chatBlock.sendBlock(sender, plugin.getSettingsManager().getPageSize());
//
//            if (more) {
//                plugin.getStorageManager().addChatBlock(sender, chatBlock);
//                ChatBlock.sendBlank(sender);
//                ChatBlock.sendMessage(sender, headColor + MessageFormat.format(plugin.getLang("view.next.page"), plugin.getSettingsManager().getCommandMore()));
//            }

            ChatBlock.sendBlank(sender);

        } else {
            ChatBlock.sendMessage(sender, ChatColor.RED + Language.getTranslation("insufficient.permissions"));
        }
    }

}
