package br.com.dogvision.user.repository;

import br.com.dogvision.user.dto.response.MonitorResponse;
import br.com.dogvision.user.model.Coordinator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CoordinatorRepository extends JpaRepository<Coordinator, UUID> {

//    @Query("""
//           select c
//           from Coordinator c
//           join fetch c.employee e
//           join fetch e.user u
//           """)
//    List<Coordinator> findAllWithEmployeeAndUser();
//
//    @Query("""
//           select c
//           from Coordinator c
//           join fetch c.employee e
//           join fetch e.user u
//           where c.id = :id
//           """)
//    Optional<Coordinator> findByIdWithEmployeeAndUser(UUID id);

//    @Query("""
//            select c
//            from Coordinator c
//            join fetch c.employee e
//            join fetch e.user u
//            where u.registration = :registration
//            """)
    @Query("select c from Coordinator c join fetch c.user u where u.registration = :registration")
    Optional<Coordinator> findByRegistration(@Param("registration") String registration);
}
