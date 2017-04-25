package tekkan.synappz.com.tekkan.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import tekkan.synappz.com.tekkan.R;

/**
 * Created by Ajaya Tiwari on 24/04/17.
 */

public class AddPetAdapter extends RecyclerView.Adapter<AddPetAdapter.ViewHolder> {

    // after creating model will implements
    //private List<Model> itemsList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_add_new_pet, parent, false);

        return new ViewHolder(itemView);
    }


    //constuctor for adding list
//    public AddPetAdapter(List<Model> itemsList) {
//        this.itemsList = itemsList;
//    }

        public AddPetAdapter() {

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, badgeCount;
        ImageView profileIcon;

        public ViewHolder(View view) {
            super(view);
            profileIcon = (ImageView)view.findViewById(R.id.profile_image);
            name = (TextView) view.findViewById(R.id.profile_name);
            badgeCount = (TextView) view.findViewById(R.id.badge_item);
        }
    }

}
