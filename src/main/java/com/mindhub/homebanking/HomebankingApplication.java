package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(
			ClientRepository repository,
			AccountRepository accountRepository,
			TransactionRepository transactionRepository,
			LoanRepository loanRepository,
			ClientLoanRepository clientLoanRepository,
			CardRepository cardRepository) {
		return args -> {
			Client client1 = repository.save(new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("hola")));

			Account account1 = accountRepository.save(new Account("VIN001", LocalDateTime.now(), 5000, client1));
			transactionRepository.save(new Transaction(TransactionType.DEBIT, -10000, "deposit", LocalDateTime.now(), account1));
			transactionRepository.save(new Transaction(TransactionType.CREDIT, 15000, "credit", LocalDateTime.now(), account1));

			Account account2 = accountRepository.save(new Account("VIN002", LocalDateTime.now().plusDays(1), 7500, client1));
			transactionRepository.save(new Transaction(TransactionType.CREDIT, 20000, "credit card", LocalDateTime.now(), account2));
			transactionRepository.save(new Transaction(TransactionType.DEBIT, -10000, "deposit", LocalDateTime.now(), account2));

			List<Integer> hipoteca = List.of(12, 24, 36, 48, 60);
			Loan hipotecario = loanRepository.save(new Loan(hipoteca, "Hipotecario", 500000));
			List<Integer> person = List.of(6, 12, 24);
			Loan personal = loanRepository.save(new Loan(person, "Personal", 100000));
			List<Integer> auto = List.of(6, 12, 24, 36);
			Loan automotriz = loanRepository.save(new Loan(auto, "Automotriz", 300000));

			clientLoanRepository.save(new ClientLoan(400000, 60, client1,hipotecario));

			clientLoanRepository.save(new ClientLoan(50000, 12, client1,personal));

			cardRepository.save(new Card("Melba Morel", CardType.DEBIT, CardColor.GOLD, "4345 5689 7584 2423", "256", LocalDateTime.now(), LocalDateTime.now().plusYears(5), client1 ));
		   	cardRepository.save(new Card("Melba Morel", CardType.CREDIT, CardColor.TITANIUM, "4345 4859 6325 8564", "369", LocalDateTime.now(), LocalDateTime.now().plusYears(5), client1 ));


			Client client2 = repository.save(new Client("Carolina", "Zapata", "carolinazapatavidal@gmail.com", passwordEncoder.encode("1234")));

			Account account3 = accountRepository.save(new Account("VIN367", LocalDateTime.now(), 10000, client2));
			Account account4 = accountRepository.save(new Account("VIN368", LocalDateTime.now(), 100000, client2));
			transactionRepository.save(new Transaction(TransactionType.DEBIT, -5000, "deposit2", LocalDateTime.now(), account3));
			transactionRepository.save(new Transaction(TransactionType.CREDIT, 10000, "credit 3", LocalDateTime.now(), account3));

			clientLoanRepository.save(new ClientLoan(100000, 24,client2, personal));

			clientLoanRepository.save(new ClientLoan(200000, 36, client2, automotriz));

			cardRepository.save(new Card("Carolina Zapata", CardType.CREDIT, CardColor.TITANIUM, "4345 7845 9586 2514", "485", LocalDateTime.now(), LocalDateTime.now().plusYears(5), client2 ));

		};
	}
}