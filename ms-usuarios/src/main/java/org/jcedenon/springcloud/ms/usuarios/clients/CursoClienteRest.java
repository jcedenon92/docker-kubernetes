package org.jcedenon.springcloud.ms.usuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-cursos", url = "ms-cursos:8002")
public interface CursoClienteRest {

    @DeleteMapping("/cursos/eliminar-curso-usuario/{id}")
    void eliminarCursoUsuarioPorId(@PathVariable ("id") Long id);
}
