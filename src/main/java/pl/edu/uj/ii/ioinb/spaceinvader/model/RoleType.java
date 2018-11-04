package pl.edu.uj.ii.ioinb.spaceinvader.model;

public enum RoleType {
    ADMIN("ADMIN"), USER("USER");

    private String type;
    RoleType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
