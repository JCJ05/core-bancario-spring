const modal_codigo = document.querySelector('#exampleModal4');
const formulario_codigo = document.querySelector('#formulario_codigo');


const abrirModalCodigo = async () => {
   
    await sendCodigoUsuario();
    const modalAbrir = bootstrap.Modal.getOrCreateInstance(modal_codigo);
    modalAbrir.show();
}

const sendCodigoUsuario = async () => {
  
    const url = '/api/transferencia/envio/codigo';
    const response = await fetch(url);
    const data = await response.json();

    console.log(data);
}

formulario_codigo.addEventListener('submit', async event => {
  
    event.preventDefault();

    const codigo = document.querySelector('#numero_codigo').value;
    const error_codigo = document.querySelector('#error_codigo');

    error_codigo.innerHTML = "";

    if(codigo.length == 0){
        
        error_codigo.classList.add('text-danger');
        error_codigo.innerHTML = "Ingrese un codigo";
        return;
    }

    await callApiCodigo(codigo);

})

const callApiCodigo = async (codigo) => {

    const url = '/api/transferencia/consulta/codigo';
    const codigo_usuario = {
        "codigo": codigo
    }

    const response = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(codigo_usuario)
    });

    const data = await response.json();

    response.status == 400? errorCodigo(data): successCodigo();

}

const successCodigo = async() => {
  
    const url = '/api/transferencia/envio/transferencia';
    const cuerpo = {
        "numero_cuenta": localStorage.getItem('numero_cuenta'),
        "tipo_tarjeta": localStorage.getItem('tipo_tarjeta'),
        "monto": localStorage.getItem('monto_total'),
        "tipo_moneda": localStorage.getItem('tipo_moneda')
    }

    console.log(cuerpo);

    const response = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(cuerpo)
    })

    const data = await response.json();

    console.log(data);

    response.status == 400? errorCodigo(data): successTransferencia(data);
}

const errorCodigo = (data) => {

    const modalCerrar = bootstrap.Modal.getInstance(modal_codigo);
    modalCerrar.hide();
  
    Swal.fire({
        title: "Error",
        text: data.mensaje,
        icon: "error",
        button: "Aceptar",
    });
}

const successTransferencia = (data) => {

    const modalCerrar = bootstrap.Modal.getInstance(modal_codigo);
    modalCerrar.hide();

    Swal.fire({
        title: "Exito",
        text: data.mensaje,
        icon: "success",
        button: "Aceptar",
    });

    setTimeout(() => {

        location.reload();

    }, 1000);
    
}


export {abrirModalCodigo}