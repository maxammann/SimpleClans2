/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Created: 02.09.12 18:29
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

    public boolean createRequest(Request create)
    {

        //hmm damn you DAUs ....
        for (Request request : requests) {

            if (request.getRequester().equals(create.getRequester())) {
                return false;
            }

            for (ClanPlayer clanPlayer : request.getAcceptors()) {
                for (ClanPlayer acceptor : create.getAcceptors()) {
                    if (acceptor.equals(clanPlayer)) {
                        return false;
                    }
                }
            }
        }

        requests.add(create);
        sendRequest(create);
        return true;
    }

    public boolean vote(ClanPlayer acceptor, VoteResult result)
    {
        Iterator<Request> it = requests.iterator();
        while (it.hasNext()) {
            Request request = it.next();
            if (request.getAcceptors().contains(acceptor)) {
                if (acceptor.getLastVoteResult() == VoteResult.UNKNOWN) {
                    if (result == VoteResult.DENY) {
                        //cancel request
                        cancelRequest(request);
                        it.remove();
                    } else {
                        acceptor.setLastVoteResult(result);
                    }

                    if (checkRequest(request)) {
                        //if everyone has voted
                        processRequest(request);
                        it.remove();
                    }

                    return true;
                }
            }
        }

        return false;
    }

    public void clearRequests(String player)
    {
        Iterator<Request> it = requests.iterator();

        while (it.hasNext()) {
            Request request = it.next();

            if (request.getRequester().equals(player)) {
                it.remove();
            }

            for (ClanPlayer clanPlayer : request.getAcceptors()) {
                if (clanPlayer.getName().equals(player)) {
                    it.remove();
                }
            }
        }
    }

    public void processRequest(Request request)
    {
        for (ClanPlayer acceptor : request.getAcceptors()) {
            acceptor.toPlayer().sendMessage("accepted");
            acceptor.setLastVoteResult(VoteResult.UNKNOWN);
        }

        request.runGoal();
        request.getRequester().toPlayer().sendMessage("accept");
    }

    public void cancelRequest(Request request)
    {

        for (ClanPlayer acceptor : request.getAcceptors()) {
            acceptor.toPlayer().sendMessage("Cancelled");
            acceptor.setLastVoteResult(VoteResult.UNKNOWN);
        }

        request.getRequester().toPlayer().sendMessage("Cancelledbo");
    }

    public boolean checkRequest(Request request)
    {
        for (ClanPlayer acceptor : request.getAcceptors()) {
            if (acceptor.getLastVoteResult() == VoteResult.UNKNOWN) {
                return false;
            }
        }

        return true;
    }

    public void sendRequest(Request request)
    {
        for (ClanPlayer clanPlayer : request.getAcceptors()) {

            if (clanPlayer == null) {
                continue;
            }

            clanPlayer.toPlayer().sendMessage(request.getMessage());
        }
    }
}
