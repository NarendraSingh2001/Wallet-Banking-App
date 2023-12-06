package com.fullstack.WalletBankingApp.repository;

import com.fullstack.WalletBankingApp.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction,String> {
    Page<Transaction> findByWalletId(String walletId, Pageable paging);

}
