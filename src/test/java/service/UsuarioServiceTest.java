package service;

import com.neurosfera.exception.*;
import com.neurosfera.repository.*;
import com.neurosfera.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private DistritoRepository distritoRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private Distrito distrito;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNmUsuario("João Silva");
        usuario.setEmailUsuario("joao@teste.com");
        usuario.setSenhaUsuario("senha123");
        usuario.setTpUsuario(TipoUsuario.APRENDIZ);
        usuario.setStUsuario(true);
        usuario.setDtCadastroUsuario(LocalDate.now());
        usuario.setDistritosInscritos(new HashSet<>());

        distrito = new Distrito();
        distrito.setIdDistrito(1L);
        distrito.setNmDistrito("Distrito Tecnologia");
        distrito.setAreaDistrito("Tecnologia");
        distrito.setStDistrito(true);
    }

    @Test
    void testListarTodos() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<Usuario> resultado = usuarioService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId_Sucesso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNmUsuario());
    }

    @Test
    void testBuscarPorId_UsuarioNaoEncontrado() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class,
                () -> usuarioService.buscarPorId(999L));
    }

    @Test
    void testCriar_Sucesso() {
        when(usuarioRepository.existsByEmailUsuario("joao@teste.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.criar(usuario);

        assertNotNull(resultado);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testCriar_EmailJaCadastrado() {
        when(usuarioRepository.existsByEmailUsuario("joao@teste.com")).thenReturn(true);

        assertThrows(EmailJaCadastradoException.class,
                () -> usuarioService.criar(usuario));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testCriar_TipoUsuarioInvalido() {
        usuario.setTpUsuario(null);
        when(usuarioRepository.existsByEmailUsuario("joao@teste.com")).thenReturn(false);

        assertThrows(TipoUsuarioInvalidoException.class,
                () -> usuarioService.criar(usuario));
    }

    @Test
    void testBuscarAtivos() {
        when(usuarioRepository.findByStUsuarioTrue()).thenReturn(List.of(usuario));

        List<Usuario> resultado = usuarioService.buscarAtivos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getStUsuario());
    }

    @Test
    void testAlterarStatus() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.alterarStatus(1L, false);

        assertNotNull(resultado);
        assertFalse(resultado.getStUsuario());
    }

    @Test
    void testDeletar_Sucesso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(usuario);

        usuarioService.deletar(1L);

        verify(usuarioRepository).delete(usuario);
    }

    @Test
    void testInscreverNoDistrito_Sucesso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(distritoRepository.findById(1L)).thenReturn(Optional.of(distrito));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.inscreverNoDistrito(1L, 1L);

        assertNotNull(resultado);
        assertTrue(resultado.getDistritosInscritos().contains(distrito));
    }

    @Test
    void testInscreverNoDistrito_JaInscrito() {
        usuario.getDistritosInscritos().add(distrito);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(distritoRepository.findById(1L)).thenReturn(Optional.of(distrito));

        assertThrows(UsuarioJaInscritoException.class,
                () -> usuarioService.inscreverNoDistrito(1L, 1L));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testInscreverNoDistrito_DistritoNaoEncontrado() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(distritoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(DistritoNaoEncontradoException.class,
                () -> usuarioService.inscreverNoDistrito(1L, 999L));
    }

    @Test
    void testDesinscreverDoDistrito_Sucesso() {
        usuario.getDistritosInscritos().add(distrito);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(distritoRepository.findById(1L)).thenReturn(Optional.of(distrito));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.desinscreverDoDistrito(1L, 1L);

        assertNotNull(resultado);
        assertFalse(resultado.getDistritosInscritos().contains(distrito));
    }

    @Test
    void testListarDistritosDoUsuario() {
        usuario.getDistritosInscritos().add(distrito);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Set<Distrito> resultado = usuarioService.listarDistritosDoUsuario(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(distrito));
    }
}
