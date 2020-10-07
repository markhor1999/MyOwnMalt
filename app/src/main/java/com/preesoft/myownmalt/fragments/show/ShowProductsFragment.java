package com.preesoft.myownmalt.fragments.show;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.preesoft.myownmalt.R;
import com.preesoft.myownmalt.activities.auth.LoginActivity;
import com.preesoft.myownmalt.fragments.show.adapter.ShowProductsAdapter;
import com.preesoft.myownmalt.fragments.show.model.ShowProductsModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ShowProductsFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {

    private FloatingActionButton mAddProductButton;
    private RecyclerView recyclerView;
    private List<ShowProductsModel> showProductsModelList = new ArrayList<>();
    private ShowProductsAdapter mShowProductsAdapter;

    private FirebaseFirestore mRootRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_products, container, false);
        setHasOptionsMenu(true);

        mRootRef = FirebaseFirestore.getInstance();

        mAddProductButton = view.findViewById(R.id.floatingActionButton);
        recyclerView = view.findViewById(R.id.recyclerView);
        mShowProductsAdapter = new ShowProductsAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getAllProducts();

        mAddProductButton.setOnClickListener(this);


        showProductsModelList.clear();
        return view;
    }

    private void getAllProducts() {
        CollectionReference collectionReference = mRootRef.collection(getString(R.string.products_collection));

        mRootRef.collection(getString(R.string.products_collection))
                .orderBy("priority")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Log.w(TAG, "listen:error", error);
                            return;
                        }
                        if (value != null) {
                            showProductsModelList.clear();
                            for (QueryDocumentSnapshot snapshot: value) {
                                ShowProductsModel showProductsModel = snapshot.toObject(ShowProductsModel.class);
                                showProductsModelList.add(showProductsModel);
                            }
                            mShowProductsAdapter.setData(showProductsModelList);
                            recyclerView.setAdapter(mShowProductsAdapter);
                        }
                    }
                });
                /*collectionReference
                        .orderBy("priority")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot snapshot: queryDocumentSnapshots) {
                            ShowProductsModel showProductsModel = snapshot.toObject(ShowProductsModel.class);
                            showProductsModelList.add(showProductsModel);
                        }
                        mShowProductsAdapter.setData(showProductsModelList);
                        recyclerView.setAdapter(mShowProductsAdapter);
                    }
                });*/
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.show_all_products_menu, menu);

        MenuItem search = menu.findItem(R.id.show_products_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.isSubmitButtonEnabled();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mAddProductButton) {
            Navigation.findNavController(v).navigate(R.id.action_showProductsFragment_to_addProductFragment);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query != null) {
            searchThroughDatabase(query);
        }
        return true;
    }


    @Override
    public boolean onQueryTextChange(String query) {
        /*if (query != null) {
            searchThroughDatabase(query);
        }*/
        return true;
    }

    private void searchThroughDatabase(String query) {
        final List<ShowProductsModel> queryShowProductsModelList = new ArrayList<>();
        CollectionReference collectionReference = mRootRef.collection(getString(R.string.products_collection));
        collectionReference
                .orderBy("searchName")
                .startAt(query.toLowerCase())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot snapshot: queryDocumentSnapshots) {
                    ShowProductsModel showProductsModel = snapshot.toObject(ShowProductsModel.class);
                    queryShowProductsModelList.add(showProductsModel);
                }

                mShowProductsAdapter.setData(queryShowProductsModelList);
            }
        });
        Log.d(TAG, "searchThroughDatabase: called");
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.show_products_logout) {
            FirebaseAuth.getInstance().signOut();
            sendUserToLoginActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendUserToLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }
}