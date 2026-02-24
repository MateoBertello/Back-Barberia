package controller;

import entity.Servicio;
import service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {
    @Autowired
    private ServicioService servicioService;

    @GetMapping
    public List<Servicio> listar() { return servicioService.listarTodos(); }

    @PostMapping
    public Servicio crear(@RequestBody Servicio servicio) { return servicioService.guardar(servicio); }
}