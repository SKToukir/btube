package bdtube.vumobile.com.bdtube.CustomAdopter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import bdtube.vumobile.com.bdtube.Holder.AllNavigationList;
import bdtube.vumobile.com.bdtube.Model.NavigationList;
import bdtube.vumobile.com.bdtube.R;

/**
 * Created by IT-10 on 1/4/2016.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<NavigationList> {

    Context context;
    //private BusyDialog busyNow;
    com.android.volley.toolbox.ImageLoader vImageLoader; //Volley Network ImageLoader



    public NavigationDrawerAdapter(Context context, ArrayList<NavigationList> navigationLists) {
        super(context, R.layout.custom_navigation_drawer, AllNavigationList.getNavigationList());
        this.context = context;
        //	imageLoader = new ImageLoader(context);

    }

    static class ViewHolder {
        RelativeLayout videoImagePreview;
        ImageButton VideoViewClick;
        TextView txtNewMovies;
        NetworkImageView thumb;
        ImageButton IBShare;
        ImageButton IBComment;
        ImageButton IBFavourite;

        RatingBar ratingBar;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View v = convertView;

        if (v == null) {
            final LayoutInflater vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.custom_navigation_drawer, null);

            holder = new ViewHolder();
            // holder.videoImagePreview = (RelativeLayout) v.findViewById(R.id.videoImagePreview);
           // holder.txtNewMovies = (TextView) v.findViewById(R.id.txtNewMovies);

          //  holder.IBComment=(ImageButton) v.findViewById(R.id.IBComment);



            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        if (position < AllNavigationList.getNavigationList().size()) {

            holder.txtNewMovies.setOnClickListener(litchener);
           // holder.IBShare.setOnClickListener(litchener);



            final NavigationList query = AllNavigationList.getNavigationList().elementAt(
                    position);
            String ContentType= query.getPosition().toString().trim();

           holder.txtNewMovies.setText(""+ContentType.replaceAll("_", " "));

           Toast.makeText(getContext(), ContentType, Toast.LENGTH_LONG).show();

        }

        return v;
    }
    View.OnClickListener litchener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.txtBanglaMusic:
               /*     CommentDialog commentDialog= new CommentDialog();
                    //shareDialog.Dialog(ctx);
                    commentDialog.Comment(context);*/
                    break;


            }
        }
    };

}
