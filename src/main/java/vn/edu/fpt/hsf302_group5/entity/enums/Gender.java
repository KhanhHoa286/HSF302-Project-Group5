package vn.edu.fpt.hsf302_group5.entity.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum Gender {
    MALE("Nam"), FEMALE("Nữ"), OTHER("Khác");
    private String value;
    Gender(String value) {
        this.value = value;
    }
}
