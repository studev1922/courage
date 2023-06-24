package courage.model.repositories.UserRepositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import courage.model.entities.User;

public interface URoleRepository  extends JpaRepository<User.Role, Integer>  {
   // @formatter:off

   default <S extends User.Role> S save(S entity){throw exception();}
   default <S extends User.Role> List<S> saveAll(Iterable<S> entities){throw exception();}
   default <S extends User.Role> S saveAndFlush(S entity){throw exception();}
   default <S extends User.Role> List<S> saveAllAndFlush(Iterable<S> entities) {throw exception();}
   default void delete(User.Role entity){throw exception();}
   default void deleteAll(){throw exception();}
   default void deleteAll(Iterable<? extends User.Role> entities){throw exception();}
   default void deleteAllById(Iterable<? extends Integer> ids){throw exception();}
   default void deleteAllByIdInBatch(Iterable<Integer> ids){throw exception();}
   default void deleteAllInBatch(){throw exception();}
   default void deleteAllInBatch(Iterable<User.Role> entities){throw exception();}
   default void deleteById(Integer id){throw exception();}
   default void deleteInBatch(Iterable<User.Role> entities){throw exception();}
   default IllegalAccessError exception(){return new IllegalAccessError("This method not supported!");}
}