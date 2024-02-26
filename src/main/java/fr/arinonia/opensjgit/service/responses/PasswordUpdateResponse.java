package fr.arinonia.opensjgit.service.responses;

public class PasswordUpdateResponse {

    private boolean success;
    private String errorMessage;

    public PasswordUpdateResponse() {}

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
