package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping(value = "/api")
public class TransactionController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    public OTPService otpService;

    @Transactional
    @PostMapping("/sendOtp")
    public ResponseEntity<Object> sendOtp(Authentication authentication) throws MessagingException {
        Client client = this.clientRepository.findByEmail(authentication.getName());

        //Logo
        FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/web/img/LOGO_CON_TEXTO.png"));

        //OTP
        int otp = otpService.generateOTP(client.getEmail());
        emailService.sendOtpMessage(
                "noreply@antartidabank.com",
                client.getEmail(),
                "Confirm your transfer",
                "<html><header style=\"margin-top: 0; margin-bottom:16px; font-size:20px; line-height:32px; letter-spacing: -0.02em;\">\n" +
                        "<b>OTP Transfer</b>\n<br>"+
                        client.getFirstName()+ " "+client.getLastName() + "\n" +
                        "</header>\n" +
                        "<body><h3> Your OTP number is "+otp+"</h3>\n" +
                        "<h3>This password will expire in 3 minutes</h3></body>\n " +
                        "<footer><p style=\"margin: 0;\">\n" +
                        "Antartida Bank Team\n" +
                        "</p>\n" +
                        "<p>\n" +
                        "<small style=\"color:#B8B3B2; text-align: center\">\n" +
                        "Note: This e-mail is generated automatically, please do not reply to this message.</small></p></footer></html>",
                "LOGO_CON_TEXTO.png",
                file
        );

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> transfer(
            Authentication authentication,
            @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,
            @RequestParam double amount, @RequestParam String description,
            @RequestParam int otpnum) throws MessagingException {

        Client client = this.clientRepository.findByEmail(authentication.getName());
        Account originAccount = accountRepository.findByNumber(fromAccountNumber);
        Account destinationAccount = accountRepository.findByNumber(toAccountNumber);

        //Verificar que los parámetros no estén vacíos
        if (amount <= 0 ) {
            return new ResponseEntity<>("Ingresa un monto de transacción.", HttpStatus.FORBIDDEN);
        }

        if (description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Falta información.", HttpStatus.FORBIDDEN);
        }

        //Verificar que los números de cuenta no sean iguales
        if (fromAccountNumber.equals(toAccountNumber) ) {
            return new ResponseEntity<>("Elige una cuenta de destino diferente", HttpStatus.FORBIDDEN);
        }

        //Verificar que exista la cuenta de origen
        if (originAccount == null) {
            return new ResponseEntity<>("La cuenta de origen no existe.", HttpStatus.FORBIDDEN);
        }

        //Verificar que la cuenta de origen pertenezca al cliente autenticado
        if (!client.getAccounts().contains(originAccount)){
            return new ResponseEntity<>("El cliente no posee esta cuenta.", HttpStatus.FORBIDDEN);
        }

        //Verificar que exista la cuenta de destino
        if (destinationAccount == null) {
            return new ResponseEntity<>("La cuenta de destino no existe", HttpStatus.FORBIDDEN);
        }

        //Verificar que la cuenta de origen tenga el monto disponible.
        if (originAccount.getBalance() < amount) {
            return new ResponseEntity<>("No tiene suficientes fondos.", HttpStatus.FORBIDDEN);
        }

        //Verificar la OTP
        if (otpnum < 0) {
            return new ResponseEntity<>("Ingresa la OTP", HttpStatus.FORBIDDEN);
        }

        int serverOtp = otpService.getOtp(client.getEmail());
        if (serverOtp < 0) {
            return new ResponseEntity<>("No tienes una OTP activa", HttpStatus.FORBIDDEN);
        }

        if (otpnum != serverOtp) {
            return new ResponseEntity<>("OTP ingresada es inválida", HttpStatus.FORBIDDEN);
        }

        //Se elimina la OTP guardada
        otpService.clearOTP(client.getEmail());

        transactionRepository.save(new Transaction(
                TransactionType.DEBIT,
                -amount,
                description+" "+destinationAccount.getNumber(),
                LocalDateTime.now(),
                originAccount
                )
        );

        transactionRepository.save(new Transaction(
                TransactionType.CREDIT,
                +amount,
                description+" "+originAccount.getNumber(),
                LocalDateTime.now(),
                destinationAccount)
        );

        //Logo
        FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/web/img/LOGO_CON_TEXTO.png"));

        emailService.send(
                "noreply@antartidabank.com",
                client.getEmail(),
                new Date(),
                "Transfer Notification",
                "<html><header style=\"margin-top: 0; margin-bottom:16px; font-size:20px; line-height:32px; letter-spacing: -0.02em;\">\n" +
                        "<b>Electronic Funds Transfer Voucher</b>\n<br>"+
                        client.getFirstName()+ " "+client.getLastName() + "\n" +
                        "</header>\n" +
                        "<body><h3> A transfer for $"+amount+" has been made from your "+originAccount.getNumber()+" account to the "+ destinationAccount.getNumber()+" account</h3></body>\n " +
                        "<footer><p style=\"margin: 0;\">\n" +
                        "Antartida Bank Team\n" +
                        "</p>\n" +
                        "<p>\n" +
                        "<small style=\"color:#B8B3B2; text-align: center\">\n" +
                        "Note: This e-mail is generated automatically, please do not reply to this message.</small></p></footer></html>",
                "LOGO_CON_TEXTO.png",
                file

        );

        originAccount.setBalance(originAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
