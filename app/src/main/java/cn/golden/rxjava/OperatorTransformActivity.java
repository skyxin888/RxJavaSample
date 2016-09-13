package cn.golden.rxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

/**
 * Created by user on 16/8/4.
 */
public class OperatorTransformActivity extends BaseActivity{

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
            description = getResources().getStringArray(R.array.operator_transform_description)[category];
            title = getResources().getStringArray(R.array.operator_transform_title)[category];
            initViews();
        }
    }
    private void initViews(){
        switch (category){
            case Opertor.Transform.map:
                mButton.setText("map");
                break;
            case Opertor.Transform.flatmap:
                mButton.setText("flatmap");
                break;
            case Opertor.Transform.switchMap:
                mButton.setText("switchMap");
                break;
            case Opertor.Transform.scan:
                mButton.setText("scan");
                break;
            case Opertor.Transform.groupBy:
                mButton.setText("groupBy");
                break;
            case Opertor.Transform.buffer:
                mButton.setText("buffer");
                break;
            case Opertor.Transform.window:
                mButton.setText("window");
                break;
            case Opertor.Transform.cast:
                mButton.setText("cast");
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
            case Opertor.Transform.map:
                RxforMap();
                break;
            case Opertor.Transform.flatmap:
                RxforFlatMap();
                break;
            case Opertor.Transform.switchMap:
                RxforSwitchMap();
                break;
            case Opertor.Transform.scan:
                RxforScan();
                break;
            case Opertor.Transform.groupBy:
                RxforGroupby();
                break;
            case Opertor.Transform.buffer:
                RxforBuffer();
                break;
            case Opertor.Transform.window:
                RxforWindow();
                break;
            case Opertor.Transform.cast:
                RxforCast();
                break;
            default:
                break;
        }
    }

    private void RxforCast() {
        Observable.range(1,5).cast(Integer.class).subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                mBuilder.append("cast onComplete()\n");
                mResult.setText(mBuilder.toString());
                mBuilder.setLength(0);
            }

            @Override
            public void onError(Throwable e) {
                mBuilder.append("cast onError()\n");
                mResult.setText(mBuilder.toString());
                mBuilder.setLength(0);
            }

            @Override
            public void onNext(Integer i) {
                mBuilder.append("cast onNext() : ").append(i).append("\n");
                mResult.setText(mBuilder.toString());
            }
        });
    }

    private void RxforWindow() {
        Observable.interval(1, TimeUnit.SECONDS).take(12)
                .window(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Observable<Long>>() {
                    @Override
                    public void call(Observable<Long> observable) {
                        mBuilder.append("begin......").append("\n");
                        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                mBuilder.append("window Next : " + aLong).append("\n");
                                mResult.setText(mBuilder.toString());
                            }
                        });
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mBuilder.append("window onComplete()");
                        mResult.setText(mBuilder.toString());
                        mBuilder.setLength(0);
                    }
                });
    }

    private void RxforBuffer() {
        Observable.range(1, 5).buffer(5, 1).subscribe(new Observer<List<Integer>>() {
            @Override
            public void onCompleted() {
                mBuilder.append("onCompleted()").append("\n");
                mResult.setText(mBuilder.toString());
                mBuilder.setLength(0);
            }

            @Override
            public void onError(Throwable e) {
                mBuilder.append("onError()").append("\n");
                mResult.setText(mBuilder.toString());
                mBuilder.setLength(0);

            }

            @Override
            public void onNext(List<Integer> strings) {
                mBuilder.append(strings).append("\n");
            }
        });
    }

    private void RxforGroupby() {
        Observable.range(0, 10).groupBy(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
//                return  2;
                return integer % 3;////分成0，1，2 三个小组
            }
        }).subscribe(new Observer<GroupedObservable<Integer, Integer>>() {
            @Override
            public void onCompleted() {
                mBuilder.append("onCompleted()").append("\n");
                mResult.setText(mBuilder.toString());
                mBuilder.setLength(0);
            }

            @Override
            public void onError(Throwable e) {
                mBuilder.append("onError()").append("\n");
            }

            @Override
            public void onNext(final GroupedObservable<Integer, Integer> integerIntegerGroupedObservable) {
                integerIntegerGroupedObservable.subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        mBuilder.append("inter onCompleted()").append("\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mBuilder.append("inter onError()").append("\n");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        mBuilder.append("inter onNext()").append(integerIntegerGroupedObservable.getKey())
                                .append("  value:").append(integer).append("\n");
                        mResult.setText(mBuilder.toString());

                    }
                });
            }
        });
    }

    private void RxforScan() {
        Observable.just(10,30,50).scan(new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                mBuilder.append("onCompleted");
                mResult.setText(mBuilder.toString());
                mBuilder.setLength(0);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                mBuilder.append("switchMap Next:").append(integer).append("\n");
            }
        });
    }

    private void RxforSwitchMap() {
        //switchMap
        Observable.just(10, 30, 50).switchMap(new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {
                //10的延迟执行时间为200毫秒、20和30的延迟执行时间为180毫秒
                int delay = 200;
                if (integer > 10)
                    delay = 180;

                return Observable.from(new Integer[]{integer, integer / 2}).delay(delay, TimeUnit.MILLISECONDS);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                mBuilder.append("switchMap Next:").append(integer).append("\n");
                mResult.setText(mBuilder.toString());
            }
        });
    }

    private void RxforFlatMap() {
        //flatMap
        Observable.just(10, 30, 50).flatMap(new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {
                //10的延迟执行时间为200毫秒、20和30的延迟执行时间为180毫秒
                int delay = 200;
                if (integer > 10)
                    delay = 180;

                return Observable.from(new Integer[]{integer, integer / 2}).delay(delay, TimeUnit.MILLISECONDS);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                mBuilder.append("flatMap Next:").append(integer).append("\n");
                mResult.setText(mBuilder.toString());
            }
        });
        //concatMap
        Observable.just(10, 30, 50).concatMap(new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {
                //10的延迟执行时间为200毫秒、20和30的延迟执行时间为180毫秒
                int delay = 200;
                if (integer > 10)
                    delay = 180;

                return Observable.from(new Integer[]{integer, integer / 2}).delay(delay, TimeUnit.MILLISECONDS);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                mBuilder.append("concatMap Next:").append(integer).append("\n");
                mResult.setText(mBuilder.toString());
            }
        });
    }

    private void RxforMap() {
        Observable a = Observable.just("hello");
        Observable b = a.map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return s +",map";
            }
        });
        b.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                mBuilder.append("map Next:").append(s);
                mResult.setText(mBuilder.toString());
                mBuilder.setLength(0);
            }
        });
    }

}
