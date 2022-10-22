const tarjeta = document.querySelector('#tarjeta'),
	  btnAbrirFormulario = document.querySelector('#btn-abrir-formulario'),
	  formulario = document.querySelector('#formulario-tarjeta'),
	  numeroTarjeta = document.querySelector('#tarjeta .numero'),
	  nombreTarjeta = document.querySelector('#tarjeta .nombre'),
	  logoMarca = document.querySelector('#logo-marca'),
	  firma = document.querySelector('#tarjeta .firma p'),
	  mesExpiracion = document.querySelector('#tarjeta .mes'),
	  yearExpiracion = document.querySelector('#tarjeta .year');
	  ccv = document.querySelector('#tarjeta .ccv');


const mostrarFrente = () => {
	if(tarjeta.classList.contains('active')){
		tarjeta.classList.remove('active');
	}
}


tarjeta.addEventListener('click', () => {
	tarjeta.classList.toggle('active');
});


btnAbrirFormulario.addEventListener('click', () => {
	btnAbrirFormulario.classList.toggle('active');
	formulario.classList.toggle('active');
});

formulario.inputNumero.addEventListener('keydown', (e) => {
    
    var code = (e.which) ? e.which : e.keyCode;

    if((code < 48 || code> 57) && code != 8){
        
        e.preventDefault();
        return false;

     }
   

});

formulario.inputNumero.onpaste = function(e){
  
    e.preventDefault();
    return false;

}


formulario.inputNumero.oncopy = function(e){
  
    e.preventDefault();
    return false;

}

formulario.inputNombre.oncopy = function(e){
  
    e.preventDefault();
    return false;

}

formulario.inputNombre.onpaste = function(e){
  
    e.preventDefault();
    return false;

}

formulario.inputCCV.onpaste = function(e){
  
    e.preventDefault();
    return false;

}

formulario.inputCCV.oncopy = function(e){
  
    e.preventDefault();
    return false;

}


formulario.inputNombre.addEventListener('keydown', (e) => {
  
    var code = (e.which) ? e.which : e.keyCode;

    if(code >= 48 && code <=57){
    
        e.preventDefault();
        return false;
        
    }


});

formulario.inputCCV.addEventListener('keydown', (e) => {
   
    var code = (e.which) ? e.which : e.keyCode;

    if((code < 48 || code>57) && code != 8){
        
        e.preventDefault();
        return false;

     }

});


formulario.inputNumero.addEventListener('keyup', (e) => {


	let valorInput = e.target.value;

	formulario.inputNumero.value = valorInput
	
	.replace(/\s/g, '')
	.replace(/([0-9]{4})/g, '$1 ')
	.trim();

	numeroTarjeta.textContent = valorInput;

	if(valorInput == ''){
		numeroTarjeta.textContent = '#### #### #### ####';

		logoMarca.innerHTML = '';
	}

	if(valorInput[0] == 4){
		logoMarca.innerHTML = '';
		const imagen = document.createElement('img');
		imagen.src = '/img/visa.png';
		logoMarca.appendChild(imagen);
	} else if(valorInput[0] == 5){
		logoMarca.innerHTML = '';
		const imagen = document.createElement('img');
		imagen.src = '/img/mastercard.png';
		logoMarca.appendChild(imagen);
	}

	mostrarFrente();
});


formulario.inputNombre.addEventListener('keyup', (e) => {
	let valorInput = e.target.value;

	formulario.inputNombre.value = valorInput.replace(/[0-9]/g, '');
	nombreTarjeta.textContent = valorInput;
	firma.textContent = valorInput;

	if(valorInput == ''){
		nombreTarjeta.textContent = 'TU NOMBRE AQUI';
	}

	mostrarFrente();
});

formulario.selectMes.addEventListener('change', (e) => {
	mesExpiracion.textContent = e.target.value;
	mostrarFrente();
});

formulario.selectYear.addEventListener('change', (e) => {
	yearExpiracion.textContent = e.target.value;
	mostrarFrente();
});


formulario.inputCCV.addEventListener('keyup', () => {
	if(!tarjeta.classList.contains('active')){
		tarjeta.classList.toggle('active');
	}

	formulario.inputCCV.value = formulario.inputCCV.value
	
	.replace(/\s/g, '')
	

	ccv.textContent = formulario.inputCCV.value;
});