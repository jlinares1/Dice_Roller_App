package edu.ggc.lutz.diceroller_solution;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity
        implements RollLengthDialogFragment.OnRollLengthSelectedListener {

    private static String TAG = "Dice";
    public static final int MAX_DICE = 5;
    private static final int REQUEST_CODE = 999;
    private int mVisibleDice;
    private Dice[] mDice;
    private ImageView[] mDiceImageViews;
    private Menu mMenu;
    private CountDownTimer mTimer;
    private int mTimerLength = 2000;
    private int currentDice;
    private GestureDetectorCompat mDetector;
    private LinearLayout linearRight;
    private ImageView ivDice5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an array of Dice
        mDice = new Dice[MAX_DICE];
        for (int i = 0; i < MAX_DICE; i++) {
            mDice[i] = new Dice(i + 1);
        }

        // Create an array of ImageViews
        mDiceImageViews = new ImageView[MAX_DICE];
        mDiceImageViews[0] = findViewById(R.id.dice1);
        mDiceImageViews[1] = findViewById(R.id.dice2);
        mDiceImageViews[2] = findViewById(R.id.dice3);
        mDiceImageViews[3] = findViewById(R.id.dice4);
        mDiceImageViews[4] = findViewById(R.id.dice5);

        // All dice are initially visible
        mVisibleDice = MAX_DICE;

        showDice();

        for (ImageView iv : mDiceImageViews)
            registerForContextMenu(iv);

        mDetector = new GestureDetectorCompat(this, new DiceGestureListener());

        linearRight = findViewById(R.id.rightColumn);
        ivDice5 = findViewById(R.id.dice5);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);

        for (int i = 0; i < mDiceImageViews.length; i++)
            if (mDiceImageViews[i] == ((ImageView)v))
                currentDice = i;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_one:
                mDice[currentDice].addOne();
                showDice();
                return true;
            case R.id.subtract_one:
                mDice[currentDice].subtractOne();
                showDice();
                return true;
            case R.id.roll:
                rollDice(currentDice);
                showDice();
                return true;
            case R.id.change_dice:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.change_dice);

                final CharSequence[] configs = {"4-Sided", "6-Sided", "10-Sided"};

                builder.setItems(configs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {            // 3-Sided
                            mDice[currentDice].setSides(4);
                        }
                        else if (item == 1) {
                            mDice[currentDice].setSides(6);// 4-Sided
                        }
                        else {
                            mDice[currentDice].setSides(10);   // 6-Sided
                        }
                        showDice();
                    }
                });
                AlertDialog alert = builder.create();
               alert.show();
               return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    private void showDice() {
        // Display only the number of dice visible
        for (int i = 0; i < mVisibleDice; i++) {
            Drawable diceDrawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                diceDrawable = getResources().getDrawable(mDice[i].getImageId(),
                        getApplicationContext().getTheme());
            } else {
                diceDrawable = getResources().getDrawable(mDice[i].getImageId());
            }

            mDiceImageViews[i].setImageDrawable(diceDrawable);
            mDiceImageViews[i].setContentDescription(Integer.toString(mDice[i].getNumber()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Determine which menu option was chosen
        switch (item.getItemId()) {
            case R.id.action_stop:
                mTimer.cancel();
                mTimer.onFinish();
//                item.setVisible(false);
                return true;

            case R.id.action_roll:
                rollDice();
                return true;

            case R.id.action_one:
                changeDiceVisibility(1);
                linearRight.setVisibility(View.GONE);
                showDice();
                return true;

            case R.id.action_two:
                changeDiceVisibility(2);
                linearRight.setVisibility(View.GONE);
                showDice();
                return true;

            case R.id.action_three:
                changeDiceVisibility(3);
                linearRight.setVisibility(View.GONE);
                ivDice5.setVisibility(View.VISIBLE);
                showDice();
                return true;

            case R.id.action_four:
                changeDiceVisibility(4);
                linearRight.setVisibility(View.VISIBLE);
                showDice();
                return true;

            case R.id.action_five:
                changeDiceVisibility(5);
                linearRight.setVisibility(View.VISIBLE);
                showDice();
                return true;

            case R.id.action_color:
                Intent intent = new Intent(MainActivity.this, ColorChooser.class);
                startActivityForResult(intent, REQUEST_CODE);
                return true;

            case R.id.action_about:
                Intent intent1 = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent1);
                return true;

            // Action item added to app bar
            case R.id.action_roll_length:
                FragmentManager manager = getFragmentManager();
                RollLengthDialogFragment dialog = new RollLengthDialogFragment();
                dialog.show(manager, "rollLengthDialog");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            int c = data.getExtras().getInt("color");
            Log.v("CHECkMain", "Color: " + c);
            for(int i = 0; i < MAX_DICE; i++){
                mDiceImageViews[i].setColorFilter(c);
            }
        }
    }

    private void changeDiceVisibility(int numVisible) {
        mVisibleDice = numVisible;

        // Make dice visible
        for (int i = 0; i < numVisible; i++) {
            mDiceImageViews[i].setVisibility(View.VISIBLE);
        }

        // Hide remaining dice
        for (int i = numVisible; i < MAX_DICE; i++) {
            mDiceImageViews[i].setVisibility(View.GONE);
        }

    }

    // overload for 0, 1 args
    private void rollDice() { rollDice(0, mVisibleDice); }
    private void rollDice(int single) { rollDice(single, single+1); }


    private void rollDice(final int start, final int end) {
        mMenu.findItem(R.id.action_stop).setVisible(true);
        mMenu.findItem(R.id.action_roll).setVisible(false);
        mMenu.findItem(R.id.action_roll_length).setVisible(false);

        if (mTimer != null) {
            mTimer.cancel();
        }

        mTimer = new CountDownTimer(mTimerLength, 100) {
            public void onTick(long millisUntilFinished) {
                for (int i = start; i < end; i++) {
                    mDice[i].roll();
                }
                showDice();
            }

            public void onFinish() {
                mMenu.findItem(R.id.action_stop).setVisible(false);
                mMenu.findItem(R.id.action_roll).setVisible(true);
                mMenu.findItem(R.id.action_roll_length).setVisible(true);
            }
        }.start();
    }

    @Override
    public void onRollLengthClick(int which) {
        // Convert to milliseconds
        mTimerLength = 1000 * (which + 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private class DiceGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            rollDice();
            return true;
        }
    }

}

