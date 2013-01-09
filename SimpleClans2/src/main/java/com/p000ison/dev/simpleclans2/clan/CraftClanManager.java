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


package com.p000ison.dev.simpleclans2.clan;

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clan.ClanManager;
import com.p000ison.dev.simpleclans2.api.events.ClanCreateEvent;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * The ClanManager handels everything with clans.
 */
public class CraftClanManager implements ClanManager {
    private SimpleClans plugin;
    private Set<Clan> clans = new HashSet<Clan>();


    public CraftClanManager(SimpleClans plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public Set<Clan> getClans()
    {
        return Collections.unmodifiableSet(clans);
    }

    public Set<Clan> getModifyAbleClans()
    {
        return clans;
    }

    @Override
    public boolean removeClan(Clan clan)
    {
        return clans.remove(clan);
    }

    @Override
    public Clan getClan(String tag)
    {
        String lowerTag = ChatColor.stripColor(tag.toLowerCase(Locale.US));

        for (Clan clan : clans) {
            if (clan.getCleanTag().startsWith(lowerTag)) {
                return clan;
            }
        }
        return null;
    }

    @Override
    public Clan getClan(long id)
    {
        for (Clan clan : clans) {
            if (clan.getID() == id) {
                return clan;
            }
        }

        return null;
    }

    @Override
    public Clan createClan(Clan clan)
    {
        ClanCreateEvent event = new ClanCreateEvent(clan);
        plugin.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return null;
        }

        clan.updateLastAction();
        ((CraftClan) clan).setFoundedTime(System.currentTimeMillis());
        plugin.getDataManager().getDatabase().save((CraftClan) clan);
        clan.update();
        clans.add(clan);
        return clan;
    }

    @Override
    public Clan createClan(String tag, String name)
    {
        Clan clan = new CraftClan(plugin, tag, name);
        return createClan(clan);
    }

    @Override
    public Set<Clan> convertIdSetToClanSet(Set<Long> ids)
    {
        HashSet<Clan> allies = new HashSet<Clan>();

        for (long clanId : ids) {
            Clan ally = plugin.getClanManager().getClan(clanId);
            allies.add(ally);
        }

        return allies;
    }

    public void importClans(Set<CraftClan> clans)
    {
        this.clans.addAll(clans);
    }

    @Override
    public boolean existsClanByTag(String tag)
    {
        String cleanInputTag = ChatBlock.cleanString(tag);

        for (Clan clan : getClans()) {
            String clanTag = ChatBlock.cleanString(clan.getTag());
            if (clanTag.equalsIgnoreCase(cleanInputTag)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean existsClanByName(String name)
    {
        String cleanInputTag = ChatBlock.cleanString(name);
        for (Clan clan : getClans()) {
            String clanName = ChatBlock.cleanString(clan.getName());
            if (clanName.equalsIgnoreCase(cleanInputTag)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean existsClan(String tag, String name)
    {
        String cleanInputTag = ChatBlock.cleanString(tag);
        String cleanInputName = ChatBlock.cleanString(name);

        for (Clan clan : getClans()) {
            String clanTag = ChatBlock.cleanString(clan.getTag());
            String clanName = ChatBlock.cleanString(clan.getName());
            if (clanName.equalsIgnoreCase(cleanInputName) || clanTag.equalsIgnoreCase(cleanInputTag)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Clan getClanExact(String tag)
    {
        for (Clan clan : clans) {
            if (clan.getTag().equals(tag)) {
                return clan;
            }
        }
        return null;
    }

    @Override
    public int getRivalAbleClanCount()
    {
        int i = 0;
        for (Clan clan : clans) {
            if (!plugin.getSettingsManager().isUnRivalAble(clan)) {
                i++;
            }
        }

        return i;
    }

    @Override
    public boolean verifyClanTag(CommandSender reportTo, String tag, String tagBefore, boolean mod)
    {
        String cleanTag = ChatColor.stripColor(tag).toLowerCase(Locale.US);

        if (!mod) {
            if (cleanTag.length() > plugin.getSettingsManager().getMaxTagLenght()) {
                reportTo.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("your.clan.tag.cannot.be.longer.than.characters"), plugin.getSettingsManager().getMaxTagLenght()));
                return false;
            }

            if (cleanTag.length() < plugin.getSettingsManager().getMinTagLenght()) {
                reportTo.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("your.clan.tag.must.be.longer.than.characters"), plugin.getSettingsManager().getMinTagLenght()));
                return false;
            }

            if (GeneralHelper.containsColor(tag, '§', plugin.getSettingsManager().getDisallowedColors())) {
                reportTo.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("your.tag.cannot.contain.the.following.colors"), GeneralHelper.arrayToString(plugin.getSettingsManager().getDisallowedColors())));
                return false;
            }
            if (plugin.getSettingsManager().isTagDisallowed(cleanTag)) {
                reportTo.sendMessage(ChatColor.RED + Language.getTranslation("that.tag.name.is.disallowed"));
                return false;
            }
        }

        if (tagBefore != null) {
            if (!plugin.getSettingsManager().isModifyTagCompletely()) {
                if (!cleanTag.equalsIgnoreCase(ChatColor.stripColor(tagBefore))) {
                    reportTo.sendMessage(ChatColor.RED + Language.getTranslation("you.can.only.modify.the.color.and.case.of.the.tag"));
                    return false;
                }
            }
        }

        if (!cleanTag.matches("[0-9a-zA-Z]*")) {
            reportTo.sendMessage(ChatColor.RED + Language.getTranslation("your.clan.tag.can.only.contain.letters.numbers.and.color.codes"));
            return false;
        }

        return true;
    }

    @Override
    public boolean verifyClanName(CommandSender reportTo, String name, boolean mod)
    {
        if (!mod) {
            if (ChatColor.stripColor(name).length() > plugin.getSettingsManager().getMaxNameLenght()) {
                reportTo.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("your.clan.name.cannot.be.longer.than.characters"), plugin.getSettingsManager().getMaxTagLenght()));
                return false;
            }

            if (ChatColor.stripColor(name).length() < plugin.getSettingsManager().getMinNameLenght()) {
                reportTo.sendMessage(ChatColor.RED + MessageFormat.format(Language.getTranslation("your.clan.name.must.be.longer.than.characters"), plugin.getSettingsManager().getMinTagLenght()));
                return false;
            }
        }

        if (name.contains("§")) {
            reportTo.sendMessage(ChatColor.RED + Language.getTranslation("your.clan.name.cannot.contain.color.codes"));
            return false;
        }

        return true;
    }
}
