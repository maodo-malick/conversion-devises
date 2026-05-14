package conversion_devises.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {

        USER,
        ADMIN;
        //sérialisation JSON de l'enum
        @JsonValue
        public String getValue() {
                return this.name();
        }
}
