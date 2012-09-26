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
 *     Created: 02.09.12 18:33
 */


package com.p000ison.dev.simpleclans2.requests;

import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Represents a RequestManager
 */
public class RequestManager {
    public Set<Request> requests = new HashSet<Request>();

    public boolean createRequest(Request created)
    {

        //hmm damn you DAUs ....

        if (created instanceof MultipleAcceptorsRequest) {
            MultipleAcceptorsRequest multiCreated = (MultipleAcceptorsRequest) created;

            //go through all requests
            for (Request request : requests) {
                for (ClanPlayer clanPlayer : multiCreated.getAcceptors()) {
                    if (request.hasRequestToHandle(clanPlayer)) {
                        return false;
                    }
                }
            }
        } else if (created instanceof SingleAcceptorRequest) {
            SingleAcceptorRequest singleCreated = (SingleAcceptorRequest) created;

            //go through all requests
            for (Request request : requests) {

                if (request.hasRequestToHandle(singleCreated.getAcceptor())) {
                    return false;
                }
            }
        }


        requests.add(created);
        created.sendRequest();
        return true;
    }

    public Request vote(ClanPlayer acceptor, VoteResult result)
    {

        Iterator<Request> it = requests.iterator();
        while (it.hasNext()) {
            Request request = it.next();
            System.out.println(request.getRequester().getName());
            System.out.println(((SingleAcceptorRequest)request).getAcceptor().getName()); ;
            if (request.isAcceptor(acceptor)) {

                if (result == VoteResult.ACCEPT) {
                    request.accept(acceptor);
                } else if (result == VoteResult.DENY) {
                    request.deny(acceptor);
                } else if (result == VoteResult.ABSTAINED) {
                    request.deny(acceptor);
                } else {
                    System.out.println(1);
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

                System.out.println(2);
                return request;
            }
        }

        System.out.println(3);
        return null;
    }

    public void clearRequests(String player)
    {
        Iterator<Request> it = requests.iterator();

        while (it.hasNext()) {
            Request request = it.next();

            if (request.getRequester().getName().equals(player)) {
                it.remove();
            }

            if (request instanceof MultipleAcceptorsRequest) {
                for (ClanPlayer clanPlayer : ((MultipleAcceptorsRequest) request).getAcceptors()) {
                    if (clanPlayer.getName().equals(player)) {
                        it.remove();
                    }
                }
            } else {
                if (((SingleAcceptorRequest) request).getAcceptor().getName().equals(player)) {
                    it.remove();
                }
            }
        }
    }
}
