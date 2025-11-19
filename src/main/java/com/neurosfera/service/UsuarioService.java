package com.neurosfera.service;

import com.neurosfera.exception.*;
import com.neurosfera.repository.Distrito;
import com.neurosfera.repository.DistritoRepository;
import com.neurosfera.repository.TipoUsuario;
import com.neurosfera.repository.Usuario;
import com.neurosfera.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DistritoRepository distritoRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
    }

    public Usuario criar(Usuario usuario) {
        if (usuarioRepository.existsByEmailUsuario(usuario.getEmailUsuario())) {
            throw new EmailJaCadastradoException(usuario.getEmailUsuario());
        }

        if (usuario.getTpUsuario() == null) {
            throw new TipoUsuarioInvalidoException(
                    "Tipo de usuário é obrigatório. Tipos válidos: " +
                            TipoUsuario.listarTiposValidos()
            );
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
        Usuario usuario = buscarPorId(id);

        if (!usuario.getEmailUsuario().equals(usuarioAtualizado.getEmailUsuario())
                && usuarioRepository.existsByEmailUsuario(usuarioAtualizado.getEmailUsuario())) {
            throw new EmailJaCadastradoException(usuarioAtualizado.getEmailUsuario());
        }

        if (usuarioAtualizado.getTpUsuario() == null) {
            throw new TipoUsuarioInvalidoException(
                    "Tipo de usuário é obrigatório. Tipos válidos: " +
                            TipoUsuario.listarTiposValidos()
            );
        }

        usuario.setNmUsuario(usuarioAtualizado.getNmUsuario());
        usuario.setEmailUsuario(usuarioAtualizado.getEmailUsuario());
        usuario.setTpUsuario(usuarioAtualizado.getTpUsuario());
        usuario.setIdiomaUsuario(usuarioAtualizado.getIdiomaUsuario());
        usuario.setStUsuario(usuarioAtualizado.getStUsuario());

        if (usuarioAtualizado.getSenhaUsuario() != null
                && !usuarioAtualizado.getSenhaUsuario().isEmpty()) {
            usuario.setSenhaUsuario(usuarioAtualizado.getSenhaUsuario());
        }

        return usuarioRepository.save(usuario);
    }

    public void deletar(Long id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmailUsuario(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(email));
    }

    public List<Usuario> buscarPorTipo(String tipoStr) {
        TipoUsuario tipo = TipoUsuario.fromString(tipoStr);

        if (tipo == null) {
            throw new TipoUsuarioInvalidoException(
                    "Tipo de usuário inválido: " + tipoStr +
                            ". Tipos válidos: " + TipoUsuario.listarTiposValidos()
            );
        }

        return usuarioRepository.findByTpUsuario(tipo);
    }

    public List<Usuario> buscarPorTipo(TipoUsuario tipo) {
        if (tipo == null) {
            throw new TipoUsuarioInvalidoException(
                    "Tipo de usuário inválido. Tipos válidos: " + TipoUsuario.listarTiposValidos()
            );
        }
        return usuarioRepository.findByTpUsuario(tipo);
    }

    public List<Usuario> buscarAtivos() {
        return usuarioRepository.findByStUsuarioTrue();
    }

    public Usuario alterarStatus(Long id, Boolean novoStatus) {
        Usuario usuario = buscarPorId(id);
        usuario.setStUsuario(novoStatus);
        return usuarioRepository.save(usuario);
    }

    public Usuario alterarIdioma(Long id, String novoIdioma) {
        Usuario usuario = buscarPorId(id);
        usuario.setIdiomaUsuario(novoIdioma);
        return usuarioRepository.save(usuario);
    }

    public Usuario inscreverNoDistrito(Long usuarioId, Long distritoId) {
        Usuario usuario = buscarPorId(usuarioId);
        Distrito distrito = distritoRepository.findById(distritoId)
                .orElseThrow(() -> new DistritoNaoEncontradoException(distritoId));

        if (usuario.getDistritosInscritos().contains(distrito)) {
            throw new UsuarioJaInscritoException();
        }

        usuario.getDistritosInscritos().add(distrito);
        return usuarioRepository.save(usuario);
    }

    public Usuario desinscreverDoDistrito(Long usuarioId, Long distritoId) {
        Usuario usuario = buscarPorId(usuarioId);
        Distrito distrito = distritoRepository.findById(distritoId)
                .orElseThrow(() -> new DistritoNaoEncontradoException(distritoId));

        usuario.getDistritosInscritos().remove(distrito);
        return usuarioRepository.save(usuario);
    }

    public Set<Distrito> listarDistritosDoUsuario(Long usuarioId) {
        Usuario usuario = buscarPorId(usuarioId);
        return usuario.getDistritosInscritos();
    }
}