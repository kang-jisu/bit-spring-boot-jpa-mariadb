package com.jisu.api.quiz.service;

import com.jisu.api.quiz.domain.Quiz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class QuizServiceImplTest {

    @InjectMocks
    private QuizServiceImpl quizService;

    @Mock // mock은 이거로
    GeneratorServiceImpl generatorService;

    @BeforeEach
    void setUp() {
        // injectMocks 사용해서 자동 주입했으므로 이 코드 없어도됨zServiceImpl(generatorService);
    }

    @DisplayName("50 * 30 = 1500")
    @Test
    void createQuiz() {
        given(generatorService.randomFactor()).willReturn(50,30);

        Quiz quiz = quizService.createQuiz();
        assertThat(quiz.getFactorA(), is(50));
        assertThat(quiz.getFactorB(), is(30));
        assertThat(quiz.getResult(), is(1500));
    }
}