package com.mekuate.kyala.ui.ui.widget;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Property;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.mekuate.kyala.R;
import com.mekuate.kyala.model.entities.Epreuve;
import com.mekuate.kyala.model.entities.Quize;
import com.mekuate.kyala.model.entities.quiz.Quiz;
import com.mekuate.kyala.ui.ui.Jouer.PartieExercice;
import com.mekuate.kyala.ui.ui.widget.Fab.CheckableFab;
import com.mekuate.kyala.utils.ApiLevelHelper;
import com.mekuate.kyala.utils.SvgSoftwareLayerSetter;
import com.mekuate.kyala.utils.ViewUtils;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by Mekuate on 05/07/2017.
 */

public abstract class AbsQuizView<Q extends Quiz> extends FrameLayout  {


    private static final int ANSWER_HIDE_DELAY = 500;
    private static final int FOREGROUND_COLOR_CHANGE_DELAY = 750;
    private final int mSpacingDouble;
    private final LayoutInflater mLayoutInflater;
    private Epreuve epreuve;
    private final Q mQuiz;
    private  Quize quize;
    private final Interpolator mLinearOutSlowInInterpolator;
    private final Handler mHandler;
    private final InputMethodManager mInputMethodManager;
    private boolean mAnswered;
    private TextView mQuestionView;
    private TextView question;
    private  TextView enonce;
    private ImageView image_enonce;
    private TextView link_enonce;
    private CheckableFab mSubmitAnswer;
    private Runnable mHideFabRunnable;
    private Runnable mMoveOffScreenRunnable;
    private ScrollView vue_enonce;
    private LinearLayout frame_enonce;
    SpannableString ss;
    private RequestBuilder<PictureDrawable> requestBuilder;
    /**
     * creation des vues pour les quiz.
     * @param context The context for this view.
     * @param epreuve The {@link Epreuve} this view is running in.
     * @param quiz The actual {@link Quiz} that is going to be displayed.
     *
     */
    public AbsQuizView(Context context, Epreuve epreuve, Q quiz, Quize quizemodel) {
        super(context);
        mQuiz = quiz;
        this.quize = quizemodel;
        this.epreuve = epreuve;
        mLayoutInflater = LayoutInflater.from(context);
        mSpacingDouble = getResources().getDimensionPixelSize(R.dimen.spacing_triple);
        mLinearOutSlowInInterpolator = new LinearOutSlowInInterpolator();
        mHandler = new Handler();
        mInputMethodManager = (InputMethodManager) context.getSystemService
                (Context.INPUT_METHOD_SERVICE);

        setId(quiz.getId());
        setUpQuestionView();
        mSubmitAnswer = getSubmitButton();
        LinearLayout container = createContainerLayout(context);
        View quizContentView = getInitializedContentView();

        addContentView(container, quizContentView);
        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft,
                                       int oldTop, int oldRight, int oldBottom) {
                removeOnLayoutChangeListener(this);
                addFloatingActionButton();

            }
        });
    }

    /**
     * Sets the behaviour for all question views.
     */
    private void setUpQuestionView() {
        mQuestionView = (TextView) mLayoutInflater.inflate(R.layout.question, this, false);
       question = (TextView) mQuestionView.findViewById(R.id.question_view);
        question.setText(getQuiz().getQuestion());
        setLinkEnonceQuestion();
    }


    private void setLinkEnonceQuestion(){
        vue_enonce = (ScrollView) mLayoutInflater.inflate(R.layout.enonce, this,false);
        enonce = (TextView) vue_enonce.findViewById(R.id.text_enonce);
        frame_enonce = (LinearLayout) vue_enonce.findViewById(R.id.frame_enonce);
        image_enonce = (ImageView) vue_enonce.findViewById(R.id.image_enonce);
        link_enonce =(TextView) vue_enonce.findViewById(R.id.link_enonce);
        enonce.setText(quize.getEnonce());
        //gerer l'image avec glide and androidSVG
        setImage_enonce();
        ss = new SpannableString("L'énoncé");
        // display vue enonce when there is a data to play with
        if(quize.getEnonce() ==null && quize.getImage() == null){
            vue_enonce.setVisibility(GONE);

        }else {
            vue_enonce.setVisibility(VISIBLE);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    if(frame_enonce.isShown()){
                        frame_enonce.setVisibility(GONE);

                    }else{
                        frame_enonce.setVisibility(VISIBLE);

                    }
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(true);
                }
            };
            ss.setSpan(clickableSpan,0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            link_enonce.setText(ss);
            link_enonce.setMovementMethod(LinkMovementMethod.getInstance());
            link_enonce.setHighlightColor(Color.BLUE);

        }

    }

    public void setImage_enonce() {
        image_enonce = (ImageView) vue_enonce.findViewById(R.id.image_enonce);
        Uri uri = Uri.parse("https://de.wikipedia.org/wiki/Scalable_Vector_Graphics#/media/File:SVG_logo.svg");
        requestBuilder = Glide.with(this)
                .as(PictureDrawable.class)
                .transition(withCrossFade())
                .listener(new SvgSoftwareLayerSetter());

        requestBuilder.load(uri).into(image_enonce);
    }

    private LinearLayout createContainerLayout(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setId(R.id.absQuizViewContainer);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return linearLayout;
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
        container.addView(vue_enonce,layoutParams);
        container.addView(mQuestionView, layoutParams);
        container.addView(quizContentView, layoutParams);
        addView(container, layoutParams);
    }

   private void addFloatingActionButton() {
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
    }

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
        if (null != mSubmitAnswer) {
            if (answered) {
                mSubmitAnswer.show();
            } else {
                mSubmitAnswer.hide();
            }
            mAnswered = answered;
        }
    }

    /**
     * Sets the quiz to answered if it is not already has been answered.
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
        quize.setSolved(true);

        performScoreAnimation(answerCorrect);
    }

    /**
     * Animates the view nicely when the answer has been submitted.
     *
     * @param answerCorrect <code>true</code> if the answer was correct, else <code>false</code>.
     */

    private void performScoreAnimation(final boolean answerCorrect) {
        //((PartieExercice) getContext()).lockIdlingResource();
        // Decide which background color to use.
        final int backgroundColor = ContextCompat.getColor(getContext(),
                answerCorrect ? R.color.colorVert_light : R.color.colorRed);
        adjustFab(answerCorrect, backgroundColor);
       // resizeView();
        moveViewOffScreen(answerCorrect);
        // Animate the foreground color to match the background color.
        // This overlays all content within the current view.
        //animateForegroundColor(backgroundColor);
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

                epreuve.setScore(quize, answerCorrect);
                if (getContext() instanceof PartieExercice) {
                    ((PartieExercice) getContext()).proceed();
                }
            }
        };
        mHandler.postDelayed(mMoveOffScreenRunnable,
                FOREGROUND_COLOR_CHANGE_DELAY * 2);
    }

    private void setMinHeightInternal(View view) {
        view.setMinimumHeight(getResources().getDimensionPixelSize(R.dimen.min_height_question));

    }


}


