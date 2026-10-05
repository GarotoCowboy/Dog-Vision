package br.com.dogvision.user.service;

import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.User;

public interface UserService {

    User createAccount(String registration, String email, String name, Role role);
}
