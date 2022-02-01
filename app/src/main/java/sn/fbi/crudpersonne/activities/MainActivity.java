package sn.fbi.crudpersonne.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Method;

import sn.fbi.crudpersonne.R;
import sn.fbi.crudpersonne.adapters.CustomListAdapters;
import sn.fbi.crudpersonne.controllers.PersonController;
import sn.fbi.crudpersonne.models.Person;

public class MainActivity extends AppCompatActivity {

    private int currentView;
    private Context mainContext = this;
    private ModalActivity modalActivity;

    public int getCurrentView() {
        return currentView;
    }

    public void setCurrentView(int currentView) {
        this.currentView = currentView;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentView = R.layout.activity_main;
        setCurrentView(currentView);
        try {
            buildMainView();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Toast.makeText(mainContext, "Unexpected error. (status: 0012)", Toast.LENGTH_SHORT).show();
            System.exit(0012);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mainContext, "Unexpected error. (status: 0014)", Toast.LENGTH_SHORT).show();
            System.exit(0014);
        }
    }

    public void buildMainView() throws NoSuchMethodException {
        callGetAll();

        final BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);
        //setSupportActionBar(bottomAppBar);

        final FloatingActionButton floatingActionButton = findViewById(R.id.button);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);

        final ImageButton imageButton = (ImageButton) findViewById(R.id.searchButton);

        final View search = (View) findViewById(R.id.search);

        final EditText editText = (EditText) findViewById(R.id.searchCode);

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.containerSearch);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    buildMainView();
                    swipeRefreshLayout.setRefreshing(false);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    Toast.makeText(mainContext, "Unexpected error. (status: 0012)", Toast.LENGTH_SHORT).show();
                    System.exit(0012);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mainContext, "Unexpected error. (status: 0014)", Toast.LENGTH_SHORT).show();
                    System.exit(0014);
                }
            }
        });

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Method method = null;
                try {
                    method = MainActivity.class.getDeclaredMethod("showDeleteAllDialog");
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    Toast.makeText(mainContext, "Unexpected error. (status: 0012)", Toast.LENGTH_SHORT).show();
                    System.exit(0012);
                }
                modalActivity = new ModalActivity(MainActivity.this, MainActivity.this, method);
                modalActivity.show(getSupportFragmentManager(),modalActivity.getTag());
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentView = R.layout.register_personne_layout;
                setContentView(currentView);
                buildRegisterView();
            }
        });

        coordinatorLayout.setVisibility(View.INVISIBLE);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coordinatorLayout.getVisibility() == View.INVISIBLE) {
                    coordinatorLayout.setVisibility(View.VISIBLE);
                } else {
                    coordinatorLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().trim().equals("")) {
                    Method method = null;
                    try {
                        method = MainActivity.class.getDeclaredMethod("setPersonResult");
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                        Toast.makeText(mainContext, "Unexpected error. (status: 0012)", Toast.LENGTH_SHORT).show();
                        System.exit(0012);
                    }

                    PersonController.getPerson(editText.getText().toString().trim(), mainContext, MainActivity.this, method);
                }
            }
        });
    }

    public void setPersonResult() {
        if(PersonController.getPerson().get(0) == null)
            Toast.makeText(mainContext, "Student not found", Toast.LENGTH_SHORT).show();
        else {
            ListView listView = findViewById(R.id.listView);

            listView.setAdapter(null);

            CustomListAdapters listViewAdapter = new CustomListAdapters(PersonController.getPerson(), mainContext, this);

            listView.setAdapter(listViewAdapter);

            final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.containerSearch);
            coordinatorLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void callGetAll() throws NoSuchMethodException {
        Method method = MainActivity.class.getDeclaredMethod("setContentOnListView");
        PersonController.getAllPersonsRetrofit(mainContext, this, method);
    }

    public void setContentOnListView() {

        if(PersonController.getPersonList().get(0) == null) {
            Toast.makeText(mainContext, "Student not found", Toast.LENGTH_SHORT).show();
        }
        else {
            ListView listView = findViewById(R.id.listView);

            listView.setAdapter(null);

            CustomListAdapters listViewAdapter = new CustomListAdapters(PersonController.getPersonList(), mainContext, this);

            listView.setAdapter(listViewAdapter);
        }
    }

    public void buildRegisterView() {
        final ImageButton imageButton = (ImageButton) findViewById(R.id.insert_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText firstName = (EditText) findViewById(R.id.personFirstName);
                final EditText lastName = (EditText) findViewById(R.id.personLastName);
                final EditText email = (EditText) findViewById(R.id.personEmail);

                if (firstName.getText().toString().trim().length() == 0 || email.getText().toString().trim().length() == 0) {
                    Toast.makeText(mainContext, "Data cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (lastName.getText().toString().trim().length() < 5) {
                    Toast.makeText(mainContext, "Code have to be 5 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                PersonController.insertPerson(mainContext, firstName.getText().toString().trim(), lastName.getText().toString().trim(), email.getText().toString().trim());

                firstName.setText("");
                lastName.setText("");
                email.setText("");
            }
        });
    }

    public void buildUpdateView(Person person) {
        final ImageButton imageButton = (ImageButton) findViewById(R.id.update_button);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText firstName = (EditText) findViewById(R.id.personFirstName);
                final EditText lastName = (EditText) findViewById(R.id.personLastName);
                final EditText email = (EditText) findViewById(R.id.personEmail);

                if (firstName.getText().toString().trim().length() == 0 || email.getText().toString().trim().length() == 0) {
                    Toast.makeText(mainContext, "Data cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                PersonController.updatePerson(mainContext, firstName.getText().toString().trim(), lastName.getText().toString().trim(), email.getText().toString().trim());

                currentView = R.layout.activity_main;
                setContentView(currentView);

                try {
                    buildMainView();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    Toast.makeText(mainContext, "Unexpected error. (status: 0012)", Toast.LENGTH_SHORT).show();
                    System.exit(0012);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mainContext, "Unexpected error. (status: 0014)", Toast.LENGTH_SHORT).show();
                    System.exit(0014);
                }
            }
        });
        final EditText firstName = (EditText) findViewById(R.id.personFirstNameUpdate);
        firstName.setText(person.getFirstName());

        final EditText lastName = (EditText) findViewById(R.id.personLastNameUpdate);
        lastName.setText(person.getLastName());

        final EditText email = (EditText) findViewById(R.id.personEmailUpdate);
        email.setText(person.getEmail());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_app_bar_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {

        if (currentView == R.layout.register_personne_layout || currentView == R.layout.update_persone_layout) {
            currentView = R.layout.activity_main;
            setContentView(currentView);

            try {
                buildMainView();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                Toast.makeText(mainContext, "Unexpected error. (status: 0012)", Toast.LENGTH_SHORT).show();
                System.exit(0012);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mainContext, "Unexpected error. (status: 0014)", Toast.LENGTH_SHORT).show();
                System.exit(0014);
            }
        }
        else {
            super.onBackPressed();
        }
    }

    public void showDeleteDialog(final Person person) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainContext);
        builder.setMessage("Are you sure you want to delete this Person? This action can not be undone.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PersonController.deletePerson(mainContext, person.getEmail());
                        try {
                            buildMainView();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                            Toast.makeText(mainContext, "Unexpected error. (status: 0012)", Toast.LENGTH_SHORT).show();
                            System.exit(0012);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(mainContext, "Unexpected error. (status: 0014)", Toast.LENGTH_SHORT).show();
                            System.exit(0014);
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .show();
    }

}