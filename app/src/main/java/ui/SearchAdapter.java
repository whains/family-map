package ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import model.DataCache;
import model.Event;
import model.Person;
import will.familymap.R;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{
    public DataCache data = DataCache.initialize();

    private List<Object> searchResults;
    private Context context;

    public SearchAdapter(List<Object> searchResults, Context context) {
        this.searchResults = searchResults;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        String object;
        TextView info;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getPosition();
                    Object clicked = searchResults.get(pos);
                    if (clicked.getClass().equals(Event.class)) {
                        Intent intent = new Intent(context, EventActivity.class);
                        context.startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(context, PersonActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
            info = (TextView) itemView.findViewById(R.id.info);
            icon = (ImageView) itemView.findViewById(R.id.icon);
        }

        public void setObject(String object) {
            this.object = object;
        }
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(viewItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String searchInfo = null;
        Drawable drawable = null;

        if (searchResults.get(position).getClass().equals(Event.class)) {
            Event result = (Event) searchResults.get(position);
            searchInfo = result.getEventType() + ": " + result.getCity() + ", " + result.getCountry()
                    + " -- " + result.getYear();
            drawable = context.getResources().getDrawable(R.drawable.mapmarker);

            Event event = data.getEvents().get(result.getEventID());
            data.setEvent(event);
        }

        else if (searchResults.get(position).getClass().equals(Person.class)) {
            Person result = (Person) searchResults.get(position);
            searchInfo = result.getFirstName() + " " + result.getLastName();
            if (result.getGender().toLowerCase().equals("m")) {
                drawable = context.getResources().getDrawable(R.drawable.male);
            }
            else {
                drawable = context.getResources().getDrawable(R.drawable.female);
            }

            Person person = data.getPeople().get(result.getPersonID());
            data.setPerson(person);
        }

        holder.info.setText(searchInfo);
        holder.icon.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

}
