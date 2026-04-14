package pages;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class SearchResultsPage {

    private Page page;

    public SearchResultsPage(Page page) {
        this.page = page;
    }

    public boolean isResultsDisplayed() {
        return page.locator("//div[contains(@id,'anonCarousel')]//li").count() > 0;
    }

    public boolean clickOnProduct(String productName) {

        Locator items = page.locator("//span[contains(text(),'"+productName+"')]");
        items.nth(0).click(new Locator.ClickOptions().setForce(true));
        page.waitForTimeout(9000);
        return true;
    }
    public Page clickOnProduct(BrowserContext context, String productName) {
       // Locator product = page.locator("//span[contains(text(),'\" + productName + \"')]").first();
      //  page.locator(xpath).click();
        return context.waitForPage(() -> page.locator("span.a-truncate-cut",
                new Page.LocatorOptions().setHasText(productName)
        ).click());
    }
}
