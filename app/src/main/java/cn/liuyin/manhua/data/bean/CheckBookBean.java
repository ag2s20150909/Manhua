package cn.liuyin.manhua.data.bean;

import java.util.List;

public class CheckBookBean {

    public List<ItemsBean> items;


    public static class ItemsBean {


        public int bid;
        public int cid;
        public String bookTitle;
        public String cover;
        public String tclass;
        public int tclassId;
        public String lastUpdateTitle;
        public int lastUpdateTime;
        public String keywords;
        public String state;
        public int readTime;
        public String title;
        public int colloctioned;
        public String titleAndUpdateTitle;

    }
}
