package fe.be_inkrealm_demo2.repository;

import fe.be_inkrealm_demo2.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, String> {
    // JpaRepository cung cấp sẵn các phương thức như findAll(), findById(), save(), deleteById()
}