package model;

public class OutSourcedPart extends Part {

    private String companyName;

    public OutSourcedPart(Integer id, String name, double price, int stock, int min, int max, int prodID) {
        super(id, name, price, stock, min, max, prodID, "");
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }
}
