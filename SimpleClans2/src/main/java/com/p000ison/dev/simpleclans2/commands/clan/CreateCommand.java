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
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.clan.CraftClan;
import com.p000ison.dev.simpleclans2.commands.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.database.response.responses.ClanCreateResponse;
import com.p000ison.dev.simpleclans2.language.Language;
import com.p000ison.dev.simpleclans2.util.GeneralHelper;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * Represents a CreateCommand
 */
public class CreateCommand extends GenericPlayerCommand {

    public CreateCommand(SimpleClans plugin) {
        super("CreateCommand", plugin);
        setArgumentRange(2, 20);
        setUsages(Language.getTranslation("usage.create"), Language.getTranslation("example.clan.create"));
        setIdentifiers(Language.getTranslation("create.command"));
        setPermission("simpleclans.leader.create");
    }

    @Override
    public String getMenu(ClanPlayer cp) {
        if (cp == null) {
            if (plugin.getSettingsManager().isPurchaseCreation()) {
                return Language.getTranslation("menu.create.purchase");
            } else {
                return Language.getTranslation("menu.create.default");
            }
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args) {
        ClanPlayer cp = plugin.getClanPlayerManager().getCreateClanPlayerExact(player);

        if (cp.getClan() != null) {
            ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(Language.getTranslation("you.must.first.resign"), cp.getClan().getName()));
            return;
        }

        String tag = ChatBlock.parseColors(args[0]);
        String name = ChatBlock.parseColors(GeneralHelper.arrayBoundsToString(1, args));
        boolean bypass = player.hasPermission("simpleclans.mod.bypass");

        if (!plugin.getClanManager().verifyClanTag(player, tag, null, bypass)) {
            return;
        }

        if (!plugin.getClanManager().verifyClanName(player, name, bypass)) {
            return;
        }

        if (plugin.getClanManager().existsClan(tag, name)) {
            player.sendMessage(ChatColor.RED + Language.getTranslation("clan.already.exists"));
            return;
        }

        if (SimpleClans.hasEconomy() && plugin.getSettingsManager().isPurchaseCreation() && !cp.withdraw(plugin.getSettingsManager().getPurchaseCreationPrice())) {
            ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("not.sufficient.money"));
            return;
        }

        Clan clan = new CraftClan(plugin, tag, name);

        plugin.getDataManager().addResponse(new ClanCreateResponse(plugin, clan, player, cp));

        cp.setLeader(true);
        cp.setTrusted(true);
        clan.addMember(cp);

        cp.update();

        if (plugin.getSettingsManager().requireVerification()) {
            boolean verified = player.hasPermission("simpleclans.mod.verify");

            if (!verified) {
                ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("get.your.clan.verified.to.access.advanced.features"));
            }

            clan.setVerified(verified);

        } else {
            clan.setVerified(true);
        }

        clan.update();

    }
}