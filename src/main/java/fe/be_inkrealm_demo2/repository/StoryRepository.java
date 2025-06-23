package fe.be_inkrealm_demo2.repository;

import fe.be_inkrealm_demo2.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
}
