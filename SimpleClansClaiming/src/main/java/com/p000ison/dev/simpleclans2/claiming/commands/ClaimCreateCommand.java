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

import com.p000ison.dev.commandlib.CallInformation;
import com.p000ison.dev.simpleclans2.api.SCCore;
import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.claiming.ClaimLocation;
import com.p000ison.dev.simpleclans2.claiming.SCClaimingLanguage;
import com.p000ison.dev.simpleclans2.claiming.data.ClaimingManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Represents a ClaimCreateCommand
 */
public class ClaimCreateCommand extends GenericPlayerCommand {

    private final ClaimingManager manager;

    public ClaimCreateCommand(SCCore sccore, ClaimingManager manager) {
        super("ClaimCreate", sccore);
        this.manager = manager;
        //setArgumentRange(1, 1);
        setDescription(SCClaimingLanguage.getTranslation("usage.claim.create"));
        setIdentifiers(SCClaimingLanguage.getTranslation("claim.create.command"));
        addPermission("simpleclans.leader.claim.create");

        setNeedsClan();
    }

    @Override
    public void execute(Player player, ClanPlayer cp, String[] arguments, CallInformation info) {
        Clan clan = cp.getClan();
        Location location = player.getLocation();
        ClaimLocation claimLocation = manager.toClaimLocation(location);

        int claims = manager.getClaimAmount(clan);
        boolean surrounding = manager.isSurrounding(claimLocation, clan);

        if (claims != 0 && !surrounding) {
            player.sendMessage(SCClaimingLanguage.getTranslation("surrounding.chunks"));
            return;
        }

        manager.claimArea(clan, claimLocation);
        player.sendMessage(SCClaimingLanguage.getTranslation("chunk.claimed"));
    }
}