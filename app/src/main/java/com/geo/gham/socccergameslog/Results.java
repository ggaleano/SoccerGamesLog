package com.geo.gham.socccergameslog;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Results extends AppCompatActivity {

    ListView listView;
    SimpleAdapter simpleAdapter;
    List<Map<String, String>> gameOutcome;
    List<String> playersInTheTeam;
    TextView playerAndGoals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        playersInTheTeam = new ArrayList<String>();
        playersInTheTeam.add("Geovanny");
        playersInTheTeam.add("Eduardo");
        playersInTheTeam.add("Oscar");
        playersInTheTeam.add("Ricardo");
        playersInTheTeam.add("Carlos");
        playersInTheTeam.add("Black G");
        playersInTheTeam.add("Iker");
        playersInTheTeam.add("Ryan");
        playersInTheTeam.add("Michael");
        playersInTheTeam.add("Russia");
        playersInTheTeam.add("Others");


        listView = (ListView) findViewById(R.id.listView);
        gameOutcome = new ArrayList<Map<String, String>>();
        simpleAdapter = new SimpleAdapter(this, gameOutcome,
                android.R.layout.simple_expandable_list_item_2, new String[]{"date", "winlosttie"},
                new int[]{android.R.id.text1, android.R.id.text2});


        try {

            Cursor c = MainActivity.soccerDB.rawQuery("SELECT * FROM outcome", null);

            int idIndex = c.getColumnIndex("id");
            int fullDayIndex = c.getColumnIndex("date");
            int wonLostTieIndex = c.getColumnIndex("wonlosttie");
            //int scoreUsIndex = c.getColumnIndex("scoreus");
            //int scoreEnemyIndex = c.getColumnIndex("scoreenemy");

            c.moveToFirst();


            while (c != null) {

                Map<String, String> gameResult = new HashMap<String, String>(2);
                gameResult.put("date", c.getString(fullDayIndex));
                gameResult.put("winlosttie", c.getString(wonLostTieIndex));

                gameOutcome.add(gameResult);


                c.moveToNext();

            }


        } catch (Exception e) {

            Log.e("DATA", "Command didnt execute");
            e.printStackTrace();
        }

        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //builder.setTitle(String.valueOf(listView.getItemAtPosition(position)));

                AlertDialog.Builder builder = new AlertDialog.Builder(Results.this);
                // Get the layout inflater
                LayoutInflater inflater = getLayoutInflater();
                View myview = inflater.inflate(R.layout.alert_dialog_db, null);
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(myview);

                final TextView title = (TextView) myview.findViewById(R.id.tittle);
                final TextView winLostTieTv = (TextView) myview.findViewById(R.id.winlosttie);
                final TextView score = (TextView) myview.findViewById(R.id.score);
                final TextView playerAndGoals = (TextView) myview.findViewById(R.id.playerAndGoals);

                title.setText(gameOutcome.get(position).get("date"));
                winLostTieTv.setText(gameOutcome.get(position).get("winlosttie"));


                try {
                    // select the score for that day;
                    String q = "SELECT scoreus, scoreenemy FROM outcome";

                    Cursor cursor = MainActivity.soccerDB.rawQuery(q, null);
                    int scoreUs = cursor.getColumnIndex("scoreus");
                    int scoreEnemy = cursor.getColumnIndex("scoreenemy");

                    cursor.moveToPosition(position);

                    if (cursor != null) {

                        String tempScore = "";
                        tempScore = String.valueOf(cursor.getInt(scoreUs)) + "-" + String.valueOf(cursor.getInt(scoreEnemy));
                        score.setText(tempScore);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    // select the score for that day;
                    //String tempDate = gameOutcome.get(position).get("date");
//                    String q2="SELECT * FROM goals WHERE date LIKE September";
//
//                    Cursor  cursor = MainActivity.soccerDB.rawQuery(q2,null);
                    Cursor cursor = MainActivity.soccerDB.rawQuery("SELECT * FROM goals WHERE date ='" + gameOutcome.get(position).get("date") + "'", null);
                    //int fullDayIndex = cursor.getColumnIndex("date");
                    int playerName = cursor.getColumnIndex("playername");
                    int numberOfGoals = cursor.getColumnIndex("numberofgoals");

                    cursor.moveToFirst();


                    String tempPlayerAndGoals = "";
                    while (cursor != null) {

                        //Log.i("GOALS_DB", cursor.getString(fullDayIndex));

                        tempPlayerAndGoals += cursor.getString(playerName) + " ("
                                + String.valueOf(cursor.getInt(numberOfGoals) + ")\n");
                        playerAndGoals.setText(tempPlayerAndGoals);

                        cursor.moveToNext();

                    }

//                    cursor.moveToFirst();
//
//                    if (cursor != null) {
//
//                        Log.i("NAMES", cursor.getString(playerName));
//                        Log.i("NAMES", String.valueOf(numberOfGoals));
//
////                        String tempPlayerAndGoals = "";
////                        tempPlayerAndGoals += String.valueOf(cursor.getString(playerName)) + " ("
////                                + String.valueOf(cursor.getInt(numberOfGoals) + ")\n");
////                        playerAndGoals.setText(tempPlayerAndGoals);
//
//                        cursor.moveToNext();

                    //}
                } catch (Exception e) {
                    e.printStackTrace();
                }


                builder.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        playerAndGoals = (TextView) findViewById(R.id.playerAndGoals);
        Boolean onOff = false;
        int id = item.getItemId();
        if (id == R.id.goals) {

            listView.setVisibility(View.INVISIBLE);
            playerAndGoals.setVisibility(View.VISIBLE);

            String allThePlayersWithGoals = "";

            for (int i = 0; i < playersInTheTeam.size(); i++) {
                int tempGoals = 0;

                try {

                    Cursor c = MainActivity.soccerDB.rawQuery("SELECT numberofgoals FROM goals WHERE playername ='" + playersInTheTeam.get(i) + "'", null);

                    int numGoals = c.getColumnIndex("numberofgoals");

                    c.moveToFirst();

                    while (c != null) {

                        tempGoals += c.getInt(numGoals);
                        c.moveToNext();

                    }


                } catch (Exception e) {

                    e.printStackTrace();

                }

                if (tempGoals == 0) {

                    allThePlayersWithGoals += playersInTheTeam.get(i) + " has no goals so far" + "\n";


                } else {
                    allThePlayersWithGoals += playersInTheTeam.get(i) + " has " + tempGoals + " goals " + "\n";
                }

            }

            playerAndGoals.setText(allThePlayersWithGoals);


        } else if (id == R.id.dates) {

            playerAndGoals.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);

        }
        return super.onOptionsItemSelected(item);
    }

}
