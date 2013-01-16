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
 *     Last modified: 07.01.13 14:39
 */

package com.p000ison.dev.simpleclans2.chat;

/**
 * Represents a Variable
 */
public class Variable {
    private String variable;
    private String format;

    public Variable(String variable, String format) {
        this.variable = variable;
        this.format = format;
    }

    public String format(String input) {
        return String.format(format, input);
    }

    public String getVariable() {
        return variable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Variable variable1 = (Variable) o;

        return !(variable != null ? !variable.equals(variable1.variable) : variable1.variable != null);
    }

    @Override
    public int hashCode() {
        return variable != null ? variable.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "variable='" + variable + '\'' +
                '}';
    }
}
