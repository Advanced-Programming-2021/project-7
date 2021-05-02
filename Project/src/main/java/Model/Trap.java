package Model;

public class Trap extends Card {
    private String trapType;
    private String icon;


    public Trap(String name, String type, String icon, String description, String trapType, int price) {
        super(name, description, type, price);
        this.trapType = trapType;
        this.icon = icon;
    }

    public void setTrapType(String trapType) {
        this.trapType = trapType;
    }

    public void setTrapIcon(String icon) {
        this.icon = icon;
    }

    public String getTrapType() {
        return trapType;
    }

    public String getTrapIcon() {
        return icon;
    }

    public String getTrapName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCardNumber() {
        return cardNumber;
    }
}