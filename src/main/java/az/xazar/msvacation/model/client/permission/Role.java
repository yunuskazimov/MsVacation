package az.xazar.msvacation.model.client.permission;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum Role {
    USER, ADMIN, REPORTER, MANAGER;

    @JsonValue
    public String toLower() {
        return this.toString().toLowerCase(Locale.ENGLISH);
    }
}
