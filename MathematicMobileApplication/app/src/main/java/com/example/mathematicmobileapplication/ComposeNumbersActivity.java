package com.example.mathematicmobileapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.gridlayout.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Random;

public class ComposeNumbersActivity extends AppCompatActivity {

    private TextView targetNumberText, equationText, resultText;
    private GridLayout numbersGrid;
    private Button clearButton, checkButton, newNumberButton, homeButton;
    private int targetNumber;
    private ArrayList<Integer> selectedNumbers = new ArrayList<>();
    private Random random = new Random();
    private int score = 0;
    private int totalAttempts = 0;
    private TextView scoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_compose_numbers);

            // Initialize views
            targetNumberText = findViewById(R.id.targetNumberText);
            equationText = findViewById(R.id.equationText);
            resultText = findViewById(R.id.resultText);
            numbersGrid = findViewById(R.id.numbersGrid);
            clearButton = findViewById(R.id.clearButton);
            checkButton = findViewById(R.id.checkButton);
            newNumberButton = findViewById(R.id.newNumberButton);
            homeButton = findViewById(R.id.homeButton);
            scoreText = findViewById(R.id.scoreText);

            scoreText.setText("Score: 0/0");

            // Set listeners
            clearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearSelection();
                }
            });

            checkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkComposition();
                }
            });

            newNumberButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    generateNewTarget();
                }
            });

            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            // Generate initial target number and number options
            generateNewTarget();
        } catch (Exception e) {
            // Log the exception
            Log.e("ComposeNumbersActivity","Error initializing activity", e);
            // Show a toast message
            Toast.makeText(this, "An error occurred. Please try again", Toast.LENGTH_SHORT).show();
            // Finish the activity if necessary
            finish();
        }
    }

    private void generateNewTarget() {
        // Generate target number between 5 and 20
        targetNumber = random.nextInt(16) + 5;
        targetNumberText.setText(String.valueOf(targetNumber));

        // Clear previous selections
        clearSelection();
        resultText.setText("");

        // Clear previous number grid
        numbersGrid.removeAllViews();

        // Generate number buttons
        generateNumberButtons();
    }

    private void generateNumberButtons() {
        // Generate 9 numbers from 1 to target number-1
        ArrayList<Integer> availableNumbers = new ArrayList<>();

        // Add all possible numbers from 1 to target - 1
        for (int i = 1; i < targetNumber; i++) {
            availableNumbers.add(i);
        }

        // If we have fewer than 9 numbers available, add some repeats
        while (availableNumbers.size() < 9) {
            int num = random.nextInt(targetNumber - 1) + 1;
            availableNumbers.add(num);
        }

        // If we have more than 9, trim the list
        if (availableNumbers.size() > 9) {
            java.util.Collections.shuffle(availableNumbers);
            availableNumbers = new ArrayList<>(availableNumbers.subList(0, 9));
        }

        // Create buttons for each number
        for (int i = 0; i < availableNumbers.size(); i++) {
            final Button numberButton = new Button(this);
            final int number = availableNumbers.get(i);

            numberButton.setText(String.valueOf(number));
            numberButton.setTag(number);

            // Set button appearance
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = getResources().getDimensionPixelSize(R.dimen.number_button_size);
            params.height = getResources().getDimensionPixelSize(R.dimen.number_button_size);
            params.setMargins(8, 8, 8, 8);

            // Position in grid (3x3)
            params.rowSpec = GridLayout.spec(i / 3);
            params.columnSpec = GridLayout.spec(i % 3);

            numberButton.setLayoutParams(params);
            numberButton.setBackgroundResource(R.drawable.number_button);

            // Set click listener
            numberButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedNumbers.size() < 2) {
                        selectedNumbers.add(number);
                        updateEquationDisplay();
                        v.setEnabled(false);
                    }
                }
            });

            numbersGrid.addView(numberButton);
        }
    }

    private void updateEquationDisplay() {
        StringBuilder equation = new StringBuilder();

        for (int i = 0; i < selectedNumbers.size(); i++) {
            equation.append(selectedNumbers.get(i));
            if (i < selectedNumbers.size() - 1) {
                equation.append(" + ");
            }
        }

        equationText.setText(equation.toString());
    }

    private void checkComposition() {
        if (selectedNumbers.size() < 2) {
            resultText.setText("Please select TWO numbers");
            return;
        }

        // Ensure we only use the first two selected numbers if somehow more were selected
        int sum = selectedNumbers.get(0) + selectedNumbers.get(1);

        // Show the complete equation with result
        equationText.setText(selectedNumbers.get(0) + " + " + selectedNumbers.get(1) + " = " + sum);

        totalAttempts++;
        boolean isCorrect = (sum == targetNumber);

        if (isCorrect) {
            resultText.setText("Correct! Good job!");
            score++;
        } else {
            resultText.setText("Not quite right. Try again!");
        }

        scoreText.setText("Score: " + score + "/" + totalAttempts);

        // Generate new target after delay if correct
        if (isCorrect) {
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    generateNewTarget();
                }
            }, 2000);
        }
    }

    private void clearSelection() {
        selectedNumbers.clear();
        equationText.setText("");

        // Re-enable all number buttons
        for (int i = 0; i < numbersGrid.getChildCount(); i++) {
            View child = numbersGrid.getChildAt(i);
            child.setEnabled(true);
        }
    }
}