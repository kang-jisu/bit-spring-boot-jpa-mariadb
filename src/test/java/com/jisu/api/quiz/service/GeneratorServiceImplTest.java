package com.jisu.api.quiz.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GeneratorServiceImplTest {

    @Mock
    GeneratorService generatorService;

    @BeforeEach
    void setUp() {
        generatorService = new GeneratorServiceImpl();
    }

    @Test
    @DisplayName("generatorService가 null인지")
    void generatorService(){
        assertNotNull(generatorService);
    }


    @Test
    @DisplayName("랜덤 값 발생 알고리즘 테스트")
    void randomFactor() {
        List<Integer> randoms = IntStream.range(0,1000)
                .map(i-> generatorService.randomFactor())
                .boxed() // to Wrapper Class
                .collect(Collectors.toList());
        assertThat(randoms).containsOnlyElementsOf(IntStream.range(11,100)
                .boxed()
                .collect(Collectors.toList())
        );
    }
}