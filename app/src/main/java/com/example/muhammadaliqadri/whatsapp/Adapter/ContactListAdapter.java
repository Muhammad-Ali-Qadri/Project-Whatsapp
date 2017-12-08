package com.example.muhammadaliqadri.whatsapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muhammadaliqadri.whatsapp.Model.WhatsappUser;
import com.example.muhammadaliqadri.whatsapp.R;

import java.util.ArrayList;
import java.util.List;


public class ContactListAdapter extends BaseAdapter  implements Filterable {


    Context context;
    List<WhatsappUser> rowItems;
    List<WhatsappUser> filteredRowItems;
    Filter filter;
    int x;

    List<WhatsappUser> filterRowItems;
    public ContactListAdapter(Context context, List<WhatsappUser> rowItems,int x) {
        this.context = context;
        this.rowItems = rowItems;
        filteredRowItems=new ArrayList<WhatsappUser>();
        this.x=x;
    }

    @Override
    public int getCount() {
        return filteredRowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredRowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return filteredRowItems.indexOf(getItem(position));
    }

    @Override
    public Filter getFilter() {
        if(filter == null){     filter = new ContactsFilter();    }    return filter;
    }

    /* private view holder class */
    private class ViewHolder {
        ImageView profile_pic;
        TextView member_name;
        TextView status;
        TextView message;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.contact_list_item, null);
            holder = new ViewHolder();

            holder.member_name = (TextView) convertView
                    .findViewById(R.id.member_name);
            holder.profile_pic = (ImageView) convertView
                    .findViewById(R.id.profile_pic);
            holder.status = (TextView) convertView.findViewById(R.id.status);

            holder.message = (TextView) convertView.findViewById(R.id.lastMessage);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        WhatsappUser row_pos = (WhatsappUser) getItem(position);
        holder.profile_pic.setImageBitmap(row_pos.getProfilePhoto());
        holder.member_name.setText(row_pos.getUserName());
        holder.status.setText(row_pos.getStatus());
        if(x==0){
            holder.message.setVisibility(View.GONE);
        }
        else{
            holder.message.setVisibility(View.VISIBLE);
            //set data
        }

        return convertView;
    }



    private class ContactsFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<WhatsappUser> filteredList = new ArrayList<WhatsappUser>();
                for (int i = 0; i < rowItems.size(); i++) {
                    if (rowItems.get(i).getUserName().contains(constraint)
                            ||rowItems.get(i).getPhoneNumber().contains(constraint) ) {

                        filteredList.add(rowItems.get(i));
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            } else {
                results.count = rowItems.size();
                results.values = rowItems;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredRowItems = (ArrayList<WhatsappUser>) results.values;
            notifyDataSetChanged();
        }
    }
}
