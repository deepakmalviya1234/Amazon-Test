package pages;

import com.microsoft.playwright.Page;

public class HomePage {
    private Page page;

    public HomePage(Page page) {
        this.page = page;
    }

    public void searchProduct(String product) {
        page.fill("#twotabsearchtextbox", product);
        page.press("#twotabsearchtextbox", "Enter");
        page.waitForTimeout(9000);
    }
}
