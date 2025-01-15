package mk.tradesense.tradesense.service.impl;

import mk.tradesense.tradesense.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public AccountDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return AccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("This account cannot be found-> " + username));
    }
}
