package com.app.bancario.springappcore.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.app.bancario.springappcore.model.Cuota;
import com.app.bancario.springappcore.model.Solicitud;

@Service
public class LogicaCuotasService {
    
    public List<Cuota> getCuotas(Solicitud solicitud){
       
        double cuotaMensual = solicitud.getMonto();;
        double cuotas = solicitud.getCuotas();
        double seguro = solicitud.getTarifa().getDesgravamen() / 100.0;

        // Calculo de la tasa efectiva mensual(tea)

        double tea = (solicitud.getTarifa().getTea()/100.0);
        double tem = (Math.round(((Math.pow((1+tea) , (30.0/360.0))) - 1) * 100 * 1000.0)/1000.0)/100.0;

        // Calculo de la cuota fija mensual

        double r = Math.round(cuotaMensual * ((tem * (Math.pow((1+tem) , cuotas))) / ((Math.pow((1 + tem) , cuotas)) - 1)) * 100.0)/100.0;
        System.out.println("r = " + r);

        // Calculo del seguro desgravamen

        double seguroDesgravamen = Math.round( seguro * cuotaMensual * 100.0)/100.0;

        // Calculo de la cuota final

        double cuotaFinal = r + seguroDesgravamen;

        // Algoritmo Final

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(solicitud.getFecha_desembolso());

        int year = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(year , mes , dia);

        Date fecha = new Date();

        double amortizacion = 0.0;
     
        List<Cuota> cuotasLista = new ArrayList<>();

        for(int i = 1; i <= solicitud.getCuotas(); i++){

            Cuota cuota = new Cuota();

            if(mes == 11){

                mes = 0;
                year+=1;
                calendar.set(year , mes , dia);
                fecha = calendar.getTime();

            }else {

                mes += 1;

                if(mes == 1 && dia > 28){

                    dia = getNumDiasMes(mes , year);
                }

                calendar.set(year , mes , dia);
                fecha = calendar.getTime();



            }

            int numDias = getNumDiasMes(mes , year);
            double interes = calcularInteresMensual(cuotaMensual , numDias , tea);

            if( i == 1){

                amortizacion = cuotaFinal - interes - seguroDesgravamen;

            }else {

                seguroDesgravamen = Math.round(cuotaMensual * seguro * 100.0) / 100.0;
                amortizacion = Math.round((cuotaFinal - interes - seguroDesgravamen) * 100.0)/100.0;
                r = Math.round((cuotaFinal - seguroDesgravamen) * 100.0)/100.0;

            }

            cuota.setFecha_pago(fecha);
            cuota.setEstado("Pendiente");
            cuota.setNumero_dias(numDias);
            cuota.setSaldo_capital(cuotaMensual);
            cuota.setAmortizacion(amortizacion);
            cuota.setInteres(interes);
            cuota.setCuota(r);
            cuota.setSeguro(seguroDesgravamen);
            cuota.setMora(0.0);
            cuota.setMonto_total(cuotaFinal);

            cuotasLista.add(cuota);

            cuotaMensual = Math.round((cuotaMensual - amortizacion) * 100.0)/100.0;


        }

        return cuotasLista;
        
    }
   

    public double calcularInteresMensual(double monto , int dias , double tea){

        double interesMensual = Math.round((Math.pow((tea + 1) , ((dias * 1.0)/360.0)) - 1) * monto * 100.0)/100.0;

        return interesMensual;

    }

    public int getNumDiasMes(int mes , int year){

        int numeroDias = 0;

        switch(mes){
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                numeroDias=31;
                break;
            case 3:
            case 5:
            case 8:
            case 10:
                numeroDias=30;
                break;
            case 1:

                if(year % 400 == 0 || ((year % 4 == 0) && !(year % 100 == 0))) {

                    numeroDias=29;

                }else{

                    numeroDias=28;

                }
                break;

        }

        return numeroDias;

    }

}
