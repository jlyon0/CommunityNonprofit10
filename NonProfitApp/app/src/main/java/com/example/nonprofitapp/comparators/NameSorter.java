package com.example.nonprofitapp.comparators;

import com.example.nonprofitapp.DataWrapper;

import java.util.Comparator;

public class NameSorter implements Comparator<DataWrapper> {

    @Override
    public int compare(DataWrapper order1, DataWrapper order2) {
        return order1.getDisplayName().compareTo(order2.getDisplayName());
    }
}
