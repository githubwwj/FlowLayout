package wen.view.flowlayout;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 标签流式布局
 * 选中数据在这个类中
 */
public class TagFlowLayout<T> extends FlowLayout implements TagAdapter.OnDataChangedListener {

    private TagAdapter<T> mTagAdapter;
    /**
     * -1 为不限制数量
     * 具体数字为能够选中的标签View数量
     */
    private int mSelectedMax = -1;//-1为不限制数量
    private static final String TAG = "FlowLayout";

    /**
     * 记录选中的View
     */
    private final Set<Integer> mSelectedView = new HashSet<>();

    private OnSelectListener mOnSelectListener;
    private OnTagClickListener mOnTagClickListener;

    /**
     * 清除选中的视图会自动更新适配器
     */
    public void clearSelectView() {
        mSelectedView.clear();
        if (null == mTagAdapter) {
            return;
        }
        changeAdapter();
    }

    public void setSelectedList(int... poses) {
        mSelectedView.clear();
        for (int pos : poses) {
            // -1 不限量
            if (mSelectedMax == -1) {
                mSelectedView.add(pos);
            } // 限制数量,大于允许的数据就不再增加了
            else if (mSelectedMax > mSelectedView.size()) {
                mSelectedView.add(pos);
            } else {
                break;
            }
        }
        if (null == mTagAdapter) {
            return;
        }
        changeAdapter();
    }

    public void setSelectedList(Set<Integer> set) {
        mSelectedView.clear();
        if (set != null) {
            for (Integer pos : set) {
                // -1 不限量
                if (mSelectedMax == -1) {
                    mSelectedView.add(pos);
                    // 限制数量,大于允许的数据就不再增加了
                } else if (mSelectedMax > mSelectedView.size()) {
                    mSelectedView.add(pos);
                } else {
                    break;
                }
            }
        }
        if (null == mTagAdapter) {
            return;
        }
        changeAdapter();
    }

    /**
     * 选中一组标签监听事件
     */
    public interface OnSelectListener {
        /**
         * @param selectPosSet 选中的位置集合
         */
        void onSelected(Set<Integer> selectPosSet);

        /**
         * 最多能够选择几个标签
         */
        default void onSelectedMax(int selectedMax) {
        }
    }

    /**
     * 点击某个标签的监听事件
     */
    public interface OnTagClickListener {
        /**
         * @param view     被点击的标签
         * @param position 被点击的标签位置
         * @param parent   标签父控件
         */
        void onTagClick(View view, int position, FlowLayout parent);
    }

    public TagFlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TagFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagFlowLayout(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            TagView tagView = (TagView) getChildAt(i);
            if (tagView.getVisibility() == GONE) {
                continue;
            }
            if (tagView.getTagView().getVisibility() == GONE) {
                tagView.setVisibility(GONE);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        mOnTagClickListener = onTagClickListener;
    }

    public void setAdapter(TagAdapter<T> adapter) {
        this.setAdapter(adapter, null);
    }

    public void setAdapter(TagAdapter<T> adapter, Set<Integer> set) {
        mTagAdapter = adapter;
        mTagAdapter.setOnDataChangedListener(this);
        mSelectedView.clear();
        if (set != null) {
            mSelectedView.addAll(set);
        }
        changeAdapter();
    }

    private void changeAdapter() {
        removeAllViews();
        TagAdapter<T> adapter = mTagAdapter;
        TagView tagViewContainer;
        for (int i = 0; i < adapter.getCount(); i++) {
            View tagView = adapter.getView(this, i, adapter.getItem(i));

            tagViewContainer = new TagView(getContext());
            tagView.setDuplicateParentStateEnabled(true);
            if (tagView.getLayoutParams() != null) {
                tagViewContainer.setLayoutParams(tagView.getLayoutParams());
            } else {
                MarginLayoutParams lp = new MarginLayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                lp.setMargins(dip2px(5), dip2px(5),
                        dip2px(5), dip2px(5));
                tagViewContainer.setLayoutParams(lp);
            }
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            tagView.setLayoutParams(lp);
            tagViewContainer.addView(tagView);
            addView(tagViewContainer);

            if (mSelectedView.contains(i)) {
                setChildChecked(i, tagViewContainer);
            }

            if (mTagAdapter.setSelected(i, adapter.getItem(i))) {
                mSelectedView.add(i);
                setChildChecked(i, tagViewContainer);
            }
            tagView.setClickable(false);
            final TagView finalTagViewContainer = tagViewContainer;
            final int position = i;
            tagViewContainer.setOnClickListener(v -> {
                doSelect(finalTagViewContainer, position);
                if (mOnTagClickListener != null) {
                    mOnTagClickListener.onTagClick(finalTagViewContainer, position, TagFlowLayout.this);
                }
            });
        }
    }

    public void setMaxSelectCount(int count) {
        if (mSelectedView.size() > count) {
            Log.w(TAG, "you has already select more than " + count + " views , so it will be clear .");
            mSelectedView.clear();
        }
        mSelectedMax = count;
        if (mTagAdapter == null) {
            return;
        }
        changeAdapter();
    }

    public Set<Integer> getSelectedList() {
        return new HashSet<>(mSelectedView);
    }

    private void setChildChecked(int position, TagView view) {
        view.setChecked(true);
        mTagAdapter.onSelected(position, view.getTagView());
    }

    private void setChildUnChecked(int position, TagView view) {
        view.setChecked(false);
        mTagAdapter.unSelected(position, view.getTagView());
    }

    private void doSelect(TagView child, int position) {
        if (!child.isChecked()) {
            //处理max_select=1的情况
            if (mSelectedMax == 1 && mSelectedView.size() == 1) {
                Iterator<Integer> iterator = mSelectedView.iterator();
                Integer preIndex = iterator.next();
                TagView pre = (TagView) getChildAt(preIndex);
                setChildUnChecked(preIndex, pre);
                setChildChecked(position, child);

                mSelectedView.remove(preIndex);
                mSelectedView.add(position);
            } else {
                if (mSelectedMax > 0 && mSelectedView.size() >= mSelectedMax) {
                    if (mOnSelectListener != null) {
                        mOnSelectListener.onSelectedMax(mSelectedMax);
                    }
                    return;
                }
                setChildChecked(position, child);
                mSelectedView.add(position);
            }
        } else {
            setChildUnChecked(position, child);
            mSelectedView.remove(position);
        }
        if (mOnSelectListener != null) {
            mOnSelectListener.onSelected(new HashSet<>(mSelectedView));
        }
    }

    public TagAdapter<T> getAdapter() {
        return mTagAdapter;
    }

    private static final String KEY_CHOOSE_POS = "key_choose_pos";
    private static final String KEY_DEFAULT = "key_default";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DEFAULT, super.onSaveInstanceState());

        StringBuilder selectPos = new StringBuilder();
        if (mSelectedView.size() > 0) {
            for (int key : mSelectedView) {
                selectPos.append(key).append("|");
            }
            bundle.putString(KEY_CHOOSE_POS, selectPos.substring(0, selectPos.length() - 1));
        }
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            String mSelectPos = bundle.getString(KEY_CHOOSE_POS);
            if (!TextUtils.isEmpty(mSelectPos)) {
                String[] split = mSelectPos.split("\\|");
                for (String pos : split) {
                    int index = Integer.parseInt(pos);
                    mSelectedView.add(index);
                    TagView tagView = (TagView) getChildAt(index);
                    if (tagView != null) {
                        setChildChecked(index, tagView);
                    }
                }
            }
            super.onRestoreInstanceState(bundle.getParcelable(KEY_DEFAULT));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    public void onChanged() {
        changeAdapter();
    }

    public static int dip2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, Resources.getSystem().getDisplayMetrics());
    }

}
