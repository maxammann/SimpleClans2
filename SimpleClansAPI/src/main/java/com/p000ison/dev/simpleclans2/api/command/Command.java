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
 *     Last modified: 09.01.13 19:15
 */


package com.p000ison.dev.simpleclans2.api.command;

import org.bukkit.permissions.Permissible;

/**
 * Represents a Command
 */
public interface Command {

    String getName();

    String[] getUsages();

    void setIdentifiers(String... identifiers);

    boolean isIdentifier(String cmd);

    void setUsages(String... text);

    int getMaxArguments();

    int getMinArguments();

    boolean hasPermission(Permissible sender);

    void setArgumentRange(int min, int max);

    public static enum Type {
        CLAN("clan", "sc"),
        RANK("rank"),
        BB(),
        BANK();

        private String[] command;

        private Type(String... command) {

            this.command = command;
        }

        public String[] getCommands() {
            return command.clone();
        }

        public static Type getByCommand(String command) {

            if (command == null) {
                return null;
            }

            for (Type type : values()) {
                for (String cmd : type.getCommands()) {
                    if (cmd.equalsIgnoreCase(command)) {
                        return type;
                    }
                }
            }

            return null;
        }
    }

    Type getType();
}
