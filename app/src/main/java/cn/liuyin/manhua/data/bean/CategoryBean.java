package cn.liuyin.manhua.data.bean;

import java.util.List;

public class CategoryBean {
    public Data data;

    /**
     * Data is the inner class of CategoryBean
     */
    public class Data {
        public List<Items> items;

        /**
         * Items is the inner class of Data
         */
        public class Items {
            public int classId;
            public String className;
            public String image;
            public String image_m;
        }

    }

    public Boolean success;
    public String token;
    public String message;
    public int code;
    public String version;
}

