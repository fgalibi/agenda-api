package io.fgalibi.github.agendaapi.model.repository;

import io.fgalibi.github.agendaapi.model.entity.Contato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContatoRepository extends JpaRepository<Contato, Integer> {

}
