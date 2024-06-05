package model;

/** In-House class. This extends the abstract Part class.
 * @author Taylor Aubrey
 */
public class InHousePart extends Part {
    private String machineCode;

    /**
     *
     * @param id id to set
     * @param name name to set
     * @param price price to set
     * @param stock stock to set
     * @param min minimum to set
     * @param max maximum to set
     */
    public InHousePart(int id, String name, double price, int stock, int min, int max, int prodID) {
        super(id, name, price, stock, min, max, prodID, "");
    }

    /**
     * @return the machine code
     */
    public int getMachineCode() {
        return Integer.parseInt(machineCode);
    }

    /**
     * @param machineCode the machine code to set
     */
    public void setMachineCode(int machineCode) {
        this.machineCode = String.valueOf(machineCode);
    }
}
