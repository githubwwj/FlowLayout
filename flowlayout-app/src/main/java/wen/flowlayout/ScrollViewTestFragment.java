package wen.flowlayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Set;

import wen.view.flowlayout.FlowLayout;
import wen.view.flowlayout.TagAdapter;
import wen.view.flowlayout.TagFlowLayout;

/**
 * Created by zhy on 15/9/10.
 */
public class ScrollViewTestFragment extends Fragment {
    private String[] mVals = new String[]
            {"Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome Hello", "Button Text", "TextView", "Hello",
                    "张三", "李四", "王五", "赵六", "王麦子",
                    "天琪", "田七", "王五", "赵六", "王麻子",
                    "喜鹊", "哈哈哈哈哈", "Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome Hello", "Button Text", "TextView", "Hello",
                    "张三", "李四", "王五", "赵六", "王麦子",
                    "天琪", "田七", "王五", "赵六", "王麻子",
                    "喜鹊", "哈哈哈哈哈", "Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome Hello", "Button Text", "TextView", "Hello",
                    "张三", "李四", "王五", "赵六", "王麦子",
                    "天琪", "田七", "王五", "赵六", "王麻子",
                    "喜鹊", "哈哈哈哈哈", "Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome Hello", "Button Text", "TextView", "Hello",
                    "张三", "李四", "王五", "赵六", "王麦子",
                    "天琪", "田七", "王五", "赵六", "王麻子",
                    "喜鹊", "哈哈哈哈哈"
            };

    private TagFlowLayout mFlowLayout;
    private TagAdapter<String> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sc, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final LayoutInflater mInflater = LayoutInflater.from(getActivity());
        mFlowLayout = view.findViewById(R.id.id_flowlayout);
        mFlowLayout.setSelectedList(1, 3, 5, 7, 8, 9);
        mFlowLayout.getSelectedList();
//        mFlowLayout.setMaxSelectCount(3);
        mFlowLayout.setAdapter(mAdapter = new TagAdapter<String>(mVals) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, mFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });

        mFlowLayout.setOnTagClickListener((view1, position, parent) -> {
            //Toast.makeText(getActivity(), mVals[position], Toast.LENGTH_SHORT).show();
            //view.setVisibility(View.GONE);
        });

        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                getActivity().setTitle("choose:" + selectPosSet.toString());
            }
        });

    }
}
