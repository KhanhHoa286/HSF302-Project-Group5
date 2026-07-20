package vn.edu.fpt.hsf302_group5.entity.enums;

public enum CompanyStatus {
    ACTIVE("Hoạt động"), 
    INACTIVE("Đang khóa");

    private final String displayName;

    CompanyStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
