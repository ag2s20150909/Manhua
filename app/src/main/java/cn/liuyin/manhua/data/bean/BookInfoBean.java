package cn.liuyin.manhua.data.bean;

/**
 * Created by asus on 2018/1/25.
 */

public class BookInfoBean {

    /**
     * data : {"bid":320,"cid":0,"tclassId":638,"firstCid":24497,"tclass":"少女爱情","bookTitle":"狐妖小红娘","authorId":0,"author":"夏天岛?图：小新 文：小新","state":"连载中","cover":"http://i.pufei.net/mh/2017/02/08/4385b30c1f.jpg","hot":0,"colloctions":0,"colloctioned":0,"comments":0,"lastUpdateId":108321,"lastUpdateTitle":"总242·遗憾","lastUpdateTime":1515040526,"introduction":"迷糊萝莉小狐妖，正太道士没节操。自古人妖生死恋，千载孽缘一线牵！啊，果然老了不容易肝得动。看来一周两话已经是极限了。妄想赶进度的我真是太蠢了！爬上来报进度。这期今天照样没画完！预计可能得明天半夜至后天早上\u2026\u2026（似乎是只有前两期参加活动的吧？应该不会影响大家拿优惠\u2026\u2026）","keywords":"玄幻,恋爱,搞笑,萌系"}
     * success : true
     * token : d40b6afaaeae04bb17d9ab2aee1e31c4,0,1515225306996
     * message : 操作成功
     * code : 0
     * version : 1.0.0
     */

    private DataBean data;
    private boolean success;
    private String token;
    private String message;
    private int code;
    private String version;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static class DataBean {
        /**
         * bid : 320
         * cid : 0
         * tclassId : 638
         * firstCid : 24497
         * tclass : 少女爱情
         * bookTitle : 狐妖小红娘
         * authorId : 0
         * author : 夏天岛?图：小新 文：小新
         * state : 连载中
         * cover : http://i.pufei.net/mh/2017/02/08/4385b30c1f.jpg
         * hot : 0
         * colloctions : 0
         * colloctioned : 0
         * comments : 0
         * lastUpdateId : 108321
         * lastUpdateTitle : 总242·遗憾
         * lastUpdateTime : 1515040526
         * introduction : 迷糊萝莉小狐妖，正太道士没节操。自古人妖生死恋，千载孽缘一线牵！啊，果然老了不容易肝得动。看来一周两话已经是极限了。妄想赶进度的我真是太蠢了！爬上来报进度。这期今天照样没画完！预计可能得明天半夜至后天早上……（似乎是只有前两期参加活动的吧？应该不会影响大家拿优惠……）
         * keywords : 玄幻,恋爱,搞笑,萌系
         */

        private int bid;
        private int cid;
        private int tclassId;
        private int firstCid;
        private String tclass;
        private String bookTitle;
        private int authorId;
        private String author;
        private String state;
        private String cover;
        private int hot;
        private int colloctions;
        private int colloctioned;
        private int comments;
        private int lastUpdateId;
        private String lastUpdateTitle;
        private int lastUpdateTime;
        private String introduction;
        private String keywords;

        public int getBid() {
            return bid;
        }

        public void setBid(int bid) {
            this.bid = bid;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public int getTclassId() {
            return tclassId;
        }

        public void setTclassId(int tclassId) {
            this.tclassId = tclassId;
        }

        public int getFirstCid() {
            return firstCid;
        }

        public void setFirstCid(int firstCid) {
            this.firstCid = firstCid;
        }

        public String getTclass() {
            return tclass;
        }

        public void setTclass(String tclass) {
            this.tclass = tclass;
        }

        public String getBookTitle() {
            return bookTitle;
        }

        public void setBookTitle(String bookTitle) {
            this.bookTitle = bookTitle;
        }

        public int getAuthorId() {
            return authorId;
        }

        public void setAuthorId(int authorId) {
            this.authorId = authorId;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getHot() {
            return hot;
        }

        public void setHot(int hot) {
            this.hot = hot;
        }

        public int getColloctions() {
            return colloctions;
        }

        public void setColloctions(int colloctions) {
            this.colloctions = colloctions;
        }

        public int getColloctioned() {
            return colloctioned;
        }

        public void setColloctioned(int colloctioned) {
            this.colloctioned = colloctioned;
        }

        public int getComments() {
            return comments;
        }

        public void setComments(int comments) {
            this.comments = comments;
        }

        public int getLastUpdateId() {
            return lastUpdateId;
        }

        public void setLastUpdateId(int lastUpdateId) {
            this.lastUpdateId = lastUpdateId;
        }

        public String getLastUpdateTitle() {
            return lastUpdateTitle;
        }

        public void setLastUpdateTitle(String lastUpdateTitle) {
            this.lastUpdateTitle = lastUpdateTitle;
        }

        public int getLastUpdateTime() {
            return lastUpdateTime;
        }

        public void setLastUpdateTime(int lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }
    }
}
