package bdtube.vumobile.com.bdtube.Holder;

import java.util.Vector;

import bdtube.vumobile.com.bdtube.Model.IsFavouriteChecklist;

/**
 * Created by IT-10 on 2/1/2016.
 */
public class AllFavouriteList {
    public static Vector<IsFavouriteChecklist> isFavouriteChecklists = new Vector<IsFavouriteChecklist>();


    public static Vector<IsFavouriteChecklist> getIsFavouriteChecklist() {
        return isFavouriteChecklists;
    }

    public static void setIsFavouriteChecklist(Vector<IsFavouriteChecklist> isFavouriteChecklists) {
        AllFavouriteList.isFavouriteChecklists = isFavouriteChecklists;
    }

    public static IsFavouriteChecklist getAllCommentList(int pos) {
        return isFavouriteChecklists.elementAt(pos);
    }

    public static void setIsFavouriteChecklist(IsFavouriteChecklist isFavouriteChecklist) {
        AllFavouriteList.isFavouriteChecklists.addElement(isFavouriteChecklist);
    }

    public static void removeIsFavouriteChecklist() {
        AllFavouriteList.isFavouriteChecklists.removeAllElements();
    }
}
