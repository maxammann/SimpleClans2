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

package com.p000ison.dev.simpleclans2.clan.ranks;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.rank.RankManager;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * Represents a CraftRankManager
 */
public class CraftRankManager implements RankManager {
    private SimpleClans plugin;

    public CraftRankManager(SimpleClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public CraftRank createRank(Clan clan, String name, String tag, int priority) {
        CraftRank rank = new CraftRank(name, tag, priority, clan.getID());

        plugin.getDataManager().getDatabase().save(rank);
        return rank;
    }

    public boolean verifyRankTag(CommandSender reportTo, String tag) {
        String cleanTag = ChatColor.stripColor(tag).toLowerCase(Locale.US);

        if (cleanTag.length() > plugin.getSettingsManager().getMaxRankTagLength()) {
            reportTo.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("your.rank.tag.cannot.be.longer.than.characters"), plugin.getSettingsManager().getMaxRankTagLength()));
            return false;
        }

        if (cleanTag.length() < plugin.getSettingsManager().getMinRankTagLength()) {
            reportTo.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("your.rank.tag.must.be.longer.than.characters"), plugin.getSettingsManager().getMinRankTagLength()));
            return false;
        }

        if (GeneralHelper.containsColor(tag, '§', plugin.getSettingsManager().getDisallowedColors())) {
            reportTo.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("your.tag.cannot.contain.the.following.colors"), GeneralHelper.arrayToString(plugin.getSettingsManager().getDisallowedColors())));
            return false;
        }

        if (plugin.getSettingsManager().isRankTagDisallowed(cleanTag)) {
            reportTo.sendMessage(ChatColor.RED + Language.getTranslation("that.tag.name.is.disallowed"));
            return false;
        }

        if (!cleanTag.matches("[0-9a-zA-Z§]*")) {
            reportTo.sendMessage(ChatColor.RED + Language.getTranslation("your.rank.tag.can.only.contain.letters.numbers.and.color.codes"));
            return false;
        }

        return true;
    }

    public boolean verifyRankName(CommandSender reportTo, String name) {
        if (ChatColor.stripColor(name).length() > plugin.getSettingsManager().getMaxRankNameLength()) {
            reportTo.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("your.rank.name.cannot.be.longer.than.characters"), plugin.getSettingsManager().getMaxRankNameLength()));
            return false;
        }

        if (ChatColor.stripColor(name).length() < plugin.getSettingsManager().getMinRankNameLength()) {
            reportTo.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("your.rank.name.must.be.longer.than.characters"), plugin.getSettingsManager().getMinRankNameLength()));
            return false;
        }

        if (name.contains("§")) {
            reportTo.sendMessage(ChatColor.RED + Language.getTranslation("your.rank.name.cannot.contain.color.codes"));
            return false;
        }

        return true;
    }
}
