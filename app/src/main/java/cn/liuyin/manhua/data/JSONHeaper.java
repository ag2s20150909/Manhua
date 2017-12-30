package cn.liuyin.manhua.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.liuyin.manhua.tool.FileTool;

public class JSONHeaper {


    public static int[] getIds(JSONArray array, String name) {
        int[] data = new int[array.length()];
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject temp = array.getJSONObject(i);
                data[i] = temp.getInt("bookid");
            }
        } catch (Exception e) {
        }
        return data;
    }

    public static boolean moveTopById(JSONArray array, int bookid) {
        int tag = 0;
        try {
            JSONArray temparray = new JSONArray();
            for (int i = 0; i < array.length(); i++) {

                JSONObject temp = array.getJSONObject(i);
                temparray.put(i, temp);
                if (temp.getInt("bookid") == bookid) {
                    tag = i;
                }
            }
            JSONObject mov = temparray.getJSONObject(tag);
            array.put(0, mov);
            for (int i = 0; i < tag; i++) {

                array.put(i + 1, temparray.getJSONObject(i));

            }
        } catch (JSONException e) {
            return false;
        }
        return false;
    }

    public static boolean moveUpById(JSONArray array, int bookid, int index) {
        int tag = 0;
        try {
            JSONArray temparray = new JSONArray();
            for (int i = 0; i < array.length(); i++) {

                JSONObject temp = array.getJSONObject(i);
                temparray.put(i, temp);
                if (temp.getInt("bookid") == bookid) {
                    tag = i;
                }
            }
            JSONObject mov = temparray.getJSONObject(tag);
            array.put(tag - index, mov);
            for (int i = tag - index; i < tag; i++) {

                array.put(i + 1, temparray.getJSONObject(i));

            }
        } catch (JSONException e) {
            return false;
        }
        return false;
    }

    public static boolean moveDownById(JSONArray array, int bookid, int index) {
        int tag = 0;
        int tag2;
        try {
            JSONArray temparray = new JSONArray();
            for (int i = 0; i < array.length(); i++) {

                JSONObject temp = array.getJSONObject(i);
                temparray.put(i, temp);
                if (temp.getInt("bookid") == bookid) {
                    tag = i;
                }
            }
            if (tag + index >= array.length()) {
                tag2 = array.length();
            } else {
                tag2 = tag + index;
            }
            JSONObject mov = temparray.getJSONObject(tag);
            array.put(tag2, mov);

            for (int i = tag; i < tag2; i++) {
                array.put(i, temparray.getJSONObject(i + 1));
            }
        } catch (JSONException e) {
            return false;
        }
        return false;
    }

    public static boolean removeById(JSONArray array, int bookid) {
        try {
            for (int i = 0; i < array.length(); i++) {

                JSONObject temp = array.getJSONObject(i);
                if (temp.getInt("bookid") == bookid) {
                    array.remove(i);
                    return true;
                }
            }
        } catch (JSONException e) {
            FileTool.writeError(e.getMessage());
            return false;
        }
        return false;
    }


    public static void put(JSONArray array, int id, JSONObject obj) {
        try {
            for (int i = 0; i < array.length(); i++) {

                JSONObject temp = array.getJSONObject(i);
                if (temp.getInt("bookid") == id) {
                    array.put(i, obj);
                    return;
                }
            }
            array.put(obj);
        } catch (JSONException e) {
            return;
        }
    }

    public static void set(JSONArray array, int id, JSONObject obj) {
        try {
            for (int i = 0; i < array.length(); i++) {

                JSONObject temp = array.getJSONObject(i);
                if (temp.getInt("bookid") == id) {
                    array.put(i, obj);
                    return;
                }
            }
        } catch (JSONException e) {
            return;
        }
    }


    public static JSONObject getByid(JSONArray array, int id) {
        try {
            for (int i = 0; i < array.length(); i++) {

                JSONObject temp = array.getJSONObject(i);
                if (temp.getInt("bookid") == id) {
                    return temp;
                }
            }
        } catch (JSONException e) {
            return null;
        }

        return null;
    }

    public static boolean has(JSONArray array, int id) {
        try {
            for (int i = 0; i < array.length(); i++) {

                JSONObject temp = array.getJSONObject(i);
                if (temp.getInt("bookid") == id) {
                    return true;
                }
            }
        } catch (JSONException e) {
            return false;
        }

        return false;
    }
}
