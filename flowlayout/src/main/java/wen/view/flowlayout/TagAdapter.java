package wen.view.flowlayout;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签适配器,展示到界面中的数据适配器
 */
public abstract class TagAdapter<T> {
    // 展示到界面中的数据
    private final List<T> mTagList;
    private OnDataChangedListener mOnDataChangedListener;

    public TagAdapter(List<T> tagList) {
        mTagList = tagList;
    }

    public TagAdapter(T[] datas) {
        mTagList = new ArrayList<T>();
        for (T data : datas) {
            mTagList.add(data);
        }
    }

    interface OnDataChangedListener {
        void onChanged();
    }

    void setOnDataChangedListener(OnDataChangedListener listener) {
        mOnDataChangedListener = listener;
    }

    public void addTagList(List<T> tagList) {
        if (null != tagList) {
            mTagList.addAll(tagList);
            notifyDataChanged();
        }
    }

    public int getCount() {
        return mTagList == null ? 0 : mTagList.size();
    }

    public void notifyDataChanged() {
        if (mOnDataChangedListener != null)
            mOnDataChangedListener.onChanged();
    }

    public T getItem(int position) {
        return mTagList.get(position);
    }

    public abstract View getView(FlowLayout parent, int position, T t);


    public void onSelected(int position, View view) {
        Log.d("FlowLayout", "onSelected " + position);
    }

    public void unSelected(int position, View view) {
        Log.d("FlowLayout", "unSelected " + position);
    }

    /**
     * 如果集合中没有选中此View,将会被选中
     *
     * @param position 指定某个位置的Tag是否被选中
     * @param t        对应的位置的标签数据
     * @return true选中
     */
    public boolean setSelected(int position, T t) {
        return false;
    }


}
