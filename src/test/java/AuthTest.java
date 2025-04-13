
import com.wheredidwego.MyApplication;
import com.wheredidwego.domain.User;
import com.wheredidwego.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = MyApplication.class)
public class AuthTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void set() {
        String email = "abc@naver.com";
        String password = "1234";
        String nickname = "jeong";

        User user = new User(email, password, nickname);
        userRepository.save(user);
    }

    @Test
    public void checkSaving() {
        User user = userRepository.findUserByEmail("abc@naver.com").get();
        assertEquals(user.getNickname(), "jeong");

        User user2 = userRepository.findUserByNickname("jeong").get();
        assertEquals(user2.getEmail(), "abc@naver.com");
    }
}
