package com.fullstack.WalletBankingApp.repository;

import com.fullstack.WalletBankingApp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends MongoRepository<User,String> {

}
