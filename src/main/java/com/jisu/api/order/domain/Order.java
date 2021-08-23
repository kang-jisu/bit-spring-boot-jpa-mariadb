package com.jisu.api.order.domain;

import com.jisu.api.item.domain.Item;
import com.jisu.api.user.domain.User;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "price")
    private Long price;

    @Column(name = "order_status")
    private String count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;
}
