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
 *     Created: 03.09.12 16:32
 */

package com.p000ison.dev.simpleclans2.database;

import java.util.LinkedList;
import java.util.List;

public class Field {

    private String name;
    private Type type;
    private List<Integer> length = new LinkedList<Integer>();
    private boolean canBeNull;
    private Object defaultValue;
    private boolean isPrimary;

    public Field(boolean primary, String name, Type type, boolean canBeNull, Object defaultValue, int... lengthes)
    {
        isPrimary = primary;
        this.setName(name);
        this.setType(type);
        this.setCanBeNull(canBeNull);
        this.setDefaultValue(defaultValue);

        for (int lengthT : lengthes) {
            length.add(lengthT);
        }
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public boolean hasLength()
    {
        return !length.isEmpty();
    }

    public List<Integer> getLength()
    {
        return length;
    }

    public boolean isCanBeNull()
    {
        return canBeNull;
    }

    public void setCanBeNull(boolean canBeNull)
    {
        this.canBeNull = canBeNull;
    }

    public Object getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public boolean isPrimary()
    {
        return isPrimary;
    }

    public enum Type {
        TEXT,
        INT;
    }
}
