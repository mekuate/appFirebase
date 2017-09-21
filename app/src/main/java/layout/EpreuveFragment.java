package layout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mekuate.kyala.R;
import com.mekuate.kyala.model.entities.Classe;
import com.mekuate.kyala.model.entities.Niveau;
import com.mekuate.kyala.model.entities.User;
import com.mekuate.kyala.ui.ui.Jouer.SelectionMatiere;
import com.mekuate.kyala.view.NiveauViewHolder;

/**

 * create an instance of this fragment.
 */
public class EpreuveFragment extends Fragment {
    private FloatingActionButton btn;
    private BottomSheetDialog bottomSheetDialog;
    BottomSheetBehavior behavior;
    public View rootView;
    public User user;
    public Classe classe;

    public EpreuveFragment() {
        // Required empty public constructor
    }


    public void setUser(User model){
        this.user =model;
    }

    public void setClasse(Classe classe){
        this.classe = classe;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        classe = getArguments().getParcelable("Classe");

    }

    public void getNiveauClasse(){

    }

    public FloatingActionButton getBtn(){
        return btn;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tab_examen, container, false);
        //set up data comming from activity - classe et user
        Bundle argument = getArguments();
        user = argument.getParcelable("User");
        //set up view
        btn = (FloatingActionButton) rootView.findViewById(R.id.buttonstartexam);
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        View sheetView =getActivity().getLayoutInflater().inflate(R.layout.bottonsheetniveau, null);
        bottomSheetDialog.setContentView(sheetView);
        RecyclerView recyclerView = (RecyclerView) sheetView.findViewById(R.id.recyclerViewNiveau);
        GridLayoutManager llm = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(llm);
        final DatabaseReference roofref= FirebaseDatabase.getInstance().getReference("niveau");
        roofref.keepSynced(true);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("classe").child(user.getClasse()).child("niveau");
        FirebaseRecyclerAdapter<Boolean, NiveauViewHolder> adapter = new FirebaseRecyclerAdapter<Boolean, NiveauViewHolder>(
                Boolean.class,
                R.layout.niveau_list,
                NiveauViewHolder.class,
                ref
        ) {
            @Override
            protected void populateViewHolder(final NiveauViewHolder viewHolder, final Boolean model, final int position) {
                //GET key OF NIVEAU FIRST
                String niveauKey = getRef(position).getKey();
                roofref.child(niveauKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Niveau niveau = dataSnapshot.getValue(Niveau.class);
                        viewHolder.nomNiveauTextView.setText(niveau.getNom());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), SelectionMatiere.class);
                                intent.putExtra("niveau", getRef(position).getKey());
                                intent.putExtra("User", (Parcelable)user);
                                startActivity(intent);
                                bottomSheetDialog.hide();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialogNiveau();
            }
        });
        return rootView;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


    }

    private void showBottomSheetDialogNiveau(){
        bottomSheetDialog.show();


    }



}
