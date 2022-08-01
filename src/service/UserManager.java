package service;

import db.dao.UserDAO;
import db.dao.Verification;
import domain.User;
import org.apache.commons.codec.digest.DigestUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlRootElement;

@WebService(name = "userManager")
@XmlRootElement(name = "userManager")
public class UserManager {
    static UserDAO userDao = new UserDAO();
    static Verification verification = new Verification();

    @WebMethod(operationName = "authenticate")
    public Boolean authenticateUser(@WebParam(name ="user") User user) throws SQLException
    {
        String hashPwd = DigestUtils.sha256Hex(user.getPassword());
        String userPwd = userDao.getUserPassword(user.getLogin());
        user.setPassword(hashPwd);

        return userPwd.equals(user.getPassword());
    }

    @WebMethod(operationName = "addUser")
    public Boolean addUser(
                           @WebParam(name = "tokenAdmin") String token,
                           @WebParam(name = "userToAdd") User user,
                           @WebParam(name = "typeUser") String type
                        ) throws SQLException
    {
        if (Verification.verifyToken(token)) {
            String hashPwd = DigestUtils.sha256Hex(user.getPassword());
            user.setPassword(hashPwd);
            userDao.addUser(user, type);
            return true;
        }
        return false;
    }

    @WebMethod(operationName = "listUsers")
    public ArrayList<User> listUsers(
                                       @WebParam(name = "tokenAdmin") String token
                                    ) throws SQLException
    {
        if (!Verification.verifyToken(token)) {
            System.out.println(" You haven't permission. Please generate token.");
            return null;
        }
        return userDao.getUsers();
    }

    @WebMethod(operationName = "updateUser")
    public Boolean updateUser(
                               @WebParam(name = "tokenAdmin") String token,
                               @WebParam(name = "userParameters") User user,
                               @WebParam(name = "typeUser") String type,
                               @WebParam(name = "idUser") int idUser
                            ) throws SQLException
    {
        if (Verification.verifyToken(token)) {
            String hashPwd = DigestUtils.sha256Hex(user.getPassword());
            user.setPassword(hashPwd);
            userDao.updateUser(user, type, idUser);
            return true;
        }
        return false;
    }

    @WebMethod(operationName = "deleteUser")
    public Boolean deleteUser(
                              @WebParam(name = "tokenAdmin") String token,
                              @WebParam(name = "idUser") int idUser
                            ) throws SQLException
    {
        if (Verification.verifyToken(token)) {
            userDao.deleteUser(idUser);
            return true;
        }

        return false;

    }
}
