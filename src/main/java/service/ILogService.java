package service;

import model.Log;

import java.util.List;

public interface ILogService {
    void info(String message);
    void alert(String message) ;
    void warning(String message) ;
    void danger(String message) ;
    void error(String message) ;
    Log save(Log log);
    List<Log> findAll();
    List<Log> findByUserId(int userId);
}
