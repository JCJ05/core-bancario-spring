package com.app.bancario.springappcore.integration.equifax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.bancario.springappcore.integration.equifax.dto.DatosConsulta;
import com.app.bancario.springappcore.integration.equifax.dto.UserEquifax;

@Service
public class EquifaxApi {

    @Autowired
    private RestTemplate restTemplate;


    public String findExitsUserInEquifaxByDni(String dni){

        UserEquifax equifax = null;
        String mensajeFinal = null;

        try {

           equifax = restTemplate.getForObject("https://equifax-gnaranja-22.herokuapp.com/api/{dni}", UserEquifax.class, dni);

           StringBuilder sb = new StringBuilder(""); 
           sb.append("El usuarioo: ");
           sb.append(equifax.getNombres());
           sb.append(" ");
           sb.append(equifax.getApellidos());
           sb.append(" tiene ");
           sb.append(equifax.getDatosConsultas().size());
           

           String mensaje = equifax.getDatosConsultas().size() > 1 ? " deudas registradas en Equifax con los montos de: " : " deuda registrada en equifax con el monto de: ";
           sb.append(mensaje);
           sb.append("\n");

           
           for ( DatosConsulta consulta : equifax.getDatosConsultas()) {
              
                sb.append("Monto: ");
                sb.append("S./ ");
                sb.append(consulta.getDeuda());
                sb.append(" Con Fecha registrada : ");
                sb.append(consulta.getFecha());
                sb.append(" Dias Vencidas: ");
                sb.append(consulta.getDiasVencidas());
                sb.append("\n");
           }

              mensajeFinal = sb.toString();

        } catch (Exception e) {
            
            System.out.println("No existe el usuario");
        }
      

        return mensajeFinal;

    }

    
}
