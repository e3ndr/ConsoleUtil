package xyz.e3ndr.consoleutil;

import java.io.IOException;

public class ColorTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        // FastLoggingFramework.setDefaultLevel(LogLevel.DEBUG);

        ConsoleUtil.summonConsoleWindow();

        ConsoleWindow window = new ConsoleWindow();

        window.cursorTo(0, 0);

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

        window.setAttributes(ConsoleAttribute.RESET, ConsoleAttribute.BOLD).write("BOLD ");
        window.setAttributes(ConsoleAttribute.RESET, ConsoleAttribute.STRIKETHROUGH).write("STRIKETHROUGH ");
        window.setAttributes(ConsoleAttribute.RESET, ConsoleAttribute.ITALIC).write("ITALIC ");
        window.setAttributes(ConsoleAttribute.RESET, ConsoleAttribute.UNDERLINE).write("UNDERLINE ");

        window.setAttributes(ConsoleAttribute.CUSTOR_BLINK_OFF);

        window.update();

        Thread.sleep(1000000);

    }

}
