package com.neurosfera.controller.web;

import com.neurosfera.repository.Distrito;
import com.neurosfera.service.DistritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/web/distritos")
public class DistritoWebController {

    @Autowired
    private DistritoService distritoService;

    // Listar distritos com filtros
    @GetMapping
    public String listar(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String status,
            Model model
    ) {
        List<Distrito> distritos = distritoService.listarTodos();

        // Filtrar por busca (nome ou descrição)
        if (search != null && !search.isEmpty()) {
            distritos = distritos.stream()
                    .filter(d -> d.getNmDistrito().toLowerCase().contains(search.toLowerCase()) ||
                            (d.getDsDistrito() != null && d.getDsDistrito().toLowerCase().contains(search.toLowerCase())))
                    .toList();
        }

        // Filtrar por área
        if (area != null && !area.isEmpty()) {
            distritos = distritos.stream()
                    .filter(d -> d.getAreaDistrito().equals(area))
                    .toList();
        }

        // Filtrar por status
        if (status != null && !status.isEmpty()) {
            boolean ativo = Boolean.parseBoolean(status);
            distritos = distritos.stream()
                    .filter(d -> d.getStDistrito() == ativo)
                    .toList();
        }

        model.addAttribute("distritos", distritos);
        model.addAttribute("search", search);
        model.addAttribute("area", area);
        model.addAttribute("status", status);
        return "distritos/list";
    }

    // Exibir formulário de novo distrito
    @GetMapping("/new")
    public String novoForm(Model model) {
        model.addAttribute("distrito", new Distrito());
        return "distritos/form";
    }

    // Exibir formulário de edição
    @GetMapping("/edit/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        try {
            Distrito distrito = distritoService.buscarPorId(id);
            model.addAttribute("distrito", distrito);
            return "distritos/form";
        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
            return "redirect:/web/distritos";
        }
    }

    // Salvar distrito (criar ou atualizar)
    @PostMapping
    public String salvar(@ModelAttribute Distrito distrito, RedirectAttributes redirectAttributes) {
        try {
            if (distrito.getIdDistrito() == null) {
                // Criar novo
                distritoService.criar(distrito);
                redirectAttributes.addFlashAttribute("mensagem", "Distrito criado com sucesso!");
            } else {
                // Atualizar existente
                distritoService.atualizar(distrito.getIdDistrito(), distrito);
                redirectAttributes.addFlashAttribute("mensagem", "Distrito atualizado com sucesso!");
            }
            return "redirect:/web/distritos";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/web/distritos/new";
        }
    }

    // Deletar distrito
    @GetMapping("/delete/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            distritoService.deletar(id);
            redirectAttributes.addFlashAttribute("mensagem", "Distrito excluído com sucesso!");
            return "redirect:/web/distritos";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/web/distritos";
        }
    }
}