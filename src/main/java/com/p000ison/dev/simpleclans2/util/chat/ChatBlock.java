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
 *     Created: 10/4/12 8:12 PM
 */

package com.p000ison.dev.simpleclans2.util.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

/**
 * Represents a ChatBlock
 */
public class ChatBlock extends ArrayList<StringBuilder[]> {

    private static final int COLUMN_SPACING = 12;
    private static final int MAX_LINE_LENGTH = 320;

    private Align[] alignment = null;
    private double[] columnSizes = null;

    private Align getAlign(int column)
    {
        if (alignment == null) {
            return null;
        }

        return alignment[column];
    }

    public double getMaxWidth(double col)
    {
        double maxWidth = 0;

        for (StringBuilder[] row : this) {
            if (col < row.length) {
                maxWidth = Math.max(maxWidth, msgLength(row[(int) col]));
            }
        }

        return maxWidth;
    }

    private void generateColumnSizes()
    {
        if (columnSizes == null) {
            // generate columns sizes

            int col_count = this.get(0).length;

            columnSizes = new double[col_count];

            for (int i = 0; i < col_count; i++) {
                // add custom column spacing if specified

                columnSizes[i] = getMaxWidth(i) + COLUMN_SPACING;
            }
        }
    }

    public void addRow(String... sections)
    {
        StringBuilder[] builderSections = new StringBuilder[sections.length];

        for (int i = 0; i < sections.length; i++) {
            builderSections[i] = new StringBuilder(sections[i]);
        }

        this.add(builderSections);
    }

    public boolean sendBlock(CommandSender sender)
    {
        if (this.isEmpty()) {
            throw new IllegalArgumentException("No rows added!");
        }

        generateColumnSizes();

        int firstRowLength = this.get(0).length;

        if (alignment.length != firstRowLength) {
            throw new IllegalArgumentException("The number of alignments must equal the number of sections!");
        }

        if (columnSizes.length != firstRowLength) {
            throw new IllegalArgumentException("The number of alignments must equal the number of sections!");
        }

        for (StringBuilder[] row : this) {

            StringBuilder finalRow = new StringBuilder();

            for (int column = 0; column < row.length; column++) {
                StringBuilder section = row[column];
                double columnSize = columnSizes[column];
                Align align = getAlign(column);

                if (align == null) {
                    return false;
                }

                double sectionLength = msgLength(section);

                switch (align) {
                    case RIGHT:
                        if (sectionLength > columnSize) {
                            cropRight(section, columnSize);
                        } else if (sectionLength < columnSize) {
                            padLeft(section, columnSize);
                        }
                        break;
                    case LEFT:
                        if (sectionLength > columnSize) {
                            cropRight(section, columnSize);
                        } else if (sectionLength < columnSize) {
                            padRight(section, columnSize);
                        }
                    case CENTER:
                        if (sectionLength > columnSize) {
                            cropRight(section, columnSize);
                        } else if (sectionLength < columnSize) {
                            center(section, columnSize);
                        }
                        break;
                }

                finalRow.append(section);
            }

            cropRight(finalRow, MAX_LINE_LENGTH);


            //            System.out.println(finalRow.toString());
            //            sender.sendMessage(finalRow.toString());
        }
        return true;
    }

    public void setAlignment(Align... alignment)
    {
        this.alignment = alignment;
    }

    public static void cropRight(StringBuilder text, double length)
    {
        if (text == null || text.length() == 0) {
            throw new IllegalArgumentException("The text can not be null or empty!");
        }

        while (msgLength(text) >= length) {
            text.deleteCharAt(text.length() - 1);
        }
    }


    public static void cropLeft(StringBuilder text, double length)
    {
        if (text == null || text.length() == 0) {
            throw new IllegalArgumentException("The text can not be null or empty!");
        }

        while (msgLength(text) >= length) {
            text.deleteCharAt(0);
        }
    }

    public static void padRight(StringBuilder text, double length)
    {
        if (text == null || text.length() == 0) {
            throw new IllegalArgumentException("The text can not be null or empty!");
        }

        double msgLenght = msgLength(text);

        if (msgLenght > length) {
            return;
        }

        while (msgLenght < length) {
            msgLenght += 4;
            text.append(' ');
        }
    }

    public static void padLeft(StringBuilder text, double length)
    {
        if (text == null || text.length() == 0) {
            throw new IllegalArgumentException("The text can not be null or empty!");
        }

        double msgLength = msgLength(text);

        if (msgLength > length) {
            return;
        }

        StringBuilder empty = new StringBuilder();

        while (msgLength < length) {
            msgLength += 4;
            empty.append(' ');
        }

        text.insert(0, empty);
    }

    public static void center(StringBuilder msg, double lineLength)
    {
        double length = msgLength(msg);
        double diff = lineLength - length;

        // if too big for line return it as is

        if (diff < 0) {
            return;
        }

        double sideSpace = diff / 2;

        // pad the left with space

        padLeft(msg, length + sideSpace);

        // pad the right with space

        padRight(msg, length + sideSpace + sideSpace);
    }

    public static double msgLength(StringBuilder text)
    {
        double length = 0;

        // Loop through all the characters, skipping any color characters and their following color codes

        int textLength = text.length() - 1;

        for (int x = 0; x < text.length(); x++) {
            char currentChar = text.charAt(x);

            //ignore colors, but only if there is enought space. A § at the end of the line will not be recognized
            if (currentChar == '\u00a7' && x < textLength) {
                char nextChar = text.charAt(x + 1);
                if (ChatColor.getByChar(nextChar) == null) {
                    continue;
                }
            }

            int len = charLength(currentChar);
            if (len > 0) {
                length += len;
            } else {
                x++;
            }
        }
        return length;
    }

    public static int charLength(char x)
    {
        if ("i.:,;|!".indexOf(x) != -1) {
            return 2;
        } else if ("l'".indexOf(x) != -1) {
            return 3;
        } else if ("tI[]".indexOf(x) != -1) {
            return 4;
        } else if ("fk{}<>\"*()".indexOf(x) != -1) {
            return 5;
        } else if ("abcdeghjmnopqrsuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ1234567890\\/#?$%-=_+&^".indexOf(x) != -1) {
            return 6;
        } else if ("@~".indexOf(x) != -1) {
            return 7;
        } else if (x == ' ') {
            return 4;
        } else {
            return -1;
        }
    }

    public static String parseColors(String text)
    {
        return text;
    }
}

