package cn.liuyin.manhua.data.bean;


import java.util.List;

public class ClassListBean {
    public Data data;

    /**
     * Data is the inner class of ClassListBean
     */
    public class Data {
        public int totalPage;
        public int count;
        public String className;
        public List<Items> items;

        /**
         * Items is the inner class of Data
         */
        public class Items {
            public int bid;
            public String bookTitle;
            public String author;
            public String state;
            public String cover;
            public int hot;
            public int colloctioned;
            public String lastUpdateTitle;
            public int lastUpdateTime;
            public String introduction;
            public String keywords;
        }

    }

    public boolean success;
    public String token;
    public String message;
    public int code;
    public String version;
}

