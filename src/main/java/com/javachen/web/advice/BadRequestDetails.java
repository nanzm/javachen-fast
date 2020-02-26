package com.javachen.web.advice;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class BadRequestDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("message")
    private String message;

    @JsonProperty("path")
    private String path;

    @JsonProperty("rejectedValue")
    private Object rejectedValue = null;

    public BadRequestDetails message(String message) {
        this.message = message;
        return this;
    }

    @NotNull
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BadRequestDetails path(String path) {
        this.path = path;
        return this;
    }

    /**
     * Path to erroneous parameter or request attribute
     *
     * @return path
     */
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public BadRequestDetails rejectedValue(Object rejectedValue) {
        this.rejectedValue = rejectedValue;
        return this;
    }

    /**
     * Rejected value of erroneous attribute
     *
     * @return rejectedValue
     */
    public Object getRejectedValue() {
        return rejectedValue;
    }

    public void setRejectedValue(Object rejectedValue) {
        this.rejectedValue = rejectedValue;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BadRequestDetails badRequestDetails = (BadRequestDetails) o;
        return Objects.equals(this.message, badRequestDetails.message) &&
                Objects.equals(this.path, badRequestDetails.path) &&
                Objects.equals(this.rejectedValue, badRequestDetails.rejectedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, path, rejectedValue);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BadRequestDetails {\n");

        sb.append("    message: ").append(toIndentedString(message)).append("\n");
        sb.append("    path: ").append(toIndentedString(path)).append("\n");
        sb.append("    rejectedValue: ").append(toIndentedString(rejectedValue)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

