package cn.golden.rxjava;

import android.util.Log;

import java.util.NoSuchElementException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import rx.Observer;

/**
 * Created by user on 16/7/15.
 */
public class TestObserver<T> implements Observer<T> {
    private static final String TAG = "TestObserver";

    private final BlockingDeque<Object> events = new LinkedBlockingDeque<>();

    @Override public void onCompleted() {
        Log.v(TAG, "onCompleted");
        events.addLast(new OnCompleted());
    }

    @Override public void onError(Throwable e) {
        Log.v(TAG, "onError", e);
        events.addLast(new OnError(e));
    }

    @Override public void onNext(T t) {
        Log.v(TAG, "onNext " + t);
        events.addLast(new OnNext(t));
    }

    private <E> E takeEvent(Class<E> wanted) {
        Object event;
        try {
            event = events.pollFirst(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (event == null) {
            throw new NoSuchElementException(
                    "No event found while waiting for " + wanted.getSimpleName());
        }
        return wanted.cast(event);
    }

    public T takeNext() {
        OnNext event = takeEvent(OnNext.class);
        return event.value;
    }

    public Throwable takeError() {
        return takeEvent(OnError.class).throwable;
    }

    public void assertOnCompleted() {
        takeEvent(OnCompleted.class);
    }

    public void assertNoMoreEvents() {
        try {
            Object event = takeEvent(Object.class);
            throw new IllegalStateException("Expected no more events but got " + event);
        } catch (NoSuchElementException ignored) {
        }
    }

    private final class OnNext {
        final T value;

        private OnNext(T value) {
            this.value = value;
        }

        @Override public String toString() {
            return "OnNext[" + value + "]";
        }
    }

    private final class OnCompleted {
        @Override public String toString() {
            return "OnCompleted";
        }
    }

    private final class OnError {
        private final Throwable throwable;

        private OnError(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override public String toString() {
            return "OnError[" + throwable + "]";
        }
    }
}
