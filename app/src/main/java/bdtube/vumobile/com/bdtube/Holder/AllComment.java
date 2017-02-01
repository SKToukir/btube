package bdtube.vumobile.com.bdtube.Holder;

import java.util.Vector;

import bdtube.vumobile.com.bdtube.Model.AllCommentList;

/**
 * Created by IT-10 on 1/19/2016.
 */
public class AllComment {
    public static Vector<AllCommentList> allCommentLists = new Vector<AllCommentList>();


    public static Vector<AllCommentList> getAllCommentList() {
        return allCommentLists;
    }

    public static void setAllCommentList(Vector<AllCommentList> allCommentLists) {
        AllComment.allCommentLists = allCommentLists;
    }

    public static AllCommentList getAllCommentList(int pos) {
        return allCommentLists.elementAt(pos);
    }

    public static void setAllCommentList(AllCommentList allCommentList) {
        AllComment.allCommentLists.addElement(allCommentList);
    }

    public static void removeAllCommentList() {
        AllComment.allCommentLists.removeAllElements();
    }

}
