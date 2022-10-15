package com.app.bancario.springappcore.validation;

import java.util.Calendar;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.bancario.springappcore.model.Solicitud;

@Component
public class SolicitudValidador implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
       
        return Solicitud.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        
         Solicitud solicitud = (Solicitud) target;

         if(solicitud.getNum_hijos_mayores() != null && solicitud.getNum_hijos() != null){
               
            if(solicitud.getNum_hijos_mayores() > solicitud.getNum_hijos()){

                errors.rejectValue("num_hijos_mayores", "num_hijos_mayores.invalido");
            
            }

         }

         if(solicitud.getFecha_desembolso() != null){
           
            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            int dia = calendar.get(Calendar.DAY_OF_MONTH);

            calendar.setTime(solicitud.getFecha_desembolso());

            int year2 = calendar.get(Calendar.YEAR);
            int mes2 = calendar.get(Calendar.MONTH);
            int dia2 = calendar.get(Calendar.DAY_OF_MONTH);


            if(year2 < year){

                errors.rejectValue("fecha_desembolso", "fecha_desembolso.invalido");
            }

            if(year2 == year && mes2 < mes){

                errors.rejectValue("fecha_desembolso", "fecha_desembolso.invalido");
            }

            if(year2 == year && mes2 == mes && dia2 < dia){

                errors.rejectValue("fecha_desembolso", "fecha_desembolso.invalido");
            }


        }

        
        
    }
    
}
