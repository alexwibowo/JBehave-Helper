package com.github.alexwibowo.jbehave.steps;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * User: alexwibowo
 * Date: 27/10/12
 * Time: 10:56 AM
 */
@Component
public class ResultHolder {

    private final ThreadLocal<AtomicLong> lastNumber = new ThreadLocal<AtomicLong>(){
        @Override
        protected AtomicLong initialValue() {
            return new AtomicLong(0L);
        }
    };

    public synchronized AtomicLong get() {
        return lastNumber.get();
    }

    public synchronized void set(AtomicLong value) {
        lastNumber.set(value);
    }
}
