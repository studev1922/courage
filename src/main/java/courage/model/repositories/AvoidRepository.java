package courage.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AvoidRepository<E, K> extends JpaRepository<E, K> {
   // @formatter:off
   @Deprecated default <S extends E> S save(S entity){throw exception();}
   @Deprecated default <S extends E> List<S> saveAll(Iterable<S> entities){throw exception();}
   @Deprecated default <S extends E> S saveAndFlush(S entity){throw exception();}
   @Deprecated default <S extends E> List<S> saveAllAndFlush(Iterable<S> entities) {throw exception();}
   @Deprecated default void delete(E entity){throw exception();}
   @Deprecated default void deleteAll(){throw exception();}
   @Deprecated default void deleteAll(Iterable<? extends E> entities){throw exception();}
   @Deprecated default void deleteAllById(Iterable<? extends K> ids){throw exception();}
   @Deprecated default void deleteAllByIdInBatch(Iterable<K> ids){throw exception();}
   @Deprecated default void deleteAllInBatch(){throw exception();}
   @Deprecated default void deleteAllInBatch(Iterable<E> entities){throw exception();}
   @Deprecated default void deleteById(K id){throw exception();}
   @Deprecated default void deleteInBatch(Iterable<E> entities){throw exception();}

   default IllegalAccessError exception(){return new IllegalAccessError("This method is not supported!");}
}