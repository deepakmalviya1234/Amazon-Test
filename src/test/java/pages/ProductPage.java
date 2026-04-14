package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.testng.Assert;

public class ProductPage {
    private Page page;

    public ProductPage(Page page) {
        this.page = page;
    }

    public boolean isProductPageLoaded(String productName) {
        return page.title().contains(productName);
    }

    public void selectQuantity(int qty) {
        page.selectOption("#quantity", String.valueOf(qty));
        page.waitForTimeout((1000));
    }

    public void addToCart() {
        page.click("#add-to-cart-button");
    }

    public String getSubtotal() {
        page.waitForSelector("#sw-subtotal-item-count");
        Locator subTotalLabel = page.locator("#sw-subtotal-item-count");
        Assert.assertTrue(subTotalLabel.isVisible(),"Subtotal label display");
        return page.locator("//span[@class='a-price sw-subtotal-amount']/span[1]").innerText();
    }

    public void goToCart() {
        page.click("a:has-text('Go to Cart')");
        page.waitForTimeout(6000);
    }
}
