package com.base_de_datos_I.Tienda.validations;

import com.base_de_datos_I.Tienda.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.base_de_datos_I.Tienda.models.Producto;


@Component
public class ProductoValidation implements Validator{

    @Autowired
    private ProductoService productoService;

    @Override
    public boolean supports(@SuppressWarnings("null") Class<?> clazz) {
        return Producto.class.isAssignableFrom(clazz);
    }

    @SuppressWarnings("null")
    @Override
    public void validate(@SuppressWarnings("null") Object target, @SuppressWarnings("null") Errors errors) {
        Producto producto = (Producto) target;
        System.out.println(producto.getCategoria());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "codigo", null, "El codigo es un campo requerido");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nombre", null, "El nombre es un campo requerido");
        if(productoService.existeProductoPorCodigo(producto.getCodigo())){
            errors.rejectValue("codigo", null, "El codigo de producto ya existe en nuestro sistema");
        }
        if (producto.getPrecio() <= 0) {
            errors.rejectValue("precio", null,"Ingrese un valor valido del precio");
        }
        if (producto.getCategoria() == null || producto.getCategoria().getId() == 0) {
            errors.rejectValue("categoria", null, "La categoria es requerida");
        } 
    }

}
