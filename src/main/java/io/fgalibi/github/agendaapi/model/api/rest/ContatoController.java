package io.fgalibi.github.agendaapi.model.api.rest;

import io.fgalibi.github.agendaapi.model.entity.Contato;
import io.fgalibi.github.agendaapi.model.repository.ContatoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contatos")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ContatoController {

    private final ContatoRepository repository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contato save (@RequestBody Contato contato) {
        return this.repository.save(contato);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        this.repository.deleteById(id);
    }

    @GetMapping
    public Page<Contato> lista(
            @RequestParam(value="page", defaultValue = "0") Integer pagina,
            @RequestParam(value = "size", defaultValue = "10") Integer tamanhoPagina
    ) {
        Sort sort = Sort.by(Sort.Direction.ASC, "nome");
        PageRequest pageRequest = PageRequest.of(pagina, tamanhoPagina, sort);
        return this.repository.findAll(pageRequest);
    }

    @PatchMapping("{id}/favorito")
    public void favorite(@PathVariable Integer id) {
        Optional<Contato> contato = this.repository.findById(id);
        contato.ifPresent( c -> {
            boolean favorito = c.getFavorito() == Boolean.TRUE;
            c.setFavorito(!favorito);
            this.repository.save((c));
        });
    }

    @PutMapping("{id}/foto")
    public byte[] addPhoto(@PathVariable Integer id,
                           @RequestParam("foto") Part arquivo) {
        Optional<Contato> contato = this.repository.findById(id);
        return contato.map( c -> {
            try {
                InputStream is = arquivo.getInputStream();
                byte[] bytes = new byte[(int)arquivo.getSize()];
                IOUtils.readFully(is, bytes);
                c.setFoto(bytes);
                this.repository.save(c);
                is.close();
                return bytes;
            } catch (IOException e) {
                return null;
            }
        }).orElse(null);
    }

}
