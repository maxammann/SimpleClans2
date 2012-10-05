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
 *     Created: 04.10.12 13:26
 */

package com.p000ison.dev.simpleclans2.test;

import com.p000ison.dev.simpleclans2.util.chat.Align;
import com.p000ison.dev.simpleclans2.util.chat.ChatBlock;

/**
 * Represents a Test
 */
public class Test {

    public static void main(String[] args)
    {
//        System.out.println("['s sdfg ]'s".replaceAll("[\\['s']|['\\]'s']", ""));
//        for (int i = 80; i < 103; i++) {
//            System.out.println((char)i);
//        }

//        long start = System.currentTimeMillis();
//
//        StringBuilder sb = new StringBuilder("123456789");
//        System.out.println(ChatBlock.msgLength(sb));
//
//        ChatBlock.center(sb, 70);
//        System.out.print('|');
//        System.out.print(sb.toString());
//        System.out.print('|');
//
//        System.out.println();
//
//
//        long finish = System.currentTimeMillis();
//        System.out.printf("Check took %s!", finish - start);

        long start = System.currentTimeMillis();

        ChatBlock impl = new ChatBlock();
        impl.setAlignment(Align.LEFT);
        for (int y = 0; y < 10; y++) {
            impl.addRow("1");
        }
        impl.sendBlock(null);
        long finish = System.currentTimeMillis();
        System.out.printf("Check took %s!", finish - start);


    }

    public void speedTestChatblocks()
    {
//        long[] orginalTimings = new long[5000];
//
//        for (int i = 0; i < 5000; i++) {
//            long start = System.currentTimeMillis();
//
//            com.p000ison.dev.simpleclans2.util.chat.ChatBlock block1 = new com.p000ison.dev.simpleclans2.util.chat.ChatBlock();
//            block1.setAlignment("c", "c", "r", "l");
//            for (int j = 0; j < 100; j++) {
//                block1.addRow("123456789", "123456789", "123456789", "123456789");
//            }
//
//            block1.sendBlock(sender);
//
//            long finish = System.currentTimeMillis();
//            orginalTimings[i] = finish - start;
//        }
//
//        long[] myTimings = new long[5000];
//
//
//        for (int i = 0; i < 500; i++) {
//            long start = System.currentTimeMillis();
//
//            ChatBlock block = new ChatBlock();
//            block.setAlignment(Align.CENTER, Align.CENTER, Align.RIGHT, Align.LEFT);
//            for (int j = 0; j < 100; j++) {
//                block.addRow("123456789", "123456789", "123456789", "123456789");
//            }
//
//            block.sendBlock(sender);
//
//            long finish = System.currentTimeMillis();
//            myTimings[i] = finish - start;
//        }
//
//        int my = 0;
//
//        for (long a : myTimings) {
//            my += a;
//        }
//
//        int orginal = 0;
//
//        for (long a : orginalTimings) {
//            orginal += a;
//        }
//
//        System.out.println(my);
//        System.out.println(orginal);
//        System.out.println("My: " + (my / 5000.0));
//        System.out.println("Orginal: " + (orginal / 5000.0));
    }
}
