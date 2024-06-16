package com.github.haroldjcastillo.dinner;

import static com.github.haroldjcastillo.dinner.Philosopher.PhilosopherState.EATING;
import static com.github.haroldjcastillo.dinner.Philosopher.PhilosopherState.THINKING;

import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Philosopher {

  private static final Logger log = LoggerFactory.getLogger(Philosopher.class);
  private final PhilosopherMonitor monitor;
  private final long thinkingDelaySeconds;
  private final long eatingDelaySeconds;
  PhilosopherState state;
  private final String name;
  private final Fork rightFork;
  private final Fork leftFork;

  public Philosopher(String name, Fork rightFork, Fork leftFork) {
    this(name, rightFork, leftFork, 2L, 5L);
  }

  public Philosopher(
      String name,
      Fork rightFork,
      Fork leftFork,
      long thinkingDelaySeconds,
      long eatingDelaySeconds) {
    this.name = name;
    this.rightFork = rightFork;
    this.leftFork = leftFork;
    this.state = THINKING;
    this.thinkingDelaySeconds = thinkingDelaySeconds;
    this.eatingDelaySeconds = eatingDelaySeconds;
    this.monitor = new PhilosopherMonitor(this);
    monitor.setDaemon(true);
    monitor.start();
  }

  public enum PhilosopherState {
    EATING,
    THINKING
  }

  public void join() throws InterruptedException {
    this.monitor.join();
  }

  private void think() {
    log.info("{} is thinking", this);
    state = THINKING;
    doingAction(thinkingDelaySeconds);
  }

  private void eat() {
    log.info("{} is eating with {} and {}", this, leftFork, rightFork);
    state = EATING;
    doingAction(eatingDelaySeconds);
  }

  private static void doingAction(long time) {
    try {
      TimeUnit.SECONDS.sleep(time);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException(e);
    }
  }

  private static class PhilosopherMonitor extends Thread {

    private final Philosopher philosopher;

    public PhilosopherMonitor(Philosopher philosopher) {
      super(philosopher.name);
      this.philosopher = philosopher;
    }

    @Override
    public void run() {
      //noinspection InfiniteLoopStatement
      while (true) {
        if (philosopher.state.equals(THINKING) && philosopher.leftFork.pickUp()) {
          if (philosopher.rightFork.pickUp()) {
            philosopher.eat();
            // The philosophers will start eating before the current philosopher start thinking.
            philosopher.leftFork.putDown();
            philosopher.rightFork.putDown();
            philosopher.think();
          } else {
            philosopher.leftFork.putDown();
          }
        }
      }
    }
  }

  @Override
  public String toString() {
    return "Philosopher{" + name + '}';
  }
}
