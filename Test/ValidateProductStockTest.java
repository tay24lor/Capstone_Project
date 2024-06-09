import com.example.software_1_project.AddProductScreenController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ValidateProductStockTest {

    @Test
    @DisplayName("stock is not allowed to be less than min")
    public void validateProductStockCount() {
        AddProductScreenController tester = new AddProductScreenController();
        String testStock = "4";
        System.out.println("1. Below Minimum Test:\n Test Input: " + testStock);
        System.out.println(" Parameters: Max = 10, Min = 5");
        System.out.println(" Expected Output: False\n");
        boolean value = tester.validateFields("99.99", testStock, "10", "5");
        Assertions.assertFalse(value);
    }
    @Test
    @DisplayName("stock is not allowed to be greater than max")
    public void validateProductStockCount2() {
        AddProductScreenController tester = new AddProductScreenController();
        String testStock = "11";
        System.out.println("2. Above Maximum Test:\n Test Input: " + testStock);
        System.out.println(" Parameters: Max = 10, Min = 5");
        System.out.println(" Expected Output: False\n");
        boolean value = tester.validateFields("99.99", testStock, "10", "5");
        Assertions.assertFalse(value);
    }
    @Test
    @DisplayName("stock is >= min AND <= max")
    public void validateProductStockCount3() {
        AddProductScreenController tester = new AddProductScreenController();
        String testStock = "7";
        System.out.println("3. In between Test:\n Test Input: " + testStock);
        System.out.println(" Parameters: Max = 10, Min = 5");
        System.out.println(" Expected Output: True");
        boolean value = tester.validateFields("99.99", testStock, "10", "5");
        Assertions.assertTrue(value);
    }
}
