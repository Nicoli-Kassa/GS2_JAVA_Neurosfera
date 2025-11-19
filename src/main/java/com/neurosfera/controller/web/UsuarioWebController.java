package com.neurosfera.controller.web;

import com.neurosfera.repository.Distrito;
import com.neurosfera.repository.Usuario;
import com.neurosfera.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.neurosfera.service.DistritoService;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

@Controller
@RequestMapping("/web/usuarios")
public class UsuarioWebController {

    @Autowired
    private DistritoService distritoService;

    @Autowired
    private UsuarioService usuarioService;

    // Listar usuários com filtros
    @GetMapping
    public String listar(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String status,
            Model model
    ) {
        List<Usuario> usuarios = usuarioService.listarTodos();

        // Filtrar por busca (nome ou email)
        if (search != null && !search.isEmpty()) {
            usuarios = usuarios.stream()
                    .filter(u -> u.getNmUsuario().toLowerCase().contains(search.toLowerCase()) ||
                            u.getEmailUsuario().toLowerCase().contains(search.toLowerCase()))
                    .toList();
        }

        // Filtrar por tipo
        if (tipo != null && !tipo.isEmpty()) {
            usuarios = usuarios.stream()
                    .filter(u -> u.getTpUsuario().name().equals(tipo))
                    .toList();
        }

        // Filtrar por status
        if (status != null && !status.isEmpty()) {
            boolean ativo = Boolean.parseBoolean(status);
            usuarios = usuarios.stream()
                    .filter(u -> u.getStUsuario() == ativo)
                    .toList();
        }

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("search", search);
        model.addAttribute("tipo", tipo);
        model.addAttribute("status", status);
        return "usuarios/list";
    }

    // Exibir formulário de novo usuário
    @GetMapping("/new")
    public String novoForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/form";
    }

    // Exibir formulário de edição
    @GetMapping("/edit/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        try {
            Usuario usuario = usuarioService.buscarPorId(id);
            model.addAttribute("usuario", usuario);
            return "usuarios/form";
        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
            return "redirect:/web/usuarios";
        }
    }

    // Salvar usuário (criar ou atualizar)
    @PostMapping
    public String salvar(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            if (usuario.getIdUsuario() == null) {
                // Criar novo
                usuarioService.criar(usuario);
                redirectAttributes.addFlashAttribute("mensagem", "Usuário criado com sucesso!");
            } else {
                // Atualizar existente
                usuarioService.atualizar(usuario.getIdUsuario(), usuario);
                redirectAttributes.addFlashAttribute("mensagem", "Usuário atualizado com sucesso!");
            }
            return "redirect:/web/usuarios";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/web/usuarios/new";
        }
    }

    // Alternar status (ativar/desativar)
    @GetMapping("/toggle-status/{id}")
    public String toggleStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioService.buscarPorId(id);
            usuarioService.alterarStatus(id, !usuario.getStUsuario());
            redirectAttributes.addFlashAttribute("mensagem",
                    "Status alterado com sucesso!");
            return "redirect:/web/usuarios";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/web/usuarios";
        }
    }

    // Deletar usuário
    @GetMapping("/delete/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deletar(id);
            redirectAttributes.addFlashAttribute("mensagem", "Usuário excluído com sucesso!");
            return "redirect:/web/usuarios";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/web/usuarios";
        }
    }

    // Gerenciar distritos do usuário
    @GetMapping("/{id}/distritos")
    public String gerenciarDistritos(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioService.buscarPorId(id);
            Set<Distrito> distritosInscritos = usuarioService.listarDistritosDoUsuario(id); // Nome correto

            // Buscar todos os distritos ativos
            List<Distrito> todosDistritos = distritoService.buscarAtivos(); // Já existe no DistritoService

            // Filtrar apenas os não inscritos
            Set<Long> idsInscritos = distritosInscritos.stream()
                    .map(Distrito::getIdDistrito)
                    .collect(Collectors.toSet());

            List<Distrito> distritosDisponiveis = todosDistritos.stream()
                    .filter(d -> !idsInscritos.contains(d.getIdDistrito()))
                    .toList();

            model.addAttribute("usuario", usuario);
            model.addAttribute("distritosInscritos", distritosInscritos);
            model.addAttribute("distritosDisponiveis", distritosDisponiveis);

            return "usuarios/distritos";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/web/usuarios";
        }
    }



    // Inscrever usuário em distrito
    @PostMapping("/{usuarioId}/distritos/{distritoId}/inscrever")
    public String inscreverDistrito(
            @PathVariable Long usuarioId,
            @PathVariable Long distritoId,
            RedirectAttributes redirectAttributes) {
        try {
            usuarioService.inscreverNoDistrito(usuarioId, distritoId);
            redirectAttributes.addFlashAttribute("mensagem", "Inscrito no distrito com sucesso!");
            return "redirect:/web/usuarios/" + usuarioId + "/distritos";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/web/usuarios/" + usuarioId + "/distritos";
        }
    }

    // Desinscrever usuário de distrito
    @PostMapping("/{usuarioId}/distritos/{distritoId}/desinscrever")
    public String desinscreverDistrito(
            @PathVariable Long usuarioId,
            @PathVariable Long distritoId,
            RedirectAttributes redirectAttributes) {
        try {
            usuarioService.desinscreverDoDistrito(usuarioId, distritoId);
            redirectAttributes.addFlashAttribute("mensagem", "Desinscrito do distrito com sucesso!");
            return "redirect:/web/usuarios/" + usuarioId + "/distritos";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/web/usuarios/" + usuarioId + "/distritos";
        }
    }

}