package xyz.e3ndr.consoleutil;

import java.io.IOException;

import xyz.e3ndr.consoleutil.ansi.ConsoleAttribute;
import xyz.e3ndr.consoleutil.ansi.ConsoleColor;
import xyz.e3ndr.fastloggingframework.FastLoggingFramework;
import xyz.e3ndr.fastloggingframework.logging.LogLevel;

public class WindowExample {

    public static void main(String[] args) throws IOException, InterruptedException {
        FastLoggingFramework.setDefaultLevel(LogLevel.DEBUG);

        ConsoleUtil.summonConsoleWindow();

        ConsoleWindow window = new ConsoleWindow();

        window.setAutoFlushing(false).clearScreen().cursorTo(0, 0);

        for (ConsoleColor color : ConsoleColor.values()) {
            if (!color.isLight()) {
                window.setBackgroundColor(color);
                window.write("  ");
            }
        }

        window.cursorTo(0, 1);

        for (ConsoleColor color : ConsoleColor.values()) {
            if (color.isLight()) {
                window.setBackgroundColor(color);
                window.write("  ");
            }
        }

        window.cursorTo(0, 3);

        window.setBackgroundColor(ConsoleColor.BLACK).setTextColor(ConsoleColor.WHITE);

        window.setAttributes(ConsoleAttribute.RESET, ConsoleAttribute.BOLD).write("BOLD").cursorRight(1);
        window.setAttributes(ConsoleAttribute.RESET, ConsoleAttribute.STRIKETHROUGH).write("STRIKETHROUGH").cursorRight(1);
        window.setAttributes(ConsoleAttribute.RESET, ConsoleAttribute.ITALIC).write("ITALIC").cursorRight(1);
        window.setAttributes(ConsoleAttribute.RESET, ConsoleAttribute.UNDERLINE).write("UNDERLINE").cursorRight(1);

        window.setAttributes(ConsoleAttribute.RESET, ConsoleAttribute.CURSOR_BLINK_OFF);
        window.update();

        while (true) {
            for (ConsoleColor color : ConsoleColor.values()) {
                window.cursorTo(4, 6).setBackgroundColor(color).write("    ").setBackgroundColor(ConsoleColor.BLACK);
                window.cursorTo(4, 7).setBackgroundColor(color).write("    ").setBackgroundColor(ConsoleColor.BLACK);
                window.update();
                Thread.sleep(150);
            }
        }
    }

}
