package az.xazar.msvacation.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum Result {
    PENDING("USER"), APPROVED("ADMIN"), COMPLETED("ADMIN"), DECLINED("ADMIN");

    private final String role;

    Result(String role) {
        this.role = role;
    }

    @JsonValue
    public String toLower() {
        return this.toString().toLowerCase(Locale.ENGLISH);
    }
}
