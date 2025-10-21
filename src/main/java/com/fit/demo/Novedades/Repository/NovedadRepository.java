package com.fit.demo.Novedades.Repository;
import com.fit.demo.Novedades.Entidad.Novedad;
import com.fit.demo.Novedades.Entidad.Novedad.TipoNovedad;



import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.List;

public interface NovedadRepository extends MongoRepository<Novedad, String> {
    // Buscar novedades por tipo
    List<Novedad> findByTipo(TipoNovedad tipo);

    // Buscar novedades por fecha
    List<Novedad> findByFecha(LocalDate fecha);

    // Buscar novedades por tipo y fecha
    List<Novedad> findByTipoAndFecha(TipoNovedad tipo, LocalDate fecha);
}