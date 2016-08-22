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
import cn.golden.rxjava.DividerItemDecoration;
import cn.golden.rxjava.MainActivity;
import cn.golden.rxjava.OperatorCreateActivity;
import cn.golden.rxjava.OperatorTransformActivity;
import cn.golden.rxjava.R;
import cn.golden.rxjava.adapter.BaseAdapter;
import cn.golden.rxjava.adapter.BaseViewHolder;
import cn.golden.rxjava.adapter.OnItemClickListener;

/**
 * Created by user on 16/7/15.
 */
public class OperatorListFragment extends BaseFragment{


    public  static final String TAG = "OperatorListFragment";

    public String[] operator_create = {"just()", "from()", "repeat()", "repeatWhen()", "create()", "defer()"
            , "ranger()", "interval()", "timer()", "empty()", "error()", "never()"};

    public String[] operator_transform = {"map( )","flatMap( )","switchMap( )","scan( )","groupBy( )","buffer( )","window( )","cast( )"};
    private BaseAdapter mAdapter;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private String[] operator;
    private int mType = -1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    public static OperatorListFragment newInstance(int type) {
        OperatorListFragment fragment = new OperatorListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mType = getArguments().getInt("type");
        switch (mType) {
            case MainActivity.TYPE_CREATE:
                operator = operator_create;
                break;
            case MainActivity.TYPE_TRANSFORM:
                operator = operator_transform;
                break;
            default:
                break;
        }
        initViews();
    }

    private void initViews() {
        mAdapter = new BaseAdapter() {
            @Override
            protected void onBindView(BaseViewHolder holder, int position) {
                TextView tv = holder.getView(R.id.item_name_tv);
                tv.setText(operator[position]);
            }

            @Override
            protected int getLayoutID(int position) {
                return R.layout.item_text;
            }

            @Override
            public int getItemCount() {
                return operator.length;
            }
        };
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                switch (mType) {
                    case MainActivity.TYPE_CREATE:
                        intent.setClass(getActivity(),OperatorCreateActivity.class);
                        intent.putExtra("position",position);
                        break;
                    case MainActivity.TYPE_TRANSFORM:
                        intent.setClass(getActivity(),OperatorTransformActivity.class);
                        intent.putExtra("position",position);
                        break;
                }
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
