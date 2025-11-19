package com.neurosfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuário por email
    Optional<Usuario> findByEmailUsuario(String emailUsuario);

    // Buscar usuários por tipo (MUDOU: agora recebe TipoUsuario enum)
    List<Usuario> findByTpUsuario(TipoUsuario tpUsuario);

    // Buscar usuários por status
    List<Usuario> findByStUsuario(Boolean stUsuario);

    // Buscar usuários ativos
    List<Usuario> findByStUsuarioTrue();

    // Buscar usuários por idioma
    List<Usuario> findByIdiomaUsuario(String idiomaUsuario);

    // Verificar se email já existe
    boolean existsByEmailUsuario(String emailUsuario);
}