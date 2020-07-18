package com.example.nonprofitapp.viewmodels;

import android.app.Application;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.nonprofitapp.DataRepository;
import com.example.nonprofitapp.DataWrapper;

/**
 */

public class PickupTimeViewModel extends AndroidViewModel {
    private DataRepository dataRepository;
    private DataWrapper dataWrapper;

    public PickupTimeViewModel(@NonNull Application application) {
        super(application);
        if (dataRepository != null) {
            return;
        }
        dataRepository = DataRepository.getInstance(); // gets singleton DataRepo object
        dataWrapper = dataRepository.getDataWrapper();
    }
    public void setDate(TimePicker timePicker) {
        dataWrapper.setHour(timePicker.getCurrentHour()); // 0 indexed 24hr time
        dataWrapper.setMinute(timePicker.getCurrentMinute()); // january is 0 for some reason
    }
}
