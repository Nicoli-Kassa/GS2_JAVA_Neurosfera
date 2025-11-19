package com.neurosfera.service;

import com.neurosfera.exception.DistritoNaoEncontradoException;
import com.neurosfera.exception.NomeJaCadastradoException;
import com.neurosfera.repository.Distrito;
import com.neurosfera.repository.DistritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DistritoService {

    @Autowired
    private DistritoRepository distritoRepository;

    public List<Distrito> listarTodos() {
        return distritoRepository.findAll();
    }

    public Distrito buscarPorId(Long id) {
        return distritoRepository.findById(id)
                .orElseThrow(() -> new DistritoNaoEncontradoException(id));
    }

    public Distrito criar(Distrito distrito) {
        if (distritoRepository.existsByNmDistrito(distrito.getNmDistrito())) {
            throw new NomeJaCadastradoException(distrito.getNmDistrito());
        }

        return distritoRepository.save(distrito);
    }

    public Distrito atualizar(Long id, Distrito distritoAtualizado) {
        Distrito distrito = buscarPorId(id);

        if (!distrito.getNmDistrito().equals(distritoAtualizado.getNmDistrito())
                && distritoRepository.existsByNmDistrito(distritoAtualizado.getNmDistrito())) {
            throw new NomeJaCadastradoException(distritoAtualizado.getNmDistrito());
        }

        distrito.setNmDistrito(distritoAtualizado.getNmDistrito());
        distrito.setAreaDistrito(distritoAtualizado.getAreaDistrito());
        distrito.setDsDistrito(distritoAtualizado.getDsDistrito());
        distrito.setStDistrito(distritoAtualizado.getStDistrito());

        return distritoRepository.save(distrito);
    }

    public void deletar(Long id) {
        Distrito distrito = buscarPorId(id);
        distritoRepository.delete(distrito);
    }

    public List<Distrito> buscarPorArea(String area) {
        return distritoRepository.findByAreaDistrito(area);
    }

    public List<Distrito> buscarAtivos() {
        return distritoRepository.findByStDistritoTrue();
    }

    public Distrito alterarStatus(Long id, Boolean novoStatus) {
        Distrito distrito = buscarPorId(id);
        distrito.setStDistrito(novoStatus);
        return distritoRepository.save(distrito);
    }
}