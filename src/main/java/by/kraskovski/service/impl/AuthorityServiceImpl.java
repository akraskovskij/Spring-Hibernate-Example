package by.kraskovski.service.impl;

import by.kraskovski.model.Authority;
import by.kraskovski.repository.AuthorityRepository;
import by.kraskovski.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityServiceImpl(final AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Authority find(final int id) {
        return authorityRepository.findOne(id);
    }

    @Override
    public List<Authority> findAll() {
        return authorityRepository.findAll();
    }
}
