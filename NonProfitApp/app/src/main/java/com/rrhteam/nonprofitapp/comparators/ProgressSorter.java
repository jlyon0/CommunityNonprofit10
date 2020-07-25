package com.rrhteam.nonprofitapp.comparators;


import com.rrhteam.nonprofitapp.DataWrapper;

import java.util.Comparator;

public class ProgressSorter implements Comparator<DataWrapper> {

    @Override
    public int compare(DataWrapper order1, DataWrapper order2) {
        return Integer.compare(order1.getProgress(), order2.getProgress());
    }
}
