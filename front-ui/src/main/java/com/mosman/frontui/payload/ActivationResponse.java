package com.mosman.frontui.payload;

public class ActivationResponse {

    private boolean isActivated;

    public ActivationResponse() {
    }

    public ActivationResponse(boolean isActivated) {
        this.isActivated = isActivated;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }
}
