package com.github.haroldjcastillo.dinner;

import org.junit.jupiter.api.Test;

import static com.github.haroldjcastillo.dinner.Philosopher.PhilosopherState.EATING;
import static com.github.haroldjcastillo.dinner.Philosopher.PhilosopherState.THINKING;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;

class PhilosopherTest {

    @Test
    void testEatingState() {
        final var f1 = new Fork(1);
        final var f2 = new Fork(2);
        final var p1 = new Philosopher("p1", f1, f2,1L, 1L);
        // Change the state immediately to eating
        await().atMost(1, TimeUnit.SECONDS)
                .until(() -> p1.state.equals(EATING));
        // After the eating delay change to thinking
        await().atMost(1, TimeUnit.SECONDS)
                .until(() -> p1.state.equals(THINKING));
    }

    @Test
    void testTwoPhilosophers() {
        final var f1 = new Fork(1);
        final var f2 = new Fork(2);
        final var f3 = new Fork(3);
        final var p1 = new Philosopher("p1", f1, f2, 1L, 1L);
        final var p2 = new Philosopher("p2", f2, f3, 1L, 1L);
        await().atMost(1, TimeUnit.SECONDS)
                .until(() -> p1.state.equals(EATING));
        await().atMost(1, TimeUnit.SECONDS)
                .until(() -> p2.state.equals(EATING));
    }
}
