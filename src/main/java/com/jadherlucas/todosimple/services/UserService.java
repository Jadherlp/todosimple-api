package com.jadherlucas.todosimple.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jadherlucas.todosimple.models.User;
import com.jadherlucas.todosimple.repositories.TaskRepository;
import com.jadherlucas.todosimple.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    public User buscarPeloId(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new RuntimeException(
                "Usuário não encontrado! Id:" + id + ",Tipo: " + User.class.getName()));
    }

    @Transactional
    public User criar(User obj){
        obj.setId(null);
        obj = this.userRepository.save(obj);
        this.taskRepository.saveAll(obj.getTasks());
        return obj;
    }

    @Transactional
    public User editar(User obj){
        User newObj = buscarPeloId(obj.getId());
        newObj.setPassword(obj.getPassword());
        return this.userRepository.save(newObj);
    }

    @Transactional
    public void deletar(Long id){
        buscarPeloId(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Não é possivel excluir pois há entidades relacionadas.");
        }
    }

}
