package com.github.haroldjcastillo.dinner;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.*;
import org.junit.jupiter.api.Test;

class ForkTest {

  @Test
  void testForkLockPickUp() {
    final var fork = new Fork(1);
    final var t1 = new Thread(() -> assertTrue(fork.pickUp()), "t1");
    final var t2 = new Thread(() -> assertFalse(fork.pickUp()), "t2");
    try {
      t1.start();
      t2.start();
      t1.join();
      t2.join();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  @Test
  void testForkLockPutDown() {
    final var fork = new Fork(1);
    final var t1 = new Thread(schedulePickUp(fork), "t1");
    try {
      t1.start();
      t1.join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    await().atMost(1, TimeUnit.SECONDS).until(fork::pickUp);
  }

  private Runnable schedulePickUp(Fork fork) {
    return () -> {
      if (fork.pickUp()) {
        try {
          TimeUnit.MILLISECONDS.sleep(500);
          fork.putDown();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new IllegalStateException(e);
        }
      }
    };
  }
}
