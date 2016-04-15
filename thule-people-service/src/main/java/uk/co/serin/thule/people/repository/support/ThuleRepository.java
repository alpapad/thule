package uk.co.serin.thule.people.repository.support;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface ThuleRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    void deleteByUpdatedBy(String updatedBy);
}

