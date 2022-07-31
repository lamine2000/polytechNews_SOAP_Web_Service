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

    @WebMethod(operationName = "authentication")
    public String authenticateUser(@WebParam(name ="userParameters") User user) throws SQLException
    {
        String userPwd = userDao.getUserPassword(user.getLogin());
        if (userPwd.equals(user.getPassword()))
            return "Succesfully authenticate";

        return " Authentication failed";
    }

    @WebMethod(operationName = "addUser")
    public String addUser(
                           @WebParam(name = "tokenAdmin") String token,
                           @WebParam(name = "userParameters") User user,
                           @WebParam(name = "typeUser") String type
                        ) throws SQLException
    {
        if (verification.verifyToken(token)) {
            String hashPwd = DigestUtils.sha256Hex(user.getPassword());
            userDao.addUser(user.getLogin(), hashPwd, type);
        } else {
            return " You haven't permission. Please generate token.";
        }
        return " Successfully added";
    }

    @WebMethod(operationName = "listUsers")
    public ArrayList<User> listUsers(
                                       @WebParam(name = "tokenAdmin") String token
                                    ) throws SQLException
    {
        if (!verification.verifyToken(token))
            System.out.println(" You haven't permission. Please generate token.");

        return userDao.getUsers();
    }

    @WebMethod(operationName = "updateUser")
    public String updateUser(
                               @WebParam(name = "tokenAdmin") String token,
                               @WebParam(name = "userParameters") User user,
                               @WebParam(name = "typeUser") String type,
                               @WebParam(name = "idUser") int idUser
                            ) throws SQLException
    {
        if (verification.verifyToken(token)) {
          userDao.updateUser(user.getLogin(),user.getPassword(),type,idUser);
        } else {
            return " You haven't permission. Please generate token.";
        }
        return "Successfully modified";
    }

    @WebMethod(operationName = "deleteUser")
    public String deleteUser(
                              @WebParam(name = "tokenAdmin") String token,
                              @WebParam(name = "idUser")int idUser
                            ) throws SQLException
    {
        if (verification.verifyToken(token)) {
            userDao.deleteUser(idUser);
        } else {
            return " You haven't permission. Please generate token.";
        }
        return "Successfully deleted";
    }
}
