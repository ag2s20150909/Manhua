package cn.liuyin.manhua.data;


import android.content.Context;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.liuyin.manhua.tool.ComonTool;
import cn.liuyin.manhua.tool.FileTool;

public class BookMaker {

    public static JSONArray getList(Context context, String url) {

        JSONArray marray = new JSONArray();
        try {
            JSONArray bookshelf = new JSONArray(FileTool.readFile("", "index.json"));

            JSONObject map = new JSONObject();
            String idtag = "manhua/";
            int _BookId = Integer.parseInt(url.substring(url.indexOf(idtag) + idtag.length(), url.lastIndexOf("/")));
            Document doc = Jsoup.connect(url).get();
            Elements list = doc.select(".chapter-list").select("a");
            String title = doc.select(".main-bar").select("h1").text();
            if (!FileTool.has(_BookId + "/config.json")) {
                JSONObject O = new JSONObject();
                if (FileTool.has("index.json")) {
                    try {
                        JSONArray indexjson = new JSONArray(FileTool.readFile("", "index.json"));
                        JSONObject O2 = JSONHeaper.getByid(indexjson, _BookId);
                        O.put("index", O2.getInt("possion"));
                    } catch (Exception e) {
                        O.put("index", 1);
                    }
                } else {
                    O.put("index", 1);
                }

                FileTool.writeFiles(_BookId + "", "config.json", O.toString());
            }
            int possion = 1;
            try {
                JSONObject O = new JSONObject(FileTool.readFile(_BookId + "", "config.json"));
                possion = O.getInt("index");

            } catch (JSONException e) {
                FileTool.writeError(e.getMessage());
            }
            map.put("bookid", _BookId);
            map.put("title", title);
            map.put("sum", list.size());
            map.put("possion", possion);
            map.put("unread", list.size() - possion);


            JSONHeaper.put(bookshelf, _BookId, map);


            int tag = 1;
            for (int i = list.size() - 1; i >= 0; i--) {
                Element a = list.get(i);
                JSONObject temp = new JSONObject();
                temp.put("index", list.size() - i);
                temp.put("name", a.text());
                temp.put("url", a.attr("abs:href"));
                marray.put(temp);

            }
            FileTool.writeFile("index.json", bookshelf.toString(4));
            FileTool.writeFiles(_BookId + "", "index.json", marray.toString(4));
            return marray;
            //mHander.obtainMessage(1, array).sendToTarget();
        } catch (Exception e) {
            FileTool.writeError(e.getMessage());
            return null;
            //mHander.obtainMessage(0, "error" + e).sendToTarget();
        }
    }


    public static void makeHtml(WebView wv, Book mbook, String str) {
        try {
            //http://f.pufei.net/2017/12/13/20/430ea951ad.jpg
            JSONObject obj = new JSONObject();
            obj.put("index", mbook.index);
            obj.put("title", mbook.title);
            obj.put("bookid", mbook.bookid);
            obj.put("chapterId", mbook.chapterId);
            obj.put("code", mbook.code);
            obj.put("count", mbook.count);
            JSONArray imglist = new JSONArray(str);
            obj.put("images", imglist);

            String data = "<html><head>" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no\">" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/main.css\" >" +
                    "</head>";
            data += "<body>";
            data += "<h2  id=\"title\">" + mbook.title + "(" + mbook.count + ")" + "</h3>";
            data += "<div id=\"main\" >";

            for (int i = 0; i < imglist.length(); i++) {
                String img;
                int tag = Integer.parseInt(imglist.getString(i).split("/")[0]);
                if (tag <= 201709) {
                    img = "http://cf.pufei.net/" + imglist.getString(i);
                } else {
                    img = "http://f.pufei.net/" + imglist.getString(i);
                }
                data += "<img  class=\"lazy\" data-src=\"file:///android_asset/images/loading.gif\" width=\"100%;\" src=\"" + img + "\">";

            }
            data += "</div>";
            FileTool.writeFiles(mbook.bookid + "", mbook.index + ".json", obj.toString());
            //String next=list.getJSONObject(index).getString("url");
            data += "<div id=\"buttom\"  >";
            data += "<button class=\"button\" id=\"pre\"  onclick=\"" + "Android.pre();" + "\">上一章</button>";
            data += "<button class=\"button\" id=\"next\"  onclick=\"" + "Android.next();" + "\">下一章</button>";
            data += "</div>";
            //data+="<script src=\"file:///android_asset/js/echo.min.js\"></script>";
            data += "<script>";
//			data+="Echo.init({"+
//				"offset: 0,"+
//				"throttle: 0"+
//			"});";

            data += "</script>";
            data += "</body</html>";
            FileTool.writeFile("test.html", data);
            wv.clearHistory();
            wv.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
        } catch (JSONException e) {
        }
    }

    public static Book getBook(String url) {
        Book book = new Book();
        try {
            Document doc = Jsoup.connect(url).get();
            book.title = doc.select("#mangaTitle").text();
            String tag = "cp=\"";
            String str = doc.select("script").toString();
            str = ComonTool.replaceBlank(str);
            str = str.substring(str.indexOf(tag) + tag.length());
            String booktag = "IMH.reader({";
            String bookstr = str.substring(str.indexOf(booktag) + booktag.length(), str.indexOf("}).init();"));
            book.code = str.substring(0, str.indexOf("\""));
            String[] info = bookstr.split(",");

            book.bookid = Integer.parseInt(info[0].substring(info[0].indexOf("bookId:") + 7));
            book.chapterId = Integer.parseInt(info[1].substring(info[1].indexOf("chapterId:") + 10));
            book.count = Integer.parseInt(info[3].substring(info[3].indexOf("count:") + 6));

            FileTool.writeFile("dd.html", bookstr + book.toString());
            return book;

        } catch (Exception e) {
            return null;
            //mHander.obtainMessage(0, "error" + e).sendToTarget();
        }


    }


}
