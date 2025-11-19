package service;

import com.neurosfera.exception.DistritoNaoEncontradoException;
import com.neurosfera.exception.NomeJaCadastradoException;
import com.neurosfera.repository.Distrito;
import com.neurosfera.repository.DistritoRepository;
import com.neurosfera.service.DistritoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DistritoService - Testes")
class DistritoServiceTest {

    @Mock
    private DistritoRepository distritoRepository;

    @InjectMocks
    private DistritoService distritoService;

    private Distrito distritoBase;

    @BeforeEach
    void setUp() {
        distritoBase = new Distrito();
        distritoBase.setIdDistrito(1L);
        distritoBase.setNmDistrito("Distrito Tecnologia");
        distritoBase.setAreaDistrito("Tecnologia");
        distritoBase.setDsDistrito("Distrito focado em tecnologia e inovação");
        distritoBase.setStDistrito(true);
    }

    @Nested
    @DisplayName("Testes de Listagem")
    class ListagemTests {

        @Test
        @DisplayName("Deve listar todos os distritos")
        void deveListarTodosDistritos() {
            // Arrange
            Distrito distrito2 = new Distrito();
            distrito2.setIdDistrito(2L);
            distrito2.setNmDistrito("Distrito Saúde");
            List<Distrito> distritos = Arrays.asList(distritoBase, distrito2);
            when(distritoRepository.findAll()).thenReturn(distritos);

            // Act
            List<Distrito> resultado = distritoService.listarTodos();

            // Assert
            assertThat(resultado)
                    .hasSize(2)
                    .contains(distritoBase, distrito2);
            verify(distritoRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há distritos")
        void deveRetornarListaVaziaQuandoNaoHaDistritos() {
            // Arrange
            when(distritoRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<Distrito> resultado = distritoService.listarTodos();

            // Assert
            assertThat(resultado).isEmpty();
        }

        @Test
        @DisplayName("Deve listar apenas distritos ativos")
        void deveListarApenasDistritosAtivos() {
            // Arrange
            List<Distrito> distritosAtivos = List.of(distritoBase);
            when(distritoRepository.findByStDistritoTrue()).thenReturn(distritosAtivos);

            // Act
            List<Distrito> resultado = distritoService.buscarAtivos();

            // Assert
            assertThat(resultado)
                    .hasSize(1)
                    .allMatch(Distrito::getStDistrito);
            verify(distritoRepository).findByStDistritoTrue();
        }

        @ParameterizedTest
        @ValueSource(strings = {"Tecnologia", "Medicina", "Engenharia", "Design"})
        @DisplayName("Deve buscar distritos por área")
        void deveBuscarDistritosPorArea(String area) {
            // Arrange
            distritoBase.setAreaDistrito(area);
            when(distritoRepository.findByAreaDistrito(area)).thenReturn(List.of(distritoBase));

            // Act
            List<Distrito> resultado = distritoService.buscarPorArea(area);

            // Assert
            assertThat(resultado)
                    .hasSize(1)
                    .allMatch(d -> d.getAreaDistrito().equals(area));
        }
    }

    @Nested
    @DisplayName("Testes de Busca por ID")
    class BuscaPorIdTests {

        @Test
        @DisplayName("Deve buscar distrito por ID com sucesso")
        void deveBuscarDistritoPorId() {
            // Arrange
            when(distritoRepository.findById(1L)).thenReturn(Optional.of(distritoBase));

            // Act
            Distrito resultado = distritoService.buscarPorId(1L);

            // Assert
            assertThat(resultado)
                    .isNotNull()
                    .extracting(Distrito::getIdDistrito, Distrito::getNmDistrito)
                    .containsExactly(1L, "Distrito Tecnologia");
            verify(distritoRepository).findById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção quando distrito não existe")
        void deveLancarExcecaoQuandoDistritoNaoExiste() {
            // Arrange
            when(distritoRepository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> distritoService.buscarPorId(999L))
                    .isInstanceOf(DistritoNaoEncontradoException.class)
                    .hasMessageContaining("999");
        }
    }

    @Nested
    @DisplayName("Testes de Criação")
    class CriacaoTests {

        @Test
        @DisplayName("Deve criar distrito com sucesso")
        void deveCriarDistritoComSucesso() {
            // Arrange
            when(distritoRepository.existsByNmDistrito(distritoBase.getNmDistrito())).thenReturn(false);
            when(distritoRepository.save(any(Distrito.class))).thenReturn(distritoBase);

            // Act
            Distrito resultado = distritoService.criar(distritoBase);

            // Assert
            assertThat(resultado)
                    .isNotNull()
                    .extracting(Distrito::getNmDistrito, Distrito::getAreaDistrito)
                    .containsExactly("Distrito Tecnologia", "Tecnologia");

            ArgumentCaptor<Distrito> captor = ArgumentCaptor.forClass(Distrito.class);
            verify(distritoRepository).save(captor.capture());
            assertThat(captor.getValue().getStDistrito()).isTrue();
        }

        @Test
        @DisplayName("Deve lançar exceção quando nome já está cadastrado")
        void deveLancarExcecaoQuandoNomeJaCadastrado() {
            // Arrange
            when(distritoRepository.existsByNmDistrito("Distrito Tecnologia")).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> distritoService.criar(distritoBase))
                    .isInstanceOf(NomeJaCadastradoException.class)
                    .hasMessageContaining("Distrito Tecnologia");
            verify(distritoRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes de Atualização")
    class AtualizacaoTests {

        @Test
        @DisplayName("Deve atualizar distrito com sucesso")
        void deveAtualizarDistritoComSucesso() {
            // Arrange
            Distrito distritoAtualizado = new Distrito();
            distritoAtualizado.setNmDistrito("Distrito Tech Atualizado");
            distritoAtualizado.setAreaDistrito("IA");
            distritoAtualizado.setDsDistrito("Nova descrição");
            distritoAtualizado.setStDistrito(true);

            when(distritoRepository.findById(1L)).thenReturn(Optional.of(distritoBase));
            when(distritoRepository.existsByNmDistrito("Distrito Tech Atualizado")).thenReturn(false);
            when(distritoRepository.save(any(Distrito.class))).thenReturn(distritoBase);

            // Act
            Distrito resultado = distritoService.atualizar(1L, distritoAtualizado);

            // Assert
            assertThat(resultado.getNmDistrito()).isEqualTo("Distrito Tech Atualizado");
            assertThat(resultado.getAreaDistrito()).isEqualTo("IA");
            verify(distritoRepository).save(distritoBase);
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar para nome já existente")
        void deveLancarExcecaoAoAtualizarParaNomeExistente() {
            // Arrange
            Distrito distritoAtualizado = new Distrito();
            distritoAtualizado.setNmDistrito("Distrito Existente");

            when(distritoRepository.findById(1L)).thenReturn(Optional.of(distritoBase));
            when(distritoRepository.existsByNmDistrito("Distrito Existente")).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> distritoService.atualizar(1L, distritoAtualizado))
                    .isInstanceOf(NomeJaCadastradoException.class);
            verify(distritoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve alterar status do distrito")
        void deveAlterarStatusDoDistrito() {
            // Arrange
            when(distritoRepository.findById(1L)).thenReturn(Optional.of(distritoBase));
            when(distritoRepository.save(any(Distrito.class))).thenReturn(distritoBase);

            // Act
            Distrito resultado = distritoService.alterarStatus(1L, false);

            // Assert
            assertThat(resultado.getStDistrito()).isFalse();
            verify(distritoRepository).save(distritoBase);
        }

        @Test
        @DisplayName("Deve lançar exceção ao alterar status de distrito inexistente")
        void deveLancarExcecaoAoAlterarStatusDeDistritoInexistente() {
            // Arrange
            when(distritoRepository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> distritoService.alterarStatus(999L, false))
                    .isInstanceOf(DistritoNaoEncontradoException.class);
            verify(distritoRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes de Deleção")
    class DelecaoTests {

        @Test
        @DisplayName("Deve deletar distrito com sucesso")
        void deveDeletarDistritoComSucesso() {
            // Arrange
            when(distritoRepository.findById(1L)).thenReturn(Optional.of(distritoBase));
            doNothing().when(distritoRepository).delete(distritoBase);

            // Act
            distritoService.deletar(1L);

            // Assert
            verify(distritoRepository).delete(distritoBase);
        }

        @Test
        @DisplayName("Deve lançar exceção ao deletar distrito inexistente")
        void deveLancarExcecaoAoDeletarDistritoInexistente() {
            // Arrange
            when(distritoRepository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> distritoService.deletar(999L))
                    .isInstanceOf(DistritoNaoEncontradoException.class);
            verify(distritoRepository, never()).delete(any());
        }
    }
}
