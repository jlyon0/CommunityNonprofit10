package com.example.nonprofitapp.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;

    private boolean volunteer;

    public LoggedInUser(String userId, String displayName, boolean volunteer) {
        this.userId = userId;
        this.displayName = displayName;
        this.volunteer = volunteer;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isVolunteer() {
        return volunteer;
    }
}
