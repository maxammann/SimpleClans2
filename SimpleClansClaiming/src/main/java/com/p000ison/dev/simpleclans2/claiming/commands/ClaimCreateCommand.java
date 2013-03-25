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
 *     Last modified: 16.03.13 17:07
 */

package com.p000ison.dev.simpleclans2.claiming.commands;

import com.p000ison.dev.simpleclans2.api.SCCore;
import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.api.command.GenericPlayerCommand;
import com.p000ison.dev.simpleclans2.claiming.ClaimLocation;
import com.p000ison.dev.simpleclans2.claiming.SCClaimingLanguage;
import com.p000ison.dev.simpleclans2.claiming.data.ClaimingManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Represents a ClaimCreateCommand
 */
public class ClaimCreateCommand extends GenericPlayerCommand {

    private final SCCore sccore;
    private final ClaimingManager manager;

    public ClaimCreateCommand(SCCore sccore, ClaimingManager manager) {
        super("ClaimCreate");
        this.sccore = sccore;
        this.manager = manager;
        setArgumentRange(1, 1);
        setUsages(SCClaimingLanguage.getTranslation("usage.claim.create"));
        setIdentifiers(SCClaimingLanguage.getTranslation("claim.create.command"));
        setPermission("simpleclans.leader.claim.create");
    }

    @Override
    public String getMenu(ClanPlayer cp) {
        if (cp != null) {
            return SCClaimingLanguage.getTranslation("menu.claim.create");
        }
        return null;
    }

    @Override
    public void execute(Player player, String[] args) {
        ClanPlayer clanPlayer = sccore.getClanPlayerManager().getClanPlayer(player);

        if (clanPlayer == null) {
            ChatBlock.sendMessage(player, ChatColor.RED + sccore.getTranslation("not.a.member.of.any.clan"));
            return;
        }

        Clan clan = clanPlayer.getClan();
        Location location = player.getLocation();
        ClaimLocation claimLocation = manager.toClaimLocation(location);

        int claims = manager.getClaimAmount(clan);
        boolean surrounding = manager.isSurrounding(claimLocation, clan);

        if (claims != 0 && !surrounding) {
            player.sendMessage("failed");
            return;
        }

        manager.claimArea(clan, claimLocation);
        player.sendMessage("claimed");
    }
}