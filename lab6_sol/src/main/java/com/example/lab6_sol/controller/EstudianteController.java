package com.example.lab6_sol.controller;

import com.example.lab6_sol.entity.Curso;
import com.example.lab6_sol.entity.Usuario;
import com.example.lab6_sol.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping("/lista")
    public String listaUsuarios(Model model){
        List<Usuario> estudiantes = usuarioRepository.findByRolid(5);
        model.addAttribute("estudiantes", estudiantes);
        return "lista_usuarios";
    }

    @GetMapping("/edit")
    public String editarCurso(Model model, @RequestParam("id") int id,
                              @ModelAttribute("estudiante") Usuario usuario) {

        Optional<Usuario> optUsuario = usuarioRepository.findById(id);

        if (optUsuario.isPresent()) {
            usuario = optUsuario.get();
            model.addAttribute("usuario", usuario);
            //model.addAttribute("listaProveedores", supplierRepository.findAll());
            return "form_est";
        } else {
            return "redirect:/estudiante/lista";
        }
    }
    @PostMapping("/save")
    public String guardarUsuario(@ModelAttribute("usuario") @Validated Usuario usuario, BindingResult bindingResult,
                                 RedirectAttributes attr, Model model ) {

        if (bindingResult.hasErrors()) {
            return "form_est";
        } else {

            if (usuario.getId() == 0) {
                attr.addFlashAttribute("msg", "Usuario creado exitosamente");

                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setActivo(true);
                usuarioRepository.save(usuario);
            } else {
                attr.addFlashAttribute("msg", "Usuario actualizado exitosamente");
                usuarioRepository.save(usuario);
            }

            return "redirect:/estudiante/lista";
        }
    }

    @GetMapping("/new")
    public String nuevoEstudiante(Model model, @ModelAttribute("usuario") Usuario usuario) {
        return "form_est";
    }
}
