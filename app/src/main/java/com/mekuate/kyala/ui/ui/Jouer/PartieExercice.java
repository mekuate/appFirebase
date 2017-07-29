package com.mekuate.kyala.ui.ui.Jouer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mekuate.kyala.R;
import com.mekuate.kyala.model.Persistence.FirebaseHelper;
import com.mekuate.kyala.model.entities.Classe;
import com.mekuate.kyala.model.entities.JsonAttributes;
import com.mekuate.kyala.model.entities.Matiere;
import com.mekuate.kyala.model.entities.Niveau;
import com.mekuate.kyala.model.entities.User;
import com.mekuate.kyala.ui.ui.widget.TextSharedElementCallback;
import com.mekuate.kyala.utils.ApiLevelHelper;

import java.util.List;

public class PartieExercice extends AppCompatActivity {
    private static final String STATE_IS_PLAYING = "isPlaying";
    private static final String FRAGMENT_TAG = "Quiz";

    private Interpolator mInterpolator;

    private FragmentQuiz mQuizFragment;
    private FloatingActionButton mQuizFab;
    private boolean mSavedStateIsPlaying;
    private ImageView mIcon;
    private Animator mCircularReveal;
    private ObjectAnimator mColorChange;
    //private CountingIdlingResource mCountingIdlingResource;
    private View mToolbarBack;
    private Classe mClasse;
    private Matiere mMatiere;
    private Niveau niveau;
    private FirebaseHelper firebaseHelper;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.fab_quiz:
                    startQuizFromClickOn(v);
                    break;
                case R.id.submitAnswer:
                    submitAnswer();
                    break;
                case R.id.quiz_done:
                    ActivityCompat.finishAfterTransition(PartieExercice.this);
                    break;
                case R.id.back:
                    onBackPressed();
                    break;
                default:
                    throw new UnsupportedOperationException(
                            "OnClick has not been implemented for " + getResources().
                                    getResourceName(v.getId()));
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //mCountingIdlingResource = new CountingIdlingResource("Quiz");
        mInterpolator = new FastOutSlowInInterpolator();
        firebaseHelper = new FirebaseHelper();
        if (null != savedInstanceState) {
            mSavedStateIsPlaying = savedInstanceState.getBoolean(STATE_IS_PLAYING);
        }
        super.onCreate(savedInstanceState);
        populate();

       int categoryNameTextSize = getResources()
                .getDimensionPixelSize(R.dimen.category_item_text_size);
        int paddingStart = getResources().getDimensionPixelSize(R.dimen.spacing_double);
        final int startDelay = getResources().getInteger(R.integer.toolbar_transition_duration);
        ActivityCompat.setEnterSharedElementCallback(this,
                new TextSharedElementCallback(categoryNameTextSize, paddingStart) {
                    @Override
                    public void onSharedElementStart(List<String> sharedElementNames,
                                                     List<View> sharedElements,
                                                     List<View> sharedElementSnapshots) {
                        super.onSharedElementStart(sharedElementNames,
                                sharedElements,
                                sharedElementSnapshots);
                        mToolbarBack.setScaleX(0f);
                        mToolbarBack.setScaleY(0f);
                    }

                    @Override
                    public void onSharedElementEnd(List<String> sharedElementNames,
                                                   List<View> sharedElements,
                                                   List<View> sharedElementSnapshots) {
                        super.onSharedElementEnd(sharedElementNames,
                                sharedElements,
                                sharedElementSnapshots);
                        // Make sure to perform this animation after the transition has ended.
                        ViewCompat.animate(mToolbarBack)
                                .setStartDelay(startDelay)
                                .scaleX(1f)
                                .scaleY(1f)
                                .alpha(1f);
                    }
                });


    }

    @Override
    protected void onResume() {
        if (mSavedStateIsPlaying) {
            mQuizFragment = (FragmentQuiz) getSupportFragmentManager().findFragmentByTag(
                    FRAGMENT_TAG);
            if (!mQuizFragment.hasSolvedStateListener()) {
                mQuizFragment.setSolvedStateListener(getSolvedStateListener());
            }
            findViewById(R.id.quiz_fragment_container).setVisibility(View.VISIBLE);
            mQuizFab.hide();
            mIcon.setVisibility(View.GONE);
        } else {
            initQuizFragment();
        }
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(STATE_IS_PLAYING, mQuizFab.getVisibility() == View.GONE);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (mIcon == null || mQuizFab == null) {
            // Skip the animation if icon or fab are not initialized.
            super.onBackPressed();
            return;
        }

        ViewCompat.animate(mToolbarBack)
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0f)
                .setDuration(100)
                .start();

        // Scale the icon and fab to 0 size before calling onBackPressed if it exists.
        ViewCompat.animate(mIcon)
                .scaleX(.7f)
                .scaleY(.7f)
                .alpha(0f)
                .setInterpolator(mInterpolator)
                .start();

        ViewCompat.animate(mQuizFab)
                .scaleX(0f)
                .scaleY(0f)
                .setInterpolator(mInterpolator)
                .setStartDelay(100)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onAnimationEnd(View view) {
                        if (isFinishing() ||
                                (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.JELLY_BEAN_MR1)
                                        && isDestroyed())) {
                            return;
                        }
                        PartieExercice.super.onBackPressed();
                    }
                })
                .start();
    }

    private void startQuizFromClickOn(final View clickedView) {
        initQuizFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.quiz_fragment_container, mQuizFragment, FRAGMENT_TAG)
                .commit();
        final FrameLayout container = (FrameLayout) findViewById(R.id.quiz_fragment_container);
       // container.setBackgroundColor(ContextCompat.
         //       getColor(this, mCategory.getTheme().getWindowBackgroundColor()));
        revealFragmentContainer(clickedView, container);
        // the toolbar should not have more elevation than the content while playing
        setToolbarElevation(false);
    }

    private void revealFragmentContainer(final View clickedView,
                                         final FrameLayout fragmentContainer) {
        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
            revealFragmentContainerLollipop(clickedView, fragmentContainer);
        } else {
            fragmentContainer.setVisibility(View.VISIBLE);
            clickedView.setVisibility(View.GONE);
            mIcon.setVisibility(View.GONE);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void revealFragmentContainerLollipop(final View clickedView,
                                                 final FrameLayout fragmentContainer) {
        prepareCircularReveal(clickedView, fragmentContainer);

        ViewCompat.animate(clickedView)
                .scaleX(0)
                .scaleY(0)
                .alpha(0)
                .setInterpolator(mInterpolator)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        fragmentContainer.setVisibility(View.VISIBLE);
                        clickedView.setVisibility(View.GONE);
                    }
                })
                .start();

        fragmentContainer.setVisibility(View.VISIBLE);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(mCircularReveal).with(mColorChange);
        animatorSet.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void prepareCircularReveal(View startView, FrameLayout targetView) {
        int centerX = (startView.getLeft() + startView.getRight()) / 2;
        // Subtract the start view's height to adjust for relative coordinates on screen.
        int centerY = (startView.getTop() + startView.getBottom()) / 2 - startView.getHeight();
        float endRadius = (float) Math.hypot(centerX, centerY);
        mCircularReveal = ViewAnimationUtils.createCircularReveal(
                targetView, centerX, centerY, startView.getWidth(), endRadius);
        mCircularReveal.setInterpolator(new FastOutLinearInInterpolator());

        mCircularReveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mIcon.setVisibility(View.GONE);
                mCircularReveal.removeListener(this);
            }
        });
        // Adding a color animation from the FAB's color to transparent creates a dissolve like
        // effect to the circular reveal.
       // int accentColor = ContextCompat.getColor(this, mCategory.getTheme().getAccentColor());
      //  mColorChange = ObjectAnimator.ofInt(targetView,
       //         ViewUtils.FOREGROUND_COLOR, accentColor, Color.TRANSPARENT);
       // mColorChange.setEvaluator(new ArgbEvaluator());
        //mColorChange.setInterpolator(mInterpolator);
    }

    @SuppressLint("NewApi")
    public void setToolbarElevation(boolean shouldElevate) {
        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
            mToolbarBack.setElevation(shouldElevate ?
                    getResources().getDimension(R.dimen.elevation_header) : 0);
        }
    }

    private void initQuizFragment() {
        if (mQuizFragment != null) {
            return;
        }
        mQuizFragment = FragmentQuiz.newInstance(mClasse.getId(), mMatiere.getId(),niveau.getId(), getSolvedStateListener());
        // the toolbar should not have more elevation than the content while playing
        setToolbarElevation(false);
    }

    @NonNull
    private FragmentQuiz.SolvedStateListener getSolvedStateListener() {
        return new FragmentQuiz.SolvedStateListener() {
            @Override
            public void onCategorySolved() {
                setResultSolved();
                setToolbarElevation(true);
                displayDoneFab();
            }

            private void displayDoneFab() {
                /* We're re-using the already existing fab and give it some
                 * new values. This has to run delayed due to the queued animation
                 * to hide the fab initially.
                 */
                if (null != mCircularReveal && mCircularReveal.isRunning()) {
                    mCircularReveal.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            showQuizFabWithDoneIcon();
                            mCircularReveal.removeListener(this);
                        }
                    });
                } else {
                    showQuizFabWithDoneIcon();
                }
            }

            private void showQuizFabWithDoneIcon() {
                mQuizFab.setImageResource(R.drawable.ic_tick);
                mQuizFab.setId(R.id.quiz_done);
                mQuizFab.setVisibility(View.VISIBLE);
                mQuizFab.setScaleX(0f);
                mQuizFab.setScaleY(0f);
                ViewCompat.animate(mQuizFab)
                        .scaleX(1)
                        .scaleY(1)
                        .setInterpolator(mInterpolator)
                        .setListener(null)
                        .start();
            }
        };
    }

    private void setResultSolved() {
        Intent categoryIntent = new Intent();
        categoryIntent.putExtra(JsonAttributes.ID, mMatiere.getId());
        setResult(R.id.solved, categoryIntent);
    }

    /**
     * Proceeds the quiz to it's next state.
     */
    public void proceed() {
        submitAnswer();
    }

    /**
     * Solely exists for testing purposes and making sure Espresso does not get confused.
     */
    public void lockIdlingResource() {
        //mCountingIdlingResource.increment();
    }

    private void submitAnswer() {
        //mCountingIdlingResource.decrement();
        if (!mQuizFragment.showNextPage()) {
            //// TODO: 25/07/2017 montrer le corrige de l'epreuve
            //mQuizFragment.showSumary();
            setResultSolved();
            return;
        }
        setToolbarElevation(false);
    }

    @SuppressLint("NewApi")
    private void populate() {
        Intent intent = getIntent();
        intent.setExtrasClassLoader(User.class.getClassLoader());
        User user = intent.getParcelableExtra("User");
       String niveauId =  intent.getStringExtra("niveauKey");
        String matiereId = intent.getStringExtra("matiereKey");
        String classeId = user.getClasse();

        if (null == classeId || matiereId ==null    || niveauId == null) {
            Log.w("jeu partie epreuve", "Didn't find a category. Finishing");
            finish();
        }
        mClasse = firebaseHelper.getClasseWith(classeId);
        mMatiere = firebaseHelper.getMatiere(matiereId);
        niveau = firebaseHelper.getNiveauWith(niveauId);

        //setTheme(mCategory.getTheme().getStyleId());
        /*if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this,
                    mCategory.getTheme().getPrimaryDarkColor()));
        }*/
        initLayout();
        initToolbar();
    }

    private void initLayout() {
        setContentView(R.layout.activity_partie_exercice);
        //noinspection PrivateResource
       /* mIcon = (ImageView) findViewById(R.id.icon);
        int resId = getResources().getIdentifier(IMAGE_CATEGORY + categoryId, DRAWABLE,
                getApplicationContext().getPackageName());
        mIcon.setImageResource(resId);
        mIcon.setImageResource(resId);
        ViewCompat.animate(mIcon)
                .scaleX(1)
                .scaleY(1)
                .alpha(1)
                .setInterpolator(mInterpolator)
                .setStartDelay(300)
                .start();*/
        mQuizFab = (FloatingActionButton) findViewById(R.id.fab_quiz);
        mQuizFab.setImageResource(R.drawable.ic_play);
        if (mSavedStateIsPlaying) {
            mQuizFab.hide();
        } else {
            mQuizFab.show();
        }
        mQuizFab.setOnClickListener(mOnClickListener);
    }

    private void initToolbar() {
        mToolbarBack = findViewById(R.id.back);
        mToolbarBack.setOnClickListener(mOnClickListener);
        TextView titleView = (TextView) findViewById(R.id.category_title);
       // titleView.setText( " - " + mMatiere.getNom() + " - " + niveau.getNom());
      //  titleView.setTextColor(ContextCompat.getColor(this,
            //    category.getTheme().getTextPrimaryColor()));
        if (mSavedStateIsPlaying) {
            // the toolbar should not have more elevation than the content while playing
            setToolbarElevation(false);
        }
    }

    

}
