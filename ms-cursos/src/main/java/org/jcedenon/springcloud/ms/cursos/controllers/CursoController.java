package org.jcedenon.springcloud.ms.cursos.controllers;

import feign.FeignException;
import jakarta.validation.Valid;
import org.jcedenon.springcloud.ms.cursos.models.Usuario;
import org.jcedenon.springcloud.ms.cursos.models.entity.Curso;
import org.jcedenon.springcloud.ms.cursos.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping ("/cursos")
public class CursoController {

    @Autowired
    private CursoService service;

    @GetMapping
    public ResponseEntity<List<Curso>> listar(){
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> detalle(@PathVariable ("id") Long id){
        Optional<Curso> o = service.porIdConUsuarios(id); //service.porId(id);
        if (o.isPresent()){
            return ResponseEntity.ok(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult result){
        if (result.hasErrors()){
            return validar(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(curso));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable ("id") Long id){
        if (result.hasErrors()){
            return validar(result);
        }
        Optional<Curso> o = service.porId(id);
        if (o.isPresent()){
            Curso cursoDB = o.get();
            cursoDB.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(cursoDB));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable ("id") Long id){
        Optional<Curso> o = service.porId(id);
        if (o.isPresent()){
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Metodo que se comunica con el microservicio Usuarios para asignar un usuario a un curso
     * @param usuario
     * @param cursoId
     * @return
     */
    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable ("cursoId") Long cursoId){
        Optional<Usuario> opt;
        try {
            opt = service.asignarUsuario(usuario, cursoId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje", "No existe el usuario por el id o error en la comunicación: " + e.getMessage()));
        }
        if (opt.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(opt.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Metodo que se comunica con el microservicio Usuarios para crear un usuario y asignarlo a un curso
     * @param usuario
     * @param cursoId
     * @return
     */
    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable ("cursoId") Long cursoId){
        Optional<Usuario> opt;
        try {
            opt = service.crearUsuario(usuario, cursoId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje", "No se pudo crear el usuario o error en la comunicación: " + e.getMessage()));
        }
        if (opt.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(opt.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Metodo que se comunica con el microservicio Usuarios para eliminar un usuario de un curso
     * @param usuario
     * @param cursoId
     * @return
     */
    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable ("cursoId") Long cursoId){
        Optional<Usuario> opt;
        try {
            opt = service.eliminarUsuario(usuario, cursoId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje", "No existe el usuario por el id o error en la comunicación: " + e.getMessage()));
        }
        if (opt.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(opt.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> eliminarCursoUsuarioPorId(@PathVariable ("id") Long id){
        service.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
