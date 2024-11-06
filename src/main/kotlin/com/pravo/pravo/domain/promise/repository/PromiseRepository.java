package com.pravo.pravo.domain.promise.repository;

import com.pravo.pravo.domain.promise.model.Promise;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromiseRepository extends JpaRepository<Promise, Long>, PromiseRepositoryCustom{
}
