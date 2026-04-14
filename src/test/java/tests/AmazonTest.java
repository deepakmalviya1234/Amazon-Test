package tests;

import base.BaseTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.ProductPage;
import pages.SearchResultsPage;
import utility.ConfigReader;

import java.sql.SQLOutput;

public class AmazonTest extends BaseTest {

    @Test
    public void verifyAmazonFlow() {

        String searchItem = ConfigReader.get("searchItem");
        String productName = ConfigReader.get("productName");
        int quantity = Integer.parseInt(ConfigReader.get("quantity"));

        HomePage home = new HomePage(page);
        home.searchProduct(searchItem);

        SearchResultsPage results = new SearchResultsPage(page);
        boolean isResultPresent = results.isResultsDisplayed();
        if(isResultPresent){
           Assert.assertTrue(true, "search results found");
        }else {
            Assert.assertTrue(false, "No search results found");
        }

        boolean productTab = results.clickOnProduct(productName);
        Assert.assertTrue(productTab , "Product Selected From List");

        ProductPage productPage = new ProductPage(this.page);
        Assert.assertTrue(productPage.isProductPageLoaded(productName), "Product page not loaded");

        productPage.selectQuantity(quantity);
        Locator singlePriceElement = page.locator("//span[@id='apex-pricetopay-accessibility-label']/following-sibling::span[2]//span[@class='a-price-whole']");
        String oneUnitPrice = singlePriceElement.innerText().replaceAll("[^0-9.]", "");
        double totalPrice = Double.parseDouble(oneUnitPrice)*quantity;
        productPage.addToCart();
        String subtotal = productPage.getSubtotal();
        Assert.assertTrue(subtotal.replaceAll("[^0-9.]", "").contains(String.valueOf(totalPrice)), "Subtotal Price Display and Matched");

        productPage.goToCart();

        CartPage cart = new CartPage(this.page);
        Assert.assertTrue(cart.isCartPageDisplayed(), "Cart page not displayed");

        Assert.assertTrue(cart.getProductName().contains(productName), "Wrong product in cart");
        Assert.assertEquals(cart.getQuantity(), String.valueOf(quantity), "Quantity mismatch");
    }
}
