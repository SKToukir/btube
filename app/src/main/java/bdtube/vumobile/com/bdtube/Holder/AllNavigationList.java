package bdtube.vumobile.com.bdtube.Holder;

import java.util.Vector;

import bdtube.vumobile.com.bdtube.Model.NavigationList;

/**
 * Created by IT-10 on 1/5/2016.
 */
public class AllNavigationList {

    public static Vector<NavigationList> navigationLists = new Vector<NavigationList>();


    public static Vector<NavigationList> getNavigationList() {
        return navigationLists;
    }

    public static void setAllThemeList(Vector<NavigationList> navigationLists) {
        AllNavigationList.navigationLists = navigationLists;
    }

    public static NavigationList getNavigationList(int pos) {
        return navigationLists.elementAt(pos);
    }

    public static void setNavigationList(NavigationList navigationList) {
        AllNavigationList.navigationLists.addElement(navigationList);
    }

    public static void removeNavigationList() {
        AllNavigationList.navigationLists.removeAllElements();
    }
}
