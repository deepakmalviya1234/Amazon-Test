package pages;

import com.microsoft.playwright.Page;

public class CartPage {
    private Page page;

    public CartPage(Page page) {
        this.page = page;
    }

    public boolean isCartPageDisplayed() {
        return page.locator("#sc-active-cart").isVisible();
    }

    public String getProductName() {
        return page.locator(".sc-product-title").first().innerText();
    }

    public String getQuantity() {
        return page.locator(".sc-quantity-textfield").inputValue();
    }
}
