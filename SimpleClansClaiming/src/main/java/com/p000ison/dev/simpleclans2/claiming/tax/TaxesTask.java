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
 *     Last modified: 27.02.13 17:11
 */

package com.p000ison.dev.simpleclans2.claiming.tax;

import com.p000ison.dev.simpleclans2.api.clan.Clan;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.claiming.data.ClaimingManager;

/**
 * Represents a TaxesTask
 */
public class TaxesTask implements Runnable {

    private final ClaimingManager manager;
    private final long interval;
    private long time;

    public TaxesTask(ClaimingManager manager) {
        this.manager = manager;

        interval = manager.getSettingsManager().getTaxesInterval();
    }

    @Override
    public void run() {

        time++;

        if (time >= interval) {
            time = 0;

            for (Clan clan : manager.getCore().getClanManager().getClans()) {
                double taxesPerMember = ClaimingManager.getTaxesPerMember(clan);

                if (taxesPerMember > 0.0D) {

                    collectTaxes(clan, taxesPerMember);
                }
            }
        }
    }

    public double collectTaxes(final Clan clan, final double taxes) {
        int timesCollected = 0;

        StringBuilder notPayed = new StringBuilder();

        for (ClanPlayer cp : clan.getMembers()) {

            boolean success = cp.withdraw(taxes);

            if (!success) {
                //not enough money

                notPayed.append(cp.getName()).append(" ,");

                if (ClaimingManager.isKickPlayers(clan)) {
                    cp.getClan().removeMember(cp);
                }
            } else {
                timesCollected++;
            }
        }

        notPayed.delete(notPayed.length() - 2, notPayed.length());

        clan.addBBMessage(notPayed.toString());

        return taxes * timesCollected;
    }
}
