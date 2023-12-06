package com.fullstack.WalletBankingApp.controller;

import com.fullstack.WalletBankingApp.exception.InsufficientBalanceException;
import com.fullstack.WalletBankingApp.exception.UserNotFoundException;
import com.fullstack.WalletBankingApp.model.Transaction;
import com.fullstack.WalletBankingApp.model.Wallet;
import com.fullstack.WalletBankingApp.repository.TransactionRepository;
import com.fullstack.WalletBankingApp.repository.WalletRepository;
import com.fullstack.WalletBankingApp.service.LogoutService;
import com.fullstack.WalletBankingApp.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wallet")
@CrossOrigin("*")
public class WalletController {
    @Autowired
    private WalletService walletService;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private LogoutService logoutService;
    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/recharge/{walletId}")
    public ResponseEntity<?> rechargeWallet(@PathVariable("walletId") String walletId, @RequestParam  double amount) {
        Wallet wallet = walletRepository.findById(walletId).orElse(null);
        if (wallet == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wallet not found");
        }else if(amount<1)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Amount");
        }
        walletService.rechargeWallet(wallet, amount);
        return ResponseEntity.ok(wallet.getBalance());
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferAmount(
            @RequestParam String senderWalletId,
            @RequestParam @Validated String receiverEmailId,
            @RequestParam @Validated double amount) throws InsufficientBalanceException {

        Wallet senderWallet = walletRepository.findById(senderWalletId).orElse(null);
        Wallet receiverWallet = walletRepository.findByEmail(receiverEmailId).orElse(null);
        String senderEmailId=senderWallet.getEmail();

        if (senderWallet == null || receiverWallet == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wallet not found");
        }if (senderEmailId.equals(receiverEmailId) ) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("senderWalletId and receiverWalletId must be different");
        }if( amount > senderWallet.getBalance())
        {return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("insufficient amount");
        }if(amount<=0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid amount");}
        walletService.transferAmount(senderWallet, receiverWallet, amount);
        return ResponseEntity.ok(senderWallet.getBalance());
    }

    @PostMapping("/logout")
    public String log_Out(HttpServletRequest request,
                          HttpServletResponse response,
                          Authentication authentication) {
        logoutService.logout(request, response, authentication);
        return "Logout SuccessFully";
    }

    @GetMapping("/info/{walletId}")
    public ResponseEntity<?> showWalletInfo(
            @PathVariable("walletId") String walletId,
            @RequestParam int page,
            @RequestParam int size
    ) throws UserNotFoundException {
        try {
          Wallet  wallet = walletRepository.findById(walletId)
                    .orElseThrow(() -> new UserNotFoundException("wallet not found"));

        if (wallet != null) {
            Sort sortByDateDesc = Sort.by(Sort.Direction.DESC, "timestamp");
            Pageable paging = PageRequest.of(page, size, sortByDateDesc);
            Page<Transaction> transactionsPage = transactionRepository.findByWalletId(walletId, paging);

            if (transactionsPage.hasContent()) {
                Map<String, Object> response = new HashMap<>();
                int totalPage=transactionsPage.getTotalPages();
                response.put("content", transactionsPage.getContent());
                response.put("totalPage",totalPage );
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No transactions found for the wallet");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wallet not found");
        }
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        }
    }
}
