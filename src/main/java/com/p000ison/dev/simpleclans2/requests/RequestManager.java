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

import com.p000ison.dev.simpleclans2.SimpleClans;
import com.p000ison.dev.simpleclans2.clan.Clan;
import com.p000ison.dev.simpleclans2.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.language.Language;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Represents a RequestManager
 */
public class RequestManager {
    public Set<AbstractRequest> requests = new HashSet<AbstractRequest>();
    private SimpleClans plugin;

    public RequestManager(SimpleClans plugin)
    {
        this.plugin = plugin;
    }

    public boolean createRequest(AbstractRequest created)
    {
        //hmm damn you DAUs ....

        //todo add messages if the player is already involved in a request
        if (created instanceof MultipleRequest) {
            MultipleRequest multiCreated = (MultipleRequest) created;

            //go through all requests
            for (AbstractRequest request : requests) {
                for (ClanPlayer clanPlayer : multiCreated.getAcceptors()) {
                    if (request.isClanPlayerInvolved(clanPlayer)) {
                        return false;
                    }
                }
            }
        } else if (created instanceof SingleRequest) {
            SingleRequest singleCreated = (SingleRequest) created;

            //go through all requests
            for (AbstractRequest request : requests) {

                if (request.isClanPlayerInvolved(singleCreated.getAcceptor())) {
                    return false;
                }
            }
        }

        requests.add(created);
        created.onRequesting();
        return true;
    }

    public AbstractRequest vote(ClanPlayer acceptor, Result result)
    {
        if (requests.isEmpty()) {
            return null;
        }

        Iterator<AbstractRequest> it = requests.iterator();
        while (it.hasNext()) {
            AbstractRequest request = it.next();

            if (request.isAcceptor(acceptor)) {

                switch (result) {
                    case ACCEPT:
                        request.accept();
                        request.announceMessage(Language.getTranslation("voted.to.accept", acceptor.getName()));
                        break;
                    case DENY:
                        request.deny();
                        request.announceMessage(Language.getTranslation("voted.to.deny", acceptor.getName()));
                        break;
                    case ABSTAIN:
                        if (request instanceof SingleRequest) {
                            request.announceMessage(Language.getTranslation("voted.to.abstain", acceptor.getName()));
                            return request;
                        }
                        request.abstain();
                        break;
                    default:
                        return null;
                }

                //check if we were successfully
                if (request.checkRequest()) {
                    //if everyone has voted
                    request.onAccepted();
                    it.remove();
                } else {
                    //check if everyone has voted if yes and no success -> remove
                    if (request.hasEveryoneVoted()) {
                        request.onDenied();
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
        if (requests.isEmpty()) {
            return;
        }

        Iterator<AbstractRequest> it = requests.iterator();

        while (it.hasNext()) {
            AbstractRequest request = it.next();
            if (request.isClanInvolved(clan)) {
                it.remove();
                return;
            }
        }
    }

    public int getRequests()
    {
        return requests.size();
    }


    public void clearRequests(ClanPlayer clanPlayer)
    {
        if (requests.isEmpty()) {
            return;
        }

        Iterator<AbstractRequest> it = requests.iterator();

        while (it.hasNext()) {
            AbstractRequest request = it.next();

            if (request.isRequester(clanPlayer)) {
                it.remove();
                return;
            } else if (request.isAcceptor(clanPlayer)) {
                if (request instanceof SingleRequest) {
                    this.vote(clanPlayer, Result.DENY);
                } else if (request instanceof MultipleRequest) {
                    this.vote(clanPlayer, Result.ABSTAIN);
                }
            }
        }
    }

    public void clearRequests(Player player)
    {
        if (requests.isEmpty()) {
            return;
        }

        Iterator<AbstractRequest> it = requests.iterator();

        while (it.hasNext()) {
            AbstractRequest request = it.next();

            if (request.isRequester(player)) {
                it.remove();
                return;
            } else if (request.isAcceptor(player)) {
                ClanPlayer clanPlayer = plugin.getClanPlayerManager().getClanPlayer(player);
                if (request instanceof SingleRequest) {
                    this.vote(clanPlayer, Result.DENY);
                } else if (request instanceof MultipleRequest) {
                    this.vote(clanPlayer, Result.ABSTAIN);
                }
            }
        }
    }

    public static enum Result {
        ACCEPT, DENY, ABSTAIN
    }
}
