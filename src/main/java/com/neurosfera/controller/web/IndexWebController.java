package com.neurosfera.controller.web;

import com.neurosfera.service.DistritoService;
import com.neurosfera.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class IndexWebController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DistritoService distritoService;

    @GetMapping
    public String index(Model model) {
        // Estatísticas para o dashboard
        long totalUsuarios = usuarioService.listarTodos().size();
        long totalDistritos = distritoService.listarTodos().size();
        long usuariosAtivos = usuarioService.buscarAtivos().size();
        long totalAprendizes = usuarioService.buscarPorTipo("Aprendiz").size();

        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("totalDistritos", totalDistritos);
        model.addAttribute("usuariosAtivos", usuariosAtivos);
        model.addAttribute("totalAprendizes", totalAprendizes);

        return "index";
    }

    // Redirecionar raiz para /web
    @GetMapping("/")
    public String root() {
        return "redirect:/web";
    }
}