package bdtube.vumobile.com.bdtube.Holder;

import java.util.Vector;

import bdtube.vumobile.com.bdtube.Model.HomeVideoList;

/**
 * Created by IT-10 on 12/31/2015.
 */
public class AllHomeVideo {

    public static Vector<HomeVideoList> homeVideoLists = new Vector<HomeVideoList>();


    public static Vector<HomeVideoList> getHomeVideoList() {
        return homeVideoLists;
    }

    public static void setAllThemeList(Vector<HomeVideoList> homeVideoLists) {
        AllHomeVideo.homeVideoLists = homeVideoLists;
    }

    public static HomeVideoList getHomeVideoList(int pos) {
        return homeVideoLists.elementAt(pos);
    }

    public static void setHomeVideoList(HomeVideoList homeVideoList) {
        AllHomeVideo.homeVideoLists.addElement(homeVideoList);
    }

    public static void removeHomeVideoList() {
        AllHomeVideo.homeVideoLists.removeAllElements();
    }
}
