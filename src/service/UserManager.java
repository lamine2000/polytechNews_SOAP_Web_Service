package service;

import db.dao.UserDAO;
import domain.Administrator;
import domain.Editor;
import domain.SimpleUser;
import domain.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "User Manager")
public class UserManager {
    static UserDAO userDao = new UserDAO();

    @WebMethod(operationName = "Authentification")
    public String authentificateUser(@WebParam(name = "user_login") String login, @WebParam(name = "user_password") String password) throws SQLException {
        String userPwd = userDao.getUserPassword(login);
        if (userPwd.equals(password))
            return "Succesfully authenticate";

        return " Authentification failed";

    }

    @WebMethod(operationName = "AddUser")
    public String addUsers(@WebParam(name = "id_admin") int id, @WebParam(name = "Token_admin") String token, @WebParam(name = "login_user") String login,@WebParam(name = "password_user") String password, @WebParam(name = "type_user") String type) throws SQLException {
        if (token.equals(userDao.getToken(id))) {
            String hashPwd = DigestUtils.sha256Hex(password);
            userDao.addUser(login, hashPwd, type);
        } else {
            return " You haven't permission. Please generate token.";
        }
        return " Successfully added";
    }

    @WebMethod(operationName = "ListUser")
    public ArrayList<User> listUsers(@WebParam(name = "id_admin") int id,@WebParam(name = "admin_token") String token) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        if (token.equals(userDao.getToken(id))) {
            ResultSet listOfUsers = userDao.getUsers();
            Administrator admin = new Administrator();
            SimpleUser simpleUser = new SimpleUser();
            Editor editor = new Editor();

            while (listOfUsers.next()) {
                String login = listOfUsers.getString("login");
                String pwd = listOfUsers.getString("password");
                String type = listOfUsers.getString("type");
                switch (type) {
                    case "administrateur":
                        admin.setLogin(login);
                        admin.setPassword(pwd);
                        users.add(admin);
                        break;

                    case "editeur":
                        editor.setLogin(login);
                        editor.setPassword(pwd);
                        users.add(editor);
                        break;

                    case "utilisateur simple":
                        simpleUser.setLogin(login);
                        simpleUser.setPassword(pwd);
                        users.add(simpleUser);
                        break;
                }
            }
        } else {
            System.out.println(" You haven't permission. Please generate token.");
        }
        return users;
    }

    @WebMethod(operationName = "UpdateUser")
    public String updateUsers(@WebParam(name = "id_admin") int idAdmin, @WebParam(name = "Token_admin") String token, @WebParam(name = "login_user") String login,@WebParam(name = "password_user") String password, @WebParam(name = "type_user") String type, @WebParam(name = "id_user")int idUser) throws SQLException {
        if (token.equals(userDao.getToken(idAdmin))) {
          userDao.updateUser(login,password,type,idUser);
        } else {
            return " You haven't permission. Please generate token.";
        }
        return "Successfully modified";
    }

    @WebMethod(operationName = "DeleteUser")
    public String deleteUsers(@WebParam(name = "id_admin")int idAdmin, @WebParam(name = "token") String token,@WebParam(name = "id_user")int idUser) throws SQLException{
        if (token.equals(userDao.getToken(idAdmin))) {
            userDao.deleteUser(idUser);
        } else {
            return " You haven't permission. Please generate token.";
        }
        return "Successfully deleted";
    }
}
