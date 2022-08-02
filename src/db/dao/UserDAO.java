package db.dao;
import domain.*;

import java.sql.*;
import java.util.ArrayList;
import domain.UserTypesEnum;

public class UserDAO {

    //connection function
    private static Connection getConnection() throws SQLException{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/mglsi_news","java","java");
    }

    //this function return admin's token basing on his id
    public String getToken(int id) throws SQLException{
        PreparedStatement sql = getConnection().prepareStatement("Select TokenValue from Token WHERE  IdUser=?");
        sql.setInt(1, id);
        ResultSet token = sql.executeQuery();
        sql.close();

        if(token.next())
            return token.getString("TokenValue");
        return "";
    }

    //this function return admin's token basing on his id
    public int getId(String token) throws SQLException{
        PreparedStatement sql = getConnection().prepareStatement("Select IdUser from Token WHERE  TokenValue=?");
        sql.setString(1, token);
        ResultSet id = sql.executeQuery();
        sql.close();

        if(id.next())
            return id.getInt("IdUser");
        return -1;
    }
    //This function return login and password's user
    public String getUserPassword(String login) throws SQLException {
        PreparedStatement sql = getConnection().prepareStatement("Select Password from Users WHERE  Login=?");
        sql.setString(1,login);
        ResultSet infos = sql.executeQuery();
        sql.close();

        if(infos.next())
            return infos.getString("password");
        return "";
    }

    //this function retrieves the list of users
    public ArrayList<User> getUsers() throws SQLException {
        PreparedStatement sql = getConnection().prepareStatement("Select * from User");
        ResultSet userList = sql.executeQuery();

        ArrayList<User> users = new ArrayList<>();
        Administrator admin = new Administrator();
        Editor editor = new Editor();

        while (userList.next()) {
            String login = userList.getString("login");
            String pwd = userList.getString("password");
            String type = userList.getString("type");
            switch (UserTypesEnum.valueOf(type)) {
                case ADMINISTRATOR:
                    admin.setLogin(login);
                    admin.setPassword(pwd);
                    users.add(admin);
                    break;

                case EDITOR:
                    editor.setLogin(login);
                    editor.setPassword(pwd);
                    users.add(editor);
            }
        }
        sql.close();
        return users;
    }

    //this function add user in the database
    public void addUser(User newUser, String type) throws SQLException {
        PreparedStatement sql = getConnection().prepareStatement("INSERT INTO User(Login,Password,Type) VALUES (?,?,?)");
        sql.setString(1,newUser.getLogin());
        sql.setString(2,newUser.getPassword());
        sql.setString(3,type);
        sql.executeUpdate();
        sql.close();
    }

    //update function depending on user's id
    public void updateUser(User user, String type, int id) throws SQLException {
        PreparedStatement sql = getConnection().prepareStatement("UPDATE User SET Login=?, Password=?, Type=? WHERE Id=? ");
        sql.setString(1,user.getLogin());
        sql.setString(2,user.getPassword());
        sql.setString(3,type);
        sql.setInt(4,id);
        sql.executeUpdate();
        sql.close();
    }

    //delete function depending on user's login
    public void deleteUser(int id) throws SQLException {
        PreparedStatement sql = getConnection().prepareStatement("DELETE FROM User WHERE Id=?");
        sql.setInt(1,id);
        sql.executeUpdate();
        sql.close();
    }

    public Boolean verifyToken(String token) throws SQLException {
        PreparedStatement sql = getConnection().prepareStatement("SELECT count(*) as nbToken FROM Token");
        ResultSet result = sql.executeQuery();
        sql.close();

        result.next();
        return Integer.parseInt(result.getString("nbToken")) > 0;
    }

}
