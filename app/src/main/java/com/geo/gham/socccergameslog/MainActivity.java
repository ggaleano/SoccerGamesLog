package com.geo.gham.socccergameslog;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    // 1 = 0n 0 = Off
    int winOffOn = 0;
    int loseOffOn = 0;
    int tieOffOn = 0;

    //keep track of the goals
    int usGoals = 0;
    int enemyGoals = 0;

    String fullDate = "";

    public static SQLiteDatabase soccerDB;

    public void goToResults(View view) {

        Intent i = new Intent(getApplicationContext(), Results.class);
        startActivity(i);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{

            soccerDB = this.openOrCreateDatabase("SoccerDB", MODE_PRIVATE, null);
            soccerDB.execSQL("CREATE TABLE IF NOT EXISTS outcome (id INTEGER PRIMARY KEY, date VARCHAR, wonlosttie VARCHAR, scoreus INTEGER(2), scoreenemy INTEGER(2))");


        }catch (Exception e) {

            e.printStackTrace();
        }


        final CalendarView calendarView = (CalendarView)findViewById(R.id.calendarView);
        calendarView.setShowWeekNumber(false);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, final int year, int month, final int dayOfMonth) {


                fullDate = convertNumberToMonth(month);
                fullDate += " " + String.valueOf(dayOfMonth) + " " + String.valueOf(year);
                Log.i("Calendar", fullDate);


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                // Get the layout inflater
                LayoutInflater inflater =getLayoutInflater();
                View myview = inflater.inflate(R.layout.alert, null);
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(myview);

                final Button winBt = (Button) myview.findViewById (R.id.winBt);
                final Button loseBt = (Button) myview.findViewById (R.id.loseBt);
                final Button tieBt = (Button) myview.findViewById (R.id.tieBt);

                final TextView usScore = (TextView) myview.findViewById(R.id.usScoreTv);
                final TextView enemyScore = (TextView) myview.findViewById(R.id.enemyScoreTv);

                Button usPlusBt = (Button) myview.findViewById (R.id.plusUsBt);
                Button usMinusBt = (Button) myview.findViewById (R.id.minusUsBt);
                Button enemyPlusBt = (Button) myview.findViewById (R.id.plusEnemyBt);
                Button enemyMinusBt = (Button) myview.findViewById (R.id.minusEnemyBt);

                usPlusBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        usGoals++;
                        usScore.setText(String.valueOf(usGoals));

                    }
                });

                usMinusBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (usGoals > 0) {

                            usGoals--;
                            usScore.setText(String.valueOf(usGoals));

                        }

                    }
                });

                enemyPlusBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        enemyGoals++;
                        enemyScore.setText(String.valueOf(enemyGoals));

                    }
                });

                enemyMinusBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (enemyGoals > 0) {

                            enemyGoals--;
                            enemyScore.setText(String.valueOf(enemyGoals));

                        }

                    }
                });


                winBt.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        //do some stuff
                        if (winOffOn == 0 || loseOffOn == 1 || tieOffOn == 1) {

                            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_button));
                            loseBt.setBackgroundResource(android.R.drawable.btn_default);
                            tieBt.setBackgroundResource(android.R.drawable.btn_default);
                            winOffOn = 1;
                            loseOffOn = 0;
                            tieOffOn = 0;

                        } else if (winOffOn == 1) {

                            v.setBackgroundResource(android.R.drawable.btn_default);
                            winOffOn = 0;

                        }

                    }
                });

                loseBt.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        //do other stuff
                        if (loseOffOn == 0 || winOffOn == 1 || tieOffOn == 1) {

                            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_button));
                            winBt.setBackgroundResource(android.R.drawable.btn_default);
                            tieBt.setBackgroundResource(android.R.drawable.btn_default);
                            winOffOn = 0;
                            loseOffOn = 1;
                            tieOffOn = 0;

                        } else if (loseOffOn == 1) {

                            v.setBackgroundResource(android.R.drawable.btn_default);
                            loseOffOn = 0;

                        }

                    }
                });

                tieBt.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        //do other stuff
                        if (tieOffOn == 0 || winOffOn == 1 || loseOffOn == 1) {

                            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_button));
                            winBt.setBackgroundResource(android.R.drawable.btn_default);
                            loseBt.setBackgroundResource(android.R.drawable.btn_default);
                            winOffOn = 0;
                            loseOffOn = 0;
                            tieOffOn = 1;

                        } else if (tieOffOn == 1) {

                            v.setBackgroundResource(android.R.drawable.btn_default);
                            tieOffOn = 0;

                        }

                    }
                });

                // Add action buttons
                builder.setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String outcomeOfGame = "";

                        if (winOffOn == 1) {

                            outcomeOfGame = "Won";

                        } else if (loseOffOn == 1) {

                            outcomeOfGame = "Lost";


                        } else if (tieOffOn == 1) {

                            outcomeOfGame = "Tie";

                        }

                        Log.i("APP", outcomeOfGame);

                        if (outcomeOfGame.equals("")) {


                            Toast.makeText(MainActivity.this, "You didn't select win, lost, or tie. Try Again", Toast.LENGTH_SHORT).show();

                        } else {
                            try {
                                soccerDB.execSQL("INSERT INTO outcome (date,  wonlosttie, scoreus, scoreenemy)" +
                                        " VALUES ('" + fullDate + "', '" + outcomeOfGame + "', " + usGoals + ", " + enemyGoals + ")");

//                            Cursor c = soccerDB.rawQuery("SELECT * FROM outcome", null);
//
//                            int fullDayIndex = c.getColumnIndex("date");
//                            int wonLostTieIndex = c.getColumnIndex("wonlosttie");
//                            int scoreUsIndex = c.getColumnIndex("scoreus");
//                            int scoreEnemyIndex = c.getColumnIndex("scoreenemy");
//
//                            c.moveToFirst();
//
//
//
//                            while (c != null) {
//
//                                Log.i("DATA", c.getString(fullDayIndex));
//                                Log.i("DATA", c.getString(wonLostTieIndex));
//                                Log.i("DATA", Integer.toString(c.getInt(scoreUsIndex)));
//                                Log.i("DATA", Integer.toString(c.getInt(scoreEnemyIndex)));
//
//                                c.moveToNext();
//
//                            }

                            } catch (Exception e) {

                                Log.e("DATA", "Command didnt execute");
                                e.printStackTrace();
                            }

//                        String sql = "INSERT INTO outcome (month, date, year, wonlosttie, scoreus, scoreenemy) VALUES (? , ? , ? , ? , ? , ?)";
//                        SQLiteStatement statement = soccerDB.compileStatement(sql);
//
//                        statement.bindString(1, monthName);
//                        statement.bindString(2, dayOfMonth);

                            // reseting all variables for new entry


                            Intent i = new Intent(getApplicationContext(), NewEntry.class);
                            //Bundle extras = new Bundle();
                            i.putExtra("GOALS", usGoals);
                            i.putExtra("DATE", fullDate);
                            startActivity(i);

                            winOffOn = 0;
                            loseOffOn = 0;
                            tieOffOn = 0;
                            usGoals = 0;
                            enemyGoals = 0;
                            fullDate = "";

                        }


                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }


    public String convertNumberToMonth(int month) {

        String monthName = "";

        switch(month) {

            case 0:
                monthName = "January";
                break;
            case 1:
                monthName = "February";
                break;
            case 2:
                monthName = "March";
                break;
            case 3:
                monthName = "April";
                break;
            case 4:
                monthName = "May";
                break;
            case 5:
                monthName = "June";
                break;
            case 6:
                monthName = "July";
                break;
            case 7:
                monthName = "August";
                break;
            case 8:
                monthName = "September";
                break;
            case 9:
                monthName = "October";
                break;
            case 10:
                monthName = "November";
                break;
            case 11:
                monthName = "December";
                break;
            default:
                monthName = "N/A";
                break;

        }

        return monthName;

    }

}
