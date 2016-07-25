package cn.golden.rxjava.fragment;

import android.support.v4.app.Fragment;

import rx.Subscription;

/**
 * Created by user on 16/7/14.
 */
public class BaseFragment extends Fragment{

    protected Subscription subscription;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
    }

    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

}
