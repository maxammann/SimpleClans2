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

    /**
     * Gets the name of the command
     *
     * @return The name of the command
     */
    String getName();

    /**
     * Gets the usages for this command.
     *
     * @return The usages of this command
     */
    String[] getUsages();

    /**
     * Sets the identifiers for this command. Those strings are used to detect the command.
     *
     * @param identifiers The identifiers for this command
     */
    void setIdentifiers(String... identifiers);

    /**
     * Checks whether this string is a identifier
     *
     * @param cmd The cmd to check
     * @return Whether the command is a identifier
     */
    boolean isIdentifier(String cmd);

    /**
     * Sets the usage info for this command
     *
     * @param text The usages
     */
    void setUsages(String... text);

    /**
     * @return The max arguments
     */
    int getMaxArguments();

    /**
     * @return The min arguments
     */
    int getMinArguments();

    /**
     * Checks whether the permissible (maybe a player) has the permission to execute this command
     *
     * @param sender The permissible
     * @return Whether the permissible has permission
     */
    boolean hasPermission(Permissible sender);

    /**
     * Sets the permissions for this command.
     *
     * @param permissions The permissions
     */
    void setPermission(String... permissions);

    /**
     * Sets the range of the arguments
     *
     * @param min The min arguments
     * @param max The max arguments
     */
    void setArgumentRange(int min, int max);

    public static enum Type {
        CLAN("clan", "sc"),
        RANK("rank"),
        BB("bb"),
        BANK("bank");

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

    /**
     * Gets the type of this command.
     *
     * @return The type
     */
    Type getType();

    /**
     * Sets the type of this command
     *
     * @param type The type
     */
    void setType(Type type);
}
