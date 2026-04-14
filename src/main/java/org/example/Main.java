package org.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;

public class Main {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            // Base URL (environment friendly)
            String baseUrl = System.getenv().getOrDefault("BASE_URL", "https://www.amazon.in");

            // Test Data (can be externalized)
            String searchItem = System.getenv().getOrDefault("SEARCH_ITEM", "HP smart tank");
            String productName = "Smart Tank 589";
            int quantity = 2;

            // Step 1: Navigate to Amazon
            page.navigate(baseUrl);

            // Step 2: Search for product
            page.fill("#twotabsearchtextbox", searchItem);
            page.press("#twotabsearchtextbox", "Enter");

            // Step 3: Verify search results
            page.waitForSelector(".s-main-slot .s-result-item");
            int resultsCount = page.locator(".s-main-slot .s-result-item").count();
            if (resultsCount == 0) {
                throw new RuntimeException("No search results found");
            }

            // Step 4: Click on specific product
            Locator product = page.locator("span:has-text('" + productName + "')").first();
            product.click();

            // Step 5: Handle new tab
            Page productPage = context.waitForPage(() -> product.click());
            productPage.waitForLoadState();

            // Step 6: Verify product page
            PlaywrightAssertions.assertThat(productPage).hasTitle(".*" + productName + ".*");

            // Step 7: Select quantity
            productPage.selectOption("#quantity", String.valueOf(quantity));

            // Step 8: Add to cart
            productPage.click("#add-to-cart-button");

            // Step 9: Verify subtotal
            productPage.waitForSelector("#attach-accessory-cart-subtotal");
            String subtotalText = productPage.locator("#attach-accessory-cart-subtotal").innerText();
            System.out.println("Cart Subtotal: " + subtotalText);

            if (!subtotalText.contains("₹")) {
                throw new RuntimeException("Subtotal not displayed correctly");
            }

            // Step 10: Go to Cart
            productPage.click("a:has-text('Go to Cart')");

            // Step 11: Verify cart page
            productPage.waitForSelector("#sc-active-cart");

            // Step 12: Verify item name and quantity
            String cartItemName = productPage.locator(".sc-product-title").first().innerText();
            String cartQuantity = productPage.locator(".sc-quantity-textfield").inputValue();

            if (!cartItemName.contains(productName)) {
                throw new RuntimeException("Incorrect item in cart");
            }

            if (!cartQuantity.equals(String.valueOf(quantity))) {
                throw new RuntimeException("Incorrect quantity in cart");
            }

            System.out.println("Test Passed Successfully!");

            browser.close();
        }

    }
}