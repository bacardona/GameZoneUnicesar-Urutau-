package com.gamezone.model;

/**
 * Representa un vendedor de GameZone.
 */
public class Seller extends Person {

    private String employeeCode;
    private String shift;

    /**
     * Crea un vendedor con su información básica,
     * código de empleado y turno de trabajo.
     *
     * @param id identificación del vendedor
     * @param name nombre del vendedor
     * @param phone teléfono de contacto del vendedor
     * @param employeeCode código de empleado
     * @param shift turno de trabajo
     */
    public Seller(String id, String name, String phone,
                  String employeeCode, String shift) {
        super(id, name, phone);
        this.employeeCode = employeeCode;
        this.shift = shift;
    }

    /**
     * Obtiene el código de empleado.
     *
     * @return código de empleado
     */
    public String getEmployeeCode() {
        return employeeCode;
    }

    /**
     * Modifica el código de empleado.
     *
     * @param employeeCode nuevo código de empleado
     */
    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    /**
     * Obtiene el turno de trabajo.
     *
     * @return turno de trabajo
     */
    public String getShift() {
        return shift;
    }

    /**
     * Modifica el turno de trabajo.
     *
     * @param shift nuevo turno de trabajo
     */
    public void setShift(String shift) {
        this.shift = shift;
    }
}
