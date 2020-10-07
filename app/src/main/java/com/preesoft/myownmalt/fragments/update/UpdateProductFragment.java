package com.preesoft.myownmalt.fragments.update;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.preesoft.myownmalt.R;
import com.preesoft.myownmalt.fragments.show.model.ShowProductsModel;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class UpdateProductFragment extends Fragment {

    private ShowProductsModel showProductsModel;
    private EditText name, shelfNumber, rowNumber, productNumber;

    private RelativeLayout mProgressBarLayout;
    private boolean isProgressVisible = false;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_update_product, container, false);
        setHasOptionsMenu(true);
        showProductsModel = UpdateProductFragmentArgs.fromBundle(getArguments()).getCurrentItem();

        name = view.findViewById(R.id.update_product_name_et);
        shelfNumber = view.findViewById(R.id.update_shelf_number_et);
        rowNumber = view.findViewById(R.id.update_row_number_et);
        productNumber = view.findViewById(R.id.update_product_number_et);
        mProgressBarLayout = view.findViewById(R.id.progress_bar_layout);

        name.setText(showProductsModel.getName());
        shelfNumber.setText(showProductsModel.getShelfNumber());
        rowNumber.setText(showProductsModel.getRowNumber());
        productNumber.setText(showProductsModel.getProductNumber());

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_product:
                if(!isProgressVisible) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    mProgressBarLayout.setVisibility(View.VISIBLE);
                    isProgressVisible = true;

                    String updateName = name.getText().toString();
                    String updatedSearchName = updateName.toLowerCase();
                    String updateShelfNumber = shelfNumber.getText().toString();
                    String updateRowNumber = rowNumber.getText().toString();
                    String updateProductNumber = productNumber.getText().toString();


                    CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(getString(R.string.products_collection));
                    DocumentReference documentReference = collectionReference.document(showProductsModel.getId());

                    Map map = new HashMap();
                    map.put("name", updateName);
                    map.put("shelfNumber", updateShelfNumber);
                    map.put("rowNumber", updateRowNumber);
                    map.put("searchName", updatedSearchName);
                    map.put("productNumber", updateProductNumber);


                    documentReference.update(map).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            mProgressBarLayout.setVisibility(View.INVISIBLE);
                            isProgressVisible = false;
                            Navigation.findNavController(view).navigate(R.id.action_updateProductFragment_to_showProductsFragment);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressBarLayout.setVisibility(View.INVISIBLE);
                            isProgressVisible = false;
                        }
                    });
                }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu);
    }
}