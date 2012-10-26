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


package com.p000ison.dev.simpleclans2.requests;

import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Represents a RequestManager
 */
public class RequestManager {
    public Set<AbstractRequest> requests = new HashSet<AbstractRequest>();

    public boolean createRequest(AbstractRequest created)
    {

        //hmm damn you DAUs ....

        if (created instanceof MultipleAcceptorsRequest) {
            MultipleAcceptorsRequest multiCreated = (MultipleAcceptorsRequest) created;

            //go through all requests
            for (AbstractRequest request : requests) {
                for (ClanPlayer clanPlayer : multiCreated.getAcceptors()) {
                    if (request.isClanPlayerInvolved(clanPlayer)) {
                        return false;
                    }
                }
            }
        } else if (created instanceof SingleAcceptorRequest) {
            SingleAcceptorRequest singleCreated = (SingleAcceptorRequest) created;

            //go through all requests
            for (AbstractRequest request : requests) {

                if (request.isClanPlayerInvolved(singleCreated.getAcceptor())) {
                    return false;
                }
            }
        }


        requests.add(created);
        created.sendRequest();
        return true;
    }

    public AbstractRequest vote(ClanPlayer acceptor, VoteResult result)
    {

        Iterator<AbstractRequest> it = requests.iterator();
        while (it.hasNext()) {
            AbstractRequest request = it.next();

            if (request.isAcceptor(acceptor)) {

                switch (result) {
                    case ACCEPT:
                        request.accept(acceptor);
                        break;
                    case DENY:
                        request.deny(acceptor);
                        break;
                    case ABSTAINED:
                        if (request instanceof SingleAcceptorRequest) {
                            return request;
                        }
                        request.abstain(acceptor);
                        break;
                    default:
                        return null;
                }

                //check if we were successfully
                if (request.checkRequest()) {
                    //if everyone has voted
                    request.processRequest();
                    it.remove();
                } else {
                    //check if everyone has voted if yes and no success -> remove
                    if (request.hasEveryoneVoted()) {
                        request.cancelRequest();
                        it.remove();
                    }
                }

                return request;
            }
        }

        return null;
    }

    public void clearRequests(Clan clan)
    {
        Iterator<AbstractRequest> it = requests.iterator();

        while (it.hasNext()) {
            AbstractRequest request = it.next();
//            request.get
        }
    }

    public int getRequests()
    {
        return requests.size();
    }

    public void clearRequests(String player)
    {
        Iterator<AbstractRequest> it = requests.iterator();

        while (it.hasNext()) {
            AbstractRequest request = it.next();

            if (request.getRequester().getName().equals(player)) {
                it.remove();
                //we cancel here because a player can be only once a requester of a acceptor
                return;
            }

            if (request instanceof MultipleAcceptorsRequest) {
                for (ClanPlayer clanPlayer : ((MultipleAcceptorsRequest) request).getAcceptors()) {
                    if (clanPlayer.getName().equals(player)) {
                        it.remove();
                        //we cancel here because a player can be only once a requester of a acceptor
                        return;
                    }
                }
            } else {
                if (((SingleAcceptorRequest) request).getAcceptor().getName().equals(player)) {
                    it.remove();
                    //we cancel here because a player can be only once a requester of a acceptor
                    return;
                }
            }
        }
    }
}
