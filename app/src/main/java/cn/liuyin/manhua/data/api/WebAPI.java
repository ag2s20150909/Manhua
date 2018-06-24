package cn.liuyin.manhua.data.api;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import cn.liuyin.manhua.data.bean.SearchResult;
//import cn.liuyin.manhua.data.tool.Book;
//import cn.liuyin.manhua.tool.HttpTool;

/**
 * Created by asus on 2018/3/25.
 * 这是对旧的解析网页的备份，防止api失效
 */

public class WebAPI {


//    public static SearchResult getMobileList(String url){
//        SearchResult data = new SearchResult();
//        try {
//            String d= HttpTool.httpGet(url);
//            if (d.startsWith("error:")){
//                data.success=false;
//                data.message=d;
//                return data;
//
//            }
//            Document doc = Jsoup.parse(d,url);
//
//            Elements lists = doc.select(".cont-list").select("li");
//
//            for (Element item : lists) {
//                Book temp = new Book();
//                temp.link = item.select("a").attr("abs:href");
//                temp.name = item.select("h3").text();
//                temp.img = item.select("img").attr("data-src");
//                temp.author = item.select("dd").get(0).text();
//                temp.type = item.select("dd").get(1).text();
//                temp.newChapter = item.select("dd").get(2).text();
//                temp.updateTime = item.select("dd").get(3).text();
//                data.add(temp);
//            }
//
//        } catch (Exception e) {
//            data.success=false;
//            data.message=e.getLocalizedMessage();
//
//        }
//        return data;
//    }
//
//    public static SearchResult getPcList(String url){
//        SearchResult data = new SearchResult();
//        try {
//
//            String d= HttpTool.httpGet(url);
//            if (d.startsWith("error:")){
//                data.success=false;
//                data.message=d;
//                return data;
//            }
//            Document doc = Jsoup.parse(d,url);
//            doc.setBaseUri(update);
//            //allList
//            Elements lists;
//            if (url.endsWith("update.html")) {
//                lists = doc.select(".updateList").select("li");
//            } else {
//
//                lists = doc.select(".allList").select("li");
//            }
//
//            for (Element item : lists) {
//                Book temp = new Book();
//                String nurl = item.select("a.red").attr("abs:href");
//                temp.link = nurl.substring(0, nurl.lastIndexOf("/") + 1);
//                temp.name = item.select("a.video").text();
//                temp.img = "";
//                temp.author = "";
//                temp.type = "";
//                temp.newChapter = item.select("a.red").text();
//                if (url.endsWith("update.html")) {
//                    temp.updateTime = item.select("span.red").text();
//                } else {
//                    temp.updateTime = "时间未知";
//                }
//                data.add(temp);
//            }
//
//        } catch (Exception e) {
//            data.success=false;
//            data.message=e.getLocalizedMessage();
//
//        }
//        return data;
//    }
    //	addButton("今日更新")
//	.addButton("漫画排行")
//	.addButton("少年热血")
//	.addButton("少女爱情")
//	.addButton("武侠格斗")
//	.addButton("科幻魔幻")
//	.addButton("竞技体育")
//	.addButton("搞笑喜剧")
//	.addButton("耽美BL")
//	.addButton("推理侦探")
//	.addButton("恐怖灵异")
//	.addButton("最近更新")
//	.addButton("连载大全");
//    static String update = "http://m.pufei.net/manhua/update.html";
//    String paihang = "http://m.pufei.net/manhua/paihang.html";
//    String rexue = "http://m.pufei.net/shaonianrexue/";
//    String shaonvaiqing = "http://m.pufei.net/shaonvaiqing/";
//    String wuxiagedou = "http://m.pufei.net/wuxiagedou/";
//    String kehuan = "http://m.pufei.net/kehuan/";
//    String jingjitiyu = "http://m.pufei.net/jingjitiyu/";
//    String gaoxiaoxiju = "http://m.pufei.net/gaoxiaoxiju/";
//    String danmeirensheng = "http://m.pufei.net/danmeirensheng/";
//    String zhentantuili = "http://m.pufei.net/zhentantuili/";
//    String kongbulingyi = "http://m.pufei.net/kongbulingyi/";
//    String PClianzai = "http://www.pufei.net/manhua/lianzai.html";
//    String PCupdate = "http://www.pufei.net/manhua/update.html";

    //    public void search(final String kw) {
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//
//                String u = HttpTool.search(getApplicationContext(), kw);
//                getHtml(u);
//            }
//        }).start();
//    }

//    public void getHtml(final String url) {
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//
//                try {
//                    String d = HttpTool.httpGet(url);
//                    if (d.startsWith("error:")) {
//                        mHander.obtainMessage(0, d).sendToTarget();
//                    } else {
//                        Document doc = Jsoup.parse(d, url);
//                        //FileTool.writeFile("doc.html",doc.html());
//                        Elements lists = doc.select(".cont-list").select("li");
//                        mHander.obtainMessage(0, "搜索到" + lists.size() + "条结果").sendToTarget();
//                        SearchResult data = new SearchResult();
//                        for (Element item : lists) {
//                            Book temp = new Book();
//
//                            temp.link = item.select("a").attr("abs:href") + "/";
//                            temp.name = item.select("h3").text();
//                            temp.img = item.select("img").attr("data-src");
//                            temp.author = item.select("dd").get(0).text();
//                            temp.type = item.select("dd").get(1).text();
//                            temp.newChapter = item.select("dd").get(2).text();
//                            temp.updateTime = item.select("dd").get(3).text();
//                            data.add(temp);
//                        }
//
//                        //FileTool.writeFile("update.html",doc.toString());
//                        mHander.obtainMessage(1, data).sendToTarget();
//                    }
//                } catch (Exception e) {
//                    mHander.obtainMessage(0, "error" + e).sendToTarget();
//                }
//            }
//        }).start();
//    }

}
