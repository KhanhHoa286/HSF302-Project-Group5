package vn.edu.fpt.hsf302_group5.entity.enums;

public enum IndustryStatus {
    ACTIVE("Hoạt động"), 
    INACTIVE("Không hoạt động");

    private final String displayName;

    IndustryStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
