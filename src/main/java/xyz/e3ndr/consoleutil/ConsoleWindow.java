package xyz.e3ndr.consoleutil;

import java.io.IOException;

import org.fusesource.jansi.Ansi;

import lombok.NonNull;
import xyz.e3ndr.fastloggingframework.logging.LoggingUtil;

public class ConsoleWindow {
    private Ansi ansi = Ansi.ansi();

    public ConsoleWindow cursorTo(int x, int y) {
        this.ansi.cursor(y + 1, x + 1);

        return this;
    }

    public ConsoleWindow write(Object format, Object... args) {
        String line = LoggingUtil.parseFormat(format, args);

        this.ansi.a(line);

        return this;
    }

    public ConsoleWindow setTextColor(@NonNull ConsoleColor color) {
        if (color.isLight()) {
            this.ansi.fgBright(color.getColor());
        } else {
            this.ansi.fg(color.getColor());
        }

        return this;
    }

    public ConsoleWindow setBackgroundColor(@NonNull ConsoleColor color) {
        if (color.isLight()) {
            this.ansi.bgBright(color.getColor());
        } else {
            this.ansi.bg(color.getColor());
        }

        return this;
    }

    public ConsoleWindow setAttributes(@NonNull ConsoleAttribute... attributes) {
        for (ConsoleAttribute attribute : attributes) {
            for (Ansi.Attribute ansiAttr : attribute.getAttributes()) {
                this.ansi.a(ansiAttr);
            }
        }

        return this;
    }

    public ConsoleWindow update() throws InterruptedException, IOException {
        System.out.println(this.ansi);
        this.ansi = Ansi.ansi();

        return this;
    }

}
