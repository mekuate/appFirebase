package com.mekuate.kyala.ui.ui.Jouer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewAnimator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mekuate.kyala.R;
import com.mekuate.kyala.model.Persistence.FirebaseHelper;
import com.mekuate.kyala.model.adapter.QuizAdapter;
import com.mekuate.kyala.model.entities.Epreuve;
import com.mekuate.kyala.model.entities.Matiere;
import com.mekuate.kyala.model.entities.quiz.Quiz;
import com.mekuate.kyala.ui.ui.widget.AbsQuizView;
import com.mekuate.kyala.utils.ApiLevelHelper;

import java.util.List;

/**
 * Created by Mekuate on 24/07/2017.
 */

public class FragmentQuiz extends Fragment {

    private static final String KEY_USER_INPUT = "USER_INPUT";
    private SolvedStateListener solvedStateListener;
    private AdapterViewAnimator mQuizView;
    private FirebaseHelper firebaseHelper;
    private Epreuve epreuve;
    private List <Epreuve> epreuveList;
    private List <Quiz> quizs;
    private int quizSize;
    private TextView mProgressText;
    private ProgressBar mProgressBar;
    private QuizAdapter mQuizAdapter;
    private Matiere matiere;

    public static FragmentQuiz newInstance(String classeId, String matiereId, String niveauId,
                                           SolvedStateListener solvedListerner) {
        if (classeId == null || matiereId == null || niveauId ==null) {
            throw new IllegalArgumentException("The Classe/matiere/niveau can not be null");
        }
        Bundle args = new Bundle();
        args.putString("classeId", classeId);
        args.putString("matiereId", matiereId);
        args.putString("niveauId",niveauId);
        FragmentQuiz fragment = new FragmentQuiz();
        if (solvedListerner != null) {
            fragment.solvedStateListener = solvedListerner;
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String classeId = getArguments().getString("classeId");
        String matiereId = getArguments().getString("matiereId");
        String niveauId = getArguments().getString("niveauId");
        //obtention des données utiles pour jouer  notament l'épreuve
        getData(classeId,matiereId,niveauId);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Create a themed Context and custom LayoutInflater
        // to get nicely themed views in this Fragment.
       // final LayoutInflater themedInflater = LayoutInflater.from(context);
        return inflater.inflate (R.layout.fragment_quiz, container, false);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mQuizView = (AdapterViewAnimator) view.findViewById(R.id.quiz_view);
        decideOnViewToDisplay();
        setQuizViewAnimations();
       // final AvatarView avatar = (AvatarView) view.findViewById(R.id.avatar);
        //setAvatarDrawable(avatar);
        //initProgressToolbar(view);
        super.onViewCreated(view, savedInstanceState);
    }

    public  void getData(String classeId, String matiereId, String niveauId){
        List <Epreuve> epreuves = firebaseHelper.getEpreuves(classeId,matiereId,niveauId);
        epreuve = firebaseHelper.getRandomEpreuve(epreuves);
        quizs = firebaseHelper.getQuizEpreuve(epreuve.getId());

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setQuizViewAnimations() {
        if (ApiLevelHelper.isLowerThan(Build.VERSION_CODES.LOLLIPOP)) {
            return;
        }
        // TODO set animation adapterviewAnimator for all version of android sdk
       // mQuizView.setInAnimation(getActivity(), R.animator.slide_in_bottom);
        //mQuizView.setOutAnimation(getActivity(), R.animator.slide_out_top);
    }

    private void initProgressToolbar(View view) {
        final int firstUnsolvedQuizPosition = epreuve.getFirstUnsolvedQuizPosition();
        final List<Quiz> quizzes = epreuve.getQuiz();
        quizSize = quizzes.size();
        mProgressText = (TextView) view.findViewById(R.id.progress_text);
        mProgressBar = ((ProgressBar) view.findViewById(R.id.progress));
        mProgressBar.setMax(quizSize);

        setProgress(firstUnsolvedQuizPosition);
    }

    @SuppressLint("StringFormatInvalid")
    private void setProgress(int currentQuizPosition) {
        if (!isAdded()) {
            return;
        }
        mProgressText
                .setText(getString(R.string.quiz_of_quizzes, currentQuizPosition, quizSize));
        mProgressBar.setProgress(currentQuizPosition);
    }

    @SuppressWarnings("ConstantConditions")
   /* private void setAvatarDrawable(AvatarView avatarView) {
        Player player = PreferencesHelper.getPlayer(getActivity());
        avatarView.setAvatar(player.getAvatar().getDrawableId());
        ViewCompat.animate(avatarView)
                .setInterpolator(new FastOutLinearInInterpolator())
                .setStartDelay(500)
                .scaleX(1)
                .scaleY(1)
                .start();
    }*/

    private void decideOnViewToDisplay() {
        final boolean isSolved = epreuve.isSolved();
        if (isSolved) {
           // todo show epreuve corrigé summary showSummary();
            if (null != solvedStateListener) {
                solvedStateListener.onCategorySolved();
            }
        } else {
            mQuizView.setAdapter(getQuizAdapter());
            mQuizView.setSelection(epreuve.getFirstUnsolvedQuizPosition());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        View focusedChild = mQuizView.getFocusedChild();
        if (focusedChild instanceof ViewGroup) {
            View currentView = ((ViewGroup) focusedChild).getChildAt(0);
            if (currentView instanceof AbsQuizView) {
                outState.putBundle(KEY_USER_INPUT, ((AbsQuizView) currentView).getUserInput());
            }
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        restoreQuizState(savedInstanceState);
        super.onViewStateRestored(savedInstanceState);
    }

    private void restoreQuizState(final Bundle savedInstanceState) {
        if (null == savedInstanceState) {
            return;
        }
        mQuizView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft,
                                       int oldTop, int oldRight, int oldBottom) {
                mQuizView.removeOnLayoutChangeListener(this);
                View currentChild = mQuizView.getChildAt(0);
                if (currentChild instanceof ViewGroup) {
                    final View potentialQuizView = ((ViewGroup) currentChild).getChildAt(0);
                    if (potentialQuizView instanceof AbsQuizView) {
                        ((AbsQuizView) potentialQuizView).setUserInput(savedInstanceState.
                                getBundle(KEY_USER_INPUT));
                    }
                }
            }
        });

    }

    private QuizAdapter getQuizAdapter() {
        if (null == mQuizAdapter) {
            mQuizAdapter = new QuizAdapter(getActivity(),matiere,epreuve);
        }
        return mQuizAdapter;
    }

    /**
     * Displays the next page.
     *
     * @return <code>true</code> if there's another quiz to solve, else <code>false</code>.
     */
    public boolean showNextPage() {
        if (null == mQuizView) {
            return false;
        }
        int nextItem = mQuizView.getDisplayedChild() + 1;
        setProgress(nextItem);
        final int count = mQuizView.getAdapter().getCount();
        if (nextItem < count) {
            mQuizView.showNext();
            // todo - save epreuve quiz and each score to the database
            //TopekaDatabaseHelper.updateCategory(getActivity(), mCategory);
            return true;
        }
        markEpreuveSolved();
        return false;
    }

    private void markEpreuveSolved() {
        epreuve.setSolved(true);
        // todo - save epreuve quiz and each score to the database
        //TopekaDatabaseHelper.updateCategory(getActivity(), mCategory);
    }

    /*public void showSummary() {
        @SuppressWarnings("ConstantConditions")
        final ListView scorecardView = (ListView) getView().findViewById(R.id.scorecard);
        mScoreAdapter = getScoreAdapter();
        scorecardView.setAdapter(mScoreAdapter);
        scorecardView.setVisibility(View.VISIBLE);
        mQuizView.setVisibility(View.GONE);
    }*/

    public boolean hasSolvedStateListener() {
        return solvedStateListener != null;
    }
    public void setSolvedStateListener(SolvedStateListener solvedStateListener) {
        solvedStateListener = solvedStateListener;
        if (epreuve.isSolved() && null != solvedStateListener) {
            solvedStateListener.onCategorySolved();
        }
    }

   /* private ScoreAdapter getScoreAdapter() {
        if (null == mScoreAdapter) {
            mScoreAdapter = new ScoreAdapter(mCategory);
        }
        return mScoreAdapter;
    }*/

    /**
     * Interface definition for a callback to be invoked when the quiz is started.
     */
    public interface SolvedStateListener {

        /**
         * This method will be invoked when the category has been solved.
         */
        void onCategorySolved();
    }

}
