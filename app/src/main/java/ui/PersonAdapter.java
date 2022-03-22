package ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import model.Event;
import model.Person;
import will.familymap.R;

public class PersonAdapter extends BaseExpandableListAdapter {
    TextView title;

    TextView info1;
    TextView info2;
    ImageView icon;

    private Person person;
    private List<Person> family;
    private List<Event> lifeEvents;
    private Context context;

    public PersonAdapter(Person person, List<Person> family, List<Event> lifeEvents, Context context) {
        this.person = person;
        this.family = family;
        this.lifeEvents = lifeEvents;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return 2;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == 0) { return family.size(); }
        else { return lifeEvents.size(); }
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (groupPosition == 0) { return family; }
        else { return lifeEvents; }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (groupPosition == 0) { return family.get(childPosition); }
        else { return lifeEvents.get(childPosition); }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String currTitle;
        if (groupPosition == 0) { currTitle = "FAMILY"; }
        else { currTitle = "EVENTS"; }

        if (convertView == null) {
            LayoutInflater window = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = window.inflate(R.layout.list_title, null);
        }

        title = convertView.findViewById(R.id.title);
        title.setText(currTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater window = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = window.inflate(R.layout.list_child, null);
        }

        info1 = convertView.findViewById(R.id.person);
        info2 = convertView.findViewById(R.id.info);
        icon = convertView.findViewById(R.id.icon);

        if (groupPosition == 0) {
            Person current = (Person) getChild(groupPosition, childPosition);

            info1.setText(current.getFirstName() + " " + current.getLastName());

            if (person.getFatherID() != null) {
                if (person.getFatherID().equals(current.getPersonID())) {
                    info2.setText("Father");
                }
            }

            if (person.getFatherID() != null) {
                if (person.getMotherID().equals(current.getPersonID())) {
                    info2.setText("Mother");
                }
            }

            if (person.getSpouseID() != null) {
                if (person.getSpouseID().equals(current.getPersonID())) {
                    info2.setText("Spouse");
                }
            }

            if (current.getFatherID() != null && current.getMotherID() != null) {
                if (person.getPersonID().equals(current.getFatherID())
                        || person.getPersonID().equals(current.getMotherID())) {
                    info2.setText("Child");
                }
            }

            if (current.getGender().equals("m")) {
                icon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.male));
            }
            else if (current.getGender().equals("f")) {
                icon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.female));
            }
        }

        else {
            Event current = (Event) getChild(groupPosition, childPosition);
            info1.setText(current.getEventType() + ": " + current.getCity() + ", " + current.getCountry());
            info2.setText("" + current.getYear());
            icon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.mapmarker));
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
