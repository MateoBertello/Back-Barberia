package service;

import entity.Sucursal;
import repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SucursalService {
    @Autowired
    private SucursalRepository sucursalRepository;

    public List<Sucursal> listarTodas() { return sucursalRepository.findAll(); }
    public Sucursal guardar(Sucursal sucursal) { return sucursalRepository.save(sucursal); }
}