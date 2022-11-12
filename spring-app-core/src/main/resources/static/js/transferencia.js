import {abrirModalCodigo} from './autorizacion.js';

const modalCuenta = document.querySelector('#exampleModal2');
const formulario_cuenta = document.querySelector('#formulario_cuenta');
const btn_disabled = document.querySelector('#btn_disabled');
const modal_pago = document.querySelector('#exampleModal3');


modalCuenta.addEventListener('show.bs.modal',async event => {
              
    btn_disabled.disabled = true;
 
})

modal_pago.addEventListener('show.bs.modal',async event => {
  
     const form_tarnsfer = document.querySelector('#formulario_transferencia');
     const monto_total = document.querySelector('#monto_total');
  
     monto_total.onpaste = (e) => {
        e.preventDefault();
     }

     form_tarnsfer.addEventListener('submit', transferencia);
})

const transferencia = async (e) => {

    e.preventDefault();

    const tipo_moneda = document.querySelector('#tipo_moneda').value;
    const monto_total = document.querySelector('#monto_total').value;

    if(tipo_moneda == "" || monto_total <= 0 || monto_total == ""){
      
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Todos los campos son obligatorios',
           
        });

        return;

    }

    localStorage.setItem('tipo_moneda', tipo_moneda);
    localStorage.setItem('monto_total', monto_total);

    console.log(tipo_moneda, monto_total);
    
    await ocultarModal(modal_pago);
    await abrirModalCodigo();

}

formulario_cuenta.addEventListener('submit', async event => {
  
    event.preventDefault();

    const cuenta = document.querySelector('#numero_cuenta').value;
    const tipo = document.querySelector('#tipo').value

    const error_tipo = document.querySelector('#error_tipo');
    const error_cuenta = document.querySelector('#error_numero');

    error_tipo.innerHTML = "";
    error_cuenta.innerHTML = "";

    if(tipo == "" || cuenta.length == 0){
            
          if(tipo == ""){

            error_tipo.classList.add('text-danger');
            error_tipo.innerHTML = "Elige un tipo de cuenta";

          }
            
          if(cuenta.length == 0){

            error_cuenta.classList.add('text-danger');
            error_cuenta.innerHTML = "Ingrese un numero de cuenta";

          }
           
            btn_disabled.disabled = true;
            return;
    }

    await callApiTransferencia(cuenta, tipo);

})

const callApiTransferencia = async (cuenta, tipo) => {

    localStorage.setItem('tipo_tarjeta', tipo);
  
    const url = "/api/transferencia/consulta/cuenta";
    const cuentas = {
        "numero_cuenta": cuenta,
        "tipo": tipo
    }

    const response =  await fetch(url ,  {
       
        method: 'POST',
        headers: {
          'Accept': 'application/json',                         
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(cuentas)
      });

    const data = await response.json();
    response.status == 400? mensajeDeerror(data) : creacionDelModal(data);

}

const creacionDelModal = async (data) => {

    await ocultarModal(modalCuenta);

    const {numero_cuenta, nombre , dni} = data.cuenta;

    localStorage.setItem('numero_cuenta', numero_cuenta);

    const modal_content = document.querySelector('#contenido');

    modal_content.innerHTML = `
    <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Datos de la cuenta</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
    </div>
    <div class="modal-body">
        <div class="row">
            <div class="col-6">
                <p>Numero de cuenta: ${numero_cuenta}</p>
            </div>
            <div class="col-6">
                <p>Nombre: ${nombre}</p>
                <p>Dni: ${dni}</p>
            </div>
        </div>
        <form id="formulario_transferencia">
                     
        <div class="mb-3">
        
          <label for="tip_moneda">Elige el tipo moneda</label>
          <select name="tipo_moneda" id="tipo_moneda" class="form-control">
             
             <option value="" selected>Selecciona...</option>
             <option value="soles">Soles</option>
             <option value="dolares">Dolares</option>
    
          </select>

        </div>

        <div class="mb-3">
          <label for="monto_total" class="col-form-label">Monto a depositar:</label>
          <input type="number" class="form-control" id="monto_total" step="0.01" >
        </div>
        
        <button id="btn_disabled" type="submit" class="btn btn-primary">Transferir</button>

      </form>
    </div>
    <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
    </div>
    `;

    
    await abrirModal(modal_pago);
}

const abrirModal = async (modal) => {
  
    const modalAbrir = bootstrap.Modal.getOrCreateInstance(modal_pago);
    modalAbrir.show();
}

const mensajeDeerror = async (data) => {
  
    await ocultarModal(modalCuenta);
    
    const {mensaje} = data;

    Swal.fire({

        icon: 'error',
        title: 'Oops...',
        text: mensaje,
       
    });

}

const ocultarModal = async (modal) => {
   
    const modalOcultar = bootstrap.Modal.getInstance(modal);
    modalOcultar.hide();
}

formulario_cuenta.tipo.addEventListener('change' , () => {
    
        validar_campos();
    
})

formulario_cuenta.numero_cuenta.addEventListener('keydown', (e) => {
   
    var code = (e.which) ? e.which : e.keyCode;

    if((code < 48 || code>57) && code != 8){
        
        e.preventDefault();
        return false;

     }

     validar_campos();

});

const validar_campos = () => {

    const cuenta = document.querySelector('#numero_cuenta').value;
    const tipo = document.querySelector('#tipo').value
    

    if(tipo == "" || cuenta.length == 0){

        btn_disabled.disabled = true;

    }else{

        
        btn_disabled.disabled = false;
        
    }


}