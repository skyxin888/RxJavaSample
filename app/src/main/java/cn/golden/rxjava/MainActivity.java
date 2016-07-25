package cn.golden.rxjava;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jakewharton.rxbinding.support.design.widget.RxTabLayout;

import com.jakewharton.rxbinding.support.design.widget.TabLayoutSelectionEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.golden.rxjava.fragment.CreateFragment;
import cn.golden.rxjava.fragment.TestFragment;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolBar)
    Toolbar mToolBar;

    @Bind(android.R.id.tabs)
    TabLayout mTabLayout;

    @Bind(R.id.viewPager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        setSupportActionBar(mToolBar);

        TabLayout.Tab tabOne = mTabLayout.newTab().setText("创建操作符");
        TabLayout.Tab tabTwo = mTabLayout.newTab().setText("TabTwo");
        TabLayout.Tab tabThree = mTabLayout.newTab().setText("TabThree");

        mTabLayout.addTab(tabOne);
        mTabLayout.addTab(tabTwo);
        mTabLayout.addTab(tabThree);


        MyViewPagerAdapter viewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(CreateFragment.newInstance(), tabOne.getText().toString());//添加Fragment
        viewPagerAdapter.addFragment(TestFragment.newInstance(), tabTwo.getText().toString());
        viewPagerAdapter.addFragment(TestFragment.newInstance(), tabThree.getText().toString());





        mViewPager.setAdapter(viewPagerAdapter);//设置适配器
        mTabLayout.setupWithViewPager(mViewPager);

        forSelections();
//        forSelect();

//        forSelectionEvents();
    }

    private void forSelectionEvents() {
        TestObserver<TabLayoutSelectionEvent> o = new TestObserver<>();
        Subscription subscription = RxTabLayout.selectionEvents(mTabLayout).subscribe(o);
    }

    private void forSelect() {
        Action1<? super Integer> action = RxTabLayout.select(mTabLayout);
        action.call(2);
    }

    private void forSelections() {
        RxTabLayout.selections(mTabLayout).subscribe(new Observer<TabLayout.Tab>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(TabLayout.Tab tab) {
                if (tab.isSelected()) {
                    mViewPager.setCurrentItem(tab.getPosition());
                }
            }
        });
    }


    public class MyViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();//添加的Fragment的集合
        private final List<String> mFragmentsTitles = new ArrayList<>();//每个Fragment对应的title的集合
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        /**
         * @param fragment      添加Fragment
         * @param fragmentTitle Fragment的标题，即TabLayout中对应Tab的标题
         */
        public void addFragment(Fragment fragment, String fragmentTitle) {
            mFragments.add(fragment);
            mFragmentsTitles.add(fragmentTitle);
        }

        @Override
        public Fragment getItem(int position) {
            //得到对应position的Fragment
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            //返回Fragment的数量
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //得到对应position的Fragment的title

            return mFragmentsTitles.get(position);
        }
    }

}
