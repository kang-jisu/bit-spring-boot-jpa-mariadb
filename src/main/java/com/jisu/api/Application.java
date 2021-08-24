package com.jisu.api;

import com.jisu.api.item.domain.Item;
import com.jisu.api.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    ItemService itemService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    @Override
    public void run (String...args) throws Exception {
        itemService.deleteAll();
        itemService.save(Item.builder()
                .itemBrand("삼성")
                .itemName("갤럭시")
                .itemColor("블랙")
                .build()
        );
        itemService.save(Item.builder()
                .itemBrand("애플")
                .itemName("아이폰")
                .itemColor("화이트")
                .build()
        );
        for (Item i : itemService.findAll())
            System.out.println(i.toString());
    }

}
