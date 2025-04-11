import org.openqa.selenium.By

fun String.toXPath() =
    By.xpath(this)!!