package dimo.applycaran.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dimo.applycaran.Models.Game;
import dimo.applycaran.R;
import dimo.applycaran.View.Adapter.GameAdapter;

public class GameFragment extends Fragment {
    View mView;
    RecyclerView mRecyclerView;
    GameAdapter mGameAdapter;
    ArrayList<Game> mGamesList;

    public GameFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate( R.layout.game_fragment, container, false );

        mGamesList = new ArrayList<>();
        mRecyclerView = mView.findViewById( R.id.GameRecyclerView);
        mGameAdapter = new GameAdapter(mGamesList, getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mGameAdapter);

        DefineGames();
        return mView;
    }

    private void DefineGames() {
        mGamesList.add(new Game("Chess",""));
        mGamesList.add(new Game("Backgommon",""));
        mGameAdapter.notifyDataSetChanged();

    }
}
