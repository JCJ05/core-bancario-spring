google.charts.load('current', {'packages':['bar']});
google.charts.setOnLoadCallback(drawStuff);

async function drawStuff() {
 
  const url = '/api/transferencia/movimientos/tarjeta';
  const response = await fetch(url);
  const dataObject = await response.json();

  const {movimientosAll} = dataObject;
  const {movimientos} = movimientosAll;

  const meses = ["1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9" , "10" , "11" , "12"];
  const mesesWord = ["Enero" , "Febrero" , "Marzo" , "Abril" , "Mayo" , "Junio" , "Julio" , "Agosto" , "Septiembre" , "Octubre" , "Noviembre" , "Diciembre"];
  
  var data = new google.visualization.DataTable();

  data.addColumn('string', 'Mes');
  data.addColumn('number', 'Ingresos');
  data.addColumn('number', 'Gastos');

  meses.forEach(mes => {

    let ingresos = 0;
    let gastos = 0;

    movimientos.forEach(({fechaHora , monto , tipo}) => {
       
      if(tipo === "I" || tipo === "F"){
          
          return;

      }else {

         if(fechaHora.substring(5,7) === mes && monto > 0){

             ingresos += monto;
         }

         if(fechaHora.substring(5,7) === mes && monto < 0){

               gastos += monto;
          }

      }

     
    });

      data.addRow([mesesWord[mes - 1], ingresos, Math.abs(gastos)]);
  });
 

  var options = {
    width: 1000,
    chart: {
      title: 'Gráfico de Ingresos y Gastos',
      subtitle: 'Ingresos y Gastos por mes'
    },
    bars: 'vertical' 
  };

var chart = new google.charts.Bar(document.getElementById('dual_x_div'));
chart.draw(data, options);

};