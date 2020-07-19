package com.example.nonprofitapp.viewmodels;

import android.app.Application;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.nonprofitapp.DataRepository;
import com.example.nonprofitapp.DataWrapper;

/**
 */

public class BagSelectViewModel extends AndroidViewModel {
    private DataRepository dataRepository;
    private DataWrapper dataWrapper;

    public BagSelectViewModel(@NonNull Application application) {
        super(application);
        if (dataRepository != null) {
            return;
        }
        dataRepository = DataRepository.getInstance(); // gets singleton DataRepo object
        dataWrapper = dataRepository.getDataWrapper();
    }
    public void setBag(String bag) {
        dataWrapper.setBag(bag);
    }
}
