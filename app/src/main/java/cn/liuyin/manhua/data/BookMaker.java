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
import cn.liuyin.manhua.tool.HttpTool;

public class BookMaker {

    public static JSONArray getList(Context context, String url) throws Exception {

        String idtag = "manhua/";
        int _BookId = Integer.parseInt(url.substring(url.indexOf(idtag) + idtag.length(), url.lastIndexOf("/")));
        System.out.println("<<<"+_BookId);
        String d = HttpTool.httpGet(url);
        if (d.startsWith("error:")) {
            return null;
        }
        else {
            Document doc = Jsoup.parse(d, url);
            Elements list = doc.select(".chapter-list").select("a");
            if (list.size()==0){
                throw new Exception("get list faild ,the length is 0");
            }
            String title = doc.select(".main-bar").select("h1").text();

            int possion = 1;
            try {
                JSONObject O = new JSONObject();

                if (FileTool.has(_BookId+"/config.json")) {
                    O = new JSONObject(FileTool.readFile(_BookId + "", "config.json"));
                    possion = O.getInt("index");
                }
                else {
                    O.put("index",0);
                    FileTool.writeFiles(_BookId+"","config.json",O.toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
                throw e;
            }


            JSONObject map = new JSONObject();
            JSONArray bookshelf = new JSONArray(FileTool.readFile("", "index.json"));
            map.put("bookid", _BookId);
            map.put("title", title);
            map.put("sum", list.size());
            map.put("possion", possion);
            map.put("unread", list.size() - possion);
            JSONHeaper.put(bookshelf, _BookId, map);
            JSONArray marray = new JSONArray();
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

            StringBuilder data = new StringBuilder("<html><head>" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no\">" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/main.css\" >" +
                    "</head>");
            data.append("<body>");
            data.append("<h2  id=\"title\">").append(mbook.title).append("(").append(mbook.count).append(")").append("</h3>");
            data.append("<div id=\"main\" >");

            for (int i = 0; i < imglist.length(); i++) {
                String img;
                int tag = Integer.parseInt(imglist.getString(i).split("/")[0]);
                if (tag <= 201709) {
                    img = "http://cf.pufei.net/" + imglist.getString(i);
                } else {
                    img = "http://f.pufei.net/" + imglist.getString(i);
                }
                data.append("<img  class=\"lazy\" data-src=\"file:///android_asset/images/loading.gif\" width=\"100%;\" src=\"").append(img).append("\">");

            }
            data.append("</div>");
            FileTool.writeFiles(mbook.bookid + "", mbook.index + ".json", obj.toString());
            //String next=list.getJSONObject(index).getString("url");
            data.append("<div id=\"buttom\"  >");
            data.append("<button class=\"button\" id=\"pre\"  onclick=\"" + "Android.pre();" + "\">上一章</button>");
            data.append("<button class=\"button\" id=\"next\"  onclick=\"" + "Android.next();" + "\">下一章</button>");
            data.append("</div>");

            data.append("<script>");

            data.append("</script>");
            data.append("</body</html>");
            FileTool.writeFile("test.html", data.toString());
            wv.clearHistory();
            wv.loadDataWithBaseURL(null, data.toString(), "text/html", "utf-8", null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Book getBook(JSONArray array, String bookid, int index) throws JSONException {
        Book book = new Book();
        //如果本地有数据
        if (FileTool.has(bookid + "/" + index + ".json")) {
            JSONObject dd = new JSONObject(FileTool.readFile(bookid, index + ".json"));
            book.index = dd.getInt("index");
            book.title = dd.getString("title");
            book.bookid = dd.getInt("bookid");
            book.chapterId = dd.getInt("chapterId");
            book.code = dd.getString("code");
            book.count = dd.getInt("count");
            return book;
        } else {
            String url = array.getJSONObject(index - 1).getString("url");
            String d = HttpTool.httpGet(url);
            if (d.startsWith("error:")) {
                return null;
            }
            Document doc = Jsoup.parse(d, url);
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
        }


    }


}
