package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private EntityManager entityManager;

    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    void setUp() {
        answer1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        answer2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

        answerRepository.save(answer1);
        answerRepository.save(answer2);
    }

    @AfterEach
    void tearDown() {
        answerRepository.deleteAll();
    }

    @Test
    void test_답변_저장() {
        answerRepository.deleteAll();
        entityManager.clear();
        Answer actual = answerRepository.save(answer1);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getWriterId()).isEqualTo(answer1.getWriterId()),
            () -> assertThat(actual.getQuestionId()).isEqualTo(answer1.getQuestionId())
        );
    }

    @Test
    void test_목록_조회() {
        List<Answer> answers = answerRepository.findAll();

        assertAll(
            () -> assertThat(answers).isNotNull(),
            () -> assertThat(answers).hasSize(2)
        );
    }

    @Test
    void test_답변_id로_조회() {
        Answer actual = answerRepository.findById(answer1.getId())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getId()).isEqualTo(answer1.getId())
        );
    }

    @Test
    void test_질문_id로_조회() {
        List<Answer> answers = answerRepository.findByQuestionId(answer1.getQuestionId());

        assertAll(
            () -> assertThat(answers).isNotNull(),
            () -> assertThat(answers).hasSize(2),
            () -> assertThat(answers.get(0).getQuestionId()).isEqualTo(answer1.getQuestionId())
        );
    }

    @Test
    void test_작성자_id로_조회() {
        Answer actual = answerRepository.findByWriterId(answer1.getWriterId())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getWriterId()).isEqualTo(answer1.getWriterId())
        );
    }

    @Test
    void test_답변_업데이트() {
        answer1.setContents("답변수정");

        Answer actual = answerRepository.findById(answer1.getId())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getContents()).isEqualTo("답변수정")
        );
    }

    @Test
    void test_답변_삭제() {
        answer1.setDeleted(true);

        List<Answer> answers = answerRepository.findAll();

        assertAll(
            () -> assertThat(answers).isNotNull(),
            () -> assertThat(answers).hasSize(2),
            () -> assertThat(answers.get(0).isDeleted()).isTrue(),
            () -> assertThat(answers.get(1).isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("ID로 조회 후 삭제 되지 않았음을 확인한다.")
    void test_findByIdAndDeletedFalse() {
        Answer actual = answerRepository.findByIdAndDeletedFalse(answer1.getId())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getId()).isEqualTo(answer1.getId()),
            () -> assertThat(actual.isDeleted()).isEqualTo(answer1.isDeleted()),
            () -> assertThat(actual.isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("질문 ID로 조회 후 삭제 되지 않았음을 확인한다.")
    void test_findByQuestionIdAndDeletedFalse() {
        List<Answer> answers =
            answerRepository.findByQuestionIdAndDeletedFalse(answer1.getQuestionId());

        assertAll(
            () -> assertThat(answers).isNotNull(),
            () -> assertThat(answers.get(0).getQuestionId()).isEqualTo(answer1.getQuestionId()),
            () -> assertThat(answers.get(0).isDeleted()).isEqualTo(answer1.isDeleted()),
            () -> assertThat(answers.get(0).isDeleted()).isFalse()
        );
    }
}
