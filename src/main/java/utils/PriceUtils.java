package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PriceUtils {

    public static List<Double> extractPricesFromElements(By locator) {
        List<WebElement> elements = WebDriverUtils.findElements(locator);
        return elements.stream()
                .map(WebElement::getText)
                .map(text -> text.replace("$", "").trim())
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

    public static boolean isPricesSortedAscending(List<Double> prices) {
        return prices.equals(
                prices.stream()
                        .sorted()
                        .collect(Collectors.toList())
        );
    }

    public static boolean isPricesSortedDescending(List<Double> prices) {
        return prices.equals(
                prices.stream()
                        .sorted(Comparator.reverseOrder())
                        .collect(Collectors.toList())
        );
    }
}