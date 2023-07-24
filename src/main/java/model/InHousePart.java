package model;
public class InHousePart extends Part {
    private String machineCode;

    public InHousePart(int id, String name, double price, int stock, int min, int max, String machineCode) {
        super(id, name, price, stock, min, max);
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }
}
