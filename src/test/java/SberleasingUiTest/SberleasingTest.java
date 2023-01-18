package SberleasingUiTest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class SberleasingTest extends BaseTest {

    private final static String START_URL = "https://www.google.ru/";
    private final static String SEARCH_QUERY = "СберЛизинг";
    private final static String EXPECTED_TITLE = "СберЛизинг — официальный сайт лизинговой компании." +
            " Лизинг для юридических лиц и ИП.";

    @Test
    public void findCarTest() {
        open(START_URL);
        $x("//input[@name='q']").sendKeys(SEARCH_QUERY, Keys.ENTER);
        $x("(//h3)[1]").click();
        switchTo().window(EXPECTED_TITLE);
        $x("(//button[contains(text(), 'Закрыть')])[2]").click();
        $(".sbl-btn.sbl-btn_medium.mx-auto.sbl-btn_icon.sbl-btn_icon-config")
                .shouldBe(Condition.visible);
        if($x("//div[contains(text(), 'Остались вопросы')]").exists()) {
            $x("//div[@class='comagic-call-generator__close-btn']").click();
        }
        clickAndSleep($x("(//a[text()='Подобрать по параметрам'])[1]"));
        clickAndSleep($x("//label[@id='filter-city']"));
        sendKeysAndSleep($x("//label[@id='filter-city']"), "Москва");
        clickAndSleep($x("//label[contains(text(), 'Москва')]"));
        clickAndSleep($x("//label[@id='filter-mark']"));
        sendKeysAndSleep($x("//label[@id='filter-mark']"), "Audi");
        clickAndSleep($x("//label[contains(text(), 'Audi')]"));
        clickAndSleep($x("//label[@id='filter-model']"));
        sendKeysAndSleep($x("//label[@id='filter-model']"), "A4");
        clickAndSleep($x("//label[contains(text(), 'A4')]"));
        clickAndSleep($x("(//div[@class='el-tooltip el-slider__button'])[1]"));
        actions().clickAndHold($x("((//div[@role='slider'])[1]/div/div[@tabindex])[1]"))
                .moveByOffset(25, 0)
                .release()
                .build()
                .perform();
        sleep(3000);
        clickAndSleep($x("//label[contains(text(), 'передний')]"));
        clickAndSleep($x("//label[contains(text(), 'автомат')]"));
        clickAndSleep($x("//label[@title='седан']"));
        clickAndSleep($x("//label[contains(text(), 'бензин')]"));
        clickAndSleep($x("//label[@aria-label='Цвет']"));
        clickAndSleep($x("//label[contains(text(), 'серый')]"));
        clickAndSleep($x("//a[contains(text(), 'Показать все предложения')]"));
        clickAndSleep($x("//div[@class='car-card__item-title text-body']"));
        $x("//h1").shouldHave(Condition.text("Audi"));
    }

    public void clickAndSleep(SelenideElement element) {
        element.click();
        sleep(3000);
    }

    public void sendKeysAndSleep(SelenideElement element, CharSequence sequence) {
        element.sendKeys(sequence);
        sleep(3000);
    }
}
