package br.edu.ufape.plataformaeventos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufape.plataformaeventos.model.StudentProfile;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    StudentProfile findByUserEmail(String email);

    StudentProfile findByCpf(String cpf);

}
