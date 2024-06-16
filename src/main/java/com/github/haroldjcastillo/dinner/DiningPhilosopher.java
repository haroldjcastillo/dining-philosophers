package com.github.haroldjcastillo.dinner;

public class DiningPhilosopher {

  public static void main(String[] args) {
    final var f1 = new Fork(1);
    final var f2 = new Fork(2);
    final var f3 = new Fork(3);
    final var f4 = new Fork(4);
    final var f5 = new Fork(5);

    final var p1 = new Philosopher("1-Socrates", f1, f2);
    final var p2 = new Philosopher("2-Plato", f2, f3);
    final var p3 = new Philosopher("3-Aristotle", f3, f4);
    final var p4 = new Philosopher("4-Confucius", f4, f5);
    final var p5 = new Philosopher("5-Epictetus", f5, f1);

    try {
      p1.join();
      p2.join();
      p3.join();
      p4.join();
      p5.join();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException(e);
    }
  }
}
