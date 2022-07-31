package db.dao;

import java.sql.SQLException;

public class Verification {
    static UserDAO userDao = new UserDAO();

    public Boolean verifyToken(String token) throws SQLException {
        int id = userDao.getId(token);
        return token.equals(userDao.getToken(id));
    }
}
