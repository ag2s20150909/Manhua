package cn.liuyin.manhua.tool;


import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.LinkedList;

public class UItool {
    LinkedList<Button> btns;
    ViewGroup mview;
    View.OnClickListener mlistener;

    public UItool(ViewGroup view, View.OnClickListener listener) {
        this.mview = view;
        this.mlistener = listener;
        btns = new LinkedList<Button>();
    }

    public void Build() {
        for (int i = 0; i < btns.size(); i++) {
            mview.addView(btns.get(i));
        }
    }

    public UItool addButton(String name) {
        Button btn = new Button(mview.getContext());
        btn.setText(name);
        int id = btns.size();
        btn.setId(id);
        btns.add(btn);
        btn.setOnClickListener(mlistener);
        return UItool.this;
    }
}

