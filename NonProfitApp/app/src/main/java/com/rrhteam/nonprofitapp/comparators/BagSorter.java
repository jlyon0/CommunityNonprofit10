package com.rrhteam.nonprofitapp.comparators;

import com.rrhteam.nonprofitapp.DataWrapper;

import java.util.Comparator;

public class BagSorter implements Comparator<DataWrapper> {

    @Override
    public int compare(DataWrapper order1, DataWrapper order2) {
        return order1.getBag().compareTo(order2.getBag());
    }
}
