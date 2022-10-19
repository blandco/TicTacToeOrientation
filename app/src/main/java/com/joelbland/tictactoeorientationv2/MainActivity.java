package com.joelbland.tictactoeorientationv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public final static String MA = "MainActivity";
    private TicTacToe tttGame;
    private LinearLayout layoutWrapper;
    private ButtonGridAndTextView tttView;
    private ButtonHandler bh;
    private Point size;
    private int orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w(MA,"Inside onCreate");
        super.onCreate(savedInstanceState);
        tttGame = new TicTacToe();
        bh = new ButtonHandler();
        size = new Point();

        layoutWrapper = new LinearLayout(this);
        layoutWrapper.setGravity(Gravity.CENTER_HORIZONTAL);

        Configuration config = getResources().getConfiguration();
        orientation = config.orientation;

        int w = calculateButtonWidth(config);

        tttView = new ButtonGridAndTextView(this, w, TicTacToe.SIDE, bh);
        tttView.setStatusText(tttGame.result());

        layoutWrapper.addView(tttView);

        setContentView(layoutWrapper);
    }


    private void updateDisplay(Configuration config) {
        Log.w(MA,"Inside updateDisplay");

        int w = calculateButtonWidth(config);
        tttView.resizeGrid(w);
    }

    private int calculateButtonWidth(Configuration config) {
        getWindowManager().getDefaultDisplay().getSize(size);
        int w =0;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            w = (size.y - 219)/(TicTacToe.SIDE + 1);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            w = size.x/TicTacToe.SIDE;
        }
        return w;
    }

    private void showNewGameDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("This is fun");
        alert.setMessage("Play again?");
        PlayDialog playAgain = new PlayDialog();
        alert.setPositiveButton("YES", playAgain);
        alert.setNegativeButton("NO", playAgain);
        alert.show();
    }

    private class ButtonHandler implements View.OnClickListener {
        public void onClick(View v) {
            Log.w(MA, "Inside onClick, v=" + v);
            for(int row = 0; row < TicTacToe.SIDE; row++) {
                for(int col = 0; col < TicTacToe.SIDE; col++) {
                    if(tttView.isButton((Button) v, row, col)) {
                        int play = tttGame.play(row, col);
                        String btnText = getPlayerText(play);
                        if(play == 1) {
                            tttView.setButtonText(row, col, btnText);
                        } else if (play == 2) {
                            tttView.setButtonText(row, col, btnText);
                        }
                        if(tttGame.isGameOver()) {
                            tttView.setStatusBackgroundColor(Color.RED);
                            tttView.enableButtons(false);
                            tttView.setStatusText(tttGame.result());
                            showNewGameDialog();
                        }
                    }
                }
            }
        }
    }

    private String getPlayerText(int play) {
        String btnText = "";
        if(play == 1 && orientation == Configuration.ORIENTATION_PORTRAIT) {
            btnText = "X";
        } else if(play == 1 && orientation == Configuration.ORIENTATION_LANDSCAPE) {
            btnText = "A";
        } else if (play == 2 && orientation == Configuration.ORIENTATION_PORTRAIT) {
            btnText = "O";
        } else if (play == 2 && orientation == Configuration.ORIENTATION_LANDSCAPE) {
            btnText = "Z";
        }
        return btnText;
    }

    private class PlayDialog implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            if(id == -1) {
                tttGame.resetGame();
                tttView.enableButtons(true);
                tttView.resetButtons();
                tttView.setStatusBackgroundColor(Color.GREEN);
                tttView.setStatusText(tttGame.result());
            } else if (id == -2) {
                MainActivity.this.finish();
            }
        }
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        Log.w(MA,"Inside onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
        orientation = newConfig.orientation;
        updateDisplay(newConfig);
    }
}