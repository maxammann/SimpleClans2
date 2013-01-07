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
 *     Last modified: 05.11.12 20:53
 */

package com.p000ison.dev.simpleclans2.converter;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a ConvertedClan
 */
public class ConvertedClan {
    private long id;
    private String tag;
    private List<String> rawRivals, rawAllies, rawWarring;

    public ConvertedClan(String tag)
    {
        this.tag = tag;
    }

    public void serPackedRivals(String packed)
    {
        rawRivals = Arrays.asList(packed.split("\\s*(\\||$)"));
    }

    public void setPackedAllies(String packed)
    {
        rawAllies = Arrays.asList(packed.split("\\s*(\\||$)"));
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public List<String> getRawRivals()
    {
        return rawRivals;
    }

    public List<String> getRawAllies()
    {
        return rawAllies;
    }

    public String getTag()
    {
        return tag;
    }

    public List<String> getRawWarring()
    {
        return rawWarring;
    }

    public void setRawWarring(List<String> rawWarring)
    {
        this.rawWarring = rawWarring;
    }
}
