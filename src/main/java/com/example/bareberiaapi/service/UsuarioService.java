package com.example.bareberiaapi.service;

import com.example.bareberiaapi.entity.Usuario;
import com.example.bareberiaapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> listarBarberosActivos() {
        return usuarioRepository.findByRolAndActivoTrue("BARBERO");
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}