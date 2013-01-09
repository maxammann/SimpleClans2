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
 *     Last modified: 09.01.13 19:15
 */

package com.p000ison.dev.simpleclans2.api.clan;

import org.bukkit.command.CommandSender;

import java.util.Set;

/**
 *
 */
public interface ClanManager {
    /**
     * Returns a unmodifiable Set of all clans.
     *
     * @return A set of all clans
     * @see java.util.Set
     */
    Set<Clan> getClans();

    /**
     * Removes a clan from memory
     *
     * @param clan The clan
     * @return Weather it was successfully
     */
    boolean removeClan(Clan clan);

    /**
     * Gets a clan by tag or null if there is no such clan.
     * <p>This checks for the best result and returns this.</p>
     *
     * @param tag The tag to search
     * @return The clan.
     */
    Clan getClan(String tag);

    /**
     * Gets a clan by id or null if there is no such clan.
     *
     * @param id The id
     * @return The clan or null.
     */
    Clan getClan(long id);

    Clan createClan(Clan clan);

    Clan createClan(String tag, String name);

    Set<Clan> convertIdSetToClanSet(Set<Long> ids);

    boolean existsClanByTag(String tag);

    boolean existsClanByName(String name);

    boolean existsClan(String tag, String name);

    /**
     * Gets a clan exactly by tag or null if there is no such clan.
     *
     * @param tag The tag to get.
     * @return The clan of null.
     */
    Clan getClanExact(String tag);

    int getRivalAbleClanCount();

    /**
     * Verifies the change or the set of a clan
     *
     * @param reportTo  The sender to report the errors
     * @param tag       The tag to change
     * @param tagBefore If there was a tag before, else null
     * @return Weather this tag is valid
     */
    boolean verifyClanTag(CommandSender reportTo, String tag, String tagBefore, boolean mod);

    boolean verifyClanName(CommandSender reportTo, String name, boolean mod);
}
