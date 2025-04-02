package com.example.mathematicmobileapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OrderNumbersActivity extends AppCompatActivity {

    private List<Integer> numbers = new ArrayList<>();
    private LinearLayout numbersContainer;
    private Button checkButton, resetButton, homeButton, ascendingButton, descendingButton;
    private TextView instructionText, resultText;
    private boolean isAscending = true;
    private int currentScore = 0;
    private int totalAttempts = 0;
    private TextView scoreText;

    // Variables for swapping
    private TextView firstSelectedNumber = null;
    private TextView secondSelectedNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_numbers);

        // Initialize views
        numbersContainer = findViewById(R.id.numbersContainer);
        checkButton = findViewById(R.id.checkButton);
        resetButton = findViewById(R.id.resetButton);
        homeButton = findViewById(R.id.homeButton);
        ascendingButton = findViewById(R.id.ascendingButton);
        descendingButton = findViewById(R.id.descendingButton);
        instructionText = findViewById(R.id.instructionText);
        resultText = findViewById(R.id.resultText);
        scoreText = findViewById(R.id.scoreText);

        scoreText.setText("Score: 0/0");

        // Set listeners
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOrder();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetNumbers();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ascendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAscending = true;
                instructionText.setText("Arrange numbers in INCREASING order (small to big)");
                resetNumbers();
            }
        });

        descendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAscending = false;
                instructionText.setText("Arrange numbers in DECREASING order (big to small)");
                resetNumbers();
            }
        });

        // Generate initial numbers
        generateNumbers();
    }

    private void generateNumbers() {
        numbers.clear();
        Random random = new Random();

        // Generate 5 random numbers between 1 and 999
        for (int i = 0; i < 5; i++) {
            int num = random.nextInt(999) + 1;
            // Ensure no duplicates
            while (numbers.contains(num)) {
                num = random.nextInt(999) + 1;
            }
            numbers.add(num);
        }

        displayNumbers();
    }

    private void displayNumbers() {
        numbersContainer.removeAllViews();
        // Reset selected numbers
        firstSelectedNumber = null;
        secondSelectedNumber = null;

        for (Integer number : numbers) {
            final TextView numberView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(10, 10, 10, 10);
            numberView.setLayoutParams(params);

            numberView.setText(String.valueOf(number));
            numberView.setTextSize(28);
            numberView.setPadding(30, 20, 30, 20);
            numberView.setBackgroundResource(R.drawable.number_background);
            numberView.setTag(number);

            // Set click listener for swapping
            numberView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleNumberSelection((TextView) v);
                }
            });

            numbersContainer.addView(numberView);
        }
    }

    private void handleNumberSelection(TextView selectedNumber) {
        if (firstSelectedNumber == null) {
            // First selection
            firstSelectedNumber = selectedNumber;
            firstSelectedNumber.setBackgroundResource(R.drawable.selected_number_background);
        } else if (secondSelectedNumber == null && selectedNumber != firstSelectedNumber) {
            // Second selection
            secondSelectedNumber = selectedNumber;
            secondSelectedNumber.setBackgroundResource(R.drawable.selected_number_background);

            // Swap the numbers
            swapNumbers();
        } else if (selectedNumber == firstSelectedNumber) {
            // Deselect first number
            firstSelectedNumber.setBackgroundResource(R.drawable.number_background);
            firstSelectedNumber = null;
        } else if (selectedNumber == secondSelectedNumber) {
            // Deselect second number
            secondSelectedNumber.setBackgroundResource(R.drawable.number_background);
            secondSelectedNumber = null;
        }
    }

    private void swapNumbers() {
        // Get the indices of the selected views
        int firstIndex = numbersContainer.indexOfChild(firstSelectedNumber);
        int secondIndex = numbersContainer.indexOfChild(secondSelectedNumber);

        // Swap in the numbers list
        Integer temp = numbers.get(firstIndex);
        numbers.set(firstIndex, numbers.get(secondIndex));
        numbers.set(secondIndex, temp);

        // Reset backgrounds
        firstSelectedNumber.setBackgroundResource(R.drawable.number_background);
        secondSelectedNumber.setBackgroundResource(R.drawable.number_background);

        // Reset selections
        firstSelectedNumber = null;
        secondSelectedNumber = null;

        // Redisplay the numbers
        displayNumbers();
    }

    private void checkOrder() {
        List<Integer> currentOrder = new ArrayList<>(numbers);

        List<Integer> correctOrder = new ArrayList<>(numbers);
        if (isAscending) {
            Collections.sort(correctOrder);
        } else {
            Collections.sort(correctOrder, Collections.reverseOrder());
        }

        boolean isCorrect = currentOrder.equals(correctOrder);
        totalAttempts++;

        if (isCorrect) {
            resultText.setText("Correct! Great job!");
            currentScore++;
        } else {
            resultText.setText("Not quite right. Try again!");
        }

        scoreText.setText("Score: " + currentScore + "/" + totalAttempts);

        // Generate new set of numbers after a short delay
        numbersContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isCorrect) {
                    generateNumbers();
                    resultText.setText("");
                }
            }
        }, 2000);
    }

    private void resetNumbers() {
        Collections.shuffle(numbers);
        displayNumbers();
        resultText.setText("");
    }
}