package fr.arinonia.opensjgit.service.responses;

public class RegistrationResponse {

    private boolean success;
    private String errorMessage;

    public RegistrationResponse() {}

    public RegistrationResponse(final boolean success, final String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
