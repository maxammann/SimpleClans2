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
 *     Last modified: 13.10.12 19:03
 */

package com.p000ison.dev.simpleclans2.commands.clan;

import com.p000ison.dev.commandlib.CallInformation;
import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Represents a ModifyTagCommand
 */
public class ModifyTagCommand extends GenericPlayerCommand {

    public ModifyTagCommand(SimpleClans plugin) {
        super("Modify tag", plugin);
        addArgument(Language.getTranslation("argument.tag"));
        setDescription(Language.getTranslation("description.modtag"));
        setIdentifiers(Language.getTranslation("modtag.command"));
        addPermission("simpleclans.leader.modtag");

        setNeedsClan();
        setNeedsLeader();
        setNeedsClanVerified();
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
        Clan clan = cp.getClan();

        String newTag = ChatBlock.parseColors(arguments[0]);
        String tagBefore = clan.getTag();
        boolean bypass = player.hasPermission("simpleclans.mod.bypass");

        if (!getPlugin().getClanManager().verifyClanTag(player, newTag, tagBefore, bypass)) {
            return;
        }

        if (getPlugin().getSettingsManager().isModifyTagCompletely()) {
            if (getPlugin().getClanManager().existsClanByTag(newTag)) {
                player.sendMessage(ChatColor.RED + Language.getTranslation("clan.with.this.tag.already.exists"));
                return;
            }
        }

        clan.addBBMessage(cp, MessageFormat.format(Language.getTranslation("tag.changed.to.0"), newTag));
        clan.setTag(newTag);
        clan.update();
    }
}