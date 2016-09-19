package cn.golden.rxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;

/**
 * Created by user on 16/8/4.
 */
public class OperatorCombiningActivity extends BaseActivity{

    @Bind(R.id.title)
    TextView mTitle;

    @Bind(R.id.description)
    TextView mDescription;

    @Bind(R.id.button)
    Button mButton;

    @Bind(R.id.result)
    TextView mResult;

    private int category;
    private String description;
    private String title;
    private StringBuilder mBuilder = new StringBuilder();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);
        ButterKnife.bind(this);
        category = getIntent().getIntExtra("position", -1);
        if (category != -1) {
            description = getResources().getStringArray(R.array.operator_combining_description)[category];
            title = getResources().getStringArray(R.array.operator_combining_title)[category];
            initViews();
        }
    }
    private void initViews(){
        switch (category){
            case Opertor.Combining.startWith:
                mButton.setText("startWith");
                break;
            case Opertor.Combining.merge:
                mButton.setText("merge");
                break;
            case Opertor.Combining.mergeDelayError:
                mButton.setText("mergeDelayError");
                break;
            case Opertor.Combining.zip:
                mButton.setText("zip");
                break;
            case Opertor.Combining.and:
                mButton.setText("and");
                break;
            case Opertor.Combining.combineLatest:
                mButton.setText("combineLatest");
                break;
            case Opertor.Combining.join:
                mButton.setText("join");
                break;
            case Opertor.Combining.switchOnNext:
                mButton.setText("switchOnNext");
                break;
            default:
                break;
        }
        mTitle.setText(title);
        mDescription.setText(description);
        mTitle.setText(title);
        mDescription.setText(description);
    }



    @OnClick(R.id.button)
    void onClickEvent(){
        switch (category){
            case Opertor.Combining.startWith:
                RxforstartWith();
                break;
            case Opertor.Combining.merge:
                RxforMerge();
                break;
            case Opertor.Combining.mergeDelayError:
                RxforMergeDelayError();
                break;
            case Opertor.Combining.zip:
                RxforZip();
                break;
            case Opertor.Combining.and:
                RxforAnd();
                break;
            case Opertor.Combining.combineLatest:
                RxforCombineLatest();
                break;
            case Opertor.Combining.join:
                RxforJoin();
                break;
            case Opertor.Combining.switchOnNext:
                RxforSwitch();
                break;
            default:
                break;
        }
    }

    private void RxforSwitch() {
        Observable<Observable<Long>> observable = Observable.interval(0, 500, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(Long aLong) {
                        return Observable.interval(0, 200, TimeUnit.MILLISECONDS).map(new Func1<Long, Long>() {
                            @Override
                            public Long call(Long aLong) {
                                return aLong * 10;
                            }
                        }).take(5);
                    }
                }).take(2);

        Observable.switchOnNext(observable).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Long>() {
            @Override
            public void onNext(Long item) {
                mBuilder.append("onNext :").append(String.valueOf(item)).append("\n");
                mResult.setText(mBuilder);
            }

            @Override
            public void onError(Throwable error) {
                mBuilder.append("onError \n");
                mResult.setText(mBuilder.toString());
            }

            @Override
            public void onCompleted() {
                mBuilder.append("onCompleted \n");
                mResult.setText(mBuilder.toString());
                mBuilder.setLength(0);
            }
        });
    }

    private void RxforJoin() {
        //产生0,3,6,9,12数列
        Observable<Long> observable1 = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 3;
                    }
                }).take(5);

        //产生0,10,20,30,40数列
        Observable<Long> observable2 = Observable.interval(500, 1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 10;
                    }
                }).take(5);

        observable1.join(observable2, new Func1<Long, Observable<Long>>() {
            @Override
            public Observable<Long> call(Long aLong) {
                //使Observable延迟600毫秒执行
                return Observable.just(aLong).delay(600, TimeUnit.MILLISECONDS);
            }
        }, new Func1<Long, Observable<Long>>() {
            @Override
            public Observable<Long> call(Long aLong) {
                //使Observable延迟600毫秒执行
                return Observable.just(aLong).delay(600, TimeUnit.MILLISECONDS);
            }
        }, new Func2<Long, Long, Long>() {
            @Override
            public Long call(Long aLong, Long aLong2) {
                return aLong + aLong2;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
            @Override
            public void onNext(Long item) {
                mBuilder.append("onNext :").append(String.valueOf(item)).append("\n");
                mResult.setText(mBuilder);
            }

            @Override
            public void onError(Throwable error) {
                mBuilder.append("onError \n");
                mResult.setText(mBuilder.toString());
            }

            @Override
            public void onCompleted() {
                mBuilder.append("onCompleted \n");
                mResult.setText(mBuilder.toString());
                mBuilder.setLength(0);
            }
        });
    }

    private void RxforCombineLatest() {
        //产生0,3,6,9数列
        Observable<Long> observable1 = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 3;
                    }
                }).take(4);

        //产生0,10,20,30,40数列
        Observable<Long> observable2 = Observable.interval(500, 1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 10;
                    }
                }).take(5);


        Observable.combineLatest(observable1, observable2, new Func2<Long, Long, Long>() {
            @Override
            public Long call(Long aLong, Long aLong2) {
                return aLong+aLong2;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
            @Override
            public void onNext(Long item) {
                mBuilder.append("onNext :").append(String.valueOf(item)).append("\n");
                mResult.setText(mBuilder);
            }

            @Override
            public void onError(Throwable error) {
                mBuilder.append("onError \n");
                mResult.setText(mBuilder.toString());
            }

            @Override
            public void onCompleted() {
                mBuilder.append("onCompleted \n");
                mResult.setText(mBuilder.toString());
                mBuilder.setLength(0);
            }
        });
    }

    private void RxforAnd() {

    }

    private void RxforZip() {
        Observable<Integer> observable1 = Observable.just(10,20,30,40);
        Observable<Integer> observable2 = Observable.just(4, 8, 12);
        Observable.zip(observable1, observable2, new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onNext(Integer item) {
                mBuilder.append("onNext :").append(String.valueOf(item)).append("\n");
                mResult.setText(mBuilder);
            }

            @Override
            public void onError(Throwable error) {
                mBuilder.append("onError :").append(error.getMessage()).append("\n");
                mResult.setText(mBuilder.toString());
            }

            @Override
            public void onCompleted() {
                mBuilder.append("onCompleted \n");
                mResult.setText(mBuilder.toString());
                mBuilder.setLength(0);
            }
        });

    }

    private void RxforMergeDelayError() {
        //产生0,3,6数列,最后会产生一个错误
        Observable<Long> errorObservable = Observable.error(new Exception("this is end!"));
        Observable < Long > observable1 = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 3;
                    }
                }).take(3).mergeWith(errorObservable.delay(3500, TimeUnit.MILLISECONDS));

        //产生0,10,20,30,40数列
        Observable<Long> observable2 = Observable.interval(500, 1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 10;
                    }
                }).take(5);

        Observable.mergeDelayError(observable1, observable2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onNext(Long item) {
                        mBuilder.append("onNext :").append(String.valueOf(item)).append("\n");
                        mResult.setText(mBuilder);
                    }

                    @Override
                    public void onError(Throwable error) {
                        mBuilder.append("onError :").append(error.getMessage()).append("\n");
                        mResult.setText(mBuilder.toString());
                    }

                    @Override
                    public void onCompleted() {
                        mBuilder.append("onCompleted \n");
                        mResult.setText(mBuilder.toString());
                        mBuilder.setLength(0);
                    }
                });


    }

    private void RxforMerge() {
        //产生0,3,6,9,12数列
        Observable<Long> odds = Observable.interval(0,1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 3;
                    }
                }).take(5);

        //产生0,10,20,30,40数列
        Observable<Long> evens = Observable.interval(500,1000,TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 10;
                    }
                }).take(5);


        Observable.merge(odds, evens)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onNext(Long item) {
                        mBuilder.append("onNext :").append(String.valueOf(item)).append("\n");
                        mResult.setText(mBuilder);
                    }

                    @Override
                    public void onError(Throwable error) {
                        mBuilder.append("onError \n");
                        mResult.setText(mBuilder.toString());
                    }

                    @Override
                    public void onCompleted() {
                        mBuilder.append("onCompleted \n");
                        mResult.setText(mBuilder.toString());
                        mBuilder.setLength(0);
                    }
                });

    }

    private void RxforstartWith() {
        Observable observable = Observable.range(1,4).startWith(-3);
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onStart() {
                super.onStart();
                mBuilder.append("onStart \n");
            }

            @Override
            public void onCompleted() {
                mBuilder.append("onCompleted \n");
                mResult.setText(mBuilder.toString());
                mBuilder.setLength(0);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                mBuilder.append("onNext :").append(String.valueOf(integer)).append("\n");
                mResult.setText(mBuilder);
            }
        };
        compositeSubscription.add(observable.subscribe(subscriber));
    }

}
