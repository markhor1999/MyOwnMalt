package com.preesoft.myownmalt.fragments.show.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ShowProductsModel implements Parcelable {
    String id, name, shelfNumber, rowNumber, searchName, productNumber;
    int priority;

    public ShowProductsModel(String id, String name, String shelfNumber, String rowNumber, String searchName, String productNumber, int priority) {
        this.id = id;
        this.name = name;
        this.shelfNumber = shelfNumber;
        this.rowNumber = rowNumber;
        this.searchName = searchName;
        this.productNumber = productNumber;
        this.priority = priority;
    }

    public ShowProductsModel(String name, String shelfNumber, String rowNumber, String searchName) {
        this.name = name;
        this.shelfNumber = shelfNumber;
        this.rowNumber = rowNumber;
        this.searchName = searchName;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public ShowProductsModel() {
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    protected ShowProductsModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        shelfNumber = in.readString();
        rowNumber = in.readString();
    }

    public static final Creator<ShowProductsModel> CREATOR = new Creator<ShowProductsModel>() {
        @Override
        public ShowProductsModel createFromParcel(Parcel in) {
            return new ShowProductsModel(in);
        }

        @Override
        public ShowProductsModel[] newArray(int size) {
            return new ShowProductsModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(String shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public String getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(String rowNumber) {
        this.rowNumber = rowNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(shelfNumber);
        dest.writeString(rowNumber);
    }
}
