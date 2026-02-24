package controller;

import entity.Usuario;
import service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios") // La URL base
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Para ver todos (GET a http://localhost:8080/api/usuarios)
    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listarTodos();
    }

    // Para crear uno nuevo (POST a http://localhost:8080/api/usuarios)
    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario) {
        return usuarioService.guardar(usuario);
    }
}