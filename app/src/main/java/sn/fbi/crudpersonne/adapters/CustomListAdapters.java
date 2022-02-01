package sn.fbi.crudpersonne.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import sn.fbi.crudpersonne.activities.MainActivity;
import sn.fbi.crudpersonne.R;
import sn.fbi.crudpersonne.models.Person;

public class CustomListAdapters extends ArrayAdapter<Person> implements View.OnClickListener {

    private ArrayList<Person> dataSet;
    private Context mainContext;
    private MainActivity current;

    private TextView PersonFistName;
    private TextView PersonLastName;
    private TextView PersonEmail;
    private ImageButton buttonEditPerson;
    private ImageButton buttonDeletePerson;

    public CustomListAdapters(ArrayList<Person> data ,Context mainContext, MainActivity current){
        super(mainContext, R.layout.list_item_layout, data);
        this.dataSet = data;
        this.mainContext = mainContext;
        this.current = current;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Person person = getItem(position);

        View result;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.list_item_layout, parent, false);
        PersonFistName = (TextView) convertView.findViewById(R.id.personFirstName);
        PersonLastName = (TextView) convertView.findViewById(R.id.personLastName);
        PersonEmail = (TextView) convertView.findViewById(R.id.personEmail);
        buttonEditPerson = (ImageButton) convertView.findViewById(R.id.buttonEditPerson);
        buttonDeletePerson = (ImageButton) convertView.findViewById(R.id.buttonDeletePerson);

        result = convertView;

        Animation animation = AnimationUtils.loadAnimation(mainContext, R.anim.slide_animation);
        result.startAnimation(animation);

        if (person.getFirstName().length() <= 24)
            PersonFistName.setText(person.getFirstName());
        else
            PersonFistName.setText(person.getFirstName().substring(0, 22) + "...");

        if (person.getLastName().length() <= 24)
            PersonLastName.setText(person.getLastName());
        else
            PersonLastName.setText(person.getLastName().substring(0, 22) + "...");

        PersonEmail.setText(person.getEmail());
        buttonEditPerson.setTag(position);
        buttonDeletePerson.setTag(position);

        buttonEditPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer)v.getTag();
                Object object = getItem(position);
                Person student = (Person) object;
                current.setCurrentView(R.layout.update_persone_layout);
                current.setContentView(current.getCurrentView());
                current.buildUpdateView(student);
            }
        });
        buttonDeletePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer)v.getTag();
                Object object = getItem(position);
                Person student = (Person) object;
                current.showDeleteDialog(student);
            }
        });

        return convertView;
    }

    @Override
    public void onClick(View v) {

    }
}
