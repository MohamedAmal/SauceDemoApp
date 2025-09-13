package utils;

import org.openqa.selenium.support.Color;

public class ColorUtils {

    public static String convertToHex(String cssColor) {
        return Color.fromString(cssColor).asHex();
    }
}