package com.github.haroldjcastillo.dinner;

import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fork {

  private static final Logger log = LoggerFactory.getLogger(Fork.class);

  private final int num;
  private final ReentrantLock lock;

  public Fork(int num) {
    this.num = num;
    this.lock = new ReentrantLock(true);
  }

  public boolean isInUse() {
    return lock.isLocked();
  }

  public boolean pickUp() {
    final var pickUp = lock.tryLock();
    if (pickUp) log.debug("fork {} pick up", this.num);
    return pickUp;
  }

  public void putDown() {
    if (isInUse()) {
      lock.unlock();
      log.debug("fork {} put down", this.num);
    }
  }

  @Override
  public String toString() {
    return "Fork{" + num + '}';
  }
}
