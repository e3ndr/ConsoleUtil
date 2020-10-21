package xyz.e3ndr.consoleutil;

import lombok.SneakyThrows;

public class Test {

	@SneakyThrows
	public static void main(String[] args) {
		ConsoleUtil.getHandler().summonConsole();
		System.out.println(System.getProperty("java.class.path"));
		System.out.println(System.getProperty("StartedWithConsole"));
		Thread.sleep(100000000);
	}

}
