package wen.flowlayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import wen.view.flowlayout.FlowLayout;
import wen.view.flowlayout.TagAdapter;
import wen.view.flowlayout.TagFlowLayout;

/**
 * Created by zhy on 15/9/10.
 */
public class SingleChooseFragment extends Fragment {
    private String[] mVals = new String[]
            {"Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView", "TextView", "Helloworld",
                    "Android", "Weclome Hello", "Button Text", "TextView"};

    private TagFlowLayout mFlowLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_single_choose, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final LayoutInflater mInflater = LayoutInflater.from(getActivity());
        mFlowLayout = view.findViewById(R.id.id_flowlayout);
        mFlowLayout.setMaxSelectCount(1);
        mFlowLayout.setAdapter(new TagAdapter<String>(mVals) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, mFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });

        mFlowLayout.setOnTagClickListener((view1, position, parent) -> {
            Toast.makeText(getActivity(), mVals[position], Toast.LENGTH_SHORT).show();
            //view.setVisibility(View.GONE);
        });


        mFlowLayout.setOnSelectListener(selectPosSet ->
                getActivity().setTitle("choose:" + selectPosSet.toString()));
    }
}
