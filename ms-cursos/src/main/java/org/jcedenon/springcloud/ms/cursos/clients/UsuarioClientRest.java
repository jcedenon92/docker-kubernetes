package org.jcedenon.springcloud.ms.cursos.clients;

import org.jcedenon.springcloud.ms.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ms-usuarios", url = "ms-usuarios:8001")
public interface UsuarioClientRest {

    @GetMapping("/usuarios/{id}")
    Usuario detalle(@PathVariable ("id") Long id);

    @PostMapping("/usuarios")
    Usuario crear(@RequestBody Usuario usuario);

    @GetMapping("/usuarios/usuarios-por-curso")
    List<Usuario> obtenerAlumnosPorCurso(@RequestParam Iterable<Long> ids);
}
