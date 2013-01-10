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
 *     Last modified: 13.10.12 15:05
 */

package com.p000ison.dev.simpleclans2.language;

import com.p000ison.dev.simpleclans2.api.chat.ChatBlock;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a ColorizedMap
 */
public class ColorizedMap extends HashMap<String, String> {

    private static final long serialVersionUID = 2539590894611232409L;

    public void importMap(Map<Object, Object> otherMap)
    {
        for (Map.Entry<Object, Object> current : otherMap.entrySet()) {
            this.put(current.getKey().toString(), current.getValue().toString());
        }
    }

    @Override
    public String put(String key, String value)
    {
        return super.put(key, ChatBlock.parseColors(value));
    }
}
