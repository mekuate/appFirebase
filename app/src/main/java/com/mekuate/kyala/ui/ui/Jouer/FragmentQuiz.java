package com.mekuate.kyala.ui.ui.Jouer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewAnimator;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.mekuate.kyala.R;
import com.mekuate.kyala.model.Persistence.FirebaseHelper;
import com.mekuate.kyala.model.adapter.QuizAdapter;
import com.mekuate.kyala.model.adapter.ScoreAdapter;
import com.mekuate.kyala.model.entities.Epreuve;
import com.mekuate.kyala.model.entities.Matiere;
import com.mekuate.kyala.model.entities.Quize;
import com.mekuate.kyala.model.entities.User;
import com.mekuate.kyala.model.entities.quiz.Quiz;
import com.mekuate.kyala.ui.ui.widget.AbsQuizView;
import com.mekuate.kyala.utils.ApiLevelHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mekuate on 24/07/2017.
 */

public class FragmentQuiz extends Fragment {

    private static final String KEY_USER_INPUT = "USER_INPUT";
    private SolvedStateListener solvedStateListener;
    private AdapterViewAnimator mQuizView;
    private  Epreuve mEpreuve;
    private List <Epreuve> epreuveList = new ArrayList<Epreuve>();
    private List<Quiz> quizs = new ArrayList<>();
    private int quizSize;
    private TextView mProgressText;
    private TextView mNote;
    private ProgressBar mProgressBar;
    private QuizAdapter mQuizAdapter;
    private int note;
    private Matiere matiere;
    private User mUser;
    private FloatingActionButton mDoneButton;
    String matiereId, classeId, niveauId;
    private ScoreAdapter mScoreAdapter;
    DatabaseReference ref;
    private CircleImageView avatar;
    private FirebaseHelper firebaseHelper = new FirebaseHelper();

    public static FragmentQuiz newInstance(User mUser,String classeId, String matiereId, String niveauId,
                                           SolvedStateListener solvedListerner) {
        if (classeId == null || matiereId == null || niveauId ==null) {
            throw new IllegalArgumentException("The Classe/matiere/niveau can not be null");
        }
        Bundle args = new Bundle();
        args.putString("classeId", classeId);
        args.putString("matiereId", matiereId);
        args.putString("niveauId",niveauId);
        args.putParcelable("User", mUser);
        FragmentQuiz fragment = new FragmentQuiz();
        if (solvedListerner != null) {
            fragment.solvedStateListener = solvedListerner;
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        classeId = getArguments().getString("classeId");
         matiereId = getArguments().getString("matiereId");
         niveauId = getArguments().getString("niveauId");
        mUser = getArguments().getParcelable("User");

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_quiz, container, false);
        mProgressText = (TextView) view.findViewById(R.id.progress_text);
        mNote = (TextView) view.findViewById(R.id.note);
        mProgressBar = ((ProgressBar) view.findViewById(R.id.progress));
        avatar = (CircleImageView) view.findViewById(R.id.avatar);
        mDoneButton = (FloatingActionButton) view.findViewById(R.id.quiz_done);

        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mQuizView = (AdapterViewAnimator) view.findViewById(R.id.quiz_view);
        decideOnViewToDisplay();
        setQuizViewAnimations();
        setAvatarDrawable();
        super.onViewCreated(view, savedInstanceState);
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



    @SuppressLint("StringFormatInvalid")
    private void setProgress(int currentQuizPosition) {
        if (!isAdded()) {
            return;
        }
        mProgressText.setText(currentQuizPosition +"/" +this.quizSize);

        mProgressBar.setProgress(currentQuizPosition);
        mNote.setText(String.valueOf(mEpreuve.getScore()));
    }


    private void setAvatarDrawable() {
        Picasso.with(getActivity()).load(mUser.getPhoto()).networkPolicy(NetworkPolicy.OFFLINE).into(avatar, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(getActivity()).load(mUser.getPhoto()).into(avatar);
            }
        });
       ViewCompat.animate(avatar)
                .setInterpolator(new FastOutLinearInInterpolator())
                .setStartDelay(500)
                .scaleX(1)
                .scaleY(1)
                .start();
    }

    private void decideOnViewToDisplay() {

        firebaseHelper.getEpreuve(classeId, matiereId, niveauId, new FirebaseHelper.CallbackFunction() {
            @Override
            public void doWithEpreuve(Epreuve epreuve) {
            }

            @Override
            public void doWithQuiz(Epreuve epreuve, List<Quize> quizs) {
                final boolean isSolved = epreuve.isSolved();
                // INIT PROGRESS TOOLBAR HERE
                // set the quizsize
                quizSize = quizs.size();
                mEpreuve = epreuve;

                if (isSolved) {
                    // todo show epreuve corrigé summary

                    showSummary();
                    if (null != solvedStateListener) {
                        solvedStateListener.onEpreuveSolved();
                    }
                } else {
                    mQuizAdapter = new QuizAdapter(getActivity(),epreuve,quizs);


                   mQuizView.setAdapter(mQuizAdapter);
                    int firstUnsolvedQuizPosition =0;
                    int i =0;
                    do {
                        if(quizs.get(i).isSolved()){
                            firstUnsolvedQuizPosition =i;
                        }
                        i++;
                    }while (quizs.get(i-1).isSolved());
                    mProgressBar.setMax(quizSize);
                    setProgress(firstUnsolvedQuizPosition);
                    mQuizView.setSelection(firstUnsolvedQuizPosition);

                }

            }
        });

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
        mNote.setText(String.valueOf(mEpreuve.getScore()));
        final int count = mQuizView.getAdapter().getCount();
        if (nextItem < count) {
            mQuizView.showNext();

           // todo insert note users into epreuve class
            // firebaseHelper.setEpreuveScore(this.mEpreuve);

            return true;
        }
        markEpreuveSolved();
        return false;
    }

    private void markEpreuveSolved() {
        mEpreuve.setSolved(true);
        //firebaseHelper.setEpreuveScore(this.mEpreuve);
    }

    public void showSummary() {
        @SuppressWarnings("ConstantConditions")
        final ListView scorecardView = (ListView) getView().findViewById(R.id.scorecard);
        mScoreAdapter = getScoreAdapter();
        scorecardView.setAdapter(mScoreAdapter);
        scorecardView.setVisibility(View.VISIBLE);
        firebaseHelper.setEpreuveScore(mEpreuve);
        mQuizView.setVisibility(View.GONE);
    }


    public boolean hasSolvedStateListener() {
        return solvedStateListener != null;
    }
    public void setSolvedStateListener(SolvedStateListener solvedStateListener) {
        this.solvedStateListener = solvedStateListener;
        if (mEpreuve.isSolved() && null != this.solvedStateListener) {
           this.solvedStateListener.onEpreuveSolved();
        }
    }

   private ScoreAdapter getScoreAdapter() {
        if (null == mScoreAdapter) {
            mScoreAdapter = new ScoreAdapter(mEpreuve);
        }
        return mScoreAdapter;
    }

    /**
     * Interface definition for a callback to be invoked when the quiz is started.
     */
    public interface SolvedStateListener {
        /**
         * This method will be invoked when the category has been solved.
         */
        void onEpreuveSolved();
    }

}
