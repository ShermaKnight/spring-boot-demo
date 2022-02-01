package org.example.concurrent;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

@DisplayName("ForkJoin测试用例")
public class ForkJoinTest {

    @Test
    @SneakyThrows
    @DisplayName("斐波拉契数列")
    public void fibonacci() {
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Integer> task = pool.submit(new Fibonacci(10));
        System.out.println(task.get());
    }



    @Test
    @SneakyThrows
    @DisplayName("斐波拉契数列-正向")
    public void fibonacciPositive() {
        System.out.println(fibonacciExecute(100));
    }

    private BigInteger fibonacciExecute(int n) {
        if (n <= 1) {
            return new BigInteger(String.valueOf(n));
        }
        int index = 2;
        BigInteger first = new BigInteger("1");
        BigInteger second = new BigInteger("1");
        BigInteger fibonacci = new BigInteger("0");
        while (index <= n) {
            fibonacci = first.add(second);
            System.out.println(index + ": " + fibonacci);
            first = second;
            second = fibonacci;
            index++;
        }
        return fibonacci;
    }

    private class Fibonacci extends RecursiveTask<Integer> {

        private Integer n;

        public Fibonacci(Integer n) {
            this.n = n;
        }

        @Override
        protected Integer compute() {
            if (n <= 1) {
                return n;
            }
            Fibonacci f1 = new Fibonacci(n - 1);
            f1.fork();
            Fibonacci f2 = new Fibonacci(n - 2);
            f2.fork();
            return f1.join() + f2.join();
        }
    }
}
