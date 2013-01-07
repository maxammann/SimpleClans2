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
 *     Last modified: 28.10.12 12:11
 */

package com.p000ison.dev.simpleclans2.util.chat;

/**
 * Represents a Row
 */
public class Row {
    private StringBuilder[] columns;

    public Row(StringBuilder... columns)
    {
        this.columns = columns;
    }

    public Row(Object... columns)
    {
        this.columns = new StringBuilder[columns.length];

        for (int i = 0; i < columns.length; i++) {
            Object toAdd = columns[i];

            if (toAdd == null) {
                throw new IllegalArgumentException(String.format("No argument of a row can be null! (Index: %s)", i));
            }

            this.columns[i] = new StringBuilder(toAdd.toString());
        }
    }

    public StringBuilder[] getColumns()
    {
        return columns;
    }

    public void setColumn(int index, StringBuilder text)
    {
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException("The index can not less than/equal 0!");
        } else if (index >= columns.length) {
            throw new ArrayIndexOutOfBoundsException("The index can not be greater than/equal the lenght!");
        }

        columns[index] = text;
    }

    public void setColumn(int index, String text)
    {
        setColumn(index, new StringBuilder(text));
    }

    public int getLenght()
    {
        return columns.length;
    }
}
