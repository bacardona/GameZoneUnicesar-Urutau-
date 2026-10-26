package com.gamezone.validation;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.exceptions.ResourceNotFoundException;
import com.gamezone.model.Person;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Validation rules for people (customers and sellers). Services call these
 * static methods before running any business operation. This class only
 * depends on the model and the exceptions: it never calls services or
 * repositories and never touches files.
 */
public final class PersonValidator {

    // Same rules the PersonService applied inline before this refactor.
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\d+");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private PersonValidator() {
        // Utility class: not meant to be instantiated.
    }

    /**
     * Validates the data of a person. The checks run in this order: id, name
     * (not empty, letters and spaces only), phone (not empty, digits only) and
     * email (not empty, basic format).
     *
     * @param id    person identification
     * @param name  person name
     * @param email email address
     * @param phone contact phone number
     * @throws InvalidDataException if any value is invalid
     */
    public static void validatePersonData(String id, String name, String email, String phone) {
        if (id == null || id.isBlank()) {
            throw new InvalidDataException("identificador", "no puede estar vacío");
        }
        if (name == null || name.isBlank()) {
            throw new InvalidDataException("nombre", "no puede estar vacío");
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new InvalidDataException("nombre", "solo puede contener letras y espacios");
        }
        if (phone == null || phone.isBlank()) {
            throw new InvalidDataException("teléfono", "no puede estar vacío");
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new InvalidDataException("teléfono", "solo puede contener números");
        }
        if (email == null || email.isBlank()) {
            throw new InvalidDataException("correo electrónico", "no puede estar vacío");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidDataException("correo electrónico", "no tiene un formato válido");
        }
    }

    /**
     * Validates that a person was found.
     *
     * @param person   the result of a person lookup
     * @param personId the identifier that was searched
     * @throws ResourceNotFoundException if the person is null
     */
    public static void validatePersonExists(Person person, String personId) {
        if (person == null) {
            throw new ResourceNotFoundException("persona", personId);
        }
    }

    /**
     * Validates that no registered person already uses the given identifier.
     *
     * @param existing people already registered
     * @param newId    identifier of the person that is going to be registered
     * @throws BusinessRuleException if the identifier is already in use
     */
    public static void validateUniquePerson(List<Person> existing, String newId) {
        if (existing == null) {
            return;
        }
        for (Person person : existing) {
            if (person.getID().equals(newId)) {
                throw new BusinessRuleException(
                        "Ya existe una persona registrada con el identificador: " + newId + ".");
            }
        }
    }
}