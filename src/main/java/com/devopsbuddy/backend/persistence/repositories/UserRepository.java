package com.devopsbuddy.backend.persistence.repositories;

import com.devopsbuddy.backend.persistence.domain.backend.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends CrudRepository<User, Long> {

	/**
	 * Returns a User given a username or null if not found
	 * @param username
	 * @return a User given a username or null if not found
	 */
	public User findByUsername(String username);

	/**
	 * Returns a User for the given email address or null if none is found
	 * @param email the user's email
	 * @return a User for the given email address or null if none is found
	 */
	public User findByEmail(String email);

	@Modifying
	@Query("update User u set u.password = :password where u.id = :userId")
	void updateUserPassword(@Param("userId") long userId, @Param("password") String password);
}
