package com.base_de_datos_I.Tienda.validations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.base_de_datos_I.Tienda.models.Cliente;
import com.base_de_datos_I.Tienda.service.ClienteService;

@Component
public class ClienteValidation implements Validator {

    @Autowired
    private ClienteService clienteService;

    @Override
    public boolean supports(@SuppressWarnings("null") Class<?> clazz) {
        return Cliente.class.isAssignableFrom(clazz);
    }

    @SuppressWarnings("null")
    @Override
    public void validate(@SuppressWarnings("null") Object target, @SuppressWarnings("null") Errors errors) {
        Cliente cliente = (Cliente) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dui", null, "El DUI es un campo requerido");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nombre", null, "El nombre es un campo requerido");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "apellido", null, "El apellido es un campo requerido");
        if (cliente.getDui().length() < 9 || cliente.getDui().length() > 10) {
            errors.rejectValue("dui", null, "El DUI debe contener 9 caracteres");
        } else if (clienteService.existeDuiEnLaBD(cliente.getDui()) && cliente.getDui() != null) {
            errors.rejectValue("dui", null, "El DUI ingresado ya existe en nuestro sistema");
        }
    }

}
