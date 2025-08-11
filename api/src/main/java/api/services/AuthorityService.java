package api.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.entities.Authority;
import api.exceptions.EntityNotFoundException;
import api.repositories.AuthorityRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * AuthorityService.
 */
@Slf4j
@Service
public class AuthorityService {
    @Autowired
    private AuthorityRepository authorityRepository;

    /**
     * Get authority.
     *
     * @param authority Authority name
     * @return {@link Authority}
     */
    @Transactional(readOnly = true)
    public Authority get(String authority) {
        return authorityRepository.findByAuthority(authority)
            .orElseThrow(() -> new EntityNotFoundException("Authority '" + authority + "' not found."));
    }

    /**
     * Find authority.
     *
     * @param authority Authority name
     * @return {@link Optional} {@link Authority}
     */
    @Transactional(readOnly = true)
    public Optional<Authority> find(String authority) {
        return authorityRepository.findByAuthority(authority);
    }

    /**
     * Save authority.
     *
     * @param authority Authority
     * @return Authority
     */
    @Transactional
    public Authority save(Authority authority) {
        Authority saved = authorityRepository.save(authority);
        log.info("Authority saved: " + authority);
        return saved;
    }
}
