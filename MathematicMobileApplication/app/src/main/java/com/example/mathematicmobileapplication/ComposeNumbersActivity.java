package com.example.mathematicmobileapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
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

        // Always include small numbers 1-5
        for (int i = 1; i <= Math.min(5, targetNumber-1); i++) {
            availableNumbers.add(i);
        }

        // Add some larger numbers up to target-1
        int max = targetNumber - 1;
        while (availableNumbers.size() < 9 && max > 0) {
            int num = random.nextInt(max) + 1;
            if (!availableNumbers.contains(num)) {
                availableNumbers.add(num);
            }
        }

        // Shuffle the numbers
        java.util.Collections.shuffle(availableNumbers);

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

        // Check if we have two numbers already selected
        if (selectedNumbers.size() == 2) {
            int sum = 0;
            for (int num : selectedNumbers) {
                sum += num;
            }

            equationText.append(" = ").append(sum);
        }
    }

    private void checkComposition() {
        if (selectedNumbers.size() != 2) {
            resultText.setText("Please select TWO numbers");
            return;
        }

        int sum = 0;
        for (int num : selectedNumbers) {
            sum += num;
        }

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