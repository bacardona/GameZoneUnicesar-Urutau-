package com.gamezone.model;

/**
 * Represents a seller in the GameZone system. A seller inherits common person
 * information and includes an employee code and work shift.
 */
public class Seller extends Person {

    private String employeeCode;
    private String shift;

    /**
     * Creates a seller with their basic information, employee code, and work
     * shift.
     *
     * @param id seller's identification
     * @param name seller's name
     * @param phone seller's contact phone number
     * @param employeeCode seller's employee code
     * @param shift seller's work shift
     */
    public Seller(String id, String name, String phone,
            String employeeCode, String shift) {
        super(id, name, phone);
        this.employeeCode = employeeCode;
        this.shift = shift;
    }

    /**
     * Gets the seller's employee code.
     *
     * @return seller's employee code
     */
    public String getEmployeeCode() {
        return employeeCode;
    }

    /**
     * Updates the seller's employee code.
     *
     * @param employeeCode new employee code
     */
    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    /**
     * Gets the seller's work shift.
     *
     * @return seller's work shift
     */
    public String getShift() {
        return shift;
    }

    /**
     * Updates the seller's work shift.
     *
     * @param shift new work shift
     */
    public void setShift(String shift) {
        this.shift = shift;
    }
}
