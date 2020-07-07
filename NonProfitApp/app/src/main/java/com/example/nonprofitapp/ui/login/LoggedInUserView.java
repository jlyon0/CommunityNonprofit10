package com.example.nonprofitapp.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    //... other data fields that may be accessible to the UI
    private boolean isVolunteer;

    LoggedInUserView(String displayName, boolean isVolunteer) {
        this.displayName = displayName;
        this.isVolunteer = isVolunteer;
    }

    String getDisplayName() {
        return displayName;
    }
    boolean isVolunteer() {
        return isVolunteer;
    }
}
