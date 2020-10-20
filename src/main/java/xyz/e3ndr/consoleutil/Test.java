package xyz.e3ndr.consoleutil;

public class Test {

    public static void main(String[] args) throws Exception {
        ConsoleWindow window = new ConsoleWindow();

        while (true) {
            ConsoleUtil.clearConsole();

            for (int i = 0; i <= 1000; i++) {
                window.loadingBar(0, 1, BarStyle.SQUARE, i / 1000f, 100, true);
                window.loadingBar(0, 2, BarStyle.ANGLE, i / 1000f, 100, true);
                window.loadingBar(0, 3, BarStyle.DOTS, i / 1000f, 100, true);
                window.loadingBar(0, 4, BarStyle.ARROW, i / 1000f, 100, true);

                window.update();

                Thread.sleep(10);
            }
        }
    }

}
