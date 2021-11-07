package xyz.e3ndr.consoleutil.consolewindow.impl;

import java.io.IOException;

import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.rakurai.json.element.JsonArray;
import co.casterlabs.rakurai.json.element.JsonObject;
import co.casterlabs.rakurai.json.serialization.JsonParseException;
import lombok.SneakyThrows;
import xyz.e3ndr.consoleutil.ConsoleUtil;
import xyz.e3ndr.consoleutil.ansi.ConsoleAttribute;
import xyz.e3ndr.consoleutil.ansi.ConsoleColor;
import xyz.e3ndr.consoleutil.consolewindow.BarStyle;
import xyz.e3ndr.consoleutil.consolewindow.ConsoleWindow;
import xyz.e3ndr.consoleutil.ipc.IpcChannel;
import xyz.e3ndr.consoleutil.ipc.MemoryMappedIpc;

public class RemoteConsoleWindowLauncherInstance {
    private static ConsoleWindow window;
    private static IpcChannel ipcChannel;

    public static void main(String[] args) throws IOException, InterruptedException {
        window = ConsoleUtil.getAttachedConsoleWindow();

        String ipcId = args[0];
        ipcChannel = MemoryMappedIpc.startChildIpc(ipcId);

        while (true) {
            try {
                JsonObject command = Rson.DEFAULT.fromJson(ipcChannel.read(), JsonObject.class);

                if (command.containsKey("method")) {
                    parseCommand(command);
                }
            } catch (JsonParseException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @SneakyThrows
    private static void safeIpcWrite(JsonObject packet) {
//        System.out.println(packet);
        ipcChannel.write(packet.toString());
    }

    @SuppressWarnings("deprecation")
    private static void parseCommand(JsonObject command) throws IOException, InterruptedException {
        switch (command.getString("method")) {

            case "cursorTo(int,int)": {
                window.cursorTo(
                    command.getNumber("x").intValue(),
                    command.getNumber("y").intValue()
                );
                return;
            }

            case "cursorUp(int)": {
                window.cursorUp(
                    command.getNumber("amount").intValue()
                );
                return;
            }

            case "cursorDown(int)": {
                window.cursorDown(
                    command.getNumber("amount").intValue()
                );
                return;
            }

            case "cursorLeft(int)": {
                window.cursorLeft(
                    command.getNumber("amount").intValue()
                );
                return;
            }

            case "cursorRight(int)": {
                window.cursorRight(
                    command.getNumber("amount").intValue()
                );
                return;
            }

            case "saveCursorPosition()": {
                window.saveCursorPosition();
                return;
            }

            case "restoreCursorPosition()": {
                window.restoreCursorPosition();
                return;
            }

            case "clearScreen()": {
                window.clearScreen();
                return;
            }

            case "write(LINE)": {
                window.write(
                    command.getString("LINE")
                );
                return;
            }

            case "writeAt(int,int,LINE)": {
                window.writeAt(
                    command.getNumber("x").intValue(),
                    command.getNumber("y").intValue(),
                    command.getString("line")
                );
                return;
            }

            case "loadingBar(BarStyle,double,int,boolean)": {
                window.loadingBar(
                    new BarStyle(command.getObject("style")),
                    command.getNumber("progress").doubleValue(),
                    command.getNumber("size").intValue(),
                    command.getBoolean("showPercent")
                );
                return;
            }

            case "loadingBarAt(int,int,BarStyle,double,int,boolean)": {
                window.loadingBarAt(
                    command.getNumber("x").intValue(),
                    command.getNumber("y").intValue(),
                    new BarStyle(command.getObject("style")),
                    command.getNumber("progress").doubleValue(),
                    command.getNumber("size").intValue(),
                    command.getBoolean("showPercent")
                );
                return;
            }

            case "replaceLine(LINE)": {
                window.replaceLine(
                    command.getString("LINE")
                );
                return;
            }

            case "replaceLineAt(int,int,LINE)": {
                window.replaceLineAt(
                    command.getNumber("x").intValue(),
                    command.getNumber("y").intValue(),
                    command.getString("LINE")
                );
                return;
            }

            case "setTextColor(ConsoleColor)": {
                window.setTextColor(
                    ConsoleColor.valueOf(command.getString("color"))
                );
                return;
            }

            case "setBackgroundColor(ConsoleColor)": {
                window.setBackgroundColor(
                    ConsoleColor.valueOf(command.getString("color"))
                );
                return;
            }

            case "clearLine()": {
                window.clearLine();
                return;
            }

            case "clearLine(int)": {
                window.clearLine(
                    command.getNumber("y").intValue()
                );
                return;
            }

            case "setAttributes(ConsoleAttribute[])": {
                JsonArray attributes = command.getArray("attributes");
                ConsoleAttribute[] attributesArray = new ConsoleAttribute[attributes.size()];

                for (int i = 0; i < attributesArray.length; i++) {
                    attributesArray[i] = ConsoleAttribute.valueOf(attributes.get(i).getAsString());
                }

                window.setAttributes(attributesArray);
                return;
            }

            case "update()": {
                window.update();
                return;
            }

            case "clearBuffer()": {
                window.clearBuffer();
                return;
            }

            case "setAutoFlushing(boolean)": {
                window.setAutoFlushing(
                    command.getBoolean("autoFlushing")
                );
                return;
            }

            case "bell()": {
                window.bell();
                return;
            }

            case "setSize(int,int)": {
                window.setSize(
                    command.getNumber("width").intValue(),
                    command.getNumber("height").intValue()
                );
                return;
            }

            case "setTitle(String)": {
                window.setTitle(
                    command.getString("title")
                );
                return;
            }

            case "close()": {
                System.exit(0);
                return;
            }

        }
    }

}
