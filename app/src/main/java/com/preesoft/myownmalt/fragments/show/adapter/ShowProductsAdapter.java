package com.preesoft.myownmalt.fragments.show.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.preesoft.myownmalt.R;
import com.preesoft.myownmalt.fragments.show.ShowProductsFragmentDirections;
import com.preesoft.myownmalt.fragments.show.model.ShowProductsModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ShowProductsAdapter extends RecyclerView.Adapter<ShowProductsAdapter.ShowProductsViewHolder> {
    private Context context;
    private List<ShowProductsModel> mShowProductsModel = new ArrayList<>();
    private FirebaseFirestore mRootRef;

    public ShowProductsAdapter(Context context) {
        this.context = context;
        mRootRef = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ShowProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_products_item, parent, false);
        return new ShowProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowProductsViewHolder holder, final int position) {
        holder.productName.setText(mShowProductsModel.get(position).getName());
        holder.shelfNumber.setText(mShowProductsModel.get(position).getShelfNumber());
        holder.rowNumber.setText(mShowProductsModel.get(position).getRowNumber());
        holder.productNumber.setText(mShowProductsModel.get(position).getProductNumber());

        if (mShowProductsModel.get(position).getPriority() == 1) {
            holder.pinImage.setImageResource(R.drawable.unpin);
        } else {
            holder.pinImage.setImageResource(R.drawable.pin);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = ShowProductsFragmentDirections.actionShowProductsFragmentToUpdateProductFragment(mShowProductsModel.get(position));
                Navigation.findNavController(v).navigate(action);
            }
        });

        holder.pinImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShowProductsModel.get(position).getPriority() == 1) {
                    holder.pinImage.setImageResource(R.drawable.pin);
                    mRootRef.collection(context.getString(R.string.products_collection))
                            .document(mShowProductsModel.get(position).getId())
                            .update("priority", 0);
                } else {
                    holder.pinImage.setImageResource(R.drawable.unpin);
                    mRootRef.collection(context.getString(R.string.products_collection))
                            .document(mShowProductsModel.get(position).getId())
                            .update("priority", 1);
                }
            }
        });

        /*holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.pin_or_unpin, popupMenu.getMenu());

                if (mShowProductsModel.get(position).getPriority() == 0) {
                    popupMenu.getMenu().findItem(R.id.pin_to_top).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.unpin).setVisible(true);
                } else {
                    popupMenu.getMenu().findItem(R.id.pin_to_top).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.unpin).setVisible(false);
                }
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.pin_to_top:
                                mRootRef.collection(context.getString(R.string.products_collection))
                                        .document(mShowProductsModel.get(position).getId())
                                        .update("priority", 0);
                                break;
                            case R.id.unpin:
                                mRootRef.collection(context.getString(R.string.products_collection))
                                        .document(mShowProductsModel.get(position).getId())
                                        .update("priority", 1);
                                break;
                        }
                        return true;
                    }
                });
                return true;
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return Math.max(mShowProductsModel.size(), 0);
    }

    public static class ShowProductsViewHolder extends RecyclerView.ViewHolder {
        TextView productName, shelfNumber, rowNumber, productNumber;
        ImageView pinImage;
        public ShowProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            pinImage = itemView.findViewById(R.id.show_product_pin_or_unpin);
            productName = itemView.findViewById(R.id.show_products_name_item);
            shelfNumber = itemView.findViewById(R.id.show_shelf_number_item);
            rowNumber = itemView.findViewById(R.id.show_row_number_item);
            productNumber = itemView.findViewById(R.id.show_product_number_item);
        }
    }

    public void setData(List<ShowProductsModel> showProductsModel) {
        mShowProductsModel = showProductsModel;
        notifyDataSetChanged();
    }

}
