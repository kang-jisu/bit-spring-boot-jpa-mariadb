package com.jisu.api.item;

import com.jisu.api.util.Proxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ProxyTest {

    @Test
    @DisplayName("proxy 테스트1 - intMax")
    public void intMax(){
        int a = Proxy.intMax.apply(2,3);
        assertThat(a, is(3));

    }
}
