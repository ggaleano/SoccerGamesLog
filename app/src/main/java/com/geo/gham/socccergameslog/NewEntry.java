package com.geo.gham.socccergameslog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewEntry extends AppCompatActivity {


    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    int numberOfGoals = 0;
    String date = "";
    int tempPlayersGoals = 0;

    List<String> tempPlayers;
    List<Integer> playersGoals;

    TextView newEntryTvTittle;
    TextView goalsForThePlayers;
    TextView howManyGoalsTv;

    Button save;
    Button submit;

    RelativeLayout goalsRelativeLayout;

    //SQLiteDatabase myDatabase;

    public void addGoals(View view) {

        tempPlayersGoals++;
        goalsForThePlayers.setText(String.valueOf(tempPlayersGoals));

    }

    public void subtractGoals(View view) {

        if (tempPlayersGoals > 0) {

            tempPlayersGoals--;
            goalsForThePlayers.setText(String.valueOf(tempPlayersGoals));

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        tempPlayers = new ArrayList<>();
        playersGoals = new ArrayList<>();
        goalsRelativeLayout = (RelativeLayout) findViewById(R.id.goalsAndButtonsLayout);

//        Log.i("APP map", String.valueOf(players.isEmpty()));
//        Log.i("APP map", String.valueOf(players.get(0)));
//        Log.i("APP map", String.valueOf(players.get(1)));


        newEntryTvTittle = (TextView) findViewById(R.id.newEntryTvTittle);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            numberOfGoals = extras.getInt("GOALS");
            date = extras.getString("DATE");

        } else {
            Log.i("App", "Getting goals didn't work");
        }

        updateTopTv();


        goalsForThePlayers = (TextView) findViewById(R.id.goalsForThePlayer);
        howManyGoalsTv = (TextView) findViewById(R.id.playersGoals);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // setting the buttons and onClick
        save = (Button) findViewById(R.id.save);
        submit = (Button) findViewById(R.id.submit);

        goalsRelativeLayout.setVisibility(View.INVISIBLE);


        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                final String tempClickedName = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);

                howManyGoalsTv.setText("How many goals did " +
                        tempClickedName + " scored");
                goalsRelativeLayout.setVisibility(View.VISIBLE);


                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (tempPlayersGoals <= numberOfGoals && tempPlayersGoals != 0) {
                            tempPlayers.add(tempClickedName);
                            playersGoals.add(Integer.valueOf(tempPlayersGoals));

                            //update the top tv after clicking save button and subtracting the goals
                            numberOfGoals -= tempPlayersGoals;
                            updateTopTv();
                            howManyGoalsTv.setText("Click On Another Player To\nAdd Remaining Goals");
                            tempPlayersGoals = 0;
                            goalsForThePlayers.setText("0");

                        } else {

                            Toast.makeText(NewEntry.this, "The number of goals for a player can't be greater the total goals or equal 0", Toast.LENGTH_SHORT).show();

                        }


                        if (numberOfGoals == 0) {

                            newEntryTvTittle.setText("No Goals To Report");
                            howManyGoalsTv.setText("Thank You Click Submit");
                            goalsRelativeLayout.setVisibility(View.INVISIBLE);

                            expListView.collapseGroup(0);
                        }

                    }
                });

                return false;
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    MainActivity.soccerDB.execSQL("CREATE TABLE IF NOT EXISTS goals (" +
                            "id INTEGER PRIMARY KEY, date VARCHAR, playername VARCHAR, numberofgoals INT(2))");

                    for (int i = 0; i < tempPlayers.size(); i++) {

                        MainActivity.soccerDB.execSQL("INSERT INTO goals (date,  playername, numberofgoals)" +
                                " VALUES ('" + date + "', '" + tempPlayers.get(i) + "', " + playersGoals.get(i) + ")");

                        Log.i("GOALS", tempPlayers.get(i) + " They Score: " + playersGoals.get(i) + " goals");

                    }

                    Intent i = new Intent(getApplicationContext(), Results.class);
                    finish();
                    startActivity(i);

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        });

    }

    public void updateTopTv() {

        newEntryTvTittle.setText("Out of the " + String.valueOf(numberOfGoals) + " goals how many did each player scored");

    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Players");

        // Adding child data
        List<String> players = new ArrayList<String>();
        players.add("Geovanny");
        players.add("Eduardo");
        players.add("Oscar");
        players.add("Ricardo");
        players.add("Carlos");
        players.add("Black G");
        players.add("Iker");
        players.add("Ryan");
        players.add("Michael");
        players.add("Russia");
        players.add("Others");

        listDataChild.put(listDataHeader.get(0), players); // Header, Child data
        //listDataChild.put(listDataHeader.get(1), nowShowing);
        //listDataChild.put(listDataHeader.get(2), comingSoon);
    }
}
