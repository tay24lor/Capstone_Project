package model;
public class InHousePart extends Part {
    private String machineCode;

    public InHousePart(int id, String name, double price, int stock, int min, int max, int machineCode) {
        super(id, name, price, stock, min, max);
    }

    public int getMachineCode() {
        return Integer.parseInt(machineCode);
    }

    public void setMachineCode(int machineCode) {
        this.machineCode = String.valueOf(machineCode);
    }
}
