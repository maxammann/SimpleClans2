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
 *     Last modified: 08.02.13 17:12
 */

package com.p000ison.dev.simpleclans2.rewards;

import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.api.events.ClanPlayerKillEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Represents a SimpleClansListener
 */
@SuppressWarnings("unused")
public class SimpleClansListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onClanPlayerKill(ClanPlayerKillEvent event) {
        ClanPlayer attacker = event.getClanPlayer();
        ClanPlayer victim = event.getVictim();

        if (attacker.getClan() == null || victim.getClan() == null) {
            return;
        }

        double reward = 0;
        float kdr = attacker.getKDR();
        double multiplier = 2;

        switch (event.getType()) {
            case CIVILIAN:
                break;
            case NEUTRAL:
                if (attacker.getClan().isAlly(victim.getClan())) {
                    reward = kdr * multiplier * -1;
                } else {
                    reward = kdr * multiplier;
                }
                break;
            case RIVAL:
                if (attacker.getClan().isWarring(victim.getClan())) {
                    reward = kdr * multiplier * 4;
                } else {
                    reward = kdr * multiplier * 2;
                }
                break;
        }

        if (reward > 0) {
            attacker.deposit(reward);
        } else if (reward < 0) {
            attacker.withdraw(Math.abs(reward));
        }
    }
}
