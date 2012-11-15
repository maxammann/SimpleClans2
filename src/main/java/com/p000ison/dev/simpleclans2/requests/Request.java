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
 *     Last modified: 26.10.12 16:19
 */


package com.p000ison.dev.simpleclans2.requests;

import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;

/**
 * Represents a AbstractRequest
 */
public interface Request {

    /**
     * Gets the requester of this request. The person who started the request.
     *
     * @return The requester
     */
    ClanPlayer getRequester();

    /**
     * Returns the date when this request was created
     *
     * @return The date when this request was created
     */
    long getCreatedDate();

    /**
     * Checks if a clan is involved in this request.
     *
     * @param clan The clan to check.
     * @return Checks if a clan is involved in this request.
     */
    boolean isClanInvolved(Clan clan);

    /**
     * Checks if a clanplayer is involved in this request.
     *
     * @param clanPlayer The player to check.
     * @return Checks if a player is involved in this request.
     */
    boolean isClanPlayerInvolved(ClanPlayer clanPlayer);

    /**
     * Performs a vote on this request
     */
    void accept();

    /**
     * Performs a vote on this request
     */
    void deny();

    /**
     * Performs a vote on this request. This will only work with a {@link MultipleRequest}. Not with a {@link SingleRequest}.
     */
    void abstain();

    /**
     * Checks if every one has voted.
     *
     * @return If everyone has voted.
     */
    boolean hasEveryoneVoted();

    /**
     * Checks if this request can be processed.
     *
     * @return Weather this can be processed.
     */
    boolean checkRequest();

    /**
     * This asks for the request. This message is only sent once.
     */
    void onRequesting();

    /**
     * Checks if the clanplayer is an acceptor
     *
     * @param clanPlayer The acceptor
     * @return Weather the clanplayer is an acceptor
     */
    boolean isAcceptor(ClanPlayer clanPlayer);

    /**
     * Announces a message to all players, who are involved.
     *
     * @param message The message to send
     */
    void announceMessage(String message);

    /**
     * Sends a message to the requester
     */
    void sendRequesterMessage(String message);

    /**
     * Sends a message to the announcer/s
     */
    void sendAnnouncerMessage(String message);

    /**
     * Called whenever this request is finally accepted
     *
     * @return Weather it was successfully
     */
    boolean onAccepted();

    /**
     * Called whenever this request is finally denied
     */
    void onDenied();

    boolean isRequester(ClanPlayer clanPlayer);

    int getTimesVoted();

    int getAcceptorsSize();
}
