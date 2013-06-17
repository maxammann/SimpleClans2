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

import com.p000ison.dev.commandlib.CallInformation;
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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Represents a CreateCommand
 */
public class CreateCommand extends GenericPlayerCommand {

    public CreateCommand(SimpleClans plugin) {
        super("Create", plugin);
        addArgument(Language.getTranslation("argument.tag")).addArgument(Language.getTranslation("argument.name"));
        setInfinite(true);
        setDescription(Language.getTranslation("description.create"));
        setIdentifiers(Language.getTranslation("create.command"));
        addPermission("simpleclans.leader.create");
    }

    @Override
    protected boolean displayHelpEntry(ClanPlayer cp, CommandSender sender) {
        return getPlugin().getSettingsManager().isPurchaseCreation();
    }

    @Override
    public void execute(Player player, ClanPlayer ignored, String[] arguments, CallInformation info) {
        ClanPlayer cp = getPlugin().getClanPlayerManager().getCreateClanPlayerExact(player);

        String tag = ChatBlock.parseColors(arguments[0]);
        String name = ChatBlock.parseColors(GeneralHelper.arrayBoundsToString(1, arguments));
        boolean bypass = player.hasPermission("simpleclans.mod.bypass");

        if (!getPlugin().getClanManager().verifyClanTag(player, tag, null, bypass)) {
            return;
        }

        if (!getPlugin().getClanManager().verifyClanName(player, name, bypass)) {
            return;
        }

        if (getPlugin().getClanManager().existsClan(tag, name)) {
            player.sendMessage(ChatColor.RED + Language.getTranslation("clan.already.exists"));
            return;
        }

        if (SimpleClans.hasEconomy() && getPlugin().getSettingsManager().isPurchaseCreation() && !cp.withdraw(getPlugin().getSettingsManager().getPurchaseCreationPrice())) {
            ChatBlock.sendMessage(player, ChatColor.AQUA + Language.getTranslation("not.sufficient.money"));
            return;
        }

        Clan clan = new CraftClan(getPlugin(), tag, name);

        getPlugin().getDataManager().addResponse(new ClanCreateResponse(getPlugin(), clan, player, cp));

        cp.setLeader(true);
        cp.setTrusted(true);
        clan.addMember(cp);

        cp.update();

        if (getPlugin().getSettingsManager().requireVerification()) {
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