package com.preesoft.myownmalt.fragments.add;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.preesoft.myownmalt.R;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.INPUT_METHOD_SERVICE;

public class AddProductFragment extends Fragment {
    private EditText mProductName, mShelfNumber, mRowNumber, mProductNumber;
    private FirebaseFirestore mRootRef;
    private View view;
    private RelativeLayout mProgressBarLayout;
    private boolean isProgressVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_product, container, false);
        setHasOptionsMenu(true);

        mProductName = view.findViewById(R.id.add_product_name_et);
        mShelfNumber = view.findViewById(R.id.add_shelf_number_et);
        mProductNumber = view.findViewById(R.id.add_product_number_et);
        mRowNumber = view.findViewById(R.id.add_row_number_et);
        mRootRef = FirebaseFirestore.getInstance();
        mProgressBarLayout = view.findViewById(R.id.progress_bar_layout);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.add_product_menu) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            uploadProductToDatabase();
        }
        return super.onOptionsItemSelected(item);
    }

    private void uploadProductToDatabase() {
        String name = mProductName.getText().toString();
        String productNumber = mProductNumber.getText().toString();
        String searchName = name.toLowerCase();
        String shelfNumber = mShelfNumber.getText().toString();
        String rowNumber = mRowNumber.getText().toString();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(shelfNumber) || TextUtils.isEmpty(rowNumber)) {
            Toast.makeText(getActivity(), "Please Fill All The Fields", Toast.LENGTH_SHORT).show();
        } else {
            mProgressBarLayout.setVisibility(View.VISIBLE);
            isProgressVisible = true;
            CollectionReference productsCollection = mRootRef.collection(getString(R.string.products_collection));
            String id = productsCollection.document().getId();
            Map productMap = new HashMap();
            productMap.put("id", id);
            productMap.put("name", name);
            productMap.put("shelfNumber", shelfNumber);
            productMap.put("rowNumber", rowNumber);
            productMap.put("searchName", searchName);
            productMap.put("productNumber", productNumber);
            productMap.put("priority", 1);

            DocumentReference documentReference = productsCollection.document(id);
            documentReference.set(productMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                mProgressBarLayout.setVisibility(View.INVISIBLE);
                                isProgressVisible = false;
                                Navigation.findNavController(view).navigate(R.id.action_addProductFragment_to_showProductsFragment);
                            } else {
                                mProgressBarLayout.setVisibility(View.INVISIBLE);
                                isProgressVisible = false;
                                Log.d(TAG, "onComplete: " + task.getException().getMessage());
                            }
                        }
                    });
        }
    }
}