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

import com.p000ison.dev.commandlib.CallInformation;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.Align;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.commands.GenericConsoleCommand;
import com.p000ison.dev.simpleclans2.language.Language;
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

    private NumberFormat formatter = new DecimalFormat("#.#");

    public ListCommand(SimpleClans plugin) {
        super("List", plugin);
        addArgument(Language.getTranslation("argument.page"), true, true);
        setDescription(MessageFormat.format(Language.getTranslation("description.list"), "clan"));
        setIdentifiers(Language.getTranslation("list.command"));
        addPermission("simpleclans.anyone.list");
    }

    @Override
    public void execute(CommandSender sender, String[] arguments, CallInformation info) {
        ChatColor headColor = getPlugin().getSettingsManager().getHeaderPageColor();
        ChatColor subColor = getPlugin().getSettingsManager().getSubPageColor();

        List<Clan> clans = new ArrayList<Clan>(getPlugin().getClanManager().getClans());

        if (clans.isEmpty()) {
            ChatBlock.sendMessage(sender, ChatColor.RED + Language.getTranslation("no.clans.have.been.created"));
            return;
        }

        int completeSize = clans.size();

        Collections.sort(clans, new KDRComparator());

        ChatBlock chatBlock = new ChatBlock();

        ChatBlock.sendBlank(sender);
        ChatBlock.sendMessage(sender, headColor + Language.getTranslation("total.clans") + " " + subColor + clans.size());
        ChatBlock.sendBlank(sender);

        chatBlock.setAlignment(Align.CENTER, Align.LEFT, Align.LEFT, Align.CENTER, Align.CENTER);

        chatBlock.addRow(Language.getTranslation("rank"), Language.getTranslation("tag"), Language.getTranslation("name"), Language.getTranslation("kdr"), Language.getTranslation("members"));

        int rank = 1;

        int page = info.getPage(completeSize);
        int start = info.getStartIndex(page, completeSize);
        int end = info.getEndIndex(page, completeSize);

        for (int i = start; i < end; i++) {
            Clan clan = clans.get(i);
            if (!getPlugin().getSettingsManager().isShowUnverifiedClansOnList() && !clan.isVerified()) {
                completeSize--;
                continue;
            }

            String tag = clan.getTag();
            String name = clan.getName();
            String size = ChatColor.WHITE.toString() + clan.getSize();
            String kdr = clan.isVerified() ? ChatColor.YELLOW + "" + formatter.format(clan.getKDR()) : "";

            chatBlock.addRow(String.valueOf(rank), tag, name, kdr, size);

            rank++;
        }

        chatBlock.sendBlock(sender);

        ChatBlock.sendBlank(sender);
    }
}
