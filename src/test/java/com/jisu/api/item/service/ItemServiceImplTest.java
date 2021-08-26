package com.jisu.api.item.service;

import com.jisu.api.item.domain.Item;
import com.jisu.api.item.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
//        itemService = new ItemServiceImpl(itemRepository);
    }

    @Test
    void findAll() {
        given(itemRepository.findAll()).willReturn(Arrays.asList(
                Item.builder().itemBrand("삼성").itemName("갤럭시").itemColor("블루").build(),
                Item.builder().itemBrand("삼성1").itemName("갤럭시1").itemColor("블루1").build()));
        List<Item> list = itemRepository.findAll();
        assertThat(list.size(),is(2));
    }

    @Test
    void findById() {
    }

    @Test
    void save() {
    }

    @Test
    void existsById() {
    }

    @Test
    void count() {
    }

    @Test
    void deleteAll() {
    }

    @Test
    void deleteById() {
    }
}