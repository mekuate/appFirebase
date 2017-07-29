package com.mekuate.kyala.ui.ui.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mekuate.kyala.R;
import com.mekuate.kyala.model.entities.Matiere;
import com.mekuate.kyala.model.entities.quiz.Quiz;

/**
 * Created by Mekuate on 05/07/2017.
 */

public abstract class AbsQuizView<Q extends Quiz> extends FrameLayout  {


    private static final int ANSWER_HIDE_DELAY = 500;
    private static final int FOREGROUND_COLOR_CHANGE_DELAY = 750;
    private final int mSpacingDouble=0;
    private final LayoutInflater mLayoutInflater;
    private  Matiere matiere;
    private final Q mQuiz;
    //private final Interpolator mLinearOutSlowInInterpolator;
    private final Handler mHandler = null;
   // private final InputMethodManager mInputMethodManager;
    private boolean mAnswered;
    private TextView mQuestionView;
   // private CheckableFab mSubmitAnswer;
    private Runnable mHideFabRunnable;
    private Runnable mMoveOffScreenRunnable;

    /**
     * creation des vues pour les quiz.
     *
     * @param context The context for this view.
     * @param matiere The {@link Matiere} this view is running in.
     * @param quiz The actual {@link Quiz} that is going to be displayed.
     *
     */
    public AbsQuizView(Context context, Matiere matiere, Q quiz) {
        super(context);
        mQuiz = quiz;
        matiere = matiere;
        mLayoutInflater = LayoutInflater.from(context);
        setId(quiz.getId());
        setUpQuestionView();
        LinearLayout container = createContainerLayout(context);
        View quizContentView = getInitializedContentView();
        addContentView(container, quizContentView);
        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft,
                                       int oldTop, int oldRight, int oldBottom) {
                removeOnLayoutChangeListener(this);
                //Todo Change the bottom nav bar here accordingly
                // changeBottomNavBar();
            }
        });
    }

    /**
     * Sets the behaviour for all question views.
     */
    private void setUpQuestionView() {
        mQuestionView = (TextView) mLayoutInflater.inflate(R.layout.question, this, false);
        mQuestionView.setText(getQuiz().getQuestion());
    }

    private LinearLayout createContainerLayout(Context context) {
        LinearLayout container = new LinearLayout(context);
        container.setId(R.id.absQuizViewContainer);
        container.setOrientation(LinearLayout.VERTICAL);
        return container;
    }

    private View getInitializedContentView() {
        View quizContentView = createQuizContentView();
        quizContentView.setId(R.id.quiz_content);
        quizContentView.setSaveEnabled(true);
        setDefaultPadding(quizContentView);
        if (quizContentView instanceof ViewGroup) {
            ((ViewGroup) quizContentView).setClipToPadding(false);
        }
        setMinHeightInternal(quizContentView);
        return quizContentView;
    }

    private void addContentView(LinearLayout container, View quizContentView) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        container.addView(mQuestionView, layoutParams);
        container.addView(quizContentView, layoutParams);
        addView(container, layoutParams);
    }

   /** private void addFloatingActionButton() {
        final int fabSize = getResources().getDimensionPixelSize(R.dimen.size_fab);
        int bottomOfQuestionView = findViewById(R.id.question_view).getBottom();
        final LayoutParams fabLayoutParams = new LayoutParams(fabSize, fabSize,
                Gravity.END | Gravity.TOP);
        final int halfAFab = fabSize / 2;
        fabLayoutParams.setMargins(0, // left
                bottomOfQuestionView - halfAFab, //top
                0, // right
                mSpacingDouble); // bottom
        MarginLayoutParamsCompat.setMarginEnd(fabLayoutParams, mSpacingDouble);
        if (ApiLevelHelper.isLowerThan(Build.VERSION_CODES.LOLLIPOP)) {
            // Account for the fab's emulated shadow.
            fabLayoutParams.topMargin -= (mSubmitAnswer.getPaddingTop() / 2);
        }
        addView(mSubmitAnswer, fabLayoutParams);
    }

    private CheckableFab getSubmitButton() {
        if (null == mSubmitAnswer) {
            mSubmitAnswer = (CheckableFab) getLayoutInflater()
                    .inflate(R.layout.answer_submit, this, false);
            mSubmitAnswer.hide();
            mSubmitAnswer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitAnswer(v);
                    if (mInputMethodManager.isAcceptingText()) {
                        mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    mSubmitAnswer.setEnabled(false);
                }
            });
        }
        return mSubmitAnswer;
    } */

    private void setDefaultPadding(View view) {
        view.setPadding(mSpacingDouble, mSpacingDouble, mSpacingDouble, mSpacingDouble);
    }

    protected LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

    /**
     * Implementations should create the content view for the type of
     * {} they want to display.
     *
     * @return the created view to solve the quiz.
     */
    protected abstract View createQuizContentView();

    /**
     * Implementations must make sure that the answer provided is evaluated and correctly rated.
     *
     * @return <code>true</code> if the question has been correctly answered, else
     * <code>false</code>.
     */
    protected abstract boolean isAnswerCorrect();

    /**
     * Save the user input to a bundle for orientation changes.
     *
     * @return The bundle containing the user's input.
     */
    public abstract Bundle getUserInput();

    /**
     * Restore the user's input.
     *
     * @param savedInput The input that the user made in a prior instance of this view.
     */
    public abstract void setUserInput(Bundle savedInput);

    public Q getQuiz() {
        return mQuiz;
    }

    protected boolean isAnswered() {
        return mAnswered;
    }

    /**
     * Sets the quiz to answered or unanswered.
     *
     * @param answered <code>true</code> if an answer was selected, else <code>false</code>.
     */
    protected void allowAnswer(final boolean answered) {
       /** if (null != mSubmitAnswer) {
            if (answered) {
                mSubmitAnswer.show();
            } else {
                mSubmitAnswer.hide();
            }
            mAnswered = answered;
        }*/
    }

    /**
     * Sets the quiz to answered if it not already has been answered.
     * Otherwise does nothing.
     */
    protected void allowAnswer() {
        if (!isAnswered()) {
            allowAnswer(true);
        }
    }

    /**
     * Allows children to submit an answer via code.
     */
    protected void submitAnswer() {
        submitAnswer(findViewById(R.id.submitAnswer));
    }

    @SuppressWarnings("UnusedParameters")
    private void submitAnswer(final View v) {
        final boolean answerCorrect = isAnswerCorrect();
        mQuiz.setSolved(true);
        //performScoreAnimation(answerCorrect);
    }

    /**
     * Animates the view nicely when the answer has been submitted.
     *
     * @param answerCorrect <code>true</code> if the answer was correct, else <code>false</code>.
     */
  /**  private void performScoreAnimation(final boolean answerCorrect) {
        ((QuizActivity) getContext()).lockIdlingResource();
        // Decide which background color to use.
        final int backgroundColor = ContextCompat.getColor(getContext(),
                answerCorrect ? R.color.green : R.color.red);
        adjustFab(answerCorrect, backgroundColor);
        resizeView();
        moveViewOffScreen(answerCorrect);
        // Animate the foreground color to match the background color.
        // This overlays all content within the current view.
        animateForegroundColor(backgroundColor);
    }

    @SuppressLint("NewApi")
    private void adjustFab(boolean answerCorrect, int backgroundColor) {
        mSubmitAnswer.setChecked(answerCorrect);
        mSubmitAnswer.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
        mHideFabRunnable = new Runnable() {
            @Override
            public void run() {
                mSubmitAnswer.hide();
            }
        };
        mHandler.postDelayed(mHideFabRunnable, ANSWER_HIDE_DELAY);
    }

    private void resizeView() {
        final float widthHeightRatio = (float) getHeight() / (float) getWidth();
        // Animate X and Y scaling separately to allow different start delays.
        // object animators for x and y with different durations and then run them independently
        resizeViewProperty(View.SCALE_X, .5f, 200);
        resizeViewProperty(View.SCALE_Y, .5f / widthHeightRatio, 300);
    }

    private void resizeViewProperty(Property<View, Float> property,
                                    float targetScale, int durationOffset) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, property,
                1f, targetScale);
        animator.setInterpolator(mLinearOutSlowInInterpolator);
        animator.setStartDelay(FOREGROUND_COLOR_CHANGE_DELAY + durationOffset);
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mHideFabRunnable != null) {
            mHandler.removeCallbacks(mHideFabRunnable);
        }
        if (mMoveOffScreenRunnable != null) {
            mHandler.removeCallbacks(mMoveOffScreenRunnable);
        }
        super.onDetachedFromWindow();
    }

    private void animateForegroundColor(@ColorInt final int targetColor) {
        ObjectAnimator animator = ObjectAnimator.ofInt(this, ViewUtils.FOREGROUND_COLOR,
                Color.TRANSPARENT, targetColor);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setStartDelay(FOREGROUND_COLOR_CHANGE_DELAY);
        animator.start();
    }

    private void moveViewOffScreen(final boolean answerCorrect) {
        // Move the current view off the screen.
        mMoveOffScreenRunnable = new Runnable() {
            @Override
            public void run() {
                mCategory.setScore(getQuiz(), answerCorrect);
                if (getContext() instanceof QuizActivity) {
                    ((QuizActivity) getContext()).proceed();
                }
            }
        };
        mHandler.postDelayed(mMoveOffScreenRunnable,
                FOREGROUND_COLOR_CHANGE_DELAY * 2);
    }
    }**/
    private void setMinHeightInternal(View view) {
        view.setMinimumHeight(getResources().getDimensionPixelSize(R.dimen.min_height_question));

    }


}

