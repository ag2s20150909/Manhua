package cn.liuyin.manhua.data.bean;


import java.util.List;

public class RankingBean {
    public Data data;

    /**
     * Data is the inner class of RankingBean
     */
    public class Data {
        public int totalPage;
        public int count;
        public List<Items> items;

        /**
         * Items is the inner class of Data
         */
        public class Items {
            public int bid;
            public String bookTitle;
            public String author;
            public String introduction;
            public String cover;
            public String keywords;
            public int hot;
            public int collects;
            public int lastUpdateTime;
            public String lastUpdateTitle;
            public String state;
            public int colloctioned;
        }

    }

    public boolean success;
    public String token;
    public String message;
    public int code;
    public String version;
}

