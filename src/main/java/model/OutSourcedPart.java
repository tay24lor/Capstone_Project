package model;

public class OutSourcedPart extends Part{

    private String companyName;

    public OutSourcedPart(Integer id, String name, double price, int stock, int min, int max) {
        super(id, name, price, stock, min, max);
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public static OutSourcedPart copy(Part part) {
        return new OutSourcedPart(part.getId(), part.getName(), part.getPrice(), part.getStock(), part.getMin(), part.getMax());
    }
}
