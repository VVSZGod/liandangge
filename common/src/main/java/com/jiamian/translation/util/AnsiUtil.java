package com.jiamian.translation.util;

import org.fusesource.jansi.Ansi;

public class AnsiUtil {

	public static String getAnsi(Ansi.Color color, String text, boolean flag) {
		if (flag) {
			return Ansi.ansi().eraseScreen().fg(color).a(text).reset()
					.toString();
		}
		return text;
	}
}
