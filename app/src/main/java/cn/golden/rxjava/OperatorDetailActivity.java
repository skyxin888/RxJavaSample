package cn.golden.rxjava;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by user on 16/7/19.
 */
public class OperatorDetailActivity extends BaseActivity{


    @Bind(R.id.button)
    Button mButton;

    @Bind(R.id.result)
    TextView mResult;

    @Bind(R.id.title)
    TextView mTitle;

    @Bind(R.id.description)
    TextView mDescription;

    private int category;

    private StringBuilder testResult = new StringBuilder();
    private String description;
    private String title;

    private static  final String TAG = "OperatorDetailActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_detail);
        ButterKnife.bind(this);
        category = getIntent().getIntExtra("position",-1);
        if (category != -1){
            description = getResources().getStringArray(R.array.operator_description)[category];
            title =getResources().getStringArray(R.array.operator_title)[category];
            initViews();
        }
    }

    private void initViews() {
        switch (category){
            case Opertor.just:
                mButton.setText("just");
                break;
            case Opertor.from:
                mButton.setText("from");
                break;
            case Opertor.repeat:
                mButton.setText("repeat");
                break;
            case Opertor.repeatWhen:
                mButton.setText("repeatWhen");
                break;
            case Opertor.create:
                mButton.setText("create");
                break;
            case Opertor.defer:
                mButton.setText("defer");
                break;
            case Opertor.ranger:
                mButton.setText("ranger");
                break;
            case Opertor.interval:
                mButton.setText("interval");
                break;
            case Opertor.timer:
                mButton.setText("timer");
                break;
            case Opertor.empty:
                mButton.setText("empty");
                break;
            case Opertor.error:
                mButton.setText("error");
                break;
            case Opertor.never:
                mButton.setText("never");
                break;
            default:
                break;
        }
        mTitle.setText(title);
        mDescription.setText(description);
    }

    @OnClick(R.id.button)
    void onClickEvent(){
        switch (category){
            case Opertor.just:
                RxforJust();
                break;
            case Opertor.from:
                RxforFrom();
                break;
            case Opertor.repeat:
                RxforRepeat();
                break;
            case Opertor.repeatWhen:
                RxforRepeatWhen();
                break;
            case Opertor.create:
                RxforCreate();
                break;
            case Opertor.defer:
                RxforDefer();
            case Opertor.ranger:
                RxforRanger();
                break;
            case Opertor.interval:
                RxforInterval();
                break;
            case Opertor.timer:
                RxforTimer();
                break;
            case Opertor.empty:
                RxforEmpty();
                break;
            case Opertor.error:
                RxforError();
                break;
            case Opertor.never:
                RxforNever();
                break;
            default:
                break;
        }
    }




    private void RxforJust() {
        Observable observable = Observable.just("observable", "just");
        TestSubscriber<String> subscriber = new TestSubscriber<>();
        compositeSubscription .add(observable.subscribe(subscriber));
        mResult.setText(subscriber.getResult());
    }


    private void RxforFrom() {
        Integer[] items = { 0, 1, 2, 3, 4, 5 };
        Observable myObservable = Observable.from(items);
//        compositeSubscription.add(myObservable.subscribe(
//                new Action1<Integer>() {
//                    @Override
//                    public void call(Integer item) {
//                        testResult.append("onNext :").append(String.valueOf(item)).append("\n");
//                    }
//                },
//                new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable error) {
//                        testResult.append("error :").append(error.getMessage());
//                    }
//                },
//                new Action0() {
//                    @Override
//                    public void call() {
//                        testResult.append("onCompleted");
//                        mResult.setText(testResult);
//                        testResult.setLength(0);
//                    }
//                }
//        ));
        TestSubscriber test = new TestSubscriber();
        compositeSubscription.add(myObservable.subscribe(test));
        mResult.setText(test.getResult());
    }

    private void RxforRepeat() {
        Integer[] items = { 0, 1, 2, 3, 4, 5 };
        Observable observable = Observable.from(items).repeat(2);
        TestSubscriber<String> subscriber = new TestSubscriber<>();
        compositeSubscription .add(observable.subscribe(subscriber));
        mResult.setText(subscriber.getResult());
    }

    private void RxforRepeatWhen() {
        Observable observable = Observable.just(1,2,3).repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Void> observable) {
                return Observable.timer(6, TimeUnit.SECONDS);//间隔6秒重新订阅一次
            }
        });
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onStart() {
                super.onStart();
                testResult.append("onStart \n");
                Log.d(TAG,"onStart");
            }

            @Override
            public void onCompleted() {
                Log.d(TAG,"onCompleted");
                testResult.append("onCompleted \n");
                mResult.setText(testResult);
                testResult.setLength(0);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG,"onNext : " + String.valueOf(integer));
                testResult.append("onNext :").append(String.valueOf(integer)).append("\n");
                mResult.setText(testResult);
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    private void RxforCreate() {
        Observable observable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    for (int i = 1; i < 5; i++) {
                        subscriber.onNext(i);
                    }
                    subscriber.onCompleted();
                }
            }

        });
        TestSubscriber subscriber = new TestSubscriber<>();
        compositeSubscription.add(observable.subscribe(subscriber));
        mResult.setText(subscriber.getResult());
    }

    private void RxforDefer() {
        final TestData testData = new TestData();
        testData.setValue("11");
        Observable<TestData> observable = Observable.defer(new Func0<Observable<TestData>>() {
            @Override
            public Observable<TestData> call() {
                return Observable.just(testData);
            }
        });

        testData.setValue("13");
        Subscriber<TestData> subscriber = new Subscriber<TestData>() {

            @Override
            public void onStart() {
                super.onStart();
                testResult.setLength(0);
                testResult.append("onStart").append("\n");
                Log.d(TAG,"onStart");
            }
            @Override
            public void onCompleted() {
                testResult.append("onCompleted").append("\n");
                Log.d(TAG,"onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                testResult.append("onError").append(e.getMessage()).append("\n");
                Log.d(TAG,"onError");

            }

            @Override
            public void onNext(TestData testData) {
                testResult.append("onNext").append(testData.getValue()).append("\n");
                Log.d(TAG,"onNext : " + testData.getValue());
            }
        };

        compositeSubscription.add(observable.subscribe(subscriber));
        mResult.setText(testResult);
    }

    private void RxforRanger() {
        Observable observable = Observable.range(3,2);
        TestSubscriber subscriber = new TestSubscriber();
        compositeSubscription.add(observable.subscribe(subscriber));
        mResult.setText(subscriber.getResult());
    }

    private void RxforInterval() {
        long time = 1000;
        Observable observable = Observable.interval(time, TimeUnit.MILLISECONDS);
        Subscriber<Long> subscriber = new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                testResult.append("onCompleted");
                mResult.setText(testResult);
            }

            @Override
            public void onError(Throwable e) {
                testResult.append("onError " + e.getMessage());
                mResult.setText(testResult);
            }

            @Override
            public void onNext(Long item) {
                testResult.append("onNext : ").append(String.valueOf(item)).append("\n");
                mResult.setText(testResult);
            }
        };
        compositeSubscription.add(observable.observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber));
    }

    private void RxforTimer() {
        Observable observable = Observable.timer(2, TimeUnit.SECONDS);
        final TestSubscriber subscriber = new TestSubscriber();
        compositeSubscription.add(observable.subscribe(subscriber));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mResult.setText(subscriber.getResult());
            }
        },3000);
    }

    private void RxforEmpty() {
        Observable observable = Observable.empty();
        TestSubscriber subscriber = new TestSubscriber();
        compositeSubscription.add(observable.subscribe(subscriber));
        mResult.setText(subscriber.getResult());
    }
    private void RxforError() {
        Throwable throwable = new Throwable("RxforError");
        Observable observable = Observable.error(throwable);
        TestSubscriber subscriber = new TestSubscriber();
        compositeSubscription.add(observable.subscribe(subscriber));
        mResult.setText(subscriber.getResult());
    }
    private void RxforNever() {
        Observable observable = Observable.never();
        TestSubscriber subscriber = new TestSubscriber();
        compositeSubscription.add(observable.subscribe(subscriber));
        mResult.setText(subscriber.getResult());
    }

    public class TestSubscriber<T> extends Subscriber<T>{
        StringBuilder result = new StringBuilder();
        @Override
        public void onStart() {
            super.onStart();
            result.setLength(0);
            result.append("onStart").append("\n");
            Log.d(TAG,"onStart");
        }

        @Override
        public void onCompleted() {
            result.append("onCompleted").append("\n");
            Log.d(TAG,"onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            result.append("onError : ").append(e.getMessage()).append("\n");
            Log.d(TAG,"onError");
        }

        @Override
        public void onNext(T t) {
            result.append("onNext : ").append(t.toString()).append("\n");
            Log.d(TAG,"onNext : "+t.toString());
        }

        public StringBuilder getResult(){
            return result;
        }
    }

    public class TestData {
        private String value;

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
        public Observable<String> valueObservable() {
            return Observable.just(value);
        }
    }
}
