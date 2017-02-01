package bdtube.vumobile.com.bdtube.CustomAdopter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import bdtube.vumobile.com.bdtube.Holder.AllComment;
import bdtube.vumobile.com.bdtube.Holder.AllHomeVideo;
import bdtube.vumobile.com.bdtube.Model.AllCommentList;
import bdtube.vumobile.com.bdtube.R;

/**
 * Created by IT-10 on 1/19/2016.
 */
public class CommentAdopter extends ArrayAdapter<AllCommentList> {

    Context context;

    public CommentAdopter(Context context, ArrayList<AllCommentList> allCommentLists) {
        super(context, R.layout.comment_list, AllComment.getAllCommentList());
        this.context = context;
        //	imageLoader = new ImageLoader(context);

    }

    static class ViewHolder {

        TextView tvMobileNumber;
        TextView tvComment;

    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View v = convertView;

        if (v == null) {
            final LayoutInflater vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.comment_list, null);

            holder = new ViewHolder();


            holder.tvMobileNumber = (TextView) v.findViewById(R.id.tvMobileNumber);
            holder.tvComment = (TextView) v.findViewById(R.id.tvComment);



            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        if (position < AllHomeVideo.getHomeVideoList().size()) {

            final AllCommentList query = AllComment.getAllCommentList().elementAt(
                    position);
            String TimeStamp= query.getTimeStamp().toString().trim();
            final String number= query.getMSISDN().toString().trim();
            String message= query.getValue().toString().trim();


            holder.tvMobileNumber.setText(""+number.replaceAll("_", " "));
            holder.tvComment.setText(""+message.replaceAll("_", " "));


            Log.d("messageComment",message);
            notifyDataSetChanged();

        }

        return v;
    }

}
