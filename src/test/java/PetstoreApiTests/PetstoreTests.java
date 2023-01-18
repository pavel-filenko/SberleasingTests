package PetstoreApiTests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class PetstoreTests {
    private final static String SERVER_URL = "https://petstore.swagger.io/v2";
    Random random = new Random();

    @Description("Тестовое задание. API тест сайта https://petstore.swagger.io/")
    @Test
    public void e2eApiTest() {
        UserData user = createUser(random.nextInt(1000),
                "TestUsername" + random.nextInt(1000000),
                "TestName" + random.nextInt(1000),
                "TestSurname" + random.nextInt(1000),
                "test@test.com" + random.nextInt(1000),
                "Te_StPass-word" + random.nextInt(1000),
                "8800555353" + random.nextInt(9),
                random.nextInt(5));
        ResponseData createResponse = postUser(user);
        responseAssertion(createResponse, user.getId().toString());

        UserData updatedData = createUser(random.nextInt(1000),
                "AnotherUsername" + random.nextInt(100000),
                "AnotherName", "AnotherSurname", "test@another.com", "An_otg89Pass-word",
                "+7(999)999-99-99", random.nextInt(5));
        ResponseData updateResponse = putUser(updatedData, user.getUsername());
        responseAssertion(updateResponse, updatedData.getId().toString());

        UserData acquiredUser = getUser(updatedData.getUsername());
        usersAssertion(acquiredUser, updatedData);

        loginUser(acquiredUser.getUsername(), acquiredUser.getPassword());

        ResponseData deleteResponse = deleteUser(acquiredUser.getUsername());

        responseAssertion(deleteResponse, acquiredUser.getUsername());
    }

    @Step("Создание тела запроса с данными тестового пользователя")
    public static UserData createUser(Integer id, String username, String firstName, String lastName, String email,
                                      String password, String phone, Integer userStatus) {
        return new UserData(id, username, firstName, lastName, email, password, phone, userStatus);
    }

    @Step("Отправка POST запроса по эндпойнту /user")
    public static ResponseData postUser(UserData userData) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(userData)
                .when()
                .post(SERVER_URL + "/user")
                .then()
                .statusCode(200)
                .extract().body().as(ResponseData.class);
    }

    @Step("Отправка PUT запроса по эндпойнту /user/{username}")
    public static ResponseData putUser(UserData userData, String username) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(userData)
                .when()
                .put(SERVER_URL + "/user/" + username)
                .then()
                .statusCode(200)
                .extract().body().as(ResponseData.class);
    }

    @Step("Отправка GET запроса по эндпойнту /user/{username}")
    public static UserData getUser(String username) {
        return given()
                .accept(ContentType.JSON)
                .get(SERVER_URL + "/user/" + username)
                .then()
                .statusCode(200)
                .extract().body().as(UserData.class);
    }

    @Step("Отправка DELETE запроса по эндпойнту /user/{username}")
    public static ResponseData deleteUser(String username) {
        return given()
                .when()
                .contentType(ContentType.JSON)
                .delete(SERVER_URL + "/user/" + username)
                .then()
                .statusCode(200)
                .extract().body().as(ResponseData.class);
    }

    @Step("Проверка тела ответа")
    public static void responseAssertion(ResponseData responseData, String expectedData) {
        Assert.assertNotNull(responseData.getMessage());
        Assert.assertEquals(responseData.getType(), "unknown");
        Assert.assertEquals(responseData.getCode(), 200);
        Assert.assertEquals(responseData.getMessage(), expectedData);
    }

    @Step("Сравнение данных двух пользователей")
    public static void usersAssertion(UserData firstUser, UserData secondUser) {
        Assert.assertEquals(firstUser, secondUser);
    }

    @Step("Вход пользователя в систему")
    public static void loginUser(String username, String password) {
        ResponseData loginResponse = given()
                .accept(ContentType.JSON)
                .queryParam("username", username)
                .queryParam("password", password)
                .get(SERVER_URL + "/user/login")
                .then()
                .statusCode(200)
                .extract().body().as(ResponseData.class);

        loginResponseAssertion(loginResponse);
    }

    @Step("Проверка сообщения о входе в систему")
    public static void loginResponseAssertion(ResponseData responseData) {
        Assert.assertEquals(responseData.getCode(), 200);
        Assert.assertEquals(responseData.getType(), "unknown");
        Assert.assertTrue(responseData.getMessage().matches("logged in user session:[0-9]{13}"));
    }

}
