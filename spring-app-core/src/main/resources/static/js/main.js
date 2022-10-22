const tarjeta = document.querySelector('#tarjeta');
const checboxActivar = document.querySelector('#flexSwitchCheckDefault');
const campoAbono = document.querySelector('#monto');

// * Volteamos la tarjeta para mostrar el frente.
const mostrarFrente = () => {
	if(tarjeta.classList.contains('active')){
		tarjeta.classList.remove('active');
	}
}

// * Rotacion de la tarjeta
tarjeta.addEventListener('click', () => {
	tarjeta.classList.toggle('active');
});

campoAbono.onpaste = function(e){

	e.preventDefault();
	return false;


}

campoAbono.oncopy = function(e){
  
	e.preventDefault();
	return false;

}

campoAbono.addEventListener('keydown', (e) => {
  
	var code = (e.which) ? e.which : e.keyCode;

	if(code == 190 && campoAbono.value == ""){
		console.log(campoAbono.value)
		e.preventDefault();
		return false;
	}

	if((code < 48 || code> 57) && code != 8 && code != 190){
		
		e.preventDefault();
		return false;

	 }


});

checboxActivar.addEventListener('change' , (e) => {
	
	if(e.target.checked){

		Swal.fire({
			title: 'Estas seguro de activar su tarjeta para pagos en linea?',
			text: "Recuerda que por motivos de seguridad se recomienda activar la tarjeta cuando vamos a realizar una compra bancaria!",
			icon: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#3085d6',
			cancelButtonColor: '#d33',
			confirmButtonText: 'Si activar tarjeta!',
			cancelButtonText: 'Cancelar'
		  }).then((result) => {
			if (result.isConfirmed) {
			  Swal.fire(
				'Activada!',
				'La tarjeta a sido activada.',
				'success'
			  )

			  window.location.href = "/cuenta/activar";
			}
	  })

	}else{

		
		Swal.fire({
			title: 'Estas seguro de apagar su tarjeta para pagos en linea?',
			text: "Recuerda que puna vez apagada no podras realizar ningua transferencia o compra con tu tarjeta!",
			icon: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#3085d6',
			cancelButtonColor: '#d33',
			confirmButtonText: 'Si apagar tarjeta!',
			cancelButtonText: 'Cancelar'
		  }).then((result) => {
			if (result.isConfirmed) {
			  Swal.fire(
				'Apagada!',
				'La tarjeta a sido apagada.',
				'success'
			  )

			  window.location.href = "/cuenta/desactivar";
			}
	  })


	}
	
})
