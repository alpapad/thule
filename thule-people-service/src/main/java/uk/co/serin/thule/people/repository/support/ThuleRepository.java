package uk.co.serin.thule.people.repository.support;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface ThuleRepository<T, I extends Serializable> extends JpaRepository<T, I> {
    void deleteByUpdatedBy(String updatedBy);
}

