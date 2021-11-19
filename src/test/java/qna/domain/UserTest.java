package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {

    public static final User JAVAJIGI = new User(1L, UserId.from("javajigi"), Password.from("password"), Name.from("name"),
        Email.from("javajigi@slipp.net"));
    public static final User SANJIGI = new User(2L, UserId.from("sanjigi"), Password.from("password"), Name.from("name"),
        Email.from("sanjigi@slipp.net"));

    private User user1;
    private User user2;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        user1 = new User(UserId.from("user1"), Password.from("password"), Name.from("alice"),
            Email.from("alice@gmail.com"));
        user2 = new User(UserId.from("user2"), Password.from("password"), Name.from("bob"),
            Email.from("bob@gmail.com"));

        userRepository.save(user1);
        userRepository.save(user2);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void test_사용자_저장() {
        User expected = new User(UserId.from("testuser"), Password.from("password"), Name.from("홍길동"),
            Email.from("gildong@gmail.com"));

        User actual = userRepository.save(expected);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId()),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
            () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
        );
    }

    @Test
    void test_목록_조회() {
        List<User> users = userRepository.findAll();

        assertAll(
            () -> assertThat(users).isNotNull(),
            () -> assertThat(users).hasSize(2)
        );
    }

    @Test
    void test_사용자_id로_조회() {
        User actual = userRepository.findByUserId(user1.getUserId())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getUserId()).isEqualTo(user1.getUserId())
        );
    }

    @Test
    void test_사용자_이름으로_조회() {
        User actual = userRepository.findByName(user1.getName())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(user1.getName())
        );
    }

    @Test
    void test_사용자_이메일로_조회() {
        User actual = userRepository.findByEmail(user2.getEmail())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getEmail()).isEqualTo(user2.getEmail())
        );
    }

    @Test
    void test_사용자_이메일_수정() {
        Email newEmail = Email.from("new.alice@gmail.com");
        user1.setEmail(newEmail);

        User actual = userRepository.findByUserId(user1.getUserId())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getEmail()).isEqualTo(newEmail)
        );
    }
}
