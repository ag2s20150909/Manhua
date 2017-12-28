package cn.liuyin.manhua.data;


import org.json.JSONArray;
import org.json.JSONObject;

import cn.liuyin.manhua.tool.FileTool;

public class DataManger {
    public static void scanChanges() {
        if (FileTool.has("index.json")) {
            scan();
        } else {
            FileTool.writeFile("index.json", "[]");
        }

        //update();
    }

    private static void scan() {
        try {
            JSONArray bookshelf = new JSONArray(FileTool.readFile("", "index.json"));
            for (int i = 0; i < bookshelf.length(); i++) {
                JSONObject obj = bookshelf.getJSONObject(i);

                if (FileTool.has(obj.getInt("bookid") + "/config.json")) {
                    JSONObject O = new JSONObject(FileTool.readFile(obj.getInt("bookid") + "", "config.json"));
                    obj.put("possion", O.getInt("index"));
                    obj.put("unread", obj.getInt("sum") - O.getInt("index"));
                }
                bookshelf.put(i, obj);
            }
            FileTool.writeFile("index.json", bookshelf.toString(4));
        } catch (Exception e) {
            //mHander.obtainMessage(0,e.getMessage()).sendToTarget();
        }
    }
}
