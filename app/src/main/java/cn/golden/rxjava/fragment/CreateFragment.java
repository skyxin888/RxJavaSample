package cn.golden.rxjava.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.golden.rxjava.DividerItemDecoration;
import cn.golden.rxjava.OperatorDetailActivity;
import cn.golden.rxjava.R;
import cn.golden.rxjava.adapter.BaseAdapter;
import cn.golden.rxjava.adapter.BaseViewHolder;
import cn.golden.rxjava.adapter.OnItemClickListener;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by user on 16/7/15.
 */
public class CreateFragment extends BaseFragment{


    public  static final String TAG = "CreateFragment";

    public String[] operator_create = {"just()", "from()", "repeat()", "repeatWhen()", "create()", "defer()"
            , "ranger()", "interval()", "timer()", "empty()", "error()", "never()"};

    private BaseAdapter mAdapter;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    public static CreateFragment newInstance() {
        return new CreateFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initViews();
    }

    private void initViews() {
        mAdapter = new BaseAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, int position) {
                TextView tv = holder.getView(R.id.item_name_tv);
                tv.setText(operator_create[position]);
            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.item_text;
            }

            @Override
            public int getItemCount() {
                return operator_create.length;
            }
        };
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG,"onItemClick ");
                Intent intent = new Intent(getActivity(),OperatorDetailActivity.class);
                intent.putExtra("position",position);
                getActivity().startActivity(intent);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        manager.setAutoMeasureEnabled(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
