package com.app.bancario.springappcore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.bancario.springappcore.model.CuentaAhorro;

@Repository
public interface CuentaAhorroRepository extends JpaRepository<CuentaAhorro, Long> {

    
    @Query("select c from CuentaAhorro c join fetch c.tarjeta t join fetch c.solicitud s join fetch s.usuario u where c.solicitud.usuario.id = ?1")
    Optional<CuentaAhorro> findByIdUsuario(Long idUsuario);
   
    @Query("select c from CuentaAhorro c join fetch c.tarjeta t join fetch c.solicitud s join fetch s.usuario u where c.numero_cuenta = ?1 and c.solicitud.usuario.id != ?2")
    Optional<CuentaAhorro> findByTransferenciaFuxia(String numero , Long idUsuario);
   
    @Query("select c from CuentaAhorro c join fetch c.tarjeta t join fetch c.solicitud s join fetch s.usuario u where c.cuenta_interbancaria = ?1 and c.solicitud.usuario.id != ?2")
    Optional<CuentaAhorro> findByTransferenciaInterBancaria(String numero , Long idUsuario);

    @Query("select c from CuentaAhorro c join fetch c.tarjeta t join fetch c.solicitud s join fetch s.usuario u where c.cuenta_internacional = ?1 and c.solicitud.usuario.id != ?2")
    Optional<CuentaAhorro> findByTransferenciaInternacional(String numero , Long idUsuario);
}
